package stranded.game.map;

public class Block {

    public static final int TOP = 0;
    public static final int SIDE = 1;
    public static final int BOTTOM = 2;
    public static final byte TYPE_SURFACE = 0;
    public static final byte TYPE_ROCKS = 1;
    public static final byte TYPE_GREEN_GLOW = 2;
    public static final byte TYPE_YELLOW_GLOW = 3;
    public static final byte TYPE_ORANGE_GLOW = 4;
    public static final byte TYPE_PINK_GLOW = 5;
    private byte type;
    private byte renderFaces;
    public static final double SIZE_FEET = 2;


    public void setType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }

    public byte getRenderFaces() {
        return renderFaces;
    }

    public void setRenderFaces(byte renderFaces) {
        this.renderFaces = renderFaces;
    }

    public Block(byte type) {
        this.type = type;
    }

    public boolean isOpaque() {
        return true;
    }
}
