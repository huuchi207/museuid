package br.com.museuid.util;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import br.com.museuid.app.App;

public class SoundUtils {
  public static synchronized void playNoti() {
    // cl is the ClassLoader for the current class, ie. CurrentClass.class.getClassLoader();
    URL url = App.class.getResource("/br/com/museuid/sound/noti.wav");
    AudioInputStream audioIn;
    Clip noti;
    // Get a sound clip resource.
    try {
      audioIn = AudioSystem.getAudioInputStream(url);
      noti = AudioSystem.getClip();
      noti.open(audioIn);
      noti.start();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    }
    // Open audio clip and load samples from the audio input stream.

  }
}
