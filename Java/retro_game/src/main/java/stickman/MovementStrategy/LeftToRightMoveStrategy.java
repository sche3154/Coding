package stickman.MovementStrategy;

import stickman.model.Entity;

public class LeftToRightMoveStrategy implements MoveStrategy {

    double moveRange =  32;
    double count = 0;
    boolean turnAnotherDirection = false;


    @Override
    public void moveStrategy(Entity slime) {

        // move left at first
       if(turnAnotherDirection == false & count < moveRange){
           slime.setXpos(slime.getXPos() - 0.1);
           count += 0.1;
           if(count >= moveRange){
               turnAnotherDirection = true;
               count = 0;
           }
       }
       // move right then
       else if(turnAnotherDirection == true & count < moveRange){
           slime.setXpos(slime.getXPos() + 0.1);
           count += 0.1;

           if(count >= moveRange){
               turnAnotherDirection = false;
               count = 0;
           }
       }
    }
}
