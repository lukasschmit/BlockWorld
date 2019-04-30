package stranded.geometry;

public class Vector3D {

    public double leftRight;
    public double upDown;
    public double twist;

    public Vector3D() {
        leftRight = 0.0;
        upDown = 0.0;
        twist = 0.0;
    }

    public Vector3D(double leftRight, double upDown, double twist) {
        this.leftRight = leftRight;
        this.upDown = upDown;
        this.twist = twist;
    }
}
