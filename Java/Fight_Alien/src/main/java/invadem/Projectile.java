package invadem;

import processing.core.PApplet;
import processing.core.PImage;

public class Projectile {
    private PApplet app; // the current PApplet
    private PImage img;
    private int speed; //  the bullet speed
    private int width;
    private int height;
    private int bulletX; //  x location of bullet being fired
    private int bulletY;
    private int frameX;
    private int frameY; //the current height of PApplet
    private boolean crashed; // if the bullet has hit something
    private int damage;
    private String fileName;


    public Projectile(PApplet app, PImage img, int tx, int ty, int frameY, int frameX, String fileName){//tx, ty means the current location of the tank
        this.app = app;
        this.img = img;
        this.speed = 1; // set the bullet speed to 1 pixel
//        this.width = 1;
//        this.height = 3;
        this.bulletX = tx;
        this.bulletY = ty;
        this.crashed = false;// haven't hit something
        this.frameY = frameY;
        this.frameX = frameX;
        this.fileName = fileName;
        if(this.fileName.equals("projectile.png")){ // to see if its a powerful bullet
            this.damage = 1;
        }else{
            this.damage = 3;
        }


    }

    //update the location of bullets that are fired by tank
    public void firedByTank(){ // update the location of the bullet fired by tank
        //to check if the bullet has been outside screen
        if(this.bulletY > 0 && crashed == false) {
            this.bulletY -= this.speed;
        }
    }

    //update the location of bullets that are fired by invaders
    public void firedByInvader(){ // update the location of the bullet fired by Invader
        //to check if the bullet has been outside screen
        if(this.bulletY < frameY && crashed == false) {
            this.bulletY += this.speed;
        }
    }


    // draw the bullet on the screen
    public void draw(){
        this.app.image(img, this.bulletX, this.bulletY);
    }

    public int getBulletY(){
        return  this.bulletY;
    }

    public int getBulletX(){
        return  this.bulletX;
    }

    public void setCrashed(){
        this.crashed = true;
    }

    public boolean getCrashed(){
        return this.crashed;
    }

    public int getDamage(){
        return this.damage;
    }

}
