package sample;

import javafx.animation.RotateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.image.Image;
import sample.entity.MyAudioTrack;
import sample.entity.WheelPoint;

import javax.sound.sampled.AudioSystem;
import java.awt.*;
import java.io.*;
import java.net.URI;
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
    public Label counterLabel;
    @FXML
    public Label counterLabelCaption;
    @FXML
    public PieChart wheelPC;
    @FXML
    public StackPane wheelSPane;
    @FXML
    public TabPane tabPane;
    @FXML
    public Button rollBtn;
    @FXML
    public ImageView wheelArrowImg;
    @FXML
    public ImageView browRollImg;

    private int idCounter;
    private final static String[] WAIFUS = new String[]{"Спидвагон", "Брови", "Гит", "Нюк", "Мэд", "Бьёрн", "Варан", "Пепе", "Ндиди"};
    private final static String[] GAMES = new String[]{"DOTA 2", "World of Tanks", "World of Warcraft", "Stormlord", "Chakan: The Forever Man", "The Lust of Ass 2", "Fortnite", "PUBG", "COD:Warzone", "Пук среньк Fallout 76", "Kappa в чат", "Шахматы", "Крестики-нолики", "Сапер", "Косынка", "Пить Йод", "Крутить подкрутку"};
    private final static String[] ANEK_START = new String[]{
            "Как-то раз %s решил выбрать себе псевдоним",
            "Нашел %s шляпу",
            "%s так и не научился печь хлеб",
            "Пришли как-то %s и Лупа получать зарплату",
            "Идет %s, видит — подкова на дороге",
            "Идет %s по пустыне",
            "Пошел %s, купил ваксы, лицо натёр, тело натёр"
    };
    private final static String[] ANEK_END = new String[]{
            "с тех под так и подписывал свои книги - %s.",
            "а она ему как раз.",
            "и выкинул, нахуй, c обрыва.",
            "а %s в Щепки.",
            "засунул себе в ухо и оглох.",
            "и ебанулся с лошади насмерть.",
            "схватил пузырь и убежал.",
            "\"понятно\", сказал %s и сломал ему ногу.",
            "%s не того стримера разбудил.",
            "и у него отвалилась жопа."
    };
    private final static String[] END_SOUNDS = new String[]{"300.wav", "Deep dark fantasies.wav", "fuck you.wav", "Iam an artist.wav", "NANI.wav", "Omae wa mou shindeiru.wav", "Spank.wav", "WOO.wav", "YES I AM.wav", "YES YES YES YES YES.wav"};
    private final static int JOKE_ID = 9999;
    private final static String WHEEL_LINK = "https://wheeldecide.com/?";
    private double countPoints = 0;

    @FXML
    void initialize() {
        initTable();
        initWheel();
        initTabPane();
        idCounter = 1;
        onRubblesTypeButtonPress();
    }

    private void initWheel() {
        wheelPC.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
    }

    private void initTabPane() {
        tabPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        initWheelTab();
    }

    private void initWheelTab() {
        wheelArrowImg.setImage(new Image(String.valueOf(this.getClass().getResource("resource/pics/mark.png"))));
        wheelArrowImg.fitWidthProperty().bind(wheelSPane.widthProperty());
        wheelArrowImg.fitHeightProperty().bind(wheelSPane.heightProperty());
        wheelArrowImg.setSmooth(false);
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
                    changeTableElementName(event.getRowValue(), event.getNewValue());
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
                                wheelPoint.addMultiplier();
                                changeTableElementMultiplier(wheelPoint, wheelPoint.getMultiplier());
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
                                wheelPoint.decreaseMultiplier();
                                changeTableElementMultiplier(wheelPoint, wheelPoint.getMultiplier());
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
                                changeTableElementMultiplier(wheelPoint, wheelPoint.getMultiplier() + number);
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
                                mainTable.getItems().get(getIndex()).getId() != JOKE_ID
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
        if (wheelPoint.getId() != JOKE_ID) {
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
                addToWheel(wheelPoint.getName(), wheelPoint.getMultiplier());
            } else
                changeTableElementMultiplier(oldElement, oldElement.getMultiplier() + wheelPoint.getMultiplier());
        } else {
            mainTable.getItems().add(wheelPoint);
        }
        mainTable.refresh();
    }

    private void removeFromTable(WheelPoint wheelPoint) {
        if (wheelPoint.getId() != JOKE_ID) {
            PieChart.Data toRemove = null;
            for (PieChart.Data data : wheelPC.getData()) {
                if (wheelPoint.getName().equals(data.getName()))
                    toRemove = data;
            }
            wheelPC.getData().remove(toRemove);
            //TODO исключение если ничего не нашли
        }
        mainTable.getItems().remove(wheelPoint);
        mainTable.refresh();
    }

    private void changeTableElementName(WheelPoint wheelPoint, String newName) {
        String oldName = wheelPoint.getName();
        wheelPoint.setName(newName);
        if (wheelPoint.getId() != JOKE_ID) {
            wheelPC.getData().forEach(data -> {
                if (data.getName().equals(oldName))
                    data.setName(newName);
            });
        }
        mainTable.refresh();
    }

    private void changeTableElementMultiplier(WheelPoint wheelPoint, double value) {
        wheelPoint.setMultiplier(value);
        if (wheelPoint.getId() != JOKE_ID) {
            wheelPC.getData().forEach(data -> {
                if (data.getName().equals(wheelPoint.getName()))
                    data.setPieValue(value);
            });
        }
        mainTable.refresh();
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
    }

    @FXML
    void onKekButtonPress() {
        addToTable(getJoke());
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
        openWebpage(getPointsLink());
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
            MyAudioTrack trackFinish = new MyAudioTrack(this.getClass().getResource("resource/" + getRandomStringFromArray(END_SOUNDS)), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
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
                    MyAudioTrack trackFinish = new MyAudioTrack(c.getClass().getResource("resource/" + getRandomStringFromArray(END_SOUNDS)), Collections.singletonList(Arrays.stream(AudioSystem.getMixerInfo()).iterator().next()));
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
        counterLabelCaption.setVisible(true);
        counterLabel.setVisible(true);
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
        counterLabelCaption.setVisible(false);
        counterLabel.setVisible(false);
        newMultiplierTA.setPromptText("Сумма");
        if (idColumn.isVisible())
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.65));
        else
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.70));
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
                System.out.println(data.getName());
                return data.getName();
            }
        }
        return "Пук среньк Fallout 76 (ошибка)";
    }

    private double countAllPoints() {
        double counter = 0;
        for (WheelPoint wheelPoint : mainTable.getItems()) {
            if (wheelPoint.getId() != JOKE_ID) {
                counter = counter + wheelPoint.getMultiplier();
            }
        }
        counterLabel.setText(String.valueOf(counter));
        if (counter > 100) {
            openInWebMenuItem.setText("Слишком много пунктов. Wheeldecide принимает максимум 100");
            openInWebMenuItem.setDisable(true);
        } else {
            openInWebMenuItem.setText("Открыть на Wheeldecide");
            openInWebMenuItem.setDisable(false);
        }
        countPoints = counter;
        rollBtn.setVisible(counter > 0);
        wheelArrowImg.setVisible(counter > 0);
        return counter;
    }


    private void calculateWheel(double count) {
        clearWheel();
        if (count > 0) {
            rollBtn.setVisible(true);
            for (WheelPoint wheelPoint : mainTable.getItems()) {
                if (wheelPoint.getId() != JOKE_ID) {
                    addToWheel(wheelPoint.getName(), wheelPoint.getMultiplier());
                }
            }
        } else {
            rollBtn.setVisible(false);
        }
    }


    private void addToWheel(String name, double value) {
        PieChart.Data data = new PieChart.Data(name, value);
        wheelPC.getData().add(data);
        Tooltip tooltip = new Tooltip(data.getName());
        Tooltip.install(data.getNode(), tooltip);
        data.pieValueProperty().addListener((observable, oldPieValue, newPieValue) -> {
            tooltip.setText(data.getName());
        });
    }

    private void clearWheel() {
        wheelPC.getData().clear();
        wheelPC.setRotate(0);
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
            if (wheelPoint.getId() != JOKE_ID) {
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
                if (wheelPoint.getId() != JOKE_ID) {
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

    private WheelPoint getJoke() {
        int rnd = Utils.getRandomTo(12);
        if (mainTable.getItems().size() > 0)
            rnd++;
        switch (rnd) {
            case 0:
                return new WheelPoint(JOKE_ID, "Анус себе подкрути, пес. Keepo", 0);
            case 1: {
                if (Utils.getRandomTo(1) == 0)
                    return new WheelPoint(JOKE_ID, "Стример - писька.", 0);
                else
                    return new WheelPoint(JOKE_ID, String.format("Я заготовил %s фраз, все не перекрутите. (нет)", Utils.getRandomBetween(1451, 5552)), 0);
            }
            case 2:
                return new WheelPoint(JOKE_ID, "Хватит, голова кружится.", 0);
            case 3: {
                if (Utils.getRandomTo(1) == 0)
                    return new WheelPoint(JOKE_ID, "Анимешники не человеки.", 0);
                else
                    return new WheelPoint(JOKE_ID, "Анимешники - сверхчеловеки.", 0);
            }
            case 4: {
                if (Utils.getRandomTo(1) == 0)
                    return new WheelPoint(JOKE_ID, "Ваше очко уходит в зрительный зал.", 0);
                else
                    return new WheelPoint(JOKE_ID, "Зрительный зал уходит в ваше очко.", 0);
            }
            case 5:
                return new WheelPoint(JOKE_ID, String.format("%s - лучшая вайфу. <3", getWaifu()), 0);
            case 6:
                return new WheelPoint(JOKE_ID, String.format("%s - лучшая игра.", getGame()), 0);
            case 7:
                return new WheelPoint(JOKE_ID, getRandomGameName(), 999);
            case 8:
                return new WheelPoint(JOKE_ID, "Осуждаю", 0);
            case 9:
                return new WheelPoint(JOKE_ID, "Кто спросил:\"Что делает подкрутка?\", тот выигрывает таймач.", 0);
            case 10:
                return new WheelPoint(JOKE_ID, "Ндиди", 0);
            case 11:
                return new WheelPoint(JOKE_ID, "+игра на все платформы", 0);
            case 12:
                return new WheelPoint(JOKE_ID, getAnek(), 0);
            case 13:
                return new WheelPoint(JOKE_ID, String.format("%s - ну и говно, кто это заказал?", getRandomNameFromTable()), -999);
            default:
                return new WheelPoint(JOKE_ID, getRandomGameName(), 999);
        }
    }

    private String getRandomStringFromArray(String[] array) {
        return array[Utils.getRandomTo(array.length - 1)];
    }

    private String getWaifu() {
        return getRandomStringFromArray(WAIFUS);
    }

    private String getGame() {
        return getRandomStringFromArray(GAMES);
    }

    private String getStartAnek() {
        return getRandomStringFromArray(ANEK_START);
    }

    private String getEndAnek() {
        return getRandomStringFromArray(ANEK_END);
    }

    private String getAnek() {
        return String.format(getStartAnek(), getWaifu()) + ", " + String.format(getEndAnek(), getWaifu());
    }

    private String getRandomNameFromTable() {
        return mainTable.getItems().get(Utils.getRandomTo(mainTable.getItems().size() - 1)).getName();
    }

    private String getRandomGameName() {
        if (mainTable.getItems().size() == 0) {
            return getGame();
        } else {
            if (Utils.getRandomTo(1) == 0)
                return getGame();
            else
                return getRandomNameFromTable();
        }
    }

    //TODO вынести в ютилс
    private static boolean openWebpage(String link) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(link));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
