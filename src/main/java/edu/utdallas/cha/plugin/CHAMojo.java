package edu.utdallas.cha.plugin;

import edu.utdallas.cha.analysis.Analysis;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.pitest.classinfo.CachingByteArraySource;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.classpath.ClassPath;
import org.pitest.classpath.ClassPathByteArraySource;
import org.pitest.classpath.ClassloaderByteArraySource;
import org.pitest.functional.Option;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "print-cha", requiresDependencyResolution = ResolutionScope.TEST)
public class CHAMojo extends AbstractMojo {
    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject project;

    private ClassPath classPath;

    private ClassByteArraySource classByteArraySource;

    private File buildOutputDirectory;

    public void execute() throws MojoFailureException {
        this.classPath = getClassPath();
        this.classByteArraySource = createCachedClassByteArraySource();
        this.buildOutputDirectory = new File(this.project.getBuild().getOutputDirectory());

        final Analysis cha = new Analysis(this.classByteArraySource, this.buildOutputDirectory);
        try {
            cha.start();
        } catch (Exception e) {
            throw new MojoFailureException(e.getMessage(), e);
        }

    }

    private ClassPath getClassPath() {
        final List<File> initialClassPath = getInitialClassPath();
        return new ClassPath(initialClassPath);
    }

    private ClassByteArraySource createCachedClassByteArraySource() {
        final ClassByteArraySource baseBAS = new ClassPathByteArraySource(this.classPath);
        return new CachingByteArraySource(fallbackToClassLoader(baseBAS), 200);
    }

    private List<File> getInitialClassPath() {
        final List<File> classPath = new ArrayList<>();
        try {
            for (final Object cpElement : this.project.getTestClasspathElements()) {
                classPath.add(new File((String) cpElement));
            }
        } catch (DependencyResolutionRequiredException e) {
            getLog().warn(e);
        }
        return classPath;
    }

    /*adopted from Henry Coles' PIT*/
    private ClassByteArraySource fallbackToClassLoader(final ClassByteArraySource bas) {
        final ClassByteArraySource cbas =
                ClassloaderByteArraySource.fromContext();
        return cls -> {
            final Option<byte[]> maybeBytes = bas.getBytes(cls);
            if (maybeBytes.hasSome()) {
                return maybeBytes;
            }
            return cbas.getBytes(cls);
        };
    }


}

/*


#defines(c, m) :- declares(c, m). # here, we don't care about visibility
#defines(c, m) :- extends(s, c), defines(s, m), nonprivate(m). # here, we only inherit those methods that are not private
# note that although a class cannot override a final method, the methods are inherited
# we also don't care whether or not the method is concrete

#may_call(cls, sig, cls, sig)
#direct_call(cls, sig, cls, sig)


subtype(sub, sup) :- extends(sub, sup).
subtype(sub, sup) :- subtype(sub, c), extends(c, sup).

may_call(c, m, d, n) :- direct_call(c, m, d, n), concrete(d, n).
may_call(c, m, d, n) :- direct_call(c, m, s, n), subtype(d, s), declares(d, n). # a subtype that declares the same signature, declares a concrete one
may_call(c, m, d, n) :- may_call(c, m, e, k), may_call(e, k, d, n).

 */
