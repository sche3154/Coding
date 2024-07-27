package invadem;


import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import processing.core.PApplet;
import processing.core.PFont;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;


public class App extends PApplet {
    private int width = 640;
    private int height = 480;
    private Tank tank;
//    private ArrayList<Barrier> leftBarriers; // manage all the left barriers
//    private ArrayList<Barrier> topBarriers;// manage all the top barriers
//    private ArrayList<Barrier> rightBarriers;// manage all the right barriers
//    private ArrayList<Barrier> solidBarriers;// manage all the solid barriers
    private ArrayList<Invader> swarms; // manage all the invaders
    private ArrayList<Barrier> barriers; // manage all the three barriers and its 7 components
    private ArrayList<Projectile> firedBySwarm; // manage the bullets fired by swarm // Every 5 seconds, an invader will be randomly selected to fire a projectile downwards.
    private int spacePressedTimes; // Each time the space is pressed or released add 1
    private int lpressedTimes;
    private int bpressedTimes;
    private int cpressedTimes;
    private boolean leftPressed ;
    private boolean rightPressed;
    private ArrayList<Integer> seconds;
    private int level; // To record the current level, and revise the difficulty by it
    private boolean gameOver; //To record the current gameState
    private PFont font;
    private static int highScore = 10000; // Set the default high score
    private int currentScore;
    private int money; // when you destroy a invader you get some money


    public App() {
        //Set up your objects
//        leftBarriers = new ArrayList<Barrier>();
//        topBarriers = new ArrayList<Barrier>();
//        rightBarriers = new ArrayList<Barrier>();
//        solidBarriers = new ArrayList<Barrier>();
        barriers = new ArrayList<Barrier>();
        swarms = new ArrayList<Invader>();
        firedBySwarm = new ArrayList<Projectile>();
        spacePressedTimes = 0;
        lpressedTimes = 0;
        bpressedTimes = 0;
        cpressedTimes = 0;
        leftPressed = false;
        rightPressed = false;
        level = 1;
        currentScore = 0;
        gameOver = false;
        money = 0;


    }

    public void setup() { // reset all the things
        frameRate(60);
        barriers = new ArrayList<Barrier>();
        swarms = new ArrayList<Invader>();
        firedBySwarm = new ArrayList<Projectile>();
        //set up the tank
        tank = new Tank(loadImage("tank1.png"), this.width, this.height, this);

        for(int i = 0; i < 3; i++){
            barriers.add(new Barrier(this, loadImage("barrier_left1.png"), 90 + 218*i, tank.getty() - 34, "barrier_left1.png"));
            barriers.add(new Barrier(this, loadImage("barrier_top1.png"), 98 + 218*i, tank.getty() - 34, "barrier_top1.png"));
            barriers.add(new Barrier(this, loadImage("barrier_right1.png"), 106 + 218*i, tank.getty() - 34, "barrier_right1.png"));
            barriers.add(new Barrier(this, loadImage("barrier_solid1.png"), 90 +218*i, tank.getty() - 18, "barrier_solid1.png"));
            barriers.add(new Barrier(this, loadImage("barrier_solid1.png"), 90 +218*i, tank.getty() - 26, "barrier_solid1.png"));
            barriers.add(new Barrier(this, loadImage("barrier_solid1.png"), 106 +218*i, tank.getty() - 18,"barrier_solid1.png"));
            barriers.add(new Barrier(this, loadImage("barrier_solid1.png"), 106 +218*i, tank.getty() - 26, "barrier_solid1.png"));
        }
        for(int i = 0; i < 4; i++){// count the row
            for(int j = 0; j < 10; j++){ // each row should have 10 invaders
                if(i == 0) {// the first row from the top is the armoured invader
                    swarms.add(new Invader(this, loadImage("invader1_armoured.png"), loadImage("invader2_armoured.png"), "projectile.png", this.width, this.height, 80 + j * 48, i * 48, 3, 250));
                }else if(i == 1){
                    swarms.add(new Invader(this, loadImage("invader1_power.png"), loadImage("invader2_power.png"), "projectile_lg.png", this.width, this.height,  80 + j*48, i*48, 1, 250));
                }else{
                    swarms.add(new Invader(this, loadImage("invader1.png"), loadImage("invader2.png"), "projectile.png", this.width, this.height,  80 + j*48, i*48, 1, 100));
                }
            }
        }
        seconds = new ArrayList<Integer>(); // reset the game staring time;
        font = createFont("PressStart2P-Regular.ttf", 10, true); // create the font we need
        textFont(font); // select this font to display characters;
    }

