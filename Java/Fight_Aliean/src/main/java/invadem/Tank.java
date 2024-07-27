package invadem;

import processing.core.PImage;
import processing.core.PApplet;

import java.util.ArrayList;


public class Tank  {
    private PImage img;// the tank picture
    private int tx;// the tank location in x direction
    private int ty;// the tank location in y direction
    private int width; // the width of the tank
    private int height; // the height of the tank
    private int speed; // the speed of the tank
    private int life; // the life of the tank
    private int maxLife;
    private int framex; // the width of the current Applet
    private int framey;// the height of the current Applet
    private PApplet app;// the current Applet
    private ArrayList<Projectile> tankBullets;



    public Tank(PImage img, int fx, int fy, PApplet app){
        this.app = app;
        this.framex = fx;
        this.framey = fy;
        this.img = img;
        this.width = 22;
        this.height = 14;
        this.tx = fx/2 - this.width/2; // the original location is at the middle-bottom
        this.ty = fy - this.height*2;  // the original location is at the middle-bottom
        this.life = 3;
        this.maxLife = 3;
        this.speed = 1; // set the speed to 1 pixel
        this.tankBullets = new ArrayList<Projectile>();
    }

    public void draw(){
        if(this.life > 0){
            app.image(this.img, this.tx, this.ty, this.width, this.height);
        }
    }

    // move the tank to left
    public void moveLeft(){
        //check the boundary
        if(this.tx <= 0){
            this.tx = this.tx;// can't move outside the left boundary
        }else {
            this.tx -= this.speed;// move the location by speed
        }

    }

    // move the tank to right
   public void moveRight() {//check the boundary
        if (this.tx >= (this.framex - this.width)) {
            this.tx = this.tx;// can't move outside the right boundary
        } else {
            this.tx += this.speed;// move the location by speed
        }
    }


    // if we press and release f then create a new bullet
   public boolean fire() {
        boolean fireSuccess;
        try{
            Projectile tankBullet = new Projectile(this.app, app.loadImage("projectile.png"), (this.tx + this.width/2), this.ty, this.framey, this.framex, "projectile.png");
            this.tankBullets.add(tankBullet);
        }catch (Exception ex){
            fireSuccess = true;
            return fireSuccess;
        }
        fireSuccess = true;
        return fireSuccess;
    }


    public void moveBullets(){ // update the location of the bullets and remove unvisible  bullet
        // if bullets outside the screen
        for(int i = 0; i < tankBullets.size(); i++){
            if(tankBullets.get(i).getBulletY() == 0){
                tankBullets.remove(i);//remove the bullet that reach the boundary
            }
        }
        // if the crashed status is ture
        for(int i = 0; i < tankBullets.size(); i++){
            if(tankBullets.get(i).getCrashed() == true){
                tankBullets.remove(i);//remove the bullet that reach the boundary
            }
        }
        for(int i = 0; i < tankBullets.size(); i++){
            tankBullets.get(i).firedByTank();
        }
    }

    // display all the bullets
    public void drawBullets(){
        for(int i = 0; i < tankBullets.size(); i++){
            tankBullets.get(i).draw();
        }
    }

    public void beingHit(ArrayList<Projectile> invaderBullets){ // each time the tank has been hit it will lose one 1 life
        for(int i = 0; i < invaderBullets.size(); i++){
            //To check if any projectiles has hit the tank
            if(invaderBullets.get(i).getBulletX() > this.tx && invaderBullets.get(i).getBulletX() < this.tx + this.width )
                if(invaderBullets.get(i).getBulletY() >= this.ty && invaderBullets.get(i).getBulletY() <= this.ty + this.height){
                    this.life -= invaderBullets.get(i).getDamage(); // lose one life or three life
                    invaderBullets.get(i).setCrashed();
                    break;
                }
        }
    }


    public int getty(){
        return  this.ty;
    }

    public int gettx() {
        return  this.tx;
    }

    public ArrayList<Projectile> getTankBullets(){
        return this.tankBullets;
    }

    public boolean destoryed(){
        boolean destroy = false;
        if(this.life <= 0){
            destroy = true;
        }
        return destroy;
    }

    public int  getLife(){
        return this.life;
    }

    public void addLife(){
        this.life += 1;
    }

    public void update(){
        this.img = app.loadImage("tank2.png");
        maxLife += 1;
    }

    public int getMaxLife(){
        return maxLife;
    }







}


