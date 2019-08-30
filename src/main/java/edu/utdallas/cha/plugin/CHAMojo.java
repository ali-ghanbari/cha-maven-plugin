package edu.utdallas.cha.plugin;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mojo( name = "print-cha")
public class CHAMojo extends AbstractMojo {
    /**
     * <i>Internal</i>: Project to interact with.
     */
    @Parameter(property = "project", readonly = true, required = true)
    protected MavenProject project;

    /**
     * <i>Internal</i>: Map of plugin artifacts.
     */
    @Parameter(property = "plugin.artifactMap", readonly = true, required = true)
    private Map<String, Artifact> pluginArtifactMap;

    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println(getClassPath());
    }

    private List<String> getClassPath() {
        final List<String> classPath = new ArrayList<>();

        try {
            classPath.addAll(this.project.getTestClasspathElements());
        } catch (DependencyResolutionRequiredException e) {
            getLog().error(e);
        }

//        for (final Artifact dependency : this.pluginArtifactMap.values()) {
//            //classPath.add(dependency.getFile().getAbsolutePath());
//        }
        System.out.println(this.pluginArtifactMap.keySet());

        return classPath;
    }
}
