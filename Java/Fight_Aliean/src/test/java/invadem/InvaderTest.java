package invadem;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class InvaderTest {

    @Test
    // to test if you can build an invader
    public void testInvaderConstruction() {
        PImage img = new PImage(1,1);
        PImage img2 = new PImage(1,1);
        Invader inv = new Invader(new PApplet(), img, img2, "projectile.png", 640, 480, 320, 240, 1, 100 );
        assertNotNull(inv);

    }

    @Test
    public void testInvadeSucces() {
        PImage img = new PImage(1,1);
        PImage img2 = new PImage(1,1);
        Invader inv = new Invader(new PApplet(), img, img2, "projectile.png", 640, 480, 320, 240, 1, 100 );
        inv.setY(480);
        assertEquals(true, inv.invadeSucces());

    }

    @Test
    public void testInvadeFire() {
        PImage img = new PImage(1,1);
        PImage img2 = new PImage(1,1);
        Invader inv = new Invader(new PApplet(), img, img2, "projectile.png", 640, 480, 320, 240, 1, 100 );
        ArrayList<Projectile> bullets = new ArrayList<Projectile>();
        assertEquals(true, inv.fire(bullets));
    }



}
