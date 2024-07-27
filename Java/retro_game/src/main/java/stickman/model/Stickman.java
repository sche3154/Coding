package stickman.model;

import stickman.MovementStrategy.BulletMoveStrategy;
import stickman.CollisionStrategy.CollisionStrategy;
import stickman.CollisionStrategy.HeroCollisionStrategy;

public class Stickman extends EntityImpl{


    boolean onGround = true;
    boolean jumping = false;
    boolean readyTodoanotherJump = false;
    boolean ableToFire = false;
    double jumpHeight = 60;
    double currentGroundPositionY;
    double previousY;

    int life = 3;


    public Stickman(String size, String imagePath, double position_X, double position_Y,
                    double width, double height, Layer layer){

        this.size = size;
        this.imagePath = imagePath;
        this.position_X = position_X;
        this.position_Y = position_Y;
        this.width = width;
        this.height = height;
        this.layer = layer;
        this.currentGroundPositionY = this.position_Y;
        this.previousY = this.position_Y;
    }


    public void moveLeft(double velocity){

        this.position_X -= velocity;

    }

    public void moveRight(double velocity){

        this.position_X += velocity;

    }

    public void stopMoving(){

        this.position_X = this.position_X;
    }

    public void jump(double velocity ) {

        if(onGround){

            this.previousY = this.position_Y;

            this.position_Y -= 1;
            onGround = false;
            jumping = true;
            readyTodoanotherJump = false;

        }else if(jumping){

            readyTodoanotherJump = false;
            if(this.position_Y > this.previousY - this.jumpHeight){
                this.position_Y -= 1;
            }else{
                jumping = false;
            }
        }

    }

    public void fall(double velocity){

        if(onGround == false & jumping == false){
            if(this.position_Y < this.currentGroundPositionY) {
                this.position_Y += velocity;
            }else {
                onGround = true;
                readyTodoanotherJump = true;
            }
        }
    }


//    public void setCurrentGroundPositionY(double currentGroundPositionY){
//        this.currentGroundPositionY = currentGroundPositionY;
//    }

    public void setPreviousY(double y){
        this.previousY = y;
    }


    @Override
    public CollisionStrategy getCollisionStrategy(LevelImpl level) {
        return new HeroCollisionStrategy(level);
    }

    public int lifeLoss(){
        this.life = this.life -1 ;
        return  this.life;
    }

    public int getLife(){
        return this.life;
    }

    public void returnToOriginal(){
        this.position_X = 100;
        this.position_Y = 300 - 34;
    }

    public void setAbleToFire(){
        this.ableToFire = true;
    }

    public void fire(LevelImpl level){
        if(level.getBulletsList().size() < 2  & ableToFire == true){
            Bullet bullet= new Bullet("bullet.png", this.position_X + 8, this.position_Y + this.height/2, 8, 8, Layer.EFFECT, new BulletMoveStrategy());
            level.getEntities().add(bullet);
            level.getBulletsList().add(bullet);
        }
    }

    public void setOnGround(boolean bol){
        this.onGround = bol;
    }

    public  void setReadyTodoAnotherJump(boolean bol){
        this.readyTodoanotherJump = true;
    }

    public void setJumping(boolean bol){
        this.jumping = bol;
    }
}
