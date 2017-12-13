package rps;

import javafx.util.Pair;

import java.util.Random;

public class Engine implements RPSListener {
    public static int count = 0;

    /**
     * 結果の受信
     *
     * @param i    勝利数
     * @param rps  自分チームの出したもの
     * @param rps1 相手チームの出したもの
     */
    @Override
    public void onResult(int i, Pair<RPS, RPS> rps, Pair<RPS, RPS> rps1) {
        count++;
        System.out.println("通算" + count + "回目" + "勝利数:" + i + "  相手の手：" + rps1.getKey().getRpsString() + ":" + rps1.getValue().getRpsString());
    }


    /**
     * グーチョキパーの送信
     *
     * @return 自チームの出しているもののペア
     */
    @Override
    public Pair<RPS, RPS> sendRPS() {
        Pair<RPS, RPS> pair = new Pair<>(new RPS().setRps(new Random().nextInt(3)), new RPS().setRps(new Random().nextInt(3)));
        return pair;
    }

    @Override
    public String setTeamName() {
        String name = "名無し"+new Random(1000).toString();
        return name;
    }
}
