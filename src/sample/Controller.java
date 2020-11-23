package sample;

import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import sample.entity.WheelPoint;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private int idCounter;
    private String[] WAIFUS = new String[]{"Спидвагон", "Аска", "Рей", "Коната", "Брови", "Гит", "Нюк", "Мэд", "Бьёрн", "Варан", "Пепега", "Ндиди"};
    private String[] GAMES = new String[]{"DOTA 2", "World of Tanks", "World of Warcraft", "Stormlord", "Chakan: The Forever Man", "The Lust of Ass 2", "Fortnite", "PUBG", "COD:Warzone", "Kappa в чат", "Шахматы", "Крестики-нолики", "Сапер", "Косынка", "Пить Йод", "Крутить подкрутку"};
    private int JOKE_ID = 9999;

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
                            btn.setOnAction(event -> mainTable.getItems().remove(getIndex()));
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
    }

    @FXML
    void onKekButtonPress() {
        mainTable.getItems().add(getJoke());

        RotateTransition rt = new RotateTransition(Duration.millis(3000), mainTable);
        if (mainTable.getRotate() % 360 != 0) {
            rt.setByAngle(180 * 5);
        } else {
            rt.setByAngle(180 * Utils.getRandomBetween(1, 10));
        }
        rt.play();
    }

    @FXML
    void onWriteFileButtonPress() {
        createFileAndWrite(getPointsString());
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

    private void createFileAndWrite(String s) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy_HH_mm_ss");
            File myObj = new File("BrowRoll_" + dateFormat.format(new Date()) + ".txt");
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
        int rnd = Utils.getRandomTo(13);
        if (mainTable.getItems().size() > 0)
            rnd++;
        switch (rnd) {
            case 0:
                return new WheelPoint(JOKE_ID, "Анус себе подкрути, пес. Keepo", 0);
            case 1:
                return new WheelPoint(JOKE_ID, "Стример - писька.", 0);
            case 2:
                return new WheelPoint(JOKE_ID, "Хватит, голова кружится.", 0);
            case 3:
                return new WheelPoint(JOKE_ID, "Я заготовил кучу фраз, все не перекрутите. (нет)", 0);
            case 4:
                return new WheelPoint(JOKE_ID, "Анимешники не человеки.", 0);
            case 5:
                return new WheelPoint(JOKE_ID, "Анимешники - сверхчеловеки.", 0);
            case 6:
                return new WheelPoint(JOKE_ID, "Ваше очко уходит в зрительный зал.", 0);
            case 7:
                return new WheelPoint(JOKE_ID, String.format("%s - лучшая вайфу. <3", getWaifu()), 0);
            case 8:
                return new WheelPoint(JOKE_ID, String.format("%s - лучшая игра.", getGame()), 0);
            case 9:
                return new WheelPoint(JOKE_ID, getRandomGameName(), 999);
            case 10:
                return new WheelPoint(JOKE_ID, "Осуждаю", 0);
            case 11:
                return new WheelPoint(JOKE_ID, "Кто спросил:\"Что делает подкрутка?\", тот выигрывает таймач.", 0);
            case 12:
                return new WheelPoint(JOKE_ID, "Ндиди", 0);
            case 13:
                return new WheelPoint(JOKE_ID, "+игра на все платформы", 0);
            case 14:
                return new WheelPoint(JOKE_ID, String.format("%s - ну и говно, кто это заказал?", getRandomNameFromTable()), -999);
            default:
                return new WheelPoint(JOKE_ID, getRandomGameName(), 999);
        }
    }

    private String getWaifu() {
        return WAIFUS[Utils.getRandomTo(WAIFUS.length - 1)];
    }

    private String getGame() {
        return GAMES[Utils.getRandomTo(GAMES.length - 1)];
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


}
