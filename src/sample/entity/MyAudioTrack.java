package sample.entity;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyAudioTrack {
    List<Clip> clips;
    URL url;

    public MyAudioTrack(URL url, List<Mixer.Info> mixerInfos) {
        this.url = url;
        clips = new ArrayList<>();
        try {
            for (Mixer.Info mixer : mixerInfos) {
                clips.add(AudioSystem.getClip(mixer));
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            //todo ловить ошибки
        }
    }

    public void start(int framePosition, boolean loop) {
        try {
            for (Clip clip : clips) {
                clip.open(AudioSystem.getAudioInputStream(this.url));
                //((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).getMaximum());
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
        }catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            //todo ловить ошибки
        }
        clips.forEach(clip -> clip.setFramePosition(framePosition));
        clips.forEach(clip -> clip.addLineListener(event -> {
            if (event.getType().equals(LineEvent.Type.STOP))
                event.getLine().close();
        }));
        clips.forEach(DataLine::start);
    }

    public void start() {
        start(0, false);
    }

    public void start(int framePosition) {
        start(framePosition, false);
    }

    public void stop() {
        clips.forEach(DataLine::stop);
    }

    public void close(){
        clips.forEach(Line::close);
    }
}

