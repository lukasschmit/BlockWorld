package stranded.game.entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import stranded.game.map.Block;
import stranded.geometry.Point3D;
import stranded.util.ToolBox;

public class Character extends Entity3D {

    public static final double STD_MOVE_SPEED = 6.0 * ToolBox.MPH_TO_FPS / Block.SIZE_FEET;
    protected double moveSpeedOfStd = 1.0;
    public static final double STD_JUMP_SPEED = 14.0 * ToolBox.MPH_TO_FPS / Block.SIZE_FEET;
    protected double jumpSpeedOfStd = 1.0;
    public static final double STD_AIR_CORRECT_SPEED = 6.0 * ToolBox.MPH_TO_FPS / Block.SIZE_FEET;
    protected double airCorrectSpeedOfStd = 1.0;
    protected boolean onSurface;
    protected boolean controlledByUser = false;
    protected double eyeHeight = 0.0;
    public static final double MAX_LOOK_UP = 87.5;
    public static final double MAX_LOOK_DOWN = -87.5;
    protected double headBob = 0.0;

    @Override
    public void tick(double delta, Block[][][] map) {
        
        if (controlledByUser && Mouse.isGrabbed()) {
            mouseControl();
            keyboardControl(delta);
        }
        doGravity(delta);
        moveWithMapCollision(map, delta);
    }

    protected void mouseControl() {
        double mouseDX = Mouse.getDX() * ToolBox.MOUSE_SPEED;
        double mouseDY = Mouse.getDY() * ToolBox.MOUSE_SPEED;

        rot.leftRight += mouseDX;
        while (rot.leftRight >= 360.0) {
            rot.leftRight -= 360.0;
        }
        while (rot.leftRight < 0.0) {
            rot.leftRight += 360.0;
        }

        rot.upDown -= mouseDY;
        if (rot.upDown > MAX_LOOK_UP) {
            rot.upDown = MAX_LOOK_UP;
        } else if (rot.upDown < MAX_LOOK_DOWN) {
            rot.upDown = MAX_LOOK_DOWN;
        }
    }

