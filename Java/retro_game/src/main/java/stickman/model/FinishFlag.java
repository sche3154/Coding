package stickman.model;

import stickman.CollisionStrategy.CollisionStrategy;

public class FinishFlag extends EntityImpl {



    public FinishFlag(String imagePath, double position_X, double position_Y,
                    double width, double height, Layer layer){

        this.imagePath = imagePath;
        this.position_X = position_X;
        this.position_Y = position_Y;
        this.width = width;
        this.height = height;
        this.layer = layer;
    }
    @Override
    public CollisionStrategy getCollisionStrategy(LevelImpl level) {
        return null;
    }
}
