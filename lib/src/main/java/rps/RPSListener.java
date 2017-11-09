package rps;

public interface RPSListener {
    /**
     * 結果を返すためのコールバック
     * @param victory 自分のチームの勝利数
     * @param enemy1 敵チームの出したもの1
     * @param enemy2 敵チームの出したもの2
     */
    void onResult(int victory, RPS enemy1, RPS enemy2);

    /**
     * RPSの送信
     * @return
     */
    RPS sendRPS();
}
