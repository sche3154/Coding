package stickman.view;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import stickman.model.CareTaker;
import stickman.model.GameEngine;
import stickman.model.GameManager;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handles all input for the program. Listens for KeyEvents, and relays messages to GameEngine.
 */
class KeyboardInputHandler {

    /**
     * The GameEngine for the program.
     */
    private final GameEngine model;

    /**
     * The CareTaker for save and load.
     */
    private final CareTaker careTaker = new CareTaker();

    /**
     * Whether the left key is being pressed.
     */
    private boolean left = false;

    /**
     * Whether the right key is being pressed.
     */
    private boolean right = false;

    /**
     * Set of all keys being pressed.
     */
    private Set<KeyCode> pressedKeys = new HashSet<>();

    /**
     * Map of Strings to sounds.
     */
    private Map<String, MediaPlayer> sounds = new HashMap<>();

    /**
     * Creates a new KeyboardInputHandler object.
     * @param model The GameEngine being used
     */
    KeyboardInputHandler(GameEngine model) {
        this.model = model;

        URL mediaUrl = getClass().getResource("/jump.wav");
        String jumpURL = mediaUrl.toExternalForm();

        Media sound = new Media(jumpURL);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        sounds.put("jump", mediaPlayer);
    }

    /**
     * Handles the situation when a key is pressed down.
     * @param keyEvent The key being pressed
     */
    void handlePressed(KeyEvent keyEvent) {
        if (pressedKeys.contains(keyEvent.getCode())) {
            return;
        }
        pressedKeys.add(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.UP)) {
            if (model.jump()) {
                MediaPlayer jumpPlayer = sounds.get("jump");
                jumpPlayer.stop();
                jumpPlayer.play();
            }
        }

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = true;
        }
        else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = true;
        }
        else if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            model.shoot();
            return;
        }
        else if(keyEvent.getCode().equals(KeyCode.S)){
            careTaker.addMemento(((GameManager) model).saveMemento());
            return;
        }
        else if(keyEvent.getCode().equals(KeyCode.Q)){
            try{
                ((GameManager) model).loadMemento(careTaker.getMemento());
            }catch(Exception e){
                System.out.println("You have not saved before");
            }
            
            return;
        }
        else{
            return;
        }


        if (left) {
            if (right) {
                model.stopMoving();
            } else {
                model.moveLeft();
            }
        } else {
            model.moveRight();
        }
    }

    /**
     * Handles the situation when a key is released.
     * @param keyEvent The key being released
     */
    void handleReleased(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = false;
        }
        else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = false;
        } else {
            return;
        }

        if (!(right || left)) {
            model.stopMoving();
        } else if (right) {
            model.moveRight();
        } else {
            model.moveLeft();
        }
    }
}
