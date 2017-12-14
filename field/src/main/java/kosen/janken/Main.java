package kosen.janken;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int MIN_SCREEN_WIDTH = 1000;
    private static final int MIN_SCREEN_HEIGHT = 640;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("じゃんけんするよ");
        Scene scene = new Scene(root, MIN_SCREEN_WIDTH, MIN_SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_SCREEN_WIDTH);
        primaryStage.setMinHeight(MIN_SCREEN_HEIGHT);
        Controller controller = fxmlLoader.getController();
        controller.init(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }


}
