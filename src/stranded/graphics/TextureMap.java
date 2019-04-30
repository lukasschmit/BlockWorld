package stranded.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import stranded.geometry.Point2D;
import stranded.util.Disposable;
import stranded.util.ToolBox;

public class TextureMap implements Disposable {

    public final int HEIGHT;
    public final int WIDTH;
    public final double VERTI_TEXTURES;
    public final double HORIZ_TEXTURES;
    public Texture texture;
    public int texRes = -1;
    public int texX = 0;
    public int texY = 0;
    private final static double TEX_COOR_OFFS = 0.0001;

    public TextureMap(String format, String path, int texRes) throws IOException {
        BufferedImage img = ToolBox.loadImage("/" + path);

        texture = new Texture();
        texture.load(img);
        WIDTH = img.getWidth();
        HEIGHT = img.getHeight();
        this.texRes = texRes;
        VERTI_TEXTURES = HEIGHT / texRes;
        HORIZ_TEXTURES = WIDTH / texRes;
    }

    public Point2D bottomLeft() {
        return new Point2D(texX / HORIZ_TEXTURES + TEX_COOR_OFFS, (texY + 1) / VERTI_TEXTURES - TEX_COOR_OFFS);
    }

    public Point2D bottomRight() {
        return new Point2D((texX + 1) / HORIZ_TEXTURES - TEX_COOR_OFFS, (texY + 1) / VERTI_TEXTURES - TEX_COOR_OFFS);
    }

    public Point2D topRight() {
        return new Point2D((texX + 1) / HORIZ_TEXTURES - TEX_COOR_OFFS, texY / VERTI_TEXTURES + TEX_COOR_OFFS);
    }

    public Point2D topLeft() {
        return new Point2D(texX / HORIZ_TEXTURES + TEX_COOR_OFFS, texY / VERTI_TEXTURES + TEX_COOR_OFFS);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
