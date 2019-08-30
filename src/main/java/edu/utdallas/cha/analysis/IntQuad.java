package edu.utdallas.cha.analysis;

import java.io.Serializable;
import java.util.Objects;

public class IntQuad implements Serializable {
    private static final long serialVersionUID = 42L;

    private final int first;

    private final int second;

    private final int third;

    private final int fourth;

    public IntQuad(int first, int second, int third, int fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public static IntQuad of(int first, int second, int third, int fourth) {
        return new IntQuad(first, second, third, fourth);
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getThird() {
        return third;
    }

    public int getFourth() {
        return fourth;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s", first, second, third, fourth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IntQuad)) {
            return false;
        }
        IntQuad intQuad = (IntQuad) o;
        return this.first == intQuad.first &&
                this.second == intQuad.second &&
                this.third == intQuad.third &&
                this.fourth == intQuad.fourth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.first, this.second, this.third, this.fourth);
    }
}
