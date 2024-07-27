package stickman.CollisionStrategy;

import stickman.model.*;

public class EnemyCollisionStrategy implements CollisionStrategy {

    LevelImpl currentLevel;

    public EnemyCollisionStrategy(LevelImpl level){
        this.currentLevel = level;
    }

    @Override
    // collide with bullet
    public int handleCollision(EntityImpl a, Entity other) {
        if(other instanceof Bullet){
            currentLevel.getEntities().remove(a);
            currentLevel.getEntities().remove(a);
        }

        return 1;
    }



}
