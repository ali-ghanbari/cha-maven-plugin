package edu.utdallas.cha.analysis;

import edu.utdallas.cha.relational.Domain;
import org.objectweb.asm.Type;
import org.pitest.classinfo.ClassByteArraySource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Analysis {
    private final ClassByteArraySource cbas;

    private final File buildOutputDirectory;

    public Analysis(ClassByteArraySource cbas, File buildOutputDirectory) {
        this.cbas = cbas;
        this.buildOutputDirectory = buildOutputDirectory;
    }

    public void start() throws Exception {
        final MyNestedVisitor nestedVisitor =
                new MyNestedVisitor(ClassesDom.v(), SignaturesDom.v());

        final ClassVisitorDriver classVisitorDriver =
                new ClassVisitorDriver(this.cbas, this.buildOutputDirectory, nestedVisitor);

        classVisitorDriver.start();

        final ExtendsRel extendsRel = new ExtendsRel(ClassesDom.v());

        for (final String[] p : nestedVisitor.getExtendsPairs()) {
            final String subtype = p[0];
            final String supertype = p[1];
            extendsRel.add(subtype, supertype);
        }

        final ConcreteRel concreteRel = new ConcreteRel(ClassesDom.v(), SignaturesDom.v());

        for (final String[] p : nestedVisitor.getConcreteMethods()) {
            final String className = p[0];
            final String signature = p[1];
            concreteRel.add(className, signature);
        }

        final DirectCallRel directCallRel =
                new DirectCallRel(ClassesDom.v(), SignaturesDom.v());

        for (final String[] p : nestedVisitor.getDirectCalls()) {
            directCallRel.add(p[0], p[1], p[2], p[3]);
        }

        final DeclaresRel declaresRel = new DeclaresRel(ClassesDom.v(), SignaturesDom.v());

        for (final String[] p : nestedVisitor.getDeclares()) {
            final String className = p[0];
            final String signature = p[1];
            declaresRel.add(className, signature);
        }

        ClassesDom.v().save();
        SignaturesDom.v().save();
        concreteRel.save();
        extendsRel.save();
        directCallRel.save();
        declaresRel.save();

        runBDDBDDB("cha.dlog");
    }

    private static void runBDDBDDB(final String dlogFileName) throws Exception {
        final String[] cmd = {
                "java",
                "-jar",
                "bddbddb-full.jar",
                dlogFileName
        };
        final ProcessBuilder pb = new ProcessBuilder(cmd).inheritIO();
        pb.start().waitFor();
    }
}

class MyNestedVisitor implements NestedVisitor {
    private final Domain<String> classesDom;

    private final Domain<String> signaturesDom;

    private final List<String[]> extendsPairs;

    private final List<String[]> concreteMethods;

    private final List<String[]> directCalls;

    private final List<String[]> declares;

    public MyNestedVisitor(Domain<String> classesDom, Domain<String> signaturesDom) {
        this.classesDom = classesDom;
        this.signaturesDom = signaturesDom;
        this.extendsPairs = new ArrayList<>();
        this.concreteMethods = new ArrayList<>();
        this.directCalls = new ArrayList<>();
        this.declares = new ArrayList<>();
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

    @Override
    public void visitMethod(String ownerInternalName,
                            String methodName,
                            String descriptor,
                            boolean isAbstract) {
        final String sig = makeSignature(methodName, descriptor);
        this.signaturesDom.add(sig);
        final String first = ownerInternalName.replace('/', '.');
        final String[] pair = {first, sig};
        if (!isAbstract) {
            this.concreteMethods.add(pair);
        }
        this.declares.add(pair);
    }

    private String makeSignature(String methodName, String descriptor) {
        final String args = Arrays.stream(Type.getArgumentTypes(descriptor))
                .map(Type::getClassName)
                .collect(Collectors.joining(","));
        return String.format("%s(%s)", methodName, args);
    }

    @Override
    public void visitMethodCall(String callerClassInternalName,
                                String callerMethodName,
                                String callerMethodDescriptor,
                                String calleeClassInternalName,
                                String calleeMethodName,
                                String calleeMethodDescriptor) {
        final String callerSig = makeSignature(callerMethodName, callerMethodDescriptor);
        final String calleeSig = makeSignature(calleeMethodName, calleeMethodDescriptor);
        final String first = callerClassInternalName.replace('/', '.');
        final String third = calleeClassInternalName.replace('/', '.');
        this.directCalls.add(new String[] {first, callerSig, third, calleeSig});
    }

    List<String[]> getExtendsPairs() {
        return extendsPairs;
    }

    List<String[]> getConcreteMethods() {
        return concreteMethods;
    }

    List<String[]> getDirectCalls() {
        return directCalls;
    }

    List<String[]> getDeclares() {
        return declares;
    }
}