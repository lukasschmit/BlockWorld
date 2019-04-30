package stranded.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import stranded.game.map.Block;
import stranded.geometry.Point3D;

public class ToolBox {

    public static final double MPH_TO_FPS = 1.46667;
    public static final double MOUSE_SPEED = 0.035;
    public static final String COMPANY_NAME = "company name";
    public static final String PRODUCT_NAME = "game name";
    public static final double PRODUCT_VERSION = 0.01;
    public static final double GRAVITY_ACCEL = -32.174 / Block.SIZE_FEET;

    public static BufferedImage loadImage(String fileName) {
        try {
            return ImageIO.read(ToolBox.class.getResource(fileName));
        } catch (IOException ex) {
            System.err.println("Failed to load BufferedImage.");
            System.exit(-1);
        }
        return null;
    }

    public static double distBetween(Point3D a, Point3D b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
    }

    public static DisplayMode getBestDisplayMode(float aspectRatio) {
        try {
            DisplayMode bestMode = Display.getDesktopDisplayMode(); // in case no modes match
            DisplayMode[] modes;
            int maxWidthForAspectRatio = -1;
            modes = Display.getAvailableDisplayModes();
            for (DisplayMode mode : modes) {
                if ((float) mode.getWidth() / mode.getHeight() == aspectRatio) {
                    if (mode.getWidth() >= maxWidthForAspectRatio) {
                        maxWidthForAspectRatio = mode.getWidth();
                    }
                }
            }

            for (DisplayMode mode : modes) {
                if (bestMode.getWidth() != maxWidthForAspectRatio) {
                    bestMode = mode;
                    continue;
                }

                if ((float) mode.getWidth() / mode.getHeight() == 16f / 9) {
                    if (mode.getWidth() == maxWidthForAspectRatio) {
                        if (mode.getBitsPerPixel() >= bestMode.getBitsPerPixel()) {
                            if (mode.getFrequency() >= bestMode.getFrequency()) {
                                bestMode = mode;
                            }
                        }
                    }
                }
            }
            return bestMode;
        } catch (LWJGLException ex) {
            System.err.println("Failed to find optimal fullscreen DisplayMode.");
            System.exit(-1);
        }
        return null;
    }

    public static double getTimeSeconds() {
        return (System.nanoTime() / 1000000000.0);
    }
}
