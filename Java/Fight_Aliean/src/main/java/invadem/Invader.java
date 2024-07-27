package invadem;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Invader {
    private PApplet app;
    private PImage img1; // this image will be used in moving horizontally
    private PImage img2; // this image will be used in moving downward
    private String imgOfBullet; // this image will determine which bullet will be used
    private int frameX;
    private int frameY;
    private int x; // location in x directions
    private int y; // location in y directions
    private int height;
    private int width;
    private int speed;
    private int life;
    private boolean movingDown; // to check if the invader are moving down
    private int movingSideSteps; // if it equal to 30, change side way moving direction
    private int movingDownSteps; // If it equal to 8, stop moving down and began moving side ways again
    private int points;




    public Invader(PApplet app, PImage img1, PImage img2, String imgOfBullet, int frameX, int frameY, int x, int y, int life, int points){
        this.app = app;
        this.img1 = img1;
        this.img2 = img2;
        this.imgOfBullet = imgOfBullet;
        this.frameX = frameX;
        this.frameY = frameY;
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 16;
        this.speed = 1;
        this.life = life;
        this.movingDown = false;
        this.movingSideSteps = 0;
        this.movingDownSteps = 0;
        this.points = points;
    }

    public void draw(){
        if(movingDown){
            app.image(this.img2, this.x, this.y, this.width, this.height);// if its moving down, use invader2.png
        }else{
            app.image(this.img1, this.x, this.y, this.width, this.height);// if its moving down, use invader1.png
        }

    }

    public void move(){
        if(app.frameCount % 2 == 0) {
            if (movingDown == false) { // moving side ways
                if (movingSideSteps < 30) {
                    this.x += this.speed;
                    movingSideSteps += 1;
                } else { // Have completed moving 30 steps in side way directions
                    movingDown = true;
                    movingSideSteps = 0;
                    this.speed = -1 * this.speed;
                }
            } else { // moving downwards
                if (movingDownSteps < 8) {
                    this.y += 1;
                    movingDownSteps += 1;
                } else { // Have completed moving downwards
                    movingDownSteps = 0;
                    movingDown = false;
                }
            }
        }
    }

    //if the invader has being hit, set its' life to 0, and change  the corresponding bullet's crashed status
    public void beingHit(ArrayList<Projectile> projectiles){
        for(int i = 0; i < projectiles.size(); i++){
            // the collision area is 16*16
            if(projectiles.get(i).getBulletX() >= this.x && projectiles.get(i).getBulletX() <= this.x + 20){
                if(projectiles.get(i).getBulletY() >= this.y && projectiles.get(i).getBulletY() <= this.y + 20){
                    this.life -= projectiles.get(i).getDamage();
                    projectiles.get(i).setCrashed();
                    break;
                }
            }
        }//projectiles.get(i).getBulletY() >= this.y && projectiles.get(i).getBulletY() <= this.y + 16 &&
    }

    public boolean fire(ArrayList<Projectile> bullets){
        boolean fireSuccess;
        try{
            Projectile bullet = new Projectile(this.app, app.loadImage(imgOfBullet), this.x + 8, this.y + 16, this.frameY, this.frameX, imgOfBullet);
            bullets.add(bullet);
            fireSuccess = true;
            return fireSuccess;
        }catch(Exception ex){
            fireSuccess = true;
            return  fireSuccess;
        }

    }


    public int getLife(){
        return this.life;
    }

    public void setLife(){
        this.life = -1;
    }


    public boolean invadeSucces(){
        boolean success = false;
        if(this.y >= this.frameY){
            success = true;
        }
        return success;
    }

    public void setY(int Y){
        this.y = Y;
    }

    public int getPoints(){
        return this.points;
    }



}