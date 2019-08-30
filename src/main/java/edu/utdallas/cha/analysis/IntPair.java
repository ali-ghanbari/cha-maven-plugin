package edu.utdallas.cha.analysis;

import java.io.Serializable;
import java.util.Objects;

public class IntPair implements Serializable {
    private static final long serialVersionUID = 42L;
    private final int left;
    private final int right;

    public IntPair(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public static IntPair of(int left, int right) {
        return new IntPair(left, right);
    }

    public int getLeft() {
        return this.left;
    }

    public int getRight() {
        return this.right;
    }

    @Override
    public String toString() {
        return this.left + " " + this.right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntPair)) {
            return false;
        }
        IntPair intPair = (IntPair) o;
        return this.left == intPair.left && this.right == intPair.right;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.right);
    }
}
