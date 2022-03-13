package sample.screens.fragments.wheels;

import javafx.animation.RotateTransition;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.util.Duration;
import sample.Utils;
import sample.entity.*;

import javax.sound.sampled.AudioSystem;
import java.util.*;

public class DuelWheelFragment extends WheelFragment {

    private boolean duelInProgress = false;
    private boolean freezeWheel = false;
    private boolean needsRecalculation = false;
    private boolean needsEating = false; //todo needs wizard for this
    private WheelPoint winner = null;
    private final MyAudioTrack music;

    public DuelWheelFragment(TableView<WheelPoint> table) {
        super(table);
        rollImg.setImage(new Image(String.valueOf(this.getClass().getResource("/sample/resource/pics/" + StyleProvider.getDuelRollPic()))));
        pointerVBox.getStyleClass().clear();
        pointerVBox.getStyleClass().add("vboxLongMark");
        music = new MyAudioTrack(this.getClass().getResource("/sample/resource/" + SoundsProvider.getMKMusic()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
    }

    public void startDuel(boolean duelInProgress){
        this.duelInProgress = duelInProgress;
        needsRecalculation = false;
        needsEating = false;
        if (duelInProgress) {
            calculateWheel(countAllWheelPoints());
            MyAudioTrack track = new MyAudioTrack(this.getClass().getResource("/sample/resource/" + SoundsProvider.getMKChoose()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
            track.start(0);
            music.start(0, true);
        } else {
            rollBtn.setVisible(false);
            clearWheel();
            MyAudioTrack track = new MyAudioTrack(this.getClass().getResource("/sample/resource/" + SoundsProvider.getSlap()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
            track.start();
            music.close();
        }
    }

    @Override
    public void calculateWheel(double count) {
        clearWheel();
        rollBtn.setVisible(false);
        pointerVBox.setVisible(false);

        if (count >= 2) {
            rollBtn.setVisible(true);
            pointerVBox.setVisible(true);
            List<WheelPoint> wheelPoints = new ArrayList<>(
                    table.getItems()
                            .filtered(wheelPoint -> wheelPoint.getId() != JokeGenerator.getJokeId())
            );
            wheelPoints.sort(Comparator.comparingDouble(WheelPoint::getMultiplier));
//            wheelPoints.sort(new Comparator<WheelPoint>() {
//                @Override
//                public int compare(WheelPoint o1, WheelPoint o2) {
//                    if (o1.getMultiplier() < o2.getMultiplier())
//                        return -1;
//                    if (o1.getMultiplier() > o2.getMultiplier())
//                        return 1;
//
//                    long thisBits    = Double.doubleToLongBits(o1.getMultiplier());
//                    long anotherBits = Double.doubleToLongBits(o2.getMultiplier());
//
//                    return (thisBits == anotherBits ?  -1 :
//                            (thisBits < anotherBits ? -1 :
//                                    1));
//                }
//            });
            wheelPoints.sort(Comparator.comparingDouble(WheelPoint::getMultiplier));
            addToWheel(wheelPoints.get(0));
            addToWheel(wheelPoints.get(1));
        } else {
            if (count == 1) {
                table.getItems().forEach(this::addToWheel);
            }
        }
    }

    @Override
    public void makeRoll() {
        if (!freezeWheel) {
            if (needsRecalculation || needsEating) {
                if (needsEating) {
                    eatingAnimation(this.winner);
                    table.sort();
                } else {
                    calculateWheel(countAllWheelPoints());
                    needsRecalculation = false;
                }
            } else {
                if (!wheel.getData().isEmpty()) {
                    rollBtn.setText("Крутонуть");
                    wheel.setRotate(0);
                    int timeSq = countAllWheelPoints() - 1;
                    double time = 15000.0;
                    int rotateFrom = 18000;
                    int rotateTo = 36000;
                    int rotateFromBrow = 720;
                    int rotateToBrow = 1920;
                    if (timeSq >= 0) {
                        if (time / timeSq < 3000) {
                            timeSq = 5;
                        }
                        time = time / timeSq;
                        rotateFrom = rotateFrom / timeSq;
                        rotateTo = rotateTo / timeSq;
                        rotateFromBrow = rotateFromBrow / timeSq;
                        rotateToBrow = rotateToBrow / timeSq;
                    }
                    RotateTransition rt = new RotateTransition(Duration.millis(time), wheel);
                    rt.setByAngle(Utils.getRandomBetween(rotateFrom, rotateTo));
                    rollImg.setRotate(0);
                    rollImg.setVisible(true);
                    RotateTransition rtBrow = new RotateTransition(Duration.millis(time), rollImg);
                    rtBrow.setByAngle(-Utils.getRandomBetween(rotateFromBrow, rotateToBrow));
                    WheelFragment owner = this;
                    try {
                        MyAudioTrack track = new MyAudioTrack(owner.getClass().getResource("/sample/resource/ORA ORA ORA Vs MUDA MUDA MUDA.wav"), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                        track.start(0, true);
                        rt.setOnFinished(event -> {
                            WheelPoint winner = checkRollWinner();
                            String winnerName = getWheelPointNameNotNull(winner);
                            rollBtn.setText(winnerName);
                            track.stop();
                            music.stop();
                            MyAudioTrack trackFinish = new MyAudioTrack(owner.getClass().getResource("/sample/resource/" + SoundsProvider.getMKHit()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                            trackFinish.start(0);
                            MyAudioTrack trackFinishSecond = new MyAudioTrack(owner.getClass().getResource("/sample/resource/" + SoundsProvider.getSound()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                            trackFinishSecond.start();
                            needsEating = true;
                            this.winner = winner;

                            try { //todo EXCEPTION
                                SaveLoadWizard.save(table, winnerName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        rt.setOnFinished(event -> {
                            WheelPoint winner = checkRollWinner();
                            String winnerName = getWheelPointNameNotNull(winner);
                            rollBtn.setText(winnerName);
                            music.stop();
                            needsEating = true;
                            this.winner = winner;

                            try { //todo EXCEPTION
                                SaveLoadWizard.save(table, winnerName);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        });
                    }
                    rtBrow.setOnFinished(event -> rollImg.setVisible(false));
                    rt.play();
                    rtBrow.play();
                }
            }
        }
    }

    private void eatingAnimation(WheelPoint winner) {
        freezeWheel = true;
        for (PieChart.Data data : wheel.getData()) {
            if (!data.equals(winner.getWheelData())) {
                WheelPoint loser = findWheelPointByData(data);
                if (loser != null) {
                    winner.setMultiplier(winner.getMultiplier() + loser.getMultiplier());
                    removeWheelPoint(loser);
                    MyAudioTrack track = new MyAudioTrack(this.getClass().getResource("/sample/resource/" + SoundsProvider.getMKEat()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                    music.start(0, true);
                    track.start(0);
                    if (countAllWheelPoints() == 1) {
                        MyAudioTrack winTrack = new MyAudioTrack(this.getClass().getResource("/sample/resource/" + SoundsProvider.getMKIWin()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                        winTrack.start(0);
                    }
                    break;
                }
            }
        }
        needsEating = false;
        needsRecalculation = true;
        freezeWheel = false;
    }

    //todo пересмотреть логику. Плохо что мы обращаемся так к таблице
    public void addToTable(WheelPoint wheelPoint) {
        if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
            boolean unique = true;
            WheelPoint oldElement = null;
            for (WheelPoint item : table.getItems()) {
                if (item.getName().equals(wheelPoint.getName())) {
                    unique = false;
                    oldElement = item;
                    break;
                }
            }
            if (unique) {
                table.getItems().add(wheelPoint);
                calculateWheel(countAllWheelPoints());
            } else {
                oldElement.setMultiplier(oldElement.getMultiplier() + wheelPoint.getMultiplier());
            }
        }
        table.refresh();
    }

    @Override
    public void removeWheelPoint(WheelPoint wheelPoint) {
        if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
            wheel.getData().remove(wheelPoint.getWheelData());
            if (wheel.getData().size() == 0)
                clearWheel();
            //TODO исключение если ничего не нашли
        }
        table.getItems().remove(wheelPoint);
        table.refresh();
    }

}
