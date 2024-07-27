package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import stickman.CollisionStrategy.CollisionStrategy;
import stickman.MovementStrategy.LeftToRightMoveStrategy;
import stickman.MovementStrategy.RightToLeftMoveStrategy;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameEngineImpl implements GameEngine{

    JSONArray jsonArray;
    Level current_level;
    List<Level> levelList;


    public GameEngineImpl(String fileName){

        levelList = new ArrayList<Level>();

        try {
            JSONParser jsonParser = new JSONParser();

            this.jsonArray = (JSONArray) jsonParser.parse(new FileReader(fileName));

            for (Object object: jsonArray) {

                List<Entity> entityList = new ArrayList<Entity>() ;
                Stickman stickman;

                JSONObject jsonObj = (JSONObject) object;

                String stickmanSize = (String) jsonObj.get("stickmanSize");
                double floorHeight = (double) (long) jsonObj.get("floorHeight");
                double levelHeight = (double) (long) jsonObj.get("levelHeight");
                double levelWidth = (double) (long) jsonObj.get("levelWidth");

                                                                                            // begin to parse platform
                JSONArray platforms = (JSONArray) jsonObj.get("platforms");

                for(Object platformObject: platforms){

                    JSONObject jsonPlat = (JSONObject) platformObject;

                    int size = (int) (long) jsonPlat.get("size");

                    int startPositionX = (int) (long) jsonPlat.get("startPositionX");

                    int startPositionY = (int) (long) jsonPlat.get("startPositionY");

                    for(int i = 0; i < size ; i++){
                        Platform platform = new Platform("foot_tile.png",
                                                         startPositionX +16*i ,
                                                         startPositionY  ,
                                                        16,
                                                        16,
                                                         Entity.Layer.EFFECT );

                        entityList.add(platform);
                    }
                }
                                                                                            // Finish


                                                                                            // Begin to parse SLimes

                JSONArray slimes = (JSONArray) jsonObj.get("slimes");

                int count = 0;
                for(Object slimeObject: slimes){

                    JSONObject jsonSlime = (JSONObject) slimeObject;

                    String imagePath = (String) jsonSlime.get("ImagePath");

                    double startPositionX = (double) (long) jsonSlime.get("startPositionX");

                    double startPositionY = (double) (long) jsonSlime.get("startPositionY");

                    if(count  % 2 == 0){
                        Slime slime = new Slime(imagePath, startPositionX, startPositionY -16 ,16, 16, Entity.Layer.EFFECT, new LeftToRightMoveStrategy());
                        count += 1;
                        entityList.add(slime);
                    }else {
                        Slime slime = new Slime(imagePath, startPositionX, startPositionY -16 ,16, 16, Entity.Layer.EFFECT, new RightToLeftMoveStrategy());
                        count += 1;
                        entityList.add(slime);
                    }




                }

                double flagY = (double) (long) jsonObj.get("FlagY");
                double flagX = (double) (long) jsonObj.get("FlagX");

                FinishFlag finishFlag = new FinishFlag("red Flag.png", flagX, flagY - 16, 16, 16, Entity.Layer.EFFECT);

                entityList.add(finishFlag);

                JSONArray mushRooms = (JSONArray) jsonObj.get("mushRooms");



                for(Object mushRoomObject: mushRooms){

                    JSONObject jsonMushRoom = (JSONObject) mushRoomObject;

                    double startPositionX = (double) (long) jsonMushRoom.get("mushRoomX");

                    double startPositionY = (double) (long) jsonMushRoom.get("mushRoomY");

                    double height = (double) (long) jsonMushRoom.get("height");

                    double width = (double) (long) jsonMushRoom.get("width");

                    MushRoom mushRoom = new MushRoom("Mushroom.png", startPositionX, startPositionY -height ,height, width, Entity.Layer.EFFECT);

                    entityList.add(mushRoom);
                }

                if(stickmanSize.equals("normal") ){
                    stickman = new Stickman("normal", "ch_stand1.png", 100, floorHeight-34, 20, 34, Entity.Layer.EFFECT);
                }else{
                    stickman = new Stickman("large", "ch_stand1.png", 100, floorHeight -68, 40, 68, Entity.Layer.EFFECT);
                }

                entityList.add(stickman);

                Level level = new LevelImpl(stickman, entityList, floorHeight, levelHeight, levelWidth) ;

                current_level = level;
                levelList.add(level);
            }

        } catch (IOException  | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }



    @Override
    public stickman.model.Level getCurrentLevel() {
        return this.current_level;
    }

    @Override
    public void startLevel() {

    }

    @Override
    public boolean jump() {

        current_level.jump();
        return true;
    }

    @Override
    public boolean moveLeft() {

        current_level.moveLeft();

        return true;
    }

    @Override
    public boolean moveRight() {

        current_level.moveRight();

        return true;
    }

    @Override
    public boolean stopMoving() {

        current_level.stopMoving();

        return true;
    }

    @Override
    public boolean shooting() {
        this.current_level.shooting();
        return false;
    }

    @Override
    public void stopShooting() {
        this.current_level.stopShooting();
    }

    @Override
    public void tick() {
        LevelImpl currentLevel = (LevelImpl) getCurrentLevel();


        if(currentLevel.getWon()){
            currentLevel.getEntities().removeAll(currentLevel.getEntities());
            currentLevel.getEntities().add(new YouWon("youwin.png", 0,0,640, 400, Entity.Layer.BACKGROUND));
        }else {



                if(currentLevel.getShooting()){
                    currentLevel.getStickman().fire(currentLevel);
                }

                if(currentLevel.getStickman().jumping){
                    currentLevel.getStickman().jump(1);
                }

                currentLevel.getStickman().onGround = false;

                try{
                    for(Bullet bullet : currentLevel.getBulletsList()){
                        bullet.move();

                        if(bullet.whetherFinishMoving()){
                            currentLevel.getBulletsList().remove(bullet);
                            currentLevel.getEntities().remove(bullet);
                        }
                    }
                }catch (Exception exception){

                }



                CollisionStrategy heroCollisionHandler = currentLevel.getStickman().getCollisionStrategy(currentLevel);

                try{
                    for(Entity other : currentLevel.getEntities()){
                        if(other instanceof Slime){
                            ((Slime) other).move();

                            CollisionStrategy slimeCollisionHandler = ((Slime) other).getCollisionStrategy(currentLevel);

                            for(Bullet bullet : currentLevel.getBulletsList()){
                                if(CollisionStrategy.whetherIntersect((Slime) other, bullet)){
                                    slimeCollisionHandler.handleCollision(((Slime) other), bullet);
                                }
                            }
                        }

                        if(CollisionStrategy.whetherIntersect(currentLevel.getStickman(), other)){
                            heroCollisionHandler.handleCollision(currentLevel.getStickman(), other);
                        }


                    }
                }catch (Exception exception){
                    for(Entity other : currentLevel.getEntities()){
                        if(other instanceof Slime){
                            ((Slime) other).move();
                        }

                        if(CollisionStrategy.whetherIntersect(currentLevel.getStickman(), other)){
                            heroCollisionHandler.handleCollision(currentLevel.getStickman(), other);
                        }

                    }
                }


                currentLevel.getStickman().fall(1);

                currentLevel.moveHero();



        }




    }


}
