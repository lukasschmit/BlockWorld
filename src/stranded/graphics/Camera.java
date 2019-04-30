package stranded.graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import stranded.game.map.Chunk;
import stranded.util.ToolBox;

public class Camera {

    private static float fovX = 100f;
    public static boolean fog = true;
    public static float maxChunkDist = 10f;
    private static int defaultDisplayWidth = 896;
    private static int defaultDisplayHeight = 504;
    private static float aspectRatioFullScreen = 16f / 9f;
    public static boolean fullscreen = false;
    public static boolean VSync = true;
    public static boolean solidRendering = true;
    public static boolean VBORendering = false;

    public static void initGL() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        resizeGL();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glEnable(GL11.GL_FOG);
        GL11.glFogf(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
        GL11.glFogf(GL11.GL_FOG_DENSITY, 0.035f);
        GL11.glFogf(GL11.GL_FOG_START, 0f);
        GL11.glFogf(GL11.GL_FOG_END, maxChunkDist * Chunk.SIZE);
        GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
    }

    public static void resizeGL() {
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

        float aspectRatio = (float) Display.getWidth() / Display.getHeight();
        float fovY = (float) Math.toDegrees(2 * Math.atan2(Math.tan(Math.toRadians(fovX / 2.0)), aspectRatio));

        GLU.gluPerspective(fovY, aspectRatio, 0.01f, 512f);
    }

    public static void initDisplay() {
        try {
            if (!fullscreen) {
                Display.setResizable(true);
                Display.setDisplayMode(new DisplayMode(defaultDisplayWidth, defaultDisplayHeight));
            } else {
                Display.setResizable(false);
                Display.setDisplayMode(ToolBox.getBestDisplayMode(aspectRatioFullScreen));
            }

            Display.setVSyncEnabled(VSync);
            Display.setTitle(ToolBox.PRODUCT_NAME);
            Display.setFullscreen(fullscreen);
            if (!Display.isCreated()) {
                Display.create();
                initControls();
            }
        } catch (LWJGLException ex) {
            System.err.println("Failed to initialize Display.");
            System.exit(-1);
        }
    }

    private static void initControls() {
        try {
            Mouse.create();
        } catch (LWJGLException ex) {
            System.err.println("Failed to initialize Keyboard.");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex1) {
            }
            System.exit(-1);
        }
        try {
            Keyboard.create();
        } catch (LWJGLException ex) {
            System.err.println("Failed to initialize Mouse.");
            System.exit(-1);
        }
    }
}
