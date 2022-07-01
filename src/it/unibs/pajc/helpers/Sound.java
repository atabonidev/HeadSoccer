package it.unibs.pajc.helpers;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * Classe necessaria per gestire i suoni nel gioco
 */
public class Sound {

    Clip clip;
    URL soundUrl[] = new URL[30];
    public static final int KICK_OFF = 0;
    public static final int KICK_BALL = 1;

    public Sound() {
        soundUrl[0] = getClass().getResource("/sounds/kick-off.wav");
        soundUrl[1] = getClass().getResource("/sounds/kick-ball.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

}
