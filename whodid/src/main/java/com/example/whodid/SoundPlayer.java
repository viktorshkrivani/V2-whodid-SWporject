package com.example.whodid;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
public class SoundPlayer {
    private Clip clip;

    // Constructor to initialize the SoundPlayer with a given resource URL
    public SoundPlayer(URL resourceURL) {
        try {
            // Open an audio input stream from the provided URL
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resourceURL);

            // Get a Clip object to play the audio
            clip = AudioSystem.getClip();

            // Open the clip with the audio input stream
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            // Print error messages if an exception occurs during initialization
            System.err.println("Error initializing SoundPlayer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to play the audio
    public void play() {
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(0);  // Set the play position to the beginning
            clip.start();  // Start playing the audio
        }
    }

    // Method to stop the audio
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();  // Stop playing the audio
        }

    }


    }

