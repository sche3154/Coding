package stickman.entity.still;

import stickman.entity.Entity;
import stickman.entity.GameObject;
import stickman.model.GameManager;

/**
 * The win message displayed after collecting the flag.
 */
public class GameOver extends GameObject {

    /**
     * Constructs a new win object.
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public GameOver(double x, double y) {
        super("gameOver.png", x, y, 640, 400, Layer.EFFECT);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public Entity entityCopy() {
        return new GameOver(this.xPos, this.yPos);
    }
}