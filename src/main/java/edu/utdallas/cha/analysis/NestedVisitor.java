package edu.utdallas.cha.analysis;

public interface NestedVisitor {
    void visitClass(final String internalName);
    void visitExtends(final String subtypeInternalName, final String supertypeInternalName);
}
