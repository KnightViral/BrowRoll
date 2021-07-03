package sample.screens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample.Utils;
import sample.entity.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class LoadingController {

    @FXML
    public TableView<RollSave> mainTable;
    @FXML
    public TableColumn<RollSave, String> winnerNameColumn;
    @FXML
    public TableColumn<RollSave, Void> dateColumn;
    @FXML
    public TableColumn<RollSave, Void> loadBtnColumn;
    @FXML
    public TableColumn<RollSave, Void> fileBtnColumn;

    @FXML
    void initialize() {
        initTable();
    }

    private void initTable() {
        initWinnerNameColumn();
        initDateColumn();
        initLoadBtnColumn();
        initFileBtnColumn();
        mainTable.getStylesheets().add(getClass().getResource("/sample/style.css").toExternalForm());
        fillTable();
        mainTable.getItems().sort(Comparator.comparing(RollSave::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
    }

    private void initWinnerNameColumn() {
        winnerNameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.40));
        winnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void initDateColumn() {
        dateColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.40));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void initLoadBtnColumn() {
        loadBtnColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.10));
        loadBtnColumn.setCellFactory(new Callback<TableColumn<RollSave, Void>, TableCell<RollSave, Void>>() {
            @Override
            public TableCell<RollSave, Void> call(TableColumn param) {
                return new TableCell<RollSave, Void>() {

                    final Button btn = new Button("Load");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setOnAction(event -> {
                                RollSave save = mainTable.getItems().get(getIndex());
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/screens/historycontroller.fxml"));
                                Stage stage = new Stage(StageStyle.DECORATED);
                                try {
                                    stage.setScene(new Scene(loader.load())
                                    );
                                } catch (IOException e) {
                                    //todo exceptions
                                    e.printStackTrace();
                                }
                                HistoryController historyController = loader.getController();
                                historyController.setWheelPoints(save.getList());
                                stage.setTitle(save.getDate() + " Победитель: " + save.getName());
                                stage.show();
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

    private void initFileBtnColumn() {
        fileBtnColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.10));
        fileBtnColumn.setCellFactory(new Callback<TableColumn<RollSave, Void>, TableCell<RollSave, Void>>() {
            @Override
            public TableCell<RollSave, Void> call(TableColumn param) {
                return new TableCell<RollSave, Void>() {

                    final Button btn = new Button("File");

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.setOnAction(event -> {
                                RollSave save = mainTable.getItems().get(getIndex());
                                createFileAndWrite(save);
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

    private void fillTable(){
        try {
            for (File file : ResourceWorker.getFiles(Utils.getLocalAppData(), "\\" + SaveLoadWizard.getFileName())) {
                mainTable.getItems().add(SaveLoadWizard.load(file.getName()));
            }
        } catch (Exception e){
            //todo EXCEPTION
        }
    }

    private void createFileAndWrite(RollSave save) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH_mm_ss");
            File myObj = new File("rollSave_" + sdf.format(save.getDate()) + ".csv");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(myObj), StandardCharsets.UTF_8);
                writer.write(getSaveString(save));
                writer.close();
                System.out.println("Successfully wrote to the file.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace(); //todo EXCEPTION
        }
    }

    private String getSaveString(RollSave save) {
        StringBuilder sb = new StringBuilder();

        save.getList().forEach(roll -> {
            if (sb.length() != 0) {
                sb.append("\n");
            } else {
                sb.append("Победитель;").append(save.getName()).append("\n")
                        .append("Дата;").append(save.getDate()).append("\n");
            }
            WheelPoint wheelPoint = roll.load(0);
            sb.append(wheelPoint.getName()).append(';').append(wheelPoint.getMultiplier());
        });
        return sb.toString();
    }

}
