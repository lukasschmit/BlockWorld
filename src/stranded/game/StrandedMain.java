package stranded.game;

import org.lwjgl.opengl.Display;

import stranded.graphics.Art;

/**
 * The main class.
 * 
 * @author Lukas
 */
public class StrandedMain {

    /**
     * The main method for the game. Starts off the engine and deals with uncaught exceptions.
     * 
     * @param args command-line arguments. The first should be the terrain generation mode.
     */
    public static void main(String[] args) {
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            GameEngine.init();

            GameEngine.run();
            System.out.println("Engine exited. Program will now terminate.");

            dispose();
            System.exit(0);
        } catch (Exception ex) {
            System.err.println("UNCAUGHT EXCEPTION. Program will now terminate.");
            ex.printStackTrace(System.err);
            dispose();
            System.exit(-1);
        }
    }

    /**
     * Cleans up all the program. Should be run before it quits.
     */
    public static void dispose() {
        Art.dispose();
        GameEngine.state.dispose();
        Display.destroy();
    }
}
