package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.entity.StyleProvider;

public class Main extends Application {

    private static final String VERSION = "1.3.0_snapshot";

    private static final int SCENE_WIDTH = 1280;
    private static final int SCENE_HEIGHT = 720;

    @Override
    public void start(Stage primaryStage) throws Exception{
        StyleProvider.setOwner(StyleProvider.Owners.BROWJEY);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Да, это же АУКЦыОН! Теперь со звуком!!! POG " + VERSION);
        primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/pics/" + StyleProvider.getAppIcon())));
        primaryStage.show();
        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        controller.updateUIScale(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
