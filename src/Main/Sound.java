package Main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];
    private FloatControl volumeControl;
    int volumeScale = 3;
    float volume;

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/1-06. Pallet Town Theme (1).wav");
        soundURL[1] = getClass().getResource("/sound/1-03. Title Screen.wav");
        soundURL[2] = getClass().getResource("/sound/1-11. Battle! (Trainer Battle).wav");
        soundURL[3] = getClass().getResource("/sound/1-13. Road to Viridian City - Leaving Pallet Town.wav");
        soundURL[4] = getClass().getResource("/sound/1-07. Professor Oak.wav");
        soundURL[5] = getClass().getResource("/sound/1-21. Pokémon Center.wav");
        soundURL[6] = getClass().getResource("/sound/effects/button_click.wav");
        soundURL[7] = getClass().getResource("/sound/1-38. Lavender Town Theme.wav");
        soundURL[8] = getClass().getResource("/sound/2-17. Ending Theme.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
            AudioFormat baseFormat = audioInputStream.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);

            clip = AudioSystem.getClip();
            clip.open(dais);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void checkVolume() {
        switch (volumeScale) {
            case 0:
                volume = -80.0f; // Ajustăm pentru a fi mai silențios
                break;
            case 1:
                volume = -30.0f;
                break;
            case 2:
                volume = -10.0f;
                break;
            case 3:
                volume = -5.0f;
                break;
            case 4:
                volume = 1.0f;
                break;
            case 5:
                volume = 6.0f;
                break;
        }
        volumeControl.setValue(volume);
    }
}
