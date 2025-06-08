package pt.ipbeja.estig.po2.snowman.app.model;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class responsible for managing and playing background game audio.
 * Uses Java's Clip API to handle playback of audio files stored in the resources.
 */
public class GameAudio {
    private Clip clip; // Clip used to hold and play the audio

    /**
     * Plays the specified audio file from the resources/audio directory.
     * If another audio is currently playing, it will be stopped before starting the new one.
     * The audio will loop continuously once started.
     *
     * @param filename The name of the audio file to play (e.g., "background.wav").
     */
    public void play(String filename) {
        try {
            // Stop any currently playing audio
            stop();

            // Load audio resource from the classpath
            InputStream audioSrc = getClass().getResourceAsStream("/audio/" + filename);
            if (audioSrc == null) {
                System.err.println("Audio not found: " + filename);
                return;
            }

            // Wrap the input stream in a BufferedInputStream for better performance and compatibility
            InputStream bufferedIn = new BufferedInputStream(audioSrc);

            // Create an audio stream from the buffered input
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Obtain a Clip to play the audio
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Start playing the audio clip
            clip.start();

            // Loop the audio continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Handle any errors encountered during audio playback
            e.printStackTrace();
        }
    }

    /**
     * Stops the currently playing audio clip, if one is running.
     * This is used to halt playback when transitioning between scenes or stopping music.
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();    // Stop playback
            clip.close();   // Release system resources
        }
    }
}
