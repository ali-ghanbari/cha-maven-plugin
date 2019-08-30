package edu.utdallas.cha.analysis;

import edu.utdallas.cha.relational.Domain;
import org.pitest.classinfo.ClassByteArraySource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Analysis {
    private final ClassByteArraySource cbas;

    private final File buildOutputDirectory;

    public Analysis(ClassByteArraySource cbas, File buildOutputDirectory) {
        this.cbas = cbas;
        this.buildOutputDirectory = buildOutputDirectory;
    }

    public void start() throws Exception {
        final MyNestedVisitor nestedVisitor = new MyNestedVisitor(ClassesDom.v());

        final ClassVisitorDriver classVisitorDriver =
                new ClassVisitorDriver(this.cbas, this.buildOutputDirectory, nestedVisitor);

        classVisitorDriver.start();

        final ExtendsRel extendsRel = new ExtendsRel(ClassesDom.v());

        for (final String[] p : nestedVisitor.getExtendsPairs()) {
            final String subtype = p[0];
            final String supertype = p[1];
            extendsRel.add(subtype, supertype);
        }

        ClassesDom.v().save();
        extendsRel.save();
    }
}

class MyNestedVisitor implements NestedVisitor {
    private final Domain<String> classesDom;

    private final List<String[]> extendsPairs;

    public MyNestedVisitor(Domain<String> classesDom) {
        this.classesDom = classesDom;
        this.extendsPairs = new ArrayList<>();
    }

    @Override
    public void visitClass(String internalName) {
        this.classesDom.add(internalName.replace('/', '.'));
    }

    @Override
    public void visitExtends(String subtypeInternalName, String supertypeInternalName) {
        final String first = subtypeInternalName.replace('/', '.');
        final String second = supertypeInternalName.replace('/', '.');
        this.extendsPairs.add(new String[] {first, second});
    }

    public List<String[]> getExtendsPairs() {
        return extendsPairs;
    }
}