package stranded.geometry;

public class Point3D implements Intersectable {

    public double x;
    public double y;
    public double z;

    public Point3D() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean intersects(Point3D point) {
        return point.x == x && point.y == y && point.z == z;
    }

    @Override
    public boolean intersects(Box3D box) {
        if (x >= box.x && x <= box.x + box.width) {
            if (y >= box.y && y <= box.y + box.height) {
                if (z >= box.z && x <= box.z + box.depth) {
                    return true;
                }
            }
        }
        return false;
    }
}
