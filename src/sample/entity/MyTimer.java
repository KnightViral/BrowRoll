package sample.entity;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;
import sample.Utils;

import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;

public class MyTimer implements Serializable {

    private String id;
    private String name;
    private Timeline timeline;
    private IntegerProperty timeSeconds;
    private StringProperty stringProperty;

    private boolean alarm = false;
    private String alarmFileName = "RIP EARS.wav";

    private boolean isPlaying = false;
    private boolean bindToFile = false;

    private String fileName;

    public MyTimer(String name, double hours, double minutes, double seconds) {
        this.name = name;
        setId(Utils.generateName());
        int timeInSeconds = (int) (hours * 3600 + minutes * 60 + seconds);
        addSeconds(timeInSeconds);

        initStringProperty();

        timeSeconds.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if ((Integer) newValue <= 0) {
                    if (alarm) {
                        try {
                            MyAudioTrack track = new MyAudioTrack(getClass().getResource("/sample/resource/" + alarmFileName), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                            track.start();
                        } catch (Exception e) {
                            //todo exception
                            e.printStackTrace();
                        }
                    }
                    isPlaying = false;
                }

                if (bindToFile) {
                    try {
                        PrintWriter writer = new PrintWriter(fileName);
                        writer.print(stringProperty.get());
                        writer.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initStringProperty() {
        stringProperty = new SimpleStringProperty();
        stringProperty.bind(Bindings.createStringBinding(() -> {
            Duration duration = Duration.seconds(timeSeconds.get());
            if (timeSeconds.get() > 0) {
                return String.format("%02d:%02d:%02d",
                        (long) duration.toHours(),
                        (long) duration.toMinutes() % 60,
                        (long) duration.toSeconds() % 60);
            } else {
                return StyleProvider.getTimerEndLabel();
            }
        }, timeSeconds));
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean enableAlarm, String alarmFileName) {
        this.alarm = enableAlarm;
        this.alarmFileName = alarmFileName;
    }

    public void setAlarm(boolean enableAlarm) {
        this.alarm = enableAlarm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBindToFile() {
        return bindToFile;
    }

    public void setBindToFile(boolean bindToFile) {
        this.bindToFile = bindToFile;
    }

    public StringProperty getStringProperty() {
        return stringProperty;
    }

    public void stop() {
        if (timeline != null) {
            timeline.stop();
            isPlaying = false;
        }
    }

    public void start() {
        if (timeSeconds.get() > 0) {
            timeline = new Timeline();
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(timeSeconds.get() + 1),
                            new KeyValue(timeSeconds, 0)));
            timeline.play();
            isPlaying = true;
        }
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.fileName = id + ".txt";
    }

    public int getTimeSeconds() {
        return timeSeconds.get();
    }

    public String getAlarmFileName() {
        return alarmFileName;
    }

    public void setAlarmFileName(String alarmFileName) {
        this.alarmFileName = alarmFileName;
    }

    public void addSeconds(int seconds) {
        boolean alarm = this.alarm;
        this.alarm = false;
        if (timeline != null)
            timeline.stop();
        if (timeSeconds == null)
            timeSeconds = new SimpleIntegerProperty();
        timeSeconds.set(timeSeconds.get() + seconds);
        if (timeSeconds.get() < 0)
            timeSeconds.set(0);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(timeSeconds.get() + 1),
                        new KeyValue(timeSeconds, 0)));
        if (this.isPlaying)
            timeline.play();
        this.alarm = alarm;
    }

    public void setTime(int hours, int minutes, int seconds){
        int timeInSeconds = (int)(hours * 3600 + minutes * 60 + seconds);
        boolean playing = isPlaying();
        stop();
        addSeconds(-getTimeSeconds());
        addSeconds(timeInSeconds);
        if (playing)
            start();
    }
}
