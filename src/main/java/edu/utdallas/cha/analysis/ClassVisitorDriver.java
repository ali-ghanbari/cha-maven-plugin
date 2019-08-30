package edu.utdallas.cha.analysis;

import edu.utdallas.cha.commons.Util;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.classinfo.ClassByteArraySource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class ClassVisitorDriver {
    private final ClassByteArraySource cbas;

    private final File buildOutputDir;

    private final NestedVisitor nv;

    private final Queue<String> workList;

    private final Set<String> processed;

    public ClassVisitorDriver(ClassByteArraySource cbas,
                              File buildOutputDir,
                              NestedVisitor nv) {
        this.cbas = cbas;
        this.buildOutputDir = buildOutputDir;
        this.nv = nv;
        this.workList = new LinkedList<>();
        this.processed = new HashSet<>();
    }

    public void start() throws Exception {
        final Collection<File> classFiles = Util.listClassFiles(this.buildOutputDir);
        for (final File classFile : classFiles) {
            try (final InputStream is = new FileInputStream(classFile)) {
                final ClassReader classReader = new ClassReader(is);
                final ClassVisitor classVisitor =
                        new MyClassVisitor(this, this.nv);
                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
            }
        }
        // process remaining ones
        while (!this.workList.isEmpty()) {
            final String className = this.workList.poll();
            if (!this.processed.contains(className)) {
                final byte[] bytes = this.cbas.getBytes(className).getOrElse(null);
                if (bytes != null) {
                    final ClassReader classReader = new ClassReader(bytes);
                    final ClassVisitor classVisitor =
                            new MyClassVisitor(this, this.nv);
                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
                }
            }
        }
    }

    void addToWorkList(final String className) {
        this.workList.offer(className.replace('/', '.'));
    }

    void markProcessed(final String className) {
        this.processed.add(className.replace('/', '.'));
    }
}

class MyClassVisitor extends ClassVisitor {
    private final ClassVisitorDriver classVisitorDriver;

    private final NestedVisitor nv;

    public MyClassVisitor(ClassVisitorDriver classVisitorDriver, NestedVisitor nv) {
        super(Opcodes.ASM7);
        this.classVisitorDriver = classVisitorDriver;
        this.nv = nv;
    }

    @Override
    public void visit(int version,
                      int access,
                      String name,
                      String signature,
                      String superName,
                      String[] interfaces) {
        this.nv.visitClass(name);
        this.classVisitorDriver.markProcessed(name);
        if (superName != null) {
            this.classVisitorDriver.addToWorkList(superName);
            this.nv.visitExtends(name, superName);
        }
        if (interfaces != null) {
            for (final String ifc : interfaces) {
                this.classVisitorDriver.addToWorkList(ifc);
                this.nv.visitExtends(name, ifc);
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }
}