package qlearning.neuralnetwork.qnetworktables.vectors;

/**
 * Created by finne on 07.02.2018.
 */
public class Vector3i extends Vector2i {

    public int z;

    public Vector3i(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector3i{" +
                "x=" + x +
                "y=" + y +
                "z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Vector3i vector3i = (Vector3i) o;

        return z == vector3i.z;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + z;
        return result;
    }
}