    public void settings() {
        size(this.width,this.height);

    }



    public void keyPressed(){// response to all the key down event, and change corresponding boolean values
        if(key == CODED & keyCode == LEFT){
            leftPressed = true;
            rightPressed = false;
        }
        if(key == CODED & keyCode == RIGHT){
            rightPressed = true;
            leftPressed = false;
        }
        if(key == ' '){
            spacePressedTimes += 1;
        }
        if(key == 'l'){
            lpressedTimes += 1;
        }
        if(key == 'b'){
            bpressedTimes += 1;
        }

    }


     //response to each time keyReleased
    public void keyReleased(){
        if(key == CODED & keyCode == LEFT){
            leftPressed = false;
        }
        if(key == CODED & keyCode == RIGHT){
            rightPressed = false;
        }
        if(key == ' '){
            spacePressedTimes += 1;
        }
        if(key == 'l'){
            lpressedTimes += 1;
        }
        if(key == 'b'){
            bpressedTimes += 1;
        }

    }

    // remove the Invaders from the swarm when being hit by the bullets fired by the tank
    public void invadersBeingHit(){
        for(int i = 0; i < swarms.size(); i++){
            swarms.get(i).beingHit(tank.getTankBullets()); // to check if the invader has being hit, reduce its life to zero
        }
        ArrayList<Invader> newSwarms = new ArrayList<Invader>();
        for(int i = 0; i < swarms.size(); i++){
            if(swarms.get(i).getLife() > 0){ // if life equals to 1, it means that it does not being hit , then add it to newSwarms
                newSwarms.add(swarms.get((i)));
            }else{
                currentScore += swarms.get(i).getPoints(); // update the current score;
                money += (swarms.get(i).getPoints()) / 10;
                if(currentScore >= highScore){
                    highScore = currentScore; // if current score is higher than the high score update it
                }
            }
        }

        swarms = newSwarms;
    }


    // change the life of tank when it being hit and change the status of bullets if life equals to 0 turn to game over
    public void tankBeingHit(){
        tank.beingHit(firedBySwarm);
    }

    public void barriersBeingHit(){
        for(int i = 0; i < barriers.size(); i++){
            barriers.get(i).beingHit(tank.getTankBullets());
            barriers.get(i).beingHit(firedBySwarm);
        }
    }


    public void gameOver(){ // if game is over then turn to game over screen
        if(gameOver == false){ // haven't game over
            if(tank.destoryed()){ // to check if tank has been destroyed
                gameOver = true;
            }
            for(int i = 0; i< swarms.size(); i++){
                if(swarms.get(i).invadeSucces()){  // if the invader has reached barriers
                    gameOver = true;
                    break;
                }
            }
        }else{// reset the current score and level and start again
            background(0);
            image(loadImage("gameover.png"), this.width/2 - 56, this.height/2 + 8);
            seconds.add(second());
            if(seconds.size() == 120){ // After two seconds begin next level
                currentScore = 0;
                level = 1;
                money = 0;
                gameOver = false;
                setup();
            }
        }
    }

    public void nextLevel(){// if you kill all the invaders and your tank still alive then turn to next level
        if(swarms.size() == 0 && tank.destoryed() == false){
            background(0);
            image(loadImage("nextlevel.png"), this.width/2 - 56, this.height/2 + 8);
            seconds.add(second());
            if(seconds.size() == 120){ // After two seconds begin next level
                if(this.level < 5){
                    this.level += 1; // update the level however , however the max level is 5
                }
                setup();
            }
        }

    }


