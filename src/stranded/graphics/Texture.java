package stranded.graphics;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import stranded.util.Disposable;

public class Texture implements Disposable {

    public ByteBuffer buffer;
    public int width = -1;
    public int height = -1;
    public int textureID = -1;

    // RGBA
    public void load(BufferedImage img) {
        width = img.getWidth();
        height = img.getHeight();

        BufferedImage imgRGBA = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        if (!imgRGBA.createGraphics().drawImage(img, 0, 0, null)) {
            System.out.println("wait");
            try {
                Thread.sleep(2);
            } catch (InterruptedException ex) {
            }
        }

        byte[] data = (byte[]) imgRGBA.getRaster().getDataElements(0, 0, width, height, null);
        buffer = BufferUtils.createByteBuffer(width * height * 4);
        buffer.clear();
        buffer.put(data);
        buffer.flip();
    }

    public void bind() {
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
    }

    @Override
    public void dispose() {
        GL11.glDeleteTextures(textureID);
    }
}
