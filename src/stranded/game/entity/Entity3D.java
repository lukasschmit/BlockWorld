package stranded.game.entity;

import stranded.game.map.Block;
import stranded.geometry.Point3D;
import stranded.geometry.Vector3D;
import stranded.util.ToolBox;

public abstract class Entity3D {

    protected double width = 0.5;
    protected double depth = 0.5;
    protected double height = 0.5;
    protected Point3D[] collPoints;
    protected Point3D dist = new Point3D(0.0, 0.0, 0.0);
    protected Point3D vel = new Point3D(0.0, 0.0, 0.0);
    protected Point3D pos = new Point3D(0.0, 0.0, 0.0);
    protected Vector3D rot = new Vector3D(0.0, 0.0, 0.0);

    public abstract void tick(double delta, Block[][][] map);

    public abstract Point3D getPerspectivePos();

    protected void createCollisionPoints() {
        int cp = 0;

        for (double x = -(width / 2f); x <= width / 2f; x += width / ((int) width + 1)) {
            for (double y = 0f; y <= height; y += height / ((int) height + 1)) {
                for (double z = -(depth / 2f); z <= depth / 2f; z += depth / ((int) depth + 1)) {
                    cp++;
                }
            }
        }
        collPoints = new Point3D[cp];

        int i = 0;
        for (double x = -(width / 2.0); x <= width / 2.0; x += width / ((int) width + 1)) {
            for (double y = 0.0; y <= height; y += height / ((int) height + 1)) {
                for (double z = -(depth / 2.0); z <= depth / 2.0; z += depth / ((int) depth + 1)) {
                    collPoints[i] = new Point3D(x, y, z);
                    i++;
                }
            }
        }
    }

    protected void doGravity(double delta) {
        vel.y += ToolBox.GRAVITY_ACCEL * delta;
        dist.y = vel.y + 0.5 * ToolBox.GRAVITY_ACCEL * Math.pow(delta, 2);
    }

    public Point3D getVel() {
        return dist;
    }

    public void setVel(Point3D vel) {
        this.dist = vel;
    }

    public Point3D getPos() {
        return pos;
    }

    public void setPos(Point3D position) {
        this.pos = position;
    }

    public Vector3D getRot() {
        return rot;
    }

    public void setRot(Vector3D angle) {
        this.rot = angle;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public Point3D[] getCollisionPoints() {
        return collPoints;
    }

    public void setCollisionPoints(Point3D[] collisionPoints) {
        this.collPoints = collisionPoints;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
}
