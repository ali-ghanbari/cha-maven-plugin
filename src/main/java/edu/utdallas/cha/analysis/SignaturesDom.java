package edu.utdallas.cha.analysis;

import edu.utdallas.cha.relational.Domain;

public class SignaturesDom extends Domain<String> {
    private static SignaturesDom instance = null;

    private SignaturesDom() {

    }

    public static SignaturesDom v() {
        if (instance == null) {
            instance = new SignaturesDom();
        }
        return instance;
    }

    @Override
    public String name() {
        return "S";
    }
}
