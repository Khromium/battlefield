package rps;

import javafx.util.Pair;


public interface RPSListener {
    /**
     * 結果を返すためのコールバック
     *
     * @param victory 自分のチームの勝利数
     * @param own     自分出したもの
     * @param enemy   敵チームの出したもの
     */
    void onResult(int victory, Pair<RPS, RPS> own, Pair<RPS, RPS> enemy);

    /**
     * RPSの送信
     *
     * @return
     */
    Pair<RPS, RPS> sendRPS();

    String setTeamName();
}
