package kosen.janken;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import rps.RPS;
import rps.RPSListener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Controller {
    @FXML
    private Pane main;
    @FXML
    private Label condition1, condition2;
    @FXML
    private ImageView player11, player12, player21, player22;

    public final Image rock = new Image(getClass().getResourceAsStream("/rock.png"));
    public final Image paper = new Image(getClass().getResourceAsStream("/paper.png"));
    public final Image scissor = new Image(getClass().getResourceAsStream("/scissor.png"));

    private File playerOneJar, playerTwoJar;
    private RPSListener playerRPS1, playerRPS2;

    private Stage primaryStage;

    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;

        player11.setImage(rock);
        player12.setImage(rock);
        player21.setImage(rock);
        player22.setImage(rock);
    }

    public void buttonsHandler(ActionEvent event) {
        switch (((Button) event.getSource()).getId()) {
            case "select_one"://ファイル取得
                playerOneJar = getJarFile();
                if (playerOneJar != null) {
                    playerRPS1 = loadClass(playerOneJar);
                    condition1.setText(playerOneJar.getName() + " is set.");
                }
                break;
            case "select_two"://ファイル取得
                playerTwoJar = getJarFile();
                if (playerTwoJar != null) {
                    playerRPS2 = loadClass(playerTwoJar);
                    condition2.setText(playerOneJar.getName() + " is set.");
                }
                break;
            case "run_once": //一度実行
                if (playerTwoJar != null && playerOneJar != null)/* executeTask()*/ ;
                break;
            case "run_10000":
                if (playerTwoJar != null && playerOneJar != null)

                    new ExecuteTasks().start();
                break;
        }

    }

    class ExecuteTasks extends Thread {
        @Override
        public void run() {
            int team1VictoryCount = 0;
            int team2VictoryCount = 0;

            for (int i = 0; i < 10000; i++) {
                Pair<RPS, RPS> result1 = playerRPS1.sendRPS();
                Pair<RPS, RPS> result2 = playerRPS2.sendRPS();
                int team1Victory = countVictory(result1, result2);
                int team2Victory = countVictory(result2, result1);
                team1VictoryCount += team1Victory;
                team2VictoryCount += team2Victory;
                final int team1 = team1VictoryCount;
                final int team2 = team2VictoryCount;
                Platform.runLater(() -> condition1.setText("勝利数：" + team1));
                Platform.runLater(() -> condition2.setText("勝利数：" + team2));
                if (i % 10 == 0) {
                    Platform.runLater(() -> player11.setImage(result2Image(result1.getKey())));
                    Platform.runLater(() -> player12.setImage(result2Image(result1.getValue())));
                    Platform.runLater(() -> player21.setImage(result2Image(result2.getKey())));
                    Platform.runLater(() -> player22.setImage(result2Image(result2.getValue())));
                }
//                player11.setImage(result2Image(result1.getKey()));
//                player12.setImage(result2Image(result1.getValue()));
//                player21.setImage(result2Image(result2.getKey()));
//                player22.setImage(result2Image(result2.getValue()));

                playerRPS1.onResult(team1Victory, result1, result2);
                playerRPS2.onResult(team2Victory, result2, result1);
            }
        }

        public Image result2Image(RPS rps) {
            if (rps.getRps() == RPS.ROCK)
                return rock;
            else if (rps.getRps() == RPS.PAPER)
                return paper;
            else
                return scissor;
        }

        private int countVictory(Pair<RPS, RPS> origin, Pair<RPS, RPS> enemy) {
            System.out.println("自:" + origin.getKey().getRpsString() + ":" + origin.getValue().getRpsString() + "敵:" + enemy.getKey().getRpsString() + ":" + enemy.getValue().getRpsString());
            //        System.out.println("x" + (enemy.getKey().getRps() + enemy.getValue().getRps() * 3) + " y" + (origin.getKey().getRps() + origin.getValue().getRps() * 3));
            int[][] vicotryMap =
                    {{0, 2, 0, 2, 2, 0, 0, 0, 0},//縦が自分、横が敵
                            {1, 1, 0, 1, 1, 0, 0, 0, 0},
                            {1, 0, 1, 0, 0, 0, 1, 0, 1},
                            {1, 1, 0, 1, 1, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 2, 0, 2, 2},
                            {0, 0, 0, 0, 1, 1, 0, 1, 1},
                            {1, 0, 1, 0, 0, 0, 1, 0, 1},
                            {0, 0, 0, 0, 1, 1, 0, 1, 1},
                            {2, 0, 2, 0, 0, 0, 2, 0, 0}};
            return vicotryMap[origin.getKey().getRps() + origin.getValue().getRps() * 3][enemy.getKey().getRps() + enemy.getValue().getRps() * 3];
        }

    }


    public RPSListener loadClass(File file) {
        try {
            URLClassLoader load =
                    URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
            //クラスをロード
            Class cl = null;
            cl = load.loadClass("rps.Engine");
            return (RPSListener) cl.newInstance();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 勝利判定
     *
     * @param origin 自分
     * @param enemy  敵
     * @return
     */
    private boolean isVictory(RPS origin, RPS enemy) {
        if (origin.getRps() == RPS.PAPER && enemy.getRps() == RPS.ROCK
                || origin.getRps() == RPS.ROCK && enemy.getRps() == RPS.SCISSOR
                || origin.getRps() == RPS.SCISSOR && enemy.getRps() == RPS.PAPER) return true;

        return false;
    }

    public File getJarFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File userDirectory = new File(".");
        fileChooser.setInitialDirectory(userDirectory);

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Jar File", "*.jar"));
        return fileChooser.showOpenDialog(main.getScene().getWindow());
    }
}
