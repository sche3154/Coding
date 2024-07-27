package stickman.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import stickman.entity.still.GameOver;
import stickman.level.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of GameEngine. Manages the running of the game.
 */
public class GameManager implements GameEngine, Originator {

    /**
     * The current level
     */
    private Level level;

    /**
     * List of all level files
     */
    private List<String> levelFileNames;

    /**
     * Count level index
     */
    int level_index;

    /**
     * Count current time
     */
    private int timeCount = 0;

    /**
     * The current life
     */
    private int life;

    /**
     * Total score
     */
    private static double total_score = 0;

    /**
     * Level score
     */
    private static double level_score;

    /**
     * Target Time
     */
    private static double target_Time;


    /**
     * Creates a GameManager object.
     * @param levels The config file containing the names of all the levels
     */
    public GameManager(String levels) {
        this.levelFileNames = this.readConfigFile(levels);

        level_index = 0;

        this.level = LevelBuilderImpl.generateFromFile(levelFileNames.get(0), this);

        life = LevelBuilderImpl.getInitialLife();

        target_Time  = level.getTargetTime();

    }

    @Override
    public Level getCurrentLevel() {
        return this.level;
    }

    @Override
    public boolean jump() {
        return this.level.jump();
    }

    @Override
    public boolean moveLeft() {
        return this.level.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return this.level.moveRight();
    }

    @Override
    public boolean stopMoving() {
        return this.level.stopMoving();
    }

    @Override
    public void tick() {
        this.level.tick();
        timeCount ++;

        if(timeCount * 0.017 <= target_Time){
            level_score += 1 * 0.017;
        }else{
            level_score -= 1 * 0.017;
            if(level_score <= 0){
                level_score = 0;
            }
        }

    }

    @Override
    public void shoot() {
        this.level.shoot();
    }

    @Override
    public void reset() {
        this.level = LevelBuilderImpl.generateFromFile(this.level.getSource(), this);

        this.life -= 1;

        if(life == 0){
            level.setActive(false);
        }

    }

    @Override
    public void levelTransfer() {
        if(level.getActive() == false){
            if(level_index < levelFileNames.size() - 1){
                this.level = LevelBuilderImpl.generateFromFile(levelFileNames.get(level_index+1), this);
                level_index +=1;

                timeCount = 0;
                this.total_score += level_score;
                this.level_score = 0;
            }
        }
    }

    @Override
    public int getTimeCount() {
        return timeCount;
    }

    @Override
    public int getPlayerLife() {
        return this.life;
    }

    @Override
    public double getTotalScore() {
        return total_score;
    }

    @Override
    public double getLevelScore() {
        return level_score;
    }

    /**
     * Retrieves the list of level filenames from a config file
     * @param config The config file
     * @return The list of level names
     */
    @SuppressWarnings("unchecked")
    private List<String> readConfigFile(String config) {

        List<String> res = new ArrayList<String>();

        JSONParser parser = new JSONParser();

        try {

            Reader reader = new FileReader(config);

            JSONObject object = (JSONObject) parser.parse(reader);

            JSONArray levelFiles = (JSONArray) object.get("levelFiles");

            Iterator<String> iterator = (Iterator<String>) levelFiles.iterator();

            // Get level file names
            while (iterator.hasNext()) {
                String file = iterator.next();
                res.add("levels/" + file);
            }

        } catch (IOException e) {
            System.exit(10);
            return null;
        } catch (ParseException e) {
            return null;
        }

        return res;
    }

    /**
     * Award score for killing enemy or eating mushrooms
     * @param score The score of thing player have killed
     */
    public static void addScore(int score){
        level_score += score;
    }

    @Override
    public Memento saveMemento() {
        return new Memento(((LevelManager) this.level).levelCopy()
                , this.levelFileNames, this.level_index, this.timeCount
                , this.life
                , this.target_Time, this.getLevelScore(), this.getTotalScore());
    }

    @Override
    public void loadMemento(Memento memento) {
        this.level = ((LevelManager) memento.getLevel()).levelCopy();
        this.levelFileNames = memento.getLevelFileNames();
        this.level_index = memento.getLevel_index();
        this.timeCount = memento.getTimeCount();
        this.life = memento.getLife();
        this.total_score = memento.getTotalScore();
        this.level_score = memento.getLevel_score();
        this.target_Time = memento.getTarget_Time();
    }
}