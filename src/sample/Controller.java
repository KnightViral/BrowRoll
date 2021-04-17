package sample;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.image.Image;
import sample.entity.*;

import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Controller {
    @FXML
    public TableColumn<WheelPoint, Integer> idColumn;
    @FXML
    public TableColumn<WheelPoint, String> nameColumn;
    @FXML
    public TableColumn<WheelPoint, Integer> multiplierColumn;
    @FXML
    public TableColumn<WheelPoint, Void> addColumn;
    @FXML
    public TableColumn<WheelPoint, Void> decreaseColumn;
    @FXML
    public TableColumn<WheelPoint, Void> removeColumn;
    @FXML
    public TableColumn<WheelPoint, Void> addNumberColumn;
    @FXML
    public TableColumn<WheelPoint, Void> percentColumn;
    @FXML
    public TableView<WheelPoint> mainTable;
    @FXML
    public TextField newNameTA;
    @FXML
    public TextField newMultiplierTA;
    @FXML
    public VBox mainVBox;
    @FXML
    public MenuItem openInWebMenuItem;
    @FXML
    public PieChart wheelPC;
    @FXML
    public StackPane wheelSPane;
    @FXML
    public TabPane tabPane;
    @FXML
    public Button rollBtn;
    @FXML
    public ImageView browRollImg;
    @FXML
    public MenuButton timerMenu;
    @FXML
    public CheckMenuItem randomKekCheckBox;
    @FXML
    public VBox cssImageVBox;

    private int idCounter;

    private final static String WHEEL_LINK = "https://wheeldecide.com/?";
    private double countPoints = 0;

    private MyTimer timer;

    @FXML
    void initialize() {
        initTable();
        initWheel();
        initTabPane();
        idCounter = 1;
        onRubblesTypeButtonPress();
        initTimer();
        randomKekCheckBox.setSelected(false);
    }

    private void initTimer(){
        timer = new MyTimer("main", 0, 30, 0);
        timerMenu.textProperty().bind(timer.getStringProperty());
        timer.setAlarm(true);
    }

    private void initWheel() {
        wheelPC.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }

    private void initTabPane() {
        tabPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        initWheelTab();
    }

    private void initWheelTab() {
        cssImageVBox.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        cssImageVBox.getStyleClass().add("vboxMark");
        browRollImg.setImage(new Image(String.valueOf(this.getClass().getResource("resource/pics/browFatRoll.png"))));
    }

    private void initTable() {
        initIdColumn();
        initNameColumn();
        initPercentColumn();
        initMultiplierColumn();
        initAddColumn();
        initDecreaseColumn();
        initAddNumberColumn();
        initRemoveColumn();
        mainTable.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        sort();
    }

    private void initIdColumn() {
        idColumn.setVisible(false);
        idColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.05));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void initNameColumn() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(
                TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            event.getRowValue().setName(event.getNewValue());
                }
        );
        if (idColumn.isVisible())
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.70));
        else
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.75));
    }

    private void initMultiplierColumn() {
        multiplierColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.1));
        multiplierColumn.setCellValueFactory(new PropertyValueFactory<>("multiplier"));
    }

    private void initAddColumn() {
        addColumn.setVisible(false);
        addColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.05));
        addColumn.setCellFactory(new Callback<TableColumn<WheelPoint, Void>, TableCell<WheelPoint, Void>>() {
            @Override
            public TableCell<WheelPoint, Void> call(TableColumn param) {
                return new TableCell<WheelPoint, Void>() {

                    final Button btn = new Button("+");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setOnAction(event -> {
                                WheelPoint wheelPoint = mainTable.getItems().get(getIndex());
                                wheelPoint.setMultiplier(wheelPoint.getMultiplier() + 1);
                                mainTable.refresh();
                                countAllPoints();
                            });
                            btn.setPrefHeight(21);
                            btn.setMinHeight(21);
                            setGraphic(btn);
                        }
                        setText(null);
                    }
                };
            }
        });
    }

    private void initDecreaseColumn() {
        decreaseColumn.setVisible(false);
        decreaseColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.05));
        decreaseColumn.setCellFactory(new Callback<TableColumn<WheelPoint, Void>, TableCell<WheelPoint, Void>>() {
            @Override
            public TableCell<WheelPoint, Void> call(TableColumn param) {
                return new TableCell<WheelPoint, Void>() {

                    final Button btn = new Button("-");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setOnAction(event -> {
                                WheelPoint wheelPoint = mainTable.getItems().get(getIndex());
                                wheelPoint.setMultiplier(wheelPoint.getMultiplier() - 1);
                                mainTable.refresh();
                                countAllPoints();
                            });
                            btn.setPrefHeight(21);
                            btn.setMinHeight(21);
                            setGraphic(btn);
                        }
                        setText(null);
                    }
                };
            }
        });
    }

    private void initRemoveColumn() {
        removeColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.05));
        removeColumn.setCellFactory(new Callback<TableColumn<WheelPoint, Void>, TableCell<WheelPoint, Void>>() {
            @Override
            public TableCell<WheelPoint, Void> call(TableColumn param) {
                return new TableCell<WheelPoint, Void>() {

                    final Button btn = new Button("del");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setOnAction(event -> {
                                removeFromTable(mainTable.getItems().get(getIndex()));
                                countAllPoints();
                            });
                            btn.setPrefHeight(21);
                            btn.setMinHeight(21);
                            setGraphic(btn);
                        }
                        setText(null);
                    }
                };
            }
        });
    }

    private void initAddNumberColumn() {
        addNumberColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.10));
        addNumberColumn.setCellFactory(new Callback<TableColumn<WheelPoint, Void>, TableCell<WheelPoint, Void>>() {
            @Override
            public TableCell<WheelPoint, Void> call(TableColumn param) {
                return new TableCell<WheelPoint, Void>() {

                    final TextField tf = new TextField();
                    final Button btn = new Button("+");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setOnAction(event -> {
                                WheelPoint wheelPoint = mainTable.getItems().get(getIndex());
                                double number = 0;
                                String value = tf.getText();
                                try {
                                    number = Double.parseDouble(value);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    try {
                                        number = Double.parseDouble(value.replace(',', '.'));
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                                wheelPoint.setMultiplier(wheelPoint.getMultiplier() + number);
                                mainTable.refresh();
                                countAllPoints();
                                sort();
                            });
                            btn.setPrefHeight(21);
                            btn.setMinHeight(21);
                            tf.setPrefHeight(21);
                            tf.setMinHeight(21);
                            tf.setPrefWidth(60);
                            setGraphic(new HBox(tf, btn));
                        }
                        setText(null);
                    }
                };
            }
        });
    }

    private void initPercentColumn() {
        percentColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.05));
        percentColumn.setCellFactory(new Callback<TableColumn<WheelPoint, Void>, TableCell<WheelPoint, Void>>() {
            @Override
            public TableCell<WheelPoint, Void> call(TableColumn param) {
                return new TableCell<WheelPoint, Void>() {

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (getIndex() >= 0 &&
                                mainTable.getItems().size() > getIndex() &&
                                countPoints > 0 &&
                                mainTable.getItems().get(getIndex()).getMultiplier() > 0 &&
                                mainTable.getItems().get(getIndex()).getId() != JokeGenerator.getJokeId()
                        )
                            setText(String.format("%.1f", (double) mainTable.getItems().get(getIndex()).getMultiplier() / countPoints * 100));
                        else
                            setText(null);
                    }
                };
            }
        });
    }

    private void addToTable(WheelPoint wheelPoint) {
        if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
            boolean unique = true;
            WheelPoint oldElement = null;
            for (WheelPoint item : mainTable.getItems()) {
                if (item.getName().equals(wheelPoint.getName())) {
                    unique = false;
                    oldElement = item;
                    break;
                }
            }
            if (unique) {
                mainTable.getItems().add(wheelPoint);
                addToWheel(wheelPoint);
            } else
                oldElement.setMultiplier(oldElement.getMultiplier() + wheelPoint.getMultiplier());
        } else {
            mainTable.getItems().add(wheelPoint);
        }
        mainTable.refresh();
    }

    private void removeFromTable(WheelPoint wheelPoint) {
        if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
            wheelPC.getData().remove(wheelPoint.getWheelData());
            if (wheelPC.getData().size() == 0)
                clearWheel();
            //TODO исключение если ничего не нашли
        }
        mainTable.getItems().remove(wheelPoint);
        mainTable.refresh();
    }

    private void clearTable() {
        mainTable.getItems().clear();
        mainTable.setRotate(0);
        countAllPoints();
        clearWheel();
    }

    @FXML
    void onAddButtonPress() {
        if (!newNameTA.getText().isEmpty()) {
            addToTable(new WheelPoint(idCounter, newNameTA.getText(), getNewMultiplier()));
            idCounter++;
            newNameTA.clear();
            newMultiplierTA.clear();
            sort();
        }
        countAllPoints();
        if (randomKekCheckBox.isSelected()){
            if (Utils.getRandomTo(10) > 8){
                onKekButtonPress();
            }
        }
    }

    @FXML
    void onKekButtonPress() {
        addToTable(JokeGenerator.getJoke(mainTable));
        if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Таблица"))
            randomRotateMainTable();
        sort();
        countAllPoints();
    }

    @FXML
    void onWriteFileButtonPress() {
        createFileAndWrite(getPointsString());
    }

    @FXML
    void onOpenInBrowserButtonPress() {
        Utils.openWebpage(getPointsLink());
    }

    @FXML
    void onSortButtonPress() {
        sort();
    }

    @FXML
    void onRecountWheelButtonPress() {
        calculateWheel(countAllPoints());
    }

    @FXML
    void onHideShowWheelNamesButtonPress() {
        wheelPC.setLabelsVisible(!wheelPC.labelsVisibleProperty().getValue());
    }

    @FXML
    void onTestSoundButtonPress() {
        try {
            MyAudioTrack trackFinish = new MyAudioTrack(this.getClass().getResource("resource/" + SoundsProvider.getSound()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
            trackFinish.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRollButtonPress() {
        if (!wheelPC.getData().isEmpty()) {
            rollBtn.setText("Крутонуть");
            wheelPC.setRotate(0);
            RotateTransition rt = new RotateTransition(Duration.millis(15000), wheelPC);
            rt.setByAngle(Utils.getRandomBetween(18000, 36000));
            browRollImg.setRotate(0);
            browRollImg.setVisible(true);
            RotateTransition rtBrow = new RotateTransition(Duration.millis(15000), browRollImg);
            rtBrow.setByAngle(-Utils.getRandomBetween(720, 1920));
            Controller c = this;
            try {
                MyAudioTrack track = new MyAudioTrack(c.getClass().getResource("resource/ORA ORA ORA Vs MUDA MUDA MUDA.wav"), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                track.start();
                rt.setOnFinished(event -> {
                    rollBtn.setText(checkRollWinner());
                    track.stop();
                    MyAudioTrack trackFinish = new MyAudioTrack(c.getClass().getResource("resource/" + SoundsProvider.getSound()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                    trackFinish.start();
                });
            } catch (Exception e) {
                e.printStackTrace();
                rt.setOnFinished(event -> rollBtn.setText(checkRollWinner()));
            }
            rtBrow.setOnFinished(event -> browRollImg.setVisible(false));
            rt.play();
            rtBrow.play();
        }
    }

    @FXML
    void onHundredsTypeButtonPress() {
        addColumn.setVisible(true);
        decreaseColumn.setVisible(true);
        addNumberColumn.setVisible(false);
        newMultiplierTA.setPromptText("Множитель");
        if (idColumn.isVisible())
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.65));
        else
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.70));
    }

    @FXML
    void onRubblesTypeButtonPress() {
        addColumn.setVisible(false);
        decreaseColumn.setVisible(false);
        addNumberColumn.setVisible(true);
        newMultiplierTA.setPromptText("Сумма");
        if (idColumn.isVisible())
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.65));
        else
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.70));
    }

    @FXML
    void onAnimeButtonPress() {
        newNameTA.setText(newNameTA.getText() + " (Аниме)");
    }

    @FXML
    void onMovieButtonPress() {
        newNameTA.setText(newNameTA.getText() + " (Фильм)");
    }

    @FXML
    void onGameButtonPress() {
        newNameTA.setText(newNameTA.getText() + " (Игра)");
    }

    @FXML
    void onClearTableButtonPress() {
        clearTable();
    }

    @FXML
    void onSetTimer30ButtonPress() {
        setTimerTo30();
    }

    @FXML
    void onStopTimerButtonPress() {
        timer.stop();
    }

    @FXML
    void onPlayTimer30ButtonPress() {
        timer.start();
    }

    @FXML
    void onAdd2Timer30ButtonPress() {
        timer.addSeconds(2 * 60);
    }

    @FXML
    void onRemove2Timer30ButtonPress() {
        timer.addSeconds(-2 * 60);
    }

    private void setTimerTo30(){
        timer.setTime(0,30,0);
    }

    private void sort() {
        multiplierColumn.setSortType(TableColumn.SortType.DESCENDING);
        mainTable.getSortOrder().clear();
        mainTable.getSortOrder().add(multiplierColumn);
        mainTable.sort();
    }

    private String checkRollWinner() {
        double winDegree = wheelPC.getRotate() % 360.0;
        double wheelDegreePrev = 360;
        double wheelDegree = 360;
        double step = 360.0 / countPoints;
        for (PieChart.Data data : wheelPC.getData()) {
            wheelDegreePrev = wheelDegree;
            wheelDegree = wheelDegree - data.getPieValue() * step;
            if (winDegree >= wheelDegree && winDegree <= wheelDegreePrev) {
                String res = findWheelPointNameByData(data);
                System.out.println(res);
                return res;
            }
        }
        return "Пук среньк Fallout 76 (ошибка)";
    }

    private String findWheelPointNameByData(PieChart.Data data){
        for (WheelPoint wheelPoint : mainTable.getItems()) {
            if (wheelPoint.getWheelData().equals(data)){
                return wheelPoint.getName();
            }
        }
        return "Пук среньк Fallout 76 (ошибка)";
    }

    private double countAllPoints() {
        double counter = 0;
        for (WheelPoint wheelPoint : mainTable.getItems()) {
            if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
                counter = counter + wheelPoint.getMultiplier();
            }
        }
        if (counter > 100) {
            openInWebMenuItem.setText("Слишком много пунктов. Wheeldecide принимает максимум 100");
            openInWebMenuItem.setDisable(true);
        } else {
            openInWebMenuItem.setText("Открыть на Wheeldecide");
            openInWebMenuItem.setDisable(false);
        }
        countPoints = counter;
        rollBtn.setVisible(counter > 0);
        cssImageVBox.setVisible(counter > 0);
        return counter;
    }


    private void calculateWheel(double count) {
        clearWheel();
        if (count > 0) {
            rollBtn.setVisible(true);
            for (WheelPoint wheelPoint : mainTable.getItems()) {
                if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
                    addToWheel(wheelPoint);
                }
            }
        } else {
            rollBtn.setVisible(false);
        }
    }


    private void addToWheel(WheelPoint wheelPoint) {
        wheelPC.getData().add(wheelPoint.getWheelData());
        wheelPoint.updateWheelTooltip();
    }

    private void clearWheel() {
        browRollImg.setRotate(0);
        wheelPC.getData().clear();
        wheelPC.setRotate(0);
        rollBtn.setText("Крутонуть");
        mainTable.getItems().forEach(WheelPoint::generateWheelData);
    }

    private void randomRotateMainTable() {
        RotateTransition rt = new RotateTransition(Duration.millis(2000), mainTable);
        //так сложно, чтобы иногда таблица медленно переворачивалась на 180
        int rotateNumber = Utils.getRandomBetween(1, 10);
        if (rotateNumber != 1 && rotateNumber % 2 != 0)
            rotateNumber++;
        if (mainTable.getRotate() % 360 != 0) {
            rt.setByAngle(-540);
        } else {
            mainTable.setRotate(0);
            rt.setByAngle(180 * rotateNumber);
        }
        rt.play();
    }

    private double getNewMultiplier() {
        try {
            return Double.parseDouble(newMultiplierTA.getText());
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    private String getPointsString() {
        StringBuilder sb = new StringBuilder();
        mainTable.getItems().forEach(wheelPoint -> {
            if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
                for (int i = 0; i < wheelPoint.getMultiplier(); i++) {
                    if (sb.length() != 0)
                        sb.append("\n");
                    sb.append(wheelPoint.getName());
                }
            }
        });
        return sb.toString();
    }

    private String getPointsLink() {
        StringBuilder errorSB = new StringBuilder();
        errorSB.append(WHEEL_LINK);
        errorSB.append('c').append(1).append('=').append("Пук").append('&');
        errorSB.append('c').append(2).append('=').append("Среньк").append('&');
        errorSB.append('c').append(3).append('=').append("Fallout_76").append('&');
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(WHEEL_LINK);
            int counter = 1;
            List<String> wheelPoints = new ArrayList<>();
            for (WheelPoint wheelPoint : mainTable.getItems()) {
                if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
                    for (int i = 0; i < wheelPoint.getMultiplier(); i++) {
                        wheelPoints.add(wheelPoint.getName());
                    }
                }
            }
            if (!wheelPoints.isEmpty()) {
                Collections.shuffle(wheelPoints);
                for (String wheelPoint : wheelPoints) {
                    sb.append('c').append(counter).append('=').append(URLEncoder.encode(wheelPoint, "UTF-8")).append('&');
                    counter++;
                }

                sb.append('t').append('=').append(generateBrowRollName()).append("&time=30");
            } else {
                errorSB.append('t').append('=').append("Добавьте_пункты_в_таблицу_перед_роллом").append("&time=30");
                return errorSB.toString();
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            errorSB.append('t').append('=').append("Ошибка.").append("&time=30");
            return errorSB.toString();
        }
    }

    private String generateBrowRollName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HH_mm_ss");
        return "BrowRoll_" + dateFormat.format(new Date());
    }

    private void createFileAndWrite(String s) {
        try {
            File myObj = new File(generateBrowRollName() + ".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(myObj), StandardCharsets.UTF_8);
                writer.write(s);
                writer.close();
                System.out.println("Successfully wrote to the file.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
