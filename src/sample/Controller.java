package sample;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import sample.entity.WheelPoint;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    private final static int JOKE_ID = 9999;
    private final static String HWEEL_LINK = "https://wheeldecide.com/?";

    @FXML
    void initialize() {
        initTable();
        mainTable.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        idCounter = 1;
        sort();
    }

    private void initTable() {
        initIdColumn();
        initNameColumn();
        initMultiplierColumn();
        initAddColumn();
        initDecreaseColumn();
        initRemoveColumn();
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
                    mainTable.getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
                }
        );
        if (idColumn.isVisible())
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.75));
        else
            nameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.8));
    }

    private void initMultiplierColumn() {
        multiplierColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.05));
        multiplierColumn.setCellValueFactory(new PropertyValueFactory<>("multiplier"));
    }

    private void initAddColumn() {
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
                                mainTable.refresh();
                                countAllPoints();
                            });
                            setGraphic(btn);
                        }
                        setText(null);
                    }
                };
            }
        });
    }

    private void initDecreaseColumn() {
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
                                mainTable.refresh();
                                countAllPoints();
                            });
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
                                mainTable.getItems().remove(getIndex());
                                countAllPoints();
                            });
                            setGraphic(btn);
                        }
                        setText(null);
                    }
                };
            }
        });
    }

    @FXML
    void onAddButtonPress() {
        if (!newNameTA.getText().isEmpty()) {
            mainTable.getItems().add(new WheelPoint(idCounter, newNameTA.getText(), getNewMultiplier()));
            idCounter++;
            newNameTA.clear();
            newMultiplierTA.clear();
            mainTable.sort();
        }
        countAllPoints();
    }

    @FXML
    void onKekButtonPress() {
        mainTable.getItems().add(getJoke());
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

    private void sort() {
        multiplierColumn.setSortType(TableColumn.SortType.DESCENDING);
        mainTable.getSortOrder().clear();
        mainTable.getSortOrder().add(multiplierColumn);
        mainTable.sort();
    }

    private int countAllPoints() {
        int counter = 0;
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
        return counter;
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

    private int getNewMultiplier() {
        try {
            return Integer.parseInt(newMultiplierTA.getText());
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
        errorSB.append(HWEEL_LINK);
        errorSB.append('c').append(1).append('=').append("Пук").append('&');
        errorSB.append('c').append(2).append('=').append("Среньк").append('&');
        errorSB.append('c').append(3).append('=').append("Fallout_76").append('&');
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(HWEEL_LINK);
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
                    return new WheelPoint(JOKE_ID, "Я заготовил кучу фраз, все не перекрутите. (нет)", 0);
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
