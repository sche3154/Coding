package stickman.model;

import java.util.ArrayList;
import java.util.List;

public class LevelImpl implements Level{

    Stickman stickman;
    List<Entity> entityList;
    List<Bullet> bulletsList;
    double floorHeight;
    double height;
    double width;
    boolean heroRight = false;
    boolean heroLeft = false;
    boolean won = false;
    boolean shooting = false;


    public LevelImpl(Entity stickman, List<Entity> entityList, double floorHeight, double height, double width){
        this.stickman = (Stickman) stickman;
        this.entityList = entityList;
        this.floorHeight = floorHeight;
        this.height = height;
        this.width = width;
        this.bulletsList = new ArrayList<Bullet>();
    }

    @Override
    public List<Entity> getEntities() {
        return this.entityList;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public void tick() {

    }

    @Override
    public double getFloorHeight() {
        return this.floorHeight;
    }

    @Override
    public double getHeroX() {
        return stickman.getXPos();
    }

    @Override
    public boolean jump() {
        if(getStickman().readyTodoanotherJump == true){
            this.getStickman().jumping = true;
        }
        return true;
    }

    @Override
    public boolean moveLeft() {
        this.heroLeft = true;
        return true;
    }

    @Override
    public boolean moveRight() {

        this.heroRight = true;
        return true;
    }

    @Override
    public boolean stopMoving() {

        this.heroRight = false;
        this.heroLeft =  false;
        return heroLeft & heroLeft;
    }

    @Override
    public boolean shooting() {
        this.shooting = true;

        return this.shooting;
    }

    @Override
    public void stopShooting() {
        this.shooting = false;
    }

    public boolean getShooting(){
        return this.shooting;
    }



    public Stickman getStickman() {
        return stickman;
    }


    @Override
    public void moveHero(){
        if(this.heroLeft){
            this.getStickman().moveLeft(1);
        }

        if(this.heroRight){
            this.getStickman().moveRight(1);
        }

    }

    @Override
    public void jumpTop() {

    }

    @Override
    public Entity getHero() {
        return this.stickman;
    }




    public void setWonToTrue()
    {
        this.won = true;
    }

    public boolean getWon(){
        return  this.won;
    }

    public List<Bullet> getBulletsList(){
        return this.bulletsList;
    }
}