    //choose randomly which invader should fire and remove bullets outside the screen or have been crasehed
    public void randomFire(){

        for(int i = 0; i< firedBySwarm.size(); i++){
            if(firedBySwarm.get(i).getBulletY() == this.height){
                firedBySwarm.remove(i); // remove the bullets outside the boundary
            }
        }
        for(int i = 0; i< firedBySwarm.size(); i++){
            if(firedBySwarm.get(i).getCrashed() == true){
                firedBySwarm.remove(i); // remove the bullets outside that has been crashed
            }
        }
        // The time to fire will decrease by the level
        if(second() % (5 - this.level + 1) == 0 & swarms.size() > 0 & firedBySwarm.size() < this.level){ // Every 5 seconds, an invader will be randomly selected to fire a projectile downwards.
            int index = (int) (random(0, swarms.size()));                                                    // 5
            swarms.get(index).fire(firedBySwarm);
        }
    }



    // update all the objects' locations then draw them by update screen
    public void upDateLocations(){
        if(rightPressed){
            tank.moveRight();
        }
        if(leftPressed){
            tank.moveLeft();
        }
        if(spacePressedTimes > 0 && spacePressedTimes % 2 == 0){ // if we press and release the space then fire
            tank.fire();
            spacePressedTimes = 0; // After we press and release the space , set it to zero again
        }
        tank.moveBullets();
        for(int i = 0; i < swarms.size(); i++){
            swarms.get(i).move();
        }
        for(int i = 0; i < firedBySwarm.size(); i++){ // update  the location of bullets fired by invaders
            firedBySwarm.get(i).firedByInvader();
        }

    }

    //Update the current points
    public void updatePoints(){

    }


    // draw all the things' new location on the screen
    public void updateScreen(){
        background(0);// set the Back ground colour to black
        tank.draw();//display the tank
        tank.drawBullets();// update the flying bullets and draw it
        for(int i = 0; i < barriers.size(); i++){ // draw all the barriers with its current components
            barriers.get(i).draw();
        }
        for(int i = 0; i < swarms.size(); i++){ // draw all the existing invaders
            swarms.get(i).draw();
        }
        for(int i = 0; i < firedBySwarm.size(); i++){
            firedBySwarm.get(i).draw();
        }
        if(lpressedTimes > 0 & lpressedTimes % 2 == 0){ // To check if you can buy a life
            buyLife();
            lpressedTimes = 0;
        }
        if(bpressedTimes > 0 & bpressedTimes % 2 == 0){ // To check if you can buy a tank
            updateTank();
            bpressedTimes = 0;
        }

        text("CURRENT :", 10, 20);
        text(String.valueOf(currentScore), 10, 40);
        text("HIGH :", 10, 60);
        text(String.valueOf(highScore), 10, 80);
        text("MONEY :", 550, 60);
        text(String.valueOf(money), 550, 80);
        text("LIFE : ", 550, 20);
        text(String.valueOf(tank.getLife()), 550, 40);
    }


    public void buyLife(){
        if(money >= 1000 && tank.getLife() < tank.getMaxLife()){ // if you got more than 1000 money and has less than 3 life
            tank.addLife(); // buy a life and use 1000 money
            money -= 1000;
        }

    }


    public void updateTank(){
        if(money >=  1000 && tank.getLife() == tank.getMaxLife()){
            tank.update();
            money -= 1000;
        }
    }


    public void bgm(){
        AePlayWave sound=new AePlayWave("src\\main\\resources\\11638.wav"); // to load background music
        sound.start();
    }




    // main loop
    public void draw() {
        randomFire();
        invadersBeingHit();
        tankBeingHit();
        barriersBeingHit();
        upDateLocations(); // update all the objects' locations then draw them by updateScreen()
        updateScreen(); // draw all the things' new location on the screen
        gameOver();
        nextLevel();
        bgm();


}

    public static void main(String[] args) {
        PApplet.main("invadem.App");
        }

}
