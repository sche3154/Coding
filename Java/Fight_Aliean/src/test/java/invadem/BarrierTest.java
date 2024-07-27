package invadem;

import org.junit.Test;
import processing.core.PApplet;
import processing.core.PImage;

import static org.junit.Assert.*;

public class BarrierTest {

    @Test
    // to test if you can build a barrier
    public void barrierConstruction() {
        PImage img = new PImage(1,1);;
        Barrier b =  new Barrier(new PApplet(),img, 320, 400, "barrier_left1.png");
        assertNotNull(b);
    }

    @Test
    // to test when barrier is destroyed if its destroyed status is true
    public void testBarrierNotDestroyed() {
        PImage img = new PImage(1,1);;
        Barrier b =  new Barrier(new PApplet(),img, 320, 400, "barrier_left1.png");
        b.setSprite(4);
        b.setDestroyed();
        assertEquals(true, b.getDestroyed());
    }

    @Test
    // to test the max hit times
    public void testBarrierHitPointsMax() {
        PImage img = new PImage(1,1);;
        Barrier b =  new Barrier(new PApplet(),img, 320, 400, "barrier_left1.png");
        assertEquals(3, b.getMaxHit());
    }


}
