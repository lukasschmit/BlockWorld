package stranded.geometry;

public interface Intersectable {

    public boolean intersects(Point3D point);

    public boolean intersects(Box3D box);
}
