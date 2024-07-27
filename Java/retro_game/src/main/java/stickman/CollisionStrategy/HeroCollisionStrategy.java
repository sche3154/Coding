package stickman.CollisionStrategy;

import javafx.geometry.Point2D;
import stickman.model.*;

public class HeroCollisionStrategy implements CollisionStrategy {


    LevelImpl currentLevel;

    public HeroCollisionStrategy(LevelImpl level){
        this.currentLevel = level;
    }

    @Override
    public int handleCollision(EntityImpl hero, Entity other){

        if(other instanceof Bullet){

            return 0;
        }

        nextLevel(other);
        ateMushroom(other);

        Point2D heroPosition = new Point2D(hero.getXPos(), hero.getYPos());
        Point2D otherPosition = new Point2D(other.getXPos(), other.getYPos());

        Point2D collisionVector = otherPosition.subtract(heroPosition);

         // horizontal collision
        if (Math.abs(collisionVector.getX()) > Math.abs(collisionVector.getY())) {
            //    move left then collision
            if(hero.getXPos() - (other.getXPos() + other.getWidth()) < 0 & (hero.getXPos() > other.getXPos())){
                hero.setXpos(other.getXPos() + other.getWidth());

            }
            // move right and collision
            else if((hero.getXPos() + hero.getWidth())  - other.getXPos() > 0 & (hero.getXPos() + hero.getWidth() < (other.getXPos() + other.getWidth()))){

                hero.setXpos(other.getXPos() - hero.getWidth());
            }

            if(CollideWithSlime(other)){
                return 1;
            }
        }
        // vertical collision
        else{
            // fallDown then collision
            if(((hero.getYPos() + hero.getHeight()) > other.getYPos()) & (hero.getYPos() < other.getYPos()) ){
                hero.setYpos(other.getYPos() - hero.getHeight());

                this.currentLevel.getStickman().setPreviousY(hero.getYPos());
                this.currentLevel.getStickman().setOnGround(true);
                this.currentLevel.getStickman().setReadyTodoAnotherJump(true);
                this.currentLevel.getStickman().setJumping(false);

                killSlime(other);

            }
            // jump up
            else if((hero.getYPos() < (other.getYPos() + other.getHeight())) & (hero.getYPos() + hero.getHeight() > (other.getYPos() + other.getHeight()))){
                hero.setYpos(other.getYPos() + other.getHeight());

                this.currentLevel.getStickman().setJumping(false);

            }
        }

        return 0;
    }


    boolean CollideWithSlime(Entity other){
        if(other instanceof Slime){
            currentLevel.getStickman().lifeLoss();
            int i = currentLevel.getStickman().getLife();

            currentLevel.getStickman().returnToOriginal();

        }

        return false;
    }

    boolean killSlime(Entity other){
        if(other instanceof  Slime){
            currentLevel.getEntities().remove(other);
        }

        return  true;
    }


    void nextLevel(Entity other){
        if(other instanceof  FinishFlag){
            currentLevel.setWonToTrue();
        }

    }

    void ateMushroom(Entity other){
        if(other instanceof  MushRoom){
            currentLevel.getStickman().setAbleToFire();
            currentLevel.getEntities().remove(other);
        }
    }


}
