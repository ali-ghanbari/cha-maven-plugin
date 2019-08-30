package edu.utdallas.cha.analysis;

import edu.utdallas.cha.relational.Domain;
import edu.utdallas.cha.relational.Relation;

public class ExtendsRel extends Relation<IntPair> {
    private final Domain<String> classes;

    public ExtendsRel(Domain<String> classes) {
        super();
        this.classes = classes;
    }

    @Override
    public String name() {
        return "extends";
    }

    @Override
    protected IntPair convert(String s) {
        return null;
    }

    public void add(final String subtype, final String supertype) {
        final Integer subIndex = this.classes.indexOf(subtype);
        if (subIndex == null) {
            System.err.println("warning: class " + subtype + " not found.");
            return;
        }
        final Integer supIndex = this.classes.indexOf(supertype);
        if (supIndex == null) {
            System.err.println("warning: class " + supertype + " not found.");
            return;
        }
        this.data.add(IntPair.of(subIndex, supIndex));
    }
}
