package stickman.model;

import stickman.MovementStrategy.BulletMoveStrategy;
import stickman.CollisionStrategy.CollisionStrategy;
import stickman.MovementStrategy.MoveStrategy;

public class Bullet extends EntityImpl{

    BulletMoveStrategy bulletMoveStrategy;

    public Bullet(String imagePath, double position_X, double position_Y,
                    double width, double height, Layer layer, MoveStrategy bulletMoveStrategy){

        this.imagePath = imagePath;
        this.position_X = position_X;
        this.position_Y = position_Y;
        this.width = width;
        this.height = height;
        this.layer = layer;
        this.bulletMoveStrategy = (BulletMoveStrategy) bulletMoveStrategy;
    }


    @Override
    public CollisionStrategy getCollisionStrategy(LevelImpl level) {
        return null;
    }



    public void move(){
        bulletMoveStrategy.moveStrategy(this);
    }


    public boolean whetherFinishMoving(){
       return bulletMoveStrategy.getFinishMoving();
    }

}