    protected void keyboardControl(double delta) {
        double angle = -1f;

        if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_S)
                && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle = 0.0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_S)
                && Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle = 45.0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_A)
                && !Keyboard.isKeyDown(Keyboard.KEY_S)) {
            angle = 90.0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_A)
                && Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle = 135.0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_A)
                && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle = 180.0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_A)
                && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle = 225.0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S)
                && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle = 270.0;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_S)
                && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle = 315.0;
        }
        if (angle != -1f) {
            if (onSurface) {
                dist.x = ((Math.sin(Math.toRadians(rot.leftRight + angle)) * (moveSpeedOfStd * STD_MOVE_SPEED)));
                dist.z = -((Math.cos(Math.toRadians(rot.leftRight + angle)) * (moveSpeedOfStd * STD_MOVE_SPEED)));
                headBob = Math.sin(ToolBox.getTimeSeconds() * 20f) / 20.0;
            } else {
                dist.x += Math.sin(Math.toRadians(rot.leftRight + angle)) * (delta * airCorrectSpeedOfStd * STD_AIR_CORRECT_SPEED);
                dist.z += -Math.cos(Math.toRadians(rot.leftRight + angle)) * (delta * airCorrectSpeedOfStd * STD_AIR_CORRECT_SPEED);
            }
        } else {
            headBob = 0.0;
            if (onSurface) {
                dist.x = 0.0;
                dist.y = 0.0;
                dist.z = 0.0;
            }
        }
        if ((Keyboard.isKeyDown(Keyboard.KEY_SPACE)) && onSurface) {
            vel.y = (jumpSpeedOfStd * STD_JUMP_SPEED * 3);
        }
    }

    protected void moveWithMapCollision(Block[][][] map, double delta) {
        if (collPoints == null) {
            createCollisionPoints();
        }
        Point3D newPos = new Point3D(pos.x + (dist.x * delta), pos.y + (dist.y * delta), pos.z + (dist.z * delta));
        onSurface = false;

        if (Math.abs(dist.x) >= Math.abs(dist.z) && Math.abs(dist.x) >= Math.abs(dist.z)) {
            moveX(map, newPos);
            if (Math.abs(dist.y) >= Math.abs(dist.z)) {
                moveY(map, newPos);
                moveZ(map, newPos);
            } else {
                moveZ(map, newPos);
                moveY(map, newPos);
            }
        } else if (Math.abs(dist.y) > Math.abs(dist.x) && Math.abs(dist.y) >= Math.abs(dist.z)) {
            moveY(map, newPos);
            if (Math.abs(dist.x) >= Math.abs(dist.z)) {
                moveX(map, newPos);
                moveZ(map, newPos);
            } else {
                moveZ(map, newPos);
                moveX(map, newPos);
            }
        } else if (Math.abs(dist.z) > Math.abs(dist.x) && Math.abs(dist.z) > Math.abs(dist.y)) {
            moveZ(map, newPos);
            if (Math.abs(dist.x) >= Math.abs(dist.y)) {
                moveX(map, newPos);
                moveY(map, newPos);
            } else {
                moveX(map, newPos);
                moveY(map, newPos);
            }
        }
    }

    protected void moveX(Block[][][] map, Point3D newPos) {
        boolean xGood = true;
        for (int cp = 0; cp < collPoints.length; cp++) {
            if (map[(int) (newPos.x + collPoints[cp].x)][(int) (pos.y + collPoints[cp].y)][(int) (pos.z + collPoints[cp].z)] == null) {
                continue;
            } else {
                xGood = false;
                if (vel.x > 0.0) {
                    dist.x = 0.0;
                } else if (vel.x <= 0.0f) {
                    dist.x = 0.0;
                }
                break;
            }
        }
        if (xGood) {
            pos.x = newPos.x;
        }
    }

    protected void moveY(Block[][][] map, Point3D newPos) {
        boolean yGood = true;
        for (int cp = 0; cp < collPoints.length; cp++) {
            if (map[(int) (pos.x + collPoints[cp].x)][(int) (newPos.y + collPoints[cp].y)][(int) (pos.z + collPoints[cp].z)] == null) {
                continue;
            } else {
                yGood = false;
                if (vel.y > 0.0) {
                    vel.y = 0.0;
                    dist.y = 0.0;
                } else if (vel.y <= 0.0) {
                    pos.y = (int) pos.y;
                    vel.y = 0.0;
                    dist.y = 0.0;
                    onSurface = true;
                }
                break;
            }
        }
        if (yGood) {
            pos.y = newPos.y;
        }
    }

    protected void moveZ(Block[][][] map, Point3D newPos) {
        boolean zGood = true;
        for (int cp = 0; cp < collPoints.length; cp++) {
            if (map[(int) (pos.x + collPoints[cp].x)][(int) (pos.y + collPoints[cp].y)][(int) (newPos.z + collPoints[cp].z)] == null) {
                continue;
            } else {
                zGood = false;
                if (vel.z > 0.0) {
                    dist.z = 0.0;
                } else if (vel.z <= 0.0) {
                    dist.z = 0.0;
                }
                break;
            }
        }
        if (zGood) {
            pos.z = newPos.z;
        }
    }

    @Override
    public Point3D getPerspectivePos() {
        return new Point3D(pos.x, pos.y + eyeHeight + headBob, pos.z);
    }

    public double getMoveSpeedOfStd() {
        return moveSpeedOfStd;
    }

    public void setMoveSpeedOfStd(double moveSpeedOfStd) {
        this.moveSpeedOfStd = moveSpeedOfStd;
    }

    public double getJumpSpeedOfStd() {
        return jumpSpeedOfStd;
    }

    public void setJumpSpeedOfStd(double jumpSpeedOfStd) {
        this.jumpSpeedOfStd = jumpSpeedOfStd;
    }

    public boolean isOnSurface() {
        return onSurface;
    }

    public void setOnSurface(boolean onSurface) {
        this.onSurface = onSurface;
    }

    public boolean isControlledByUser() {
        return controlledByUser;
    }

    public void setControlledByUser(boolean controlledByUser) {
        this.controlledByUser = controlledByUser;
    }

    public double getEyeHeight() {
        return eyeHeight;
    }

    public void setEyeHeight(double eyeHeight) {
        this.eyeHeight = eyeHeight;
    }

    public double getAirCorrectSpeedOfStd() {
        return airCorrectSpeedOfStd;
    }

    public void setAirCorrectSpeedOfStd(double airCorrectSpeedOfStd) {
        this.airCorrectSpeedOfStd = airCorrectSpeedOfStd;
    }
}
