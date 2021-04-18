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
import sample.entity.ResourceWorker;
import sample.entity.RollSave;
import sample.entity.SaveLoadWizard;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class LoadingController {

    @FXML
    public TableView<RollSave> mainTable;
    @FXML
    public TableColumn<RollSave, String> winnerNameColumn;
    @FXML
    public TableColumn<RollSave, Date> dateColumn;
    @FXML
    public TableColumn<RollSave, Void> loadBtnColumn;

    @FXML
    void initialize() {
        initTable();
    }

    private void initTable() {
        initWinnerNameColumn();
        initDateColumn();
        initLoadBtnColumn();
        mainTable.getStylesheets().add(getClass().getResource("/sample/style.css").toExternalForm());
        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        mainTable.getSortOrder().clear();
        mainTable.getSortOrder().add(dateColumn);
        mainTable.sort();
        fillTable();
    }

    private void initWinnerNameColumn() {
        winnerNameColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.45));
        winnerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void initDateColumn() {
        dateColumn.prefWidthProperty().bind(mainTable.widthProperty().multiply(0.45));
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

    private void fillTable(){
        try {
            for (File file : ResourceWorker.getFiles(Utils.getLocalAppData(), "\\" + SaveLoadWizard.getFileName())) {
                mainTable.getItems().add(SaveLoadWizard.load(file.getName()));
            }
        } catch (Exception e){
            //todo EXCEPTION
        }
    }

}
