package qlearning.basic.vectors;

/**
 * Created by finne on 07.02.2018.
 */
public class Vector1i {
    public int x;

    public Vector1i(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "Vector1i{" +
                "x=" + x +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector1i vector1i = (Vector1i) o;

        return x == vector1i.x;
    }

    @Override
    public int hashCode() {
        return x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
