package pt.ipbeja.estig.po2.snowman.app.model;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The GameAudio class is responsible for managing and playing background audio in the Snowman game.
 * It leverages the Java Sound API to handle audio playback, including looping and resource management.
 * <p>
 * Key Responsibilities:
 * - Playing audio files stored in the application resources
 * - Managing audio playback (start, stop, and looping)
 * - Handling audio resources efficiently
 * <p>
 * Limitations:
 * - Supports only audio files in the WAV format
 * - Cannot play multiple audio tracks simultaneously
 * - No volume control functionality
 * <p>
 * Threading Note:
 * This class is not designed for concurrent use. Ensure that method invocations occur on the same thread.
 *
 * @author Ângelo Dias, Edgar Brito
 */
public class GameAudio {

    /**
     * Active audio clip used for playback
     */
    private Clip clip;

    /**
     * Plays the specified audio file from the resources folder.
     * <p>
     * This method attempts to locate and play a WAV file from the `/audio/` directory in the
     * project's resources. Any audio already playing will be stopped and the new audio
     * will play in a continuous loop.
     *
     * @param filename Name of the audio file (e.g., "background.wav") stored in the `/resources/audio` directory.
     * @throws IllegalArgumentException If the file cannot be found or is not a valid WAV file.
     */
    public void play(String filename) {
        try {
            // Stop existing audio before starting new playback
            stop();

            // Attempt to load the audio file from resources
            InputStream audioSrc = getClass().getResourceAsStream("/audio/" + filename);
            if (audioSrc == null) {
                System.err.println("Audio file not found: " + filename);
                return;
            }

            // Create a buffered input stream for the audio file
            InputStream bufferedIn = new BufferedInputStream(audioSrc);

            // Initialize the audio stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Create and configure the audio clip
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Set the clip to loop indefinitely
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Log exceptions for debugging purposes
        }
    }

    /**
     * Stops the currently playing audio and releases associated resources.
     * <p>
     * This method ensures that any currently active audio playback is terminated
     * and system resources associated with the audio clip are properly released.
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();  // Stop playback
            clip.close(); // Release resources
        }
    }
}