package edu.utdallas.cha.analysis;

public interface NestedVisitor {
    void visitClass(String internalName);
    void visitExtends(String subtypeInternalName, String supertypeInternalName);
    void visitMethod(String ownerInternalName,
                     String methodName,
                     String descriptor,
                     boolean isAbstract);
    void visitMethodCall(String callerClassInternalName,
                         String callerMethodName,
                         String callerMethodDescriptor,
                         String calleeClassInternalName,
                         String calleeMethodName,
                         String calleeMethodDescriptor);
}
