package stickman.MovementStrategy;

import stickman.model.Entity;

public class BulletMoveStrategy implements MoveStrategy {

    double yMoveRange = 10;
    double xMoveRange = 32;
    double countXRange = 0;
    double countYRange = 0;
    double xVelocity = 0.8;
    double yVelocity = 0.4;

    boolean finishMoving = false;

    @Override
    public void moveStrategy(Entity bullet) {
        if(countXRange <= xMoveRange){
            bullet.setXpos(bullet.getXPos() + xVelocity);
            countXRange += 0.5;

            if(countXRange > xMoveRange){
                finishMoving = true;
            }
        }

        // bullet fall
        if(countYRange < yMoveRange ){
            bullet.setYpos(bullet.getYPos() + yVelocity);
            countYRange += yVelocity;
        }

    }


    public boolean getFinishMoving(){
        return this.finishMoving;
    }
}
