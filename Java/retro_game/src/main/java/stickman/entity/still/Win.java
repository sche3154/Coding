package stickman.entity.still;

import stickman.entity.Entity;
import stickman.entity.GameObject;

/**
 * The win message displayed after collecting the flag.
 */
public class Win extends GameObject {

    /**
     * Constructs a new win object.
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Win(double x, double y) {
        super("victory.png", x, y, 400, 400, Layer.EFFECT);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    @Override
    public Entity entityCopy() {
        return new Win(this.xPos, this.yPos);
    }
}
