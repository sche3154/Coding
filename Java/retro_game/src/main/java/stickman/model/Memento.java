package stickman.model;

import stickman.level.Level;

import java.util.List;

public class Memento {

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
    private int timeCount;

    /**
     * The current life
     */
    private int life;

    /**
     * Total score
     */
    private static double total_score;

    /**
     * Level score
     */
    private static double level_score;

    /**
     * Target Time
     */
    private static double target_Time;



    public Memento(Level level, List<String> levelFileNames, int level_index, int timeCount, int life, double target_Time, double level_score, double total_score){
        this.level = level;
        this.levelFileNames = levelFileNames;
        this.level_index = level_index;
        this.timeCount = timeCount;
        this.life = life;
        this.target_Time = target_Time;
        this.level_score = level_score;
        this.total_score = total_score;
    }


    public Level getLevel(){
        return this.level;
    }

    public List<String> getLevelFileNames(){
        return this.levelFileNames;
    }

    public int getLevel_index(){
        return this.level_index;
    }

    public int getTimeCount(){
        return this.timeCount;
    }

    public int getLife(){
        return this.life;
    }

    public double getTotalScore(){
        return this.total_score;
    }


    public double getLevel_score(){
        return this.level_score;
    }

    public double getTarget_Time(){
        return this.target_Time;
    }

}
