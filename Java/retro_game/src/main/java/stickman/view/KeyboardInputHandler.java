package stickman.view;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import stickman.model.GameEngine;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class KeyboardInputHandler{
    private final GameEngine model;
    private boolean left = false;
    private boolean right = false;
    private Set<KeyCode> pressedKeys = new HashSet<>();

    private Map<String, MediaPlayer> sounds = new HashMap<>();

    KeyboardInputHandler(GameEngine model) {
        this.model = model;

        URL mediaUrl = getClass().getResource("jump.mp3");

        if(mediaUrl != null){
//            String jumpURL = mediaUrl.toExternalForm();
//
//            Media sound = new Media(jumpURL);
//            MediaPlayer mediaPlayer = new MediaPlayer(sound);
//            sounds.put("jump.mp3", mediaPlayer);
        }else{

            System.out.println("This is NUll");
        }

    }

    void handlePressed(KeyEvent keyEvent) {
        if (pressedKeys.contains(keyEvent.getCode())) {
            return;
        }
        pressedKeys.add(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.UP)) {
            if (model.jump()) {
//                MediaPlayer jumpPlayer = sounds.get("jump.mp3");
//                jumpPlayer.stop();
//                jumpPlayer.play();
            }
        }

        if(keyEvent.getCode().equals(KeyCode.SPACE)){
            model.shooting();
        }

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = true;
        }
        else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = true;
        }
        else {
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

//        System.out.println(left);
//        System.out.println(right);
    }

    void handleReleased(KeyEvent keyEvent) {
        pressedKeys.remove(keyEvent.getCode());

        if (keyEvent.getCode().equals(KeyCode.SPACE)) {
            model.stopShooting();
        }

        if (keyEvent.getCode().equals(KeyCode.LEFT)) {
            left = false;
        }
        else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
            right = false;
        }
        else {
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
