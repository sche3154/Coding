package stickman.model;

import stickman.CollisionStrategy.CollisionStrategy;

public interface Entity {
    String getImagePath();
    double getXPos();
    double getYPos();
    double getHeight();
    double getWidth();

    void setXpos(double X);
    void setYpos(double Y);

    CollisionStrategy getCollisionStrategy(LevelImpl level);

    Layer getLayer();

    enum Layer{
        BACKGROUND, FOREGROUND, EFFECT
    }
}
