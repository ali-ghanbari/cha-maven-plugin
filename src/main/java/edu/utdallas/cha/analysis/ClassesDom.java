package edu.utdallas.cha.analysis;

import edu.utdallas.cha.relational.Domain;

public class ClassesDom extends Domain<String> {
    private static ClassesDom instance = null;

    private ClassesDom() {

    }

    public static ClassesDom v() {
        if (instance == null) {
            instance = new ClassesDom();
        }
        return instance;
    }

    @Override
    public String name() {
        return "C";
    }
}
