package sample.screens.fragments;

import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import sample.entity.WheelPoint;
import sample.screens.fragments.wheels.DuelWheelFragment;

public class DuelFragment {

    private VBox mainVBox;
    private TableView<WheelPoint> table;
    private TableColumn<WheelPoint, String> nameColumn;
    private TableColumn<WheelPoint, Integer> multiplierColumn;
    private TableView<WheelPoint> sourceTable;
    private ToolBar toolBar;
    private Button startBtn;
    private Button stopBtn;
    private DuelWheelFragment wheelFragment;

    public DuelFragment(TableView<WheelPoint> sourceTable) {
        this.sourceTable = sourceTable;

        mainVBox = new VBox();
        VBox.setVgrow(mainVBox, Priority.ALWAYS);

        toolBar = new ToolBar();
        toolBar.setStyle("-fx-background-color: DimGray;");
        HBox.setHgrow(toolBar, Priority.ALWAYS);
        mainVBox.getChildren().add(toolBar);

        CheckBox showTableCB = new CheckBox("Таблица");
        showTableCB.setSelected(true);
        showTableCB.setOnAction(event -> showTable(showTableCB.isSelected()));
        showTableCB.setStyle("-fx-font-weight: bold; -fx-text-fill: lightgray");
        toolBar.getItems().add(showTableCB);
        initStartBtn();
        initStopBtn();

        HBox hBox = new HBox();
        VBox.setVgrow(hBox, Priority.ALWAYS);
        hBox.getChildren().add(initTable());

        wheelFragment = new DuelWheelFragment(table);
        HBox.setHgrow(wheelFragment.getNode(), Priority.ALWAYS);
        hBox.getChildren().add(wheelFragment.getNode());

        mainVBox.getChildren().add(hBox);
    }

    private void initStartBtn() {
        startBtn = new Button("Начать");
        startBtn.setOnAction(event -> startDuel());
        toolBar.getItems().add(startBtn);
    }

    private void initStopBtn() {
        stopBtn = new Button("Прервать");
        stopBtn.setOnAction(event -> stopDuel());
    }

    private TableView<WheelPoint> initTable() {
        table = new TableView<>();
        table.prefWidthProperty().bind(mainVBox.widthProperty().multiply(0.3));
        table.setStyle("-fx-background-image: null;");
        nameColumn = new TableColumn<>();
        nameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.8));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setResizable(false);
        nameColumn.setText("Name");
        table.getColumns().add(nameColumn);
        multiplierColumn = new TableColumn<>();
        multiplierColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.3));
        multiplierColumn.setCellValueFactory(new PropertyValueFactory<>("multiplier"));
        multiplierColumn.setResizable(false);
        multiplierColumn.setText("Amount");
        table.getColumns().add(multiplierColumn);
        sort();
        return table;
    }

    public Node getNode() {
        return mainVBox;
    }

    private void startDuel() {
        toolBar.getItems().remove(startBtn);
        toolBar.getItems().add(stopBtn);
        sourceTable.getItems().forEach(item -> {
            WheelPoint clone = new WheelPoint(item.getId(), item.getName(), item.getMultiplier());
            wheelFragment.addToTable(clone);
        });
        sort();
        wheelFragment.startDuel(true);
    }

    private void stopDuel() {
        toolBar.getItems().remove(stopBtn);
        toolBar.getItems().add(startBtn);
        table.getItems().clear();
        wheelFragment.clearWheel();
        wheelFragment.startDuel(false);
    }

    private void showTable(boolean showTable) {
        table.setVisible(showTable);
        if (showTable) {
            table.prefWidthProperty().bind(mainVBox.widthProperty().multiply(0.3));
        } else {
            table.prefWidthProperty().unbind();
            table.setPrefWidth(0);
        }
    }

    private void sort() {
        multiplierColumn.setSortType(TableColumn.SortType.ASCENDING);
        table.getSortOrder().clear();
        table.getSortOrder().add(multiplierColumn);
        table.sort();
    }

}
