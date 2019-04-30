package stranded.game.state;

/**
 * A GameState may be used by the GameEngine.
 * 
 * @author Lukas
 */
public interface GameState {

    /**
     * Initializes this GameState.
     */
    public abstract void init();

    /**
     * Ticks this GameState. Should be called once every game loop.
     * 
     * @param delta time in seconds since the last tick.
     */
    public abstract void tick(double delta);

    /**
     * Renders the GameState. Should be called once every game loop.
     */
    public abstract void render();

    /**
     * Cleans up this GameState. Should be called before the state is deleted or set to null.
     */
    public abstract void dispose();
}
