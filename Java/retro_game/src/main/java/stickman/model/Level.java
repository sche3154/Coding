package stickman.model;

import java.util.List;

public interface Level {
    List<Entity> getEntities();
    double getHeight();
    double getWidth();

    void tick();

    double getFloorHeight();
    double getHeroX();

    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();
    boolean shooting();

//    boolean isHeroRight();
//    boolean isHeroLeft();

    void moveHero();

    void jumpTop();

    Entity getHero();

    void stopShooting();
}
