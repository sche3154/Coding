package invadem;

import org.junit.Test;
import processing.core.PApplet;
import processing.core.PImage;

import static org.junit.Assert.*;

public class ProjectileTest {


    @Test
    // to test if you can build a projectile
    public void testProjectileConstruction() {
        PImage img = new PImage(1,1);
        Projectile proj = new Projectile(new PApplet(), img, 320, 480, 640, 480, "projectile.png");
        assertNotNull(proj);
    }

    @Test
    // test the movement of projectile fired by tank
    public void testProjectileIsFiredByTank() {
        PImage img = new PImage(1,1);
        Projectile proj = new Projectile(new PApplet(), img, 320, 480, 640, 480, "projectile.png");
        int bulletY1 = proj.getBulletY();
        proj.firedByTank();
        int bulletY2 = proj.getBulletY();
        boolean moveUp = (bulletY1 - bulletY2) >= 0;
        assertEquals(true, moveUp);
    }

    @Test
    // test the movement of projectile fired by invaders
    public void testProjectileIsFiredByInvaders() {
        PImage img = new PImage(1,1);
        Projectile proj = new Projectile(new PApplet(), img, 320, 240, 640, 480, "projectile.png");
        int bulletY1 = proj.getBulletY();
        proj.firedByInvader();
        int bulletY2 = proj.getBulletY();
        boolean moveDown = (bulletY1 - bulletY2) <= 0;
        assertEquals(true, moveDown);
    }

    @Test
    // to test when projectile intersect with something the crash status will be true
    public void testProjectileIntersect() {
        PImage img = new PImage(1,1);
        Projectile proj = new Projectile(new PApplet(), img, 320, 240, 640, 480, "projectile.png");
        proj.setCrashed();
        assertEquals(true, proj.getCrashed());
    }

}
