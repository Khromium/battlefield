package kosen.janken;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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

/**
 * @author 松林圭
 * 2017年11月作成
 * TODO:ExecuteTasksが中にあると汚いので暇があったら分離したい
 */
public class Controller {
    @FXML
    private Pane main;
    @FXML
    private Label condition1, condition2, team1name, team2name;
    @FXML
    private ImageView player11, player12, player21, player22;

    @FXML
    private void handleDragOver(DragEvent event) {
        // ドラッグボードを取得
        Dragboard board = event.getDragboard();
        if (board.hasFiles()) {  // ドラッグされているのがファイルなら
            // コピーモードを設定(これでマウスカーソルが矢印に+のやつになる)
            event.acceptTransferModes(TransferMode.COPY);
        }
    }

    /**
     * 選択1の中にD&Dされたときに呼ばれる
     *
     * @param event
     */
    @FXML
    private void handleOneDropped(DragEvent event) {
        handleDropped(1, event);
    }

    /**
     * 選択2の中にD&Dされたときに呼ばれる
     *
     * @param event
     */
    @FXML
    private void handleTwoDropped(DragEvent event) {
        handleDropped(2, event);
    }

    /**
     * D&D関係のハンドラ
     *
     * @param index どちらの選択に落ちてきたか
     * @param event
     */
    private void handleDropped(int index, DragEvent event) {
        // ドラッグボードを取得
        Dragboard board = event.getDragboard();
        //ファイルが一つだけあって、かつディレクトリじゃないときのみ
        if (board.hasFiles() && board.getFiles().size() == 1 && !board.getFiles().get(0).isDirectory()) {
            loadTeamJar(index, board.getFiles().get(0));
            // ドロップ受け入れ
            event.setDropCompleted(true);
        } else {    // ファイル以外なら
            // ドロップ受け入れ拒否
            event.setDropCompleted(false);
        }
    }

    public final Image rock = new Image(getClass().getResourceAsStream("/rock.png"));
    public final Image paper = new Image(getClass().getResourceAsStream("/paper.png"));
    public final Image scissor = new Image(getClass().getResourceAsStream("/scissor.png"));

    private RPSListener playerRPS1, playerRPS2;

    private Stage primaryStage;

    /**
     * 初期化処理
     *
     * @param primaryStage
     */
    public void init(Stage primaryStage) {
        this.primaryStage = primaryStage;
        player11.setImage(rock);
        player12.setImage(rock);
        player21.setImage(rock);
        player22.setImage(rock);
    }

    /**
     * ボタン処理系のメソッド
     *
     * @param event
     */
    public void buttonsHandler(ActionEvent event) {
        File jarFile;
        switch (((Button) event.getSource()).getId()) {
            case "select_one"://ファイル取得
                jarFile = getJarFile();
                if (jarFile != null)
                    loadTeamJar(1, jarFile);
                break;
            case "select_two"://ファイル取得
                jarFile = getJarFile();
                if (jarFile != null)
                    loadTeamJar(2, jarFile);
                break;
            case "run_once": //一度実行
                if (playerRPS2 != null && playerRPS1 != null) new ExecuteTasks(1).start();
                break;
            case "run_10000":
                if (playerRPS2 != null && playerRPS1 != null)
                    new ExecuteTasks(10000).start();
                break;
            case "reset":
                playerRPS1 = null;
                playerRPS2 = null;
                team1name.setText("Team1");
                team2name.setText("Team2");
                player11.setImage(rock);
                player12.setImage(rock);
                player21.setImage(rock);
                player22.setImage(rock);
                condition1.setText("module data cleared");
                condition2.setText("module data cleared");
                break;
        }

    }

    private void loadTeamJar(int index, File jarFile) {
        if (index == 1) {
            playerRPS1 = loadClass(jarFile);
            if (playerRPS1 != null) {
                team1name.setText(playerRPS1.setTeamName() + "チーム");
                condition1.setText(jarFile.getName() + " is set.");
            } else {
                condition1.setText("ERROR!!");
            }
        } else if (index == 2) {
            playerRPS2 = loadClass(jarFile);
            if (playerRPS2 != null) {
                team2name.setText(playerRPS2.setTeamName() + "チーム");
                condition2.setText(jarFile.getName() + " is set.");
            } else {
                condition2.setText("ERROR!!");
            }
        }
    }

    /**
     * クラスファイルのロード
     *
     * @param file
     * @return
     */
    private RPSListener loadClass(File file) {
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
     * Jarファイルpicker
     *
     * @return
     */
    private File getJarFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File userDirectory = new File(".");
        fileChooser.setInitialDirectory(userDirectory);

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Jar File", "*.jar"));
        return fileChooser.showOpenDialog(main.getScene().getWindow());
    }

    /**
     * スレッドをメインと分けて応答がなくなるのを防ぐ
     */
    class ExecuteTasks extends Thread {
        int count = 0;

        /**
         * constructor
         *
         * @param count 何回実行するか
         */
        public ExecuteTasks(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            int team1VictoryCount = 0;
            int team2VictoryCount = 0;

            for (int i = 0; i < count; i++) {
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
                if (i % 20 == 0) {//全てを表示するととても重いので間引く
                    Platform.runLater(() -> player11.setImage(result2Image(result1.getKey())));
                    Platform.runLater(() -> player12.setImage(result2Image(result1.getValue())));
                    Platform.runLater(() -> player21.setImage(result2Image(result2.getKey())));
                    Platform.runLater(() -> player22.setImage(result2Image(result2.getValue())));
                }

                playerRPS1.onResult(team1Victory, result1, result2);
                playerRPS2.onResult(team2Victory, result2, result1);
            }
        }

        /**
         * 結果を画像に変換
         *
         * @param rps
         * @return
         */
        public Image result2Image(RPS rps) {
            if (rps.getRps() == RPS.ROCK)
                return rock;
            else if (rps.getRps() == RPS.PAPER)
                return paper;
            else
                return scissor;
        }

        /**
         * 勝利数のカウント。高速化のために配列にしている
         *
         * @param origin 自チームの出した手
         * @param enemy  敵㌠の出した手
         * @return
         */
        private int countVictory(Pair<RPS, RPS> origin, Pair<RPS, RPS> enemy) {
            System.out.println("自:" + origin.getKey().getRpsString() + ":" + origin.getValue().getRpsString() + "敵:" + enemy.getKey().getRpsString() + ":" + enemy.getValue().getRpsString());
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
}
