package stranded.game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import stranded.game.state.FirstPersonState;
import stranded.game.state.GameState;
import stranded.graphics.Art;
import stranded.graphics.Camera;
import stranded.util.PerformanceMonitor;
import stranded.util.ToolBox;

/**
 * The engine handles the game loop and the timing of it's current GameState.
 * 
 * @author Lukas
 */
public class GameEngine {

    /**
     * The ticks done per second, regardless of the f.p.s.
     */
    private static int ticksPerSecond = 100;
    /**
     * The target frames to render per second, regardless of the ticks per second.
     */
    private static int targetFPS = 60;
    /**
     * If the engine is running.
     */
    private static boolean running = false;
    /**
     * The current state of the engine.
     */
    public static GameState state;

    /**
     * Initializes the the stuff that the engine might use.
     */
    public static void init() {
        Camera.initDisplay();
        Camera.initGL();
        Art.init();

        state = new FirstPersonState();
        state.init();
    }

    /**
     * First sets running to true. Runs the main game loop, looping through it until running is false. Ticks and renders the current GameState
     * according to ticksPerSecond and targetFPS. Sends performance statistics to PerformanceMonitor.
     */
    public static void run() {
        running = true;
        double lastTime = (ToolBox.getTimeSeconds() - (1.0 / targetFPS));
        double unprocessedSeconds = 0.0;

        while (running) {
            double delta = (ToolBox.getTimeSeconds() - lastTime);
            unprocessedSeconds += delta;
            lastTime = ToolBox.getTimeSeconds();

            PerformanceMonitor.startTask("tick");
            while (unprocessedSeconds >= 1.0 / ticksPerSecond) {
                tick(1.0 / ticksPerSecond);
                unprocessedSeconds -= (1.0 / ticksPerSecond);
            }
            PerformanceMonitor.endTask("tick");

            PerformanceMonitor.startTask("render");
            render();
            PerformanceMonitor.endTask("render");

            PerformanceMonitor.startTask("other");
            if (Display.isCloseRequested()) {
                stop();
            }
            if (Display.wasResized()) {
                Camera.resizeGL();
            }
            Display.update();
            PerformanceMonitor.endTask("other");

            PerformanceMonitor.endCycle();
        }
    }

    /**
     * Handles non-state-specific key presses, and ticks the GameState with delta.
     * 
     * @param delta the time in seconds since the last tick
     */
    public static void tick(double delta) {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
                Mouse.setGrabbed(!Mouse.isGrabbed());
            } else if (Keyboard.getEventKey() == Keyboard.KEY_Y && Keyboard.getEventKeyState()) {
                Camera.fullscreen = !Camera.fullscreen;
                Camera.initDisplay();
                Camera.resizeGL();
            } else if (Keyboard.getEventKey() == Keyboard.KEY_DELETE && Keyboard.getEventKeyState()) {
                GameEngine.stop();
            } else if (Keyboard.getEventKey() == Keyboard.KEY_P && Keyboard.getEventKeyState()) {
                Camera.solidRendering = !Camera.solidRendering;
            } else if (Keyboard.getEventKey() == Keyboard.KEY_V && Keyboard.getEventKeyState()) {
                Camera.VSync = !Camera.VSync;
                Display.setVSyncEnabled(Camera.VSync);
            } else if (Keyboard.getEventKey() == Keyboard.KEY_F && Keyboard.getEventKeyState()) {
                Camera.fog = !Camera.fog;
                if (!Camera.fog) {
                    GL11.glDisable(GL11.GL_FOG);
                } else {
                    GL11.glEnable(GL11.GL_FOG);
                }
            }
        }
        state.tick(delta);
    }

    /**
     * Renders the GameState.
     */
    public static void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        state.render();
    }

    /**
     * Stops the engine.
     */
    public static void stop() {
        running = false;
    }
}
