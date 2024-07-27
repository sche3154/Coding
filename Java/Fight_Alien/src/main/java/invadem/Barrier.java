package invadem;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Barrier {
    PApplet app;
    PImage img;

    int x;
    int y;
    int width;
    int height;
    int sprite; // to see which picture should we useh
    int maxHit;
    boolean destroyed;
    String fileName;

    public Barrier(PApplet app, PImage img, int x, int y, String fileName) {
        this.app = app;
        this.img = img;
        this.width = 8;
        this.height = 8;
        this.sprite = 1;
        this.x = x;
        this.y = y;
        this.destroyed = false;
        this.fileName = fileName;
        this.maxHit = 3;

    }

    public void draw() {
        app.image(this.img, x, y, this.width, this.height);
    }


    //change the sprite when being hit, After being hit three times, set the picture to empty
    public void beingHit(ArrayList<Projectile> bullets) {
        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getBulletX() >= this.x & bullets.get(i).getBulletX() <= this.x + 8) {
                if (bullets.get(i).getBulletY() >= this.y & bullets.get(i).getBulletY() <= this.y + 8) {
                    if(bullets.get(i).getDamage() == 3){// if its powerful bullet then it will crash the barrier or tank at once
                        this.sprite = 3;
                    }
                    if (this.sprite <= 3) {  // each component can sustain 3 hits
//                        int oldSprite = this.sprite;
                        if (this.sprite < 3) {
                            this.sprite += 1;
                            String newFileName = this.fileName.replace('1', Character.forDigit(sprite, 10)); // After being hit , change the sprite
                            this.img = app.loadImage(newFileName);// After being hit , change the sprite
                            bullets.get(i).setCrashed();// change the corresponding bullets' crash status
                            break;
                        } else {
                            sprite += 1;
                            this.img = app.loadImage("empty.png");
                            bullets.get(i).setCrashed();
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setSprite(int sprite){
        this.sprite = sprite;
    }


    public void setDestroyed(){
        if(this.sprite > 3){
            this.destroyed = true;
        }
    }

    public boolean getDestroyed(){
        return this.destroyed;
    }

    public int getMaxHit(){
        return this.maxHit;
    }

}
