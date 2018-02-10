package qlearning.neuralnetwork.qnetworktables.vectors;

/**
 * Created by finne on 05.02.2018.
 */
public class Vector2i extends Vector1i{

    public int y;

    public Vector2i(int x, int y) {
        super(x);
        this.y = y;
    }


    @Override
    public String toString() {
        return "Vector2i{" +
                "x=" + x +
                "y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Vector2i vector2i = (Vector2i) o;

        return y == vector2i.y;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + y;
        return result;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
