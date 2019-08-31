package edu.utdallas.cha.analysis;

import edu.utdallas.cha.relational.Domain;
import edu.utdallas.cha.relational.Relation;

public class DirectCallRel extends Relation<IntQuad> {
    private final Domain<String> classes;

    private final Domain<String> signatures;

    public DirectCallRel(Domain<String> classes, Domain<String> signatures) {
        this.classes = classes;
        this.signatures = signatures;
    }

    @Override
    public String name() {
        return "direct_call";
    }

    @Override
    protected IntQuad convert(String s) {
        return null;
    }

    public void add(String callerOwnerClassName,
                    String callerSig,
                    String calleeOwnerClass,
                    String calleeSig) {
        final Integer callerOwnerClassIndex = this.classes.indexOf(callerOwnerClassName);
        if (callerOwnerClassIndex == null) {
            return;
        }
        final Integer callerSigIndex = this.signatures.indexOf(callerSig);
        if (callerSigIndex == null) {
            return;
        }
        final Integer calleeOwnerClassIndex = this.classes.indexOf(calleeOwnerClass);
        if (calleeOwnerClassIndex == null) {
            return;
        }
        final Integer calleeSigIndex = this.signatures.indexOf(calleeSig);
        if (calleeSigIndex == null) {
            return;
        }
        this.data.add(IntQuad.of(callerOwnerClassIndex, callerSigIndex, calleeOwnerClassIndex, calleeSigIndex));
    }
}
