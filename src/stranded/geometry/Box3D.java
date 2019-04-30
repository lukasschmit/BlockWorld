package stranded.geometry;

public class Box3D implements Intersectable {

    public double x;
    public double y;
    public double z;
    public double width;
    public double height;
    public double depth;

    public Box3D() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
        width = 1.0;
        height = 1.0;
        depth = 1.0;
    }

    public Box3D(double width, double height, double depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    public Box3D(double width, double height, double depth, double x, double y, double z) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean intersects(Point3D point) {
        if (point.x >= x && point.x <= x + width) {
            if (point.y >= y && point.y <= y + height) {
                if (point.z >= z && point.z <= z + depth) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean intersects(Box3D box) {
        if (box.x + box.width >= x && box.x <= x + width) { // intersects on x axis
            if (box.y + box.height >= y && box.y <= y + height) { // intersects on y axis
                if (box.z + box.depth >= z && box.z <= z + depth) { // intersects on z axis
                    return true;
                }
            }
        }
        return false;
    }
}
