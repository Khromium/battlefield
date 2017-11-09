package rps;

import javafx.util.Pair;
import rps.RPS;
import rps.RPSListener;

import java.util.Random;

public class Engine implements RPSListener {
    /**
     * 結果の受信
     * @param i 勝利数
     * @param rps 自分チームの出したもの
     * @param rps1 相手チームの出したもの
     */
    @Override
    public void onResult(int i, Pair<RPS, RPS> rps, Pair<RPS, RPS> rps1) {

        System.out.println("勝利数:" + i + "  相手の手：" + rps1.getKey().getRpsString() + ":" + rps1.getValue().getRpsString());
    }


    /**
     * グーチョキパーの送信
     * @return 自チームの出しているもののペア
     */
    @Override
    public Pair<RPS, RPS> sendRPS() {
        Pair<RPS, RPS> pair = new Pair<>(new RPS().setRps(new Random().nextInt(3)), new RPS().setRps(new Random().nextInt(3)));
        return pair;
    }
}
