package stranded.graphics;

import java.io.IOException;

public class Art {

    public static TextureMap blockMap;

    public static void init() {
        try {
            blockMap = new TextureMap("PNG", "art/blocks/blocks.png", 32);
        } catch (IOException ex) {
            System.err.println("Failed to load texture map.");
        }
    }

    public static void dispose() {
        blockMap.dispose();
    }
}
