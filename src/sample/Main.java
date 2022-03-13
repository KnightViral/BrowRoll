package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.entity.StyleProvider;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        StyleProvider.setOwner(StyleProvider.Owners.NUKE);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Да, это же АУКЦыОН! Теперь со звуком!!! POG 1.2.5");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resource/pics/" + StyleProvider.getAppIcon())));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
