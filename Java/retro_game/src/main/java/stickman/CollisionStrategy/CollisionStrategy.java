package stickman.CollisionStrategy;

import stickman.model.Entity;
import stickman.model.EntityImpl;

public interface CollisionStrategy {

    public static boolean whetherIntersect(EntityImpl a, Entity b){

        //  himself
        if(a.equals(b)){
            return false;
        }

        // do not care background
        if(b.getLayer().equals(Entity.Layer.BACKGROUND)){
            return false;
        }

        return (a.getXPos() < (b.getXPos() + b.getWidth())) &
                ((a.getXPos() + a.getWidth()) > b.getXPos()) &
                (a.getYPos() < (b.getYPos() + b.getHeight())) &
                ((a.getYPos() + a.getHeight()) > b.getYPos());


//        return (box1.x < (box2.x + box2.width)) and
//                ((box1.x + box1.width) > box2.x) and
//                (box1.y < (box2.y + box2.height)) and
//                ((box1.y + box1.height) > box2.y)
    }


    public int handleCollision(EntityImpl a, Entity other);
}
