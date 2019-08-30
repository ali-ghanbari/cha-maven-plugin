package edu.utdallas.cha.analysis;

import edu.utdallas.cha.relational.Domain;
import edu.utdallas.cha.relational.Relation;

public class ConcreteRel extends Relation<IntPair> {
    private final Domain<String> classes;

    private final Domain<String> signatures;

    public ConcreteRel(Domain<String> classes, Domain<String> signatures) {
        this.classes = classes;
        this.signatures = signatures;
    }

    @Override
    public String name() {
        return "concrete";
    }

    @Override
    protected IntPair convert(String s) {
        return null;
    }

    public void add(final String className, final String methodSignature) {
        final Integer classIndex = this.classes.indexOf(className);
        if (classIndex == null) {
            System.err.println("warning: class " + className + " not found.");
            return;
        }
        final Integer sigIndex = this.signatures.indexOf(methodSignature);
        if (sigIndex == null) {
            System.err.println("warning: signature " + methodSignature + " not found.");
            return;
        }
        this.data.add(IntPair.of(classIndex, sigIndex));
    }
}
