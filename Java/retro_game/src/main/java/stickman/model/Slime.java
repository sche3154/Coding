package stickman.model;

import stickman.CollisionStrategy.CollisionStrategy;
import stickman.CollisionStrategy.EnemyCollisionStrategy;
import stickman.MovementStrategy.MoveStrategy;

public class Slime extends EntityImpl{

    MoveStrategy moveStrategy;


    public Slime(String imagePath, double position_X, double position_Y,
                    double width, double height, Layer layer, MoveStrategy moveStrategy){

        this.imagePath = imagePath;
        this.position_X = position_X;
        this.position_Y = position_Y;
        this.width = width;
        this.height = height;
        this.layer = layer;
        this.moveStrategy = moveStrategy;
    }

    @Override
    public CollisionStrategy getCollisionStrategy(LevelImpl level) {
        return new EnemyCollisionStrategy(level);
    }


    public void move(){
        this.moveStrategy.moveStrategy(this);
    }
}
