package invadem;

import org.junit.Test;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TankTest {

    @Test
    // test if you can construct a tank
    public void testTankConstruction() {
        PImage img = new PImage (1,1);
        Tank tank = new Tank( img,0, 0, new PApplet());
        assertNotNull(tank);
    }

    @Test
    // test if you successfully move left
    public void testTankMoveLeft() {
        PImage img = new PImage (1,1);
        Tank tank = new Tank( img,0, 0, new PApplet());
        tank.moveLeft();
        int x1 = tank.gettx();
        tank.moveLeft();
        int x2 = tank.gettx();
        boolean moveLeft = (x1 - x2) >=  0; // If the tank is at the side of the screen, It will not move again
        assertEquals(true, moveLeft);
    }

    @Test
    // test if you can successfully move right
    public void testTankMoveRight() {
        PImage img = new PImage (1,1);
        Tank tank = new Tank( img,0, 0, new PApplet());
        tank.moveRight();
        int x1 = tank.gettx();
        tank.moveLeft();
        int x2 = tank.gettx();
        boolean moveRight = (x1 - x2) <=  0; // If the tank is at the side of the screen, It will not move again
        assertEquals(true, moveRight);
    }


    @Test
    // test if you fire successfully
    public void testFire() {
        PImage img = new PImage (1,1);
        Tank tank = new Tank( img,0, 0, new PApplet());
        assertEquals(true , tank.fire());
    }


    @Test
    // test if tank is die the method tank.destroyed() will return true
    public void testTankIsNotDead() {
        PImage img = new PImage (1,1);
        Tank tank = new Tank( img,0, 0, new PApplet());
        if(tank.getLife() >= 0){
            assertEquals(false, tank.destoryed());
        }
        else{
            assertEquals(true, tank.destoryed());
        }

    }

}
