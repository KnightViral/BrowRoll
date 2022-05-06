package sample.screens.fragments.wheels;

import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import sample.Utils;
import sample.entity.*;

import javax.sound.sampled.AudioSystem;
import java.util.Arrays;
import java.util.Collections;

public class WheelFragment {

    protected StackPane pane;
    protected PieChart wheel;
    protected VBox pointerVBox;
    protected Button rollBtn;
    protected ImageView rollImg;
    protected TableView<WheelPoint> table;

    public WheelFragment(TableView<WheelPoint> table) {
        this.table = table;
        initPane();
        initWheel();
        initPointer();
        initRollBtn();
        initRollImg();
    }

    private void initPane() {
        pane = new StackPane();
        VBox.setVgrow(pane, Priority.ALWAYS);
    }

    private void initWheel() {
        wheel = new PieChart();
        wheel.setMinHeight(0);
        wheel.setLabelLineLength(10);
        wheel.setLegendVisible(false);
        wheel.getStylesheets().add(this.getClass().getResource("/sample/" + StyleProvider.getStyle()).toExternalForm());
        pane.getChildren().add(wheel);
    }

    private void initPointer() {
        pointerVBox = new VBox();
        pointerVBox.getStylesheets().add(this.getClass().getResource("/sample/" + StyleProvider.getStyle()).toExternalForm());
        pointerVBox.getStyleClass().add("vboxMark");
        pointerVBox.setFocusTraversable(true);
        pointerVBox.setMouseTransparent(true);
        pointerVBox.setVisible(false);
        pane.getChildren().add(pointerVBox);
    }

    private void initRollBtn() {
        rollBtn = new Button();
        rollBtn.setStyle("-fx-background-color: lightgray; -fx-font-weight: bold; -fx-background-radius: 5em; -fx-text-fill: #800080");
        rollBtn.setText("Крутонуть");
        rollBtn.setVisible(false);
        rollBtn.setOnAction(event -> makeRoll());
        pane.getChildren().add(rollBtn);
    }

    private void initRollImg() {
        rollImg = new ImageView(String.valueOf(getClass().getResource("/sample/resource/pics/" + StyleProvider.getRollPic())));
        rollImg.setFitHeight(150);
        rollImg.setFitWidth(150);
        rollImg.setPickOnBounds(true);
        rollImg.setPreserveRatio(true);
        rollImg.setVisible(false);
        pane.getChildren().add(rollImg);
    }

    public void removeWheelPoint(WheelPoint wheelPoint) {
        if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
            wheel.getData().remove(wheelPoint.getWheelData());
            if (wheel.getData().size() == 0)
                clearWheel();
            //TODO исключение если ничего не нашли
        }
    }

    public void clearWheel() {
        rollImg.setRotate(0);
        wheel.getData().clear();
        wheel.setRotate(0);
        rollBtn.setText("Крутонуть");
        rollBtn.setVisible(false);
        pointerVBox.setVisible(false);
        table.getItems().forEach(WheelPoint::generateWheelData);
    }

    public void setWheelPointNames(boolean bool) {
        wheel.setLabelsVisible(bool);
    }

    public void addToWheel(WheelPoint wheelPoint) {
        wheel.getData().add(wheelPoint.getWheelData());
        wheelPoint.updateWheelTooltip();
        //todo
//        wheelPoint.getWheelData().getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
//                e -> {
//                    Pane box = new Pane();
//                    HBox.setHgrow(box, Priority.NEVER);
//                    box.setStyle("-fx-background-color: lightgray;");
//                    box.getChildren().add(new Label(wheelPoint.getName()));
//                    box.getChildren().add(new Label(Double.toString(wheelPoint.getMultiplier())));
//                    Button btn = new Button("Закрыть");
//                    btn.setOnAction(event -> pane.getChildren().remove(box));
//                    box.getChildren().add(btn);
//                    pane.getChildren().add(box);
//                });
    }

    public void makeRoll() {
        if (!wheel.getData().isEmpty()) {
            rollBtn.setText("Крутонуть");
            wheel.setRotate(0);
            RotateTransition rt = new RotateTransition(Duration.millis(15000), wheel);
            int rotations = Utils.getRandomBetween(100, 200);
            rt.setByAngle(Utils.getRandomBetween(0, 359) + 360 * rotations);
            rollImg.setRotate(0);
            rollImg.setVisible(true);
            RotateTransition rtBrow = new RotateTransition(Duration.millis(15000), rollImg);
            rtBrow.setByAngle(-Utils.getRandomBetween(720, 1920));
            WheelFragment owner = this;
            try {
                MyAudioTrack track = new MyAudioTrack(owner.getClass().getResource("/sample/resource/ORA ORA ORA Vs MUDA MUDA MUDA.wav"), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                track.start(0, true);
                rt.setOnFinished(event -> {
                    WheelPoint winner = checkRollWinner();
                    String winnerName = getWheelPointNameNotNull(winner);
                    rollBtn.setText(winnerName);
                    track.stop();
                    MyAudioTrack trackFinish = new MyAudioTrack(owner.getClass().getResource("/sample/resource/" + SoundsProvider.getSound()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                    trackFinish.start();
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

    protected WheelPoint checkRollWinner() {
        double winDegree = wheel.getRotate() % 360.0;
        double wheelDegreePrev = 360;
        double wheelDegree = 360;
        double step = 360.0 / getMultiplierFromWheel();
        for (PieChart.Data data : wheel.getData()) {
            wheelDegreePrev = wheelDegree;
            wheelDegree = wheelDegree - data.getPieValue() * step;
            if (winDegree >= wheelDegree && winDegree <= wheelDegreePrev) {
                WheelPoint res = findWheelPointByData(data);
                if (res != null){
                    System.out.println(res.getName());
                }
                return res;
            }
        }
        return null;
    }

    private double getMultiplierFromWheel(){
        double res = 0;
        for (PieChart.Data data : wheel.getData()) {
            res = res + data.getPieValue();
        }
        return res;
    }

    protected WheelPoint findWheelPointByData(PieChart.Data data){
        for (WheelPoint wheelPoint : table.getItems()) {
            if (wheelPoint.getWheelData().equals(data)){
                return wheelPoint;
            }
        }
        return null;
    }

    protected String getWheelPointNameNotNull(WheelPoint wheelPoint) {
        if (wheelPoint != null){
            return wheelPoint.getName();
        }
        return "Пук среньк Fallout 76 (ошибка)";
    }

    public void calculateWheel(double count) {
        clearWheel();
        if (count > 0) {
            rollBtn.setVisible(true);
            for (WheelPoint wheelPoint : table.getItems()) {
                if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
                    addToWheel(wheelPoint);
                }
            }
        } else {
            rollBtn.setVisible(false);
        }
    }

    protected int countAllWheelPoints() {
        int counter = 0;
        for (WheelPoint wheelPoint : table.getItems()) {
            if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
                counter++;
            }
        }
        return counter;
    }

    public Node getNode() {
        return pane;
    }

}
