package sample;

import javafx.animation.RotateTransition;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.image.Image;
import sample.entity.*;
import sample.screens.fragments.DuelFragment;

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
    @FXML
    public CheckBox regimeCheckBox;
    @FXML
    public Tab testWheelTab;
    @FXML
    public TextField searchTF;
    @FXML
    public AnchorPane mainAnchorPane;
    @FXML
    public Label timeLabel;
    @FXML
    public CheckMenuItem timerLabelCB;

    private int idCounter;

    private final static String WHEEL_LINK = "https://wheeldecide.com/?";
    private double countMultiplier = 0;
    private boolean tableSpins = false;

    private MyTimer timer;
    private DuelFragment duelFragment;

    @FXML
    void initialize() {
        initTable();
        initWheel();
        initTabPane();
        idCounter = 1;
        onRubblesTypeButtonPress();
        initTimer();
        randomKekCheckBox.setSelected(false);
        initTestWheelTab();

        searchTF.textProperty().addListener(
                (observable, oldValue, newValue) -> mainTable.refresh());
    }

    private void initTestWheelTab() {
        duelFragment = new DuelFragment(mainTable);
        testWheelTab.setContent(duelFragment.getNode());
    }

    private void initTimer(){
        timer = new MyTimer("main", 0, 30, 0);
        timerMenu.textProperty().bind(timer.getStringProperty());
        timeLabel.textProperty().bind(timer.getStringProperty());
        timer.setAlarm(true);
    }

    private void initWheel() {
        wheelPC.getStylesheets().add(getClass().getResource(StyleProvider.getStyle()).toExternalForm());
    }

    private void initTabPane() {
        tabPane.getStylesheets().add(getClass().getResource(StyleProvider.getStyle()).toExternalForm());
        initWheelTab();
    }

    private void initWheelTab() {
        cssImageVBox.getStylesheets().add(getClass().getResource(StyleProvider.getStyle()).toExternalForm());
        cssImageVBox.getStyleClass().add("vboxMark");
        browRollImg.setImage(new Image(String.valueOf(this.getClass().getResource("resource/pics/" + StyleProvider.getRollPic()))));
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
        mainTable.getStylesheets().add(getClass().getResource(StyleProvider.getStyle()).toExternalForm());
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
                                countAllMultipliers();
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
                                countAllMultipliers();
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
                                countAllMultipliers();
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
                                countAllMultipliers();
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
                                countMultiplier > 0 &&
                                mainTable.getItems().get(getIndex()).getMultiplier() > 0 &&
                                mainTable.getItems().get(getIndex()).getId() != JokeGenerator.getJokeId()
                        ){
                            setText(String.format("%.1f", (double) mainTable.getItems().get(getIndex()).getMultiplier() / countMultiplier * 100));
                            getTableRow().pseudoClassStateChanged(PseudoClass.getPseudoClass("important"),
                                    !searchTF.getText().isEmpty() &&
                                    mainTable.getItems().get(getIndex()).getName().toLowerCase().contains(searchTF.getText().toLowerCase()));
                        }
                        else {
                            setText(null);
                        }
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
        countAllMultipliers();
        clearWheel();
    }

    @FXML
    void onAddButtonPress() {
        if (!newNameTA.getText().isEmpty()) {
            if (duelRegime) {
                addToTableDuel(new WheelPoint(idCounter, newNameTA.getText(), getNewMultiplier()));
            } else {
                addToTable(new WheelPoint(idCounter, newNameTA.getText(), getNewMultiplier()));
            }
            idCounter++;
            newNameTA.clear();
            newMultiplierTA.clear();
            sort();
        }
        countAllMultipliers();
        if (randomKekCheckBox.isSelected()){
            if (Utils.getRandomTo(10) > 8){
                onKekButtonPress();
            }
        }
    }

    @FXML
    void onKekButtonPress() {
        SaveLoadWizard.save(mainTable, "Автосейв перед подкруткой.");
        if (duelRegime) {
            addToTableDuel(JokeGenerator.getJoke(mainTable));
        } else {
            addToTable(JokeGenerator.getJoke(mainTable));
        }
        if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Таблица"))
            randomRotateMainTable();
        sort();
        countAllMultipliers();
    }

    @FXML
    void onWriteFileButtonPress() {
        createFileAndWrite(getPointsString());
    }

    @FXML
    void onLoadScreenButtonPress() {
        openLoadScreen();
    }

    @FXML
    void onSaveCurrentTableButtonPress() {
        SaveLoadWizard.save(mainTable, "Сохранено вручную");
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
        if (duelRegime) {
            calculateWheelDuel(countAllWheelPoints());
        } else {
            calculateWheel(countAllMultipliers());
        }
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
        if (duelRegime) {
            makeRollDuel();
        } else {
            makeRoll();
        }
    }

    private void makeRoll() {
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
                track.start(0, true);
                rt.setOnFinished(event -> {
                    WheelPoint winner = checkRollWinner();
                    String winnerName = getWheelPointNameNotNull(winner);
                    rollBtn.setText(winnerName);
                    track.stop();
                    MyAudioTrack trackFinish = new MyAudioTrack(c.getClass().getResource("resource/" + SoundsProvider.getSound()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                    trackFinish.start();
                    try { //todo EXCEPTION
                        SaveLoadWizard.save(mainTable, winnerName);
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
                        SaveLoadWizard.save(mainTable, winnerName);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                });
            }
            rtBrow.setOnFinished(event -> browRollImg.setVisible(false));
            rt.play();
            rtBrow.play();
        }
    }

    private String getWheelPointNameNotNull(WheelPoint wheelPoint) {
        if (wheelPoint != null){
            return wheelPoint.getName();
        }
        return "Пук среньк Fallout 76 (ошибка)";
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
    void onYouTubeButtonPress() {
        newNameTA.setText(newNameTA.getText() + " (YouTube)");
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

    @FXML
    void onTimerLabelVisibilityCB() {
        timeLabel.setVisible(timerLabelCB.isSelected());
    }

    @FXML
    void onFillTestDataButtonPress() {
        addToTable(new WheelPoint(1, "isaac", 5));
        addToTable(new WheelPoint(2, "Какаята анима", 50));
        addToTable(new WheelPoint(3, "ДС нодес", 50));
        addToTable(new WheelPoint(4, "Варио", 50));
        addToTable(new WheelPoint(5, "Адвенчур айленд", 50));
        addToTable(new WheelPoint(6, "Елекс", 50));
        addToTable(new WheelPoint(7, "Якудза 0", 250));
        addToTable(new WheelPoint(8, "Геншин импакт", 50));
        addToTable(new WheelPoint(9, "Рандом дос", 51));
        addToTable(new WheelPoint(10, "Рандом снес", 51));
        addToTable(new WheelPoint(11, "Ви зе революшн", 52));
        addToTable(new WheelPoint(12, "Шадоу ман", 100));
        addToTable(new WheelPoint(13, "Чакан", 100));
        addToTable(new WheelPoint(14, "КСго", 100));
        addToTable(new WheelPoint(15, "Арех", 155));
        addToTable(new WheelPoint(16, "Саботер", 176));
        addToTable(new WheelPoint(17, "Герои 1", 200));
        addToTable(new WheelPoint(18, "Герои 2", 200));
        addToTable(new WheelPoint(19, "Герои 3", 200));
        addToTable(new WheelPoint(20, "Герои 4", 200));
        addToTable(new WheelPoint(21, "Герои 5", 200));
        addToTable(new WheelPoint(22, "Рандом амига", 200));
        addToTable(new WheelPoint(23, "Сторибук бравл", 200));
        addToTable(new WheelPoint(24, "Варфреим", 200));
        addToTable(new WheelPoint(25, "Дум 3", 200));
        addToTable(new WheelPoint(26, "Рисен 3", 200));
        addToTable(new WheelPoint(27, "Ноита", 223));
        addToTable(new WheelPoint(28, "Каулдрон", 225));
        addToTable(new WheelPoint(29, "Дайси Данжон", 250));
        addToTable(new WheelPoint(30, "рандом нес", 251));
        addToTable(new WheelPoint(31, "Андермайн", 300));
        addToTable(new WheelPoint(32, "SteamWorld dig 2", 300));
        addToTable(new WheelPoint(33, "Will Rock", 400));
        addToTable(new WheelPoint(34, "Blood", 666));
        addToTable(new WheelPoint(35, "Рандом спектрум", 666));
        addToTable(new WheelPoint(36, "BPM", 700));
        addToTable(new WheelPoint(37, "Тексторцист", 855));
        addToTable(new WheelPoint(38, "Вар Тандер", 1000));
        addToTable(new WheelPoint(39, "Руинер", 1000));
        addToTable(new WheelPoint(40, "Джорни", 1000));
        addToTable(new WheelPoint(41, "Твистед метал 2", 1750));
        addToTable(new WheelPoint(42, "Dark Messiah of Might and Magic", 100));
        idCounter = 43;
        sort();
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

    private WheelPoint checkRollWinner() {
        double winDegree = wheelPC.getRotate() % 360.0;
        double wheelDegreePrev = 360;
        double wheelDegree = 360;
        double step = 360.0 / getMultiplierFromWheel();
        for (PieChart.Data data : wheelPC.getData()) {
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
        for (PieChart.Data data : wheelPC.getData()) {
            res = res + data.getPieValue();
        }
        return res;
    }

    private WheelPoint findWheelPointByData(PieChart.Data data){
        for (WheelPoint wheelPoint : mainTable.getItems()) {
            if (wheelPoint.getWheelData().equals(data)){
                return wheelPoint;
            }
        }
        return null;
    }

    private double countAllMultipliers() {
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
        countMultiplier = counter;
        rollBtn.setVisible(counter > 0);
        cssImageVBox.setVisible(counter > 0);
        return counter;
    }

    private int countAllWheelPoints() {
        int counter = 0;
        for (WheelPoint wheelPoint : mainTable.getItems()) {
            if (wheelPoint.getId() != JokeGenerator.getJokeId()) {
                counter++;
            }
        }
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
        if (!tableSpins) {
            tableSpins = true;
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
            rt.setOnFinished(event -> {
                tableSpins = false;
            });
            rt.play();
        }
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

    private void openLoadScreen() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("screens/loadingcontroller.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        try {
            stage.setScene(new Scene(loader.load())
            );
        } catch (IOException e) {
            //todo exceptions
            e.printStackTrace();
        }
        stage.setTitle("Логи ауков");

        stage.show();
    }

    //=============================================================================================================================================================

    private boolean duelRegime = false;
    private boolean freezeWheel = false;
    private boolean needsRecalculation = false;
    private boolean needsEating = false; //todo needs wizard for this
    private WheelPoint winner = null;
    private MyAudioTrack music = null;

    @FXML
    void onSwitchRegime() {
        switchRegimes(regimeCheckBox.isSelected());
    }

    private void switchRegimes(boolean duelRegime){
        this.duelRegime = duelRegime;
        needsRecalculation = false;
        needsEating = false;
        winner = null;
        if (music == null) {
            music = new MyAudioTrack(this.getClass().getResource("resource/" + SoundsProvider.getMKMusic()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
        }
        if (duelRegime) {
            SaveLoadWizard.save(mainTable, "Включен режим дуэли. Автосохранение.");
            browRollImg.setImage(new Image(String.valueOf(this.getClass().getResource("resource/pics/" + StyleProvider.getDuelRollPic()))));
            calculateWheelDuel(countAllWheelPoints());
            MyAudioTrack track = new MyAudioTrack(this.getClass().getResource("resource/" + SoundsProvider.getMKChoose()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
            track.start(0);
            music.start(0, true);
        } else {
            browRollImg.setImage(new Image(String.valueOf(this.getClass().getResource("resource/pics/" + StyleProvider.getRollPic()))));
            calculateWheel(countAllMultipliers());
            MyAudioTrack track = new MyAudioTrack(this.getClass().getResource("resource/" + SoundsProvider.getSlap()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
            track.start();
            music.close();
        }
    }

    private void calculateWheelDuel(double count) {
        clearWheel();
        rollBtn.setVisible(false);
        cssImageVBox.setVisible(false);

        if (count >= 2) {
            rollBtn.setVisible(true);
            cssImageVBox.setVisible(true);
            List<WheelPoint> wheelPoints = new ArrayList<>(
                    mainTable.getItems()
                    .filtered(wheelPoint -> wheelPoint.getId() != JokeGenerator.getJokeId())
            );
            //wheelPoints.sort(Comparator.comparingDouble(WheelPoint::getMultiplier));
            wheelPoints.sort(new Comparator<WheelPoint>() {
                @Override
                public int compare(WheelPoint o1, WheelPoint o2) {
                    if (o1.getMultiplier() < o2.getMultiplier())
                        return -1;
                    if (o1.getMultiplier() > o2.getMultiplier())
                        return 1;

                    long thisBits    = Double.doubleToLongBits(o1.getMultiplier());
                    long anotherBits = Double.doubleToLongBits(o2.getMultiplier());

                    return (thisBits == anotherBits ?  -1 :
                            (thisBits < anotherBits ? -1 :
                                    1));
                }
            });
            wheelPoints.sort(Comparator.comparingDouble(WheelPoint::getMultiplier));
            addToWheel(wheelPoints.get(0));
            addToWheel(wheelPoints.get(1));
        } else {
            if (count == 1) {
                mainTable.getItems().forEach(this::addToWheel);
            }
        }
    }

    private void makeRollDuel() {
        if (!freezeWheel) {
            if (needsRecalculation || needsEating) {
                if (needsEating) {
                    eatingAnimation(this.winner);
                    sort();
                } else {
                    calculateWheelDuel(countAllWheelPoints());
                    needsRecalculation = false;
                }
            } else {
                if (!wheelPC.getData().isEmpty()) {
                    rollBtn.setText("Крутонуть");
                    wheelPC.setRotate(0);
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
                    RotateTransition rt = new RotateTransition(Duration.millis(time), wheelPC);
                    rt.setByAngle(Utils.getRandomBetween(rotateFrom, rotateTo));
                    browRollImg.setRotate(0);
                    browRollImg.setVisible(true);
                    RotateTransition rtBrow = new RotateTransition(Duration.millis(time), browRollImg);
                    rtBrow.setByAngle(-Utils.getRandomBetween(rotateFromBrow, rotateToBrow));
                    Controller c = this;
                    try {
                        MyAudioTrack track = new MyAudioTrack(c.getClass().getResource("resource/ORA ORA ORA Vs MUDA MUDA MUDA.wav"), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                        track.start(0, true);
                        rt.setOnFinished(event -> {
                            WheelPoint winner = checkRollWinner();
                            String winnerName = getWheelPointNameNotNull(winner);
                            rollBtn.setText(winnerName);
                            track.stop();
                            MyAudioTrack trackFinish = new MyAudioTrack(c.getClass().getResource("resource/" + SoundsProvider.getMKHit()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                            trackFinish.start(0);
                            needsEating = true;
                            this.winner = winner;

                            try { //todo EXCEPTION
                                SaveLoadWizard.save(mainTable, winnerName);
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
                            needsEating = true;
                            this.winner = winner;

                            try { //todo EXCEPTION
                                SaveLoadWizard.save(mainTable, winnerName);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        });
                    }
                    rtBrow.setOnFinished(event -> browRollImg.setVisible(false));
                    rt.play();
                    rtBrow.play();
                }
            }
        }
    }

    private void eatingAnimation(WheelPoint winner) {
        freezeWheel = true;
        for (PieChart.Data data : wheelPC.getData()) {
            if (!data.equals(winner.getWheelData())) {
                WheelPoint loser = findWheelPointByData(data);
                if (loser != null) {
                    winner.setMultiplier(winner.getMultiplier() + loser.getMultiplier());
                    removeFromTable(loser);
                    MyAudioTrack track = new MyAudioTrack(this.getClass().getResource("resource/" + SoundsProvider.getMKEat()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
                    track.start(0);
                    if (countAllWheelPoints() == 1) {
                        MyAudioTrack winTrack = new MyAudioTrack(this.getClass().getResource("resource/" + SoundsProvider.getMKIWin()), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
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


    private void addToTableDuel(WheelPoint wheelPoint) {
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
                calculateWheelDuel(countAllWheelPoints());
            } else
                oldElement.setMultiplier(oldElement.getMultiplier() + wheelPoint.getMultiplier());
        } else {
            mainTable.getItems().add(wheelPoint);
        }
        mainTable.refresh();
    }
}
