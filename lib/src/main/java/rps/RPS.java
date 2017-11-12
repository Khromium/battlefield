package rps;

public class RPS {
    private int rps;
    public static int ROCK = 0;
    public static int PAPER = 2;
    public static int SCISSOR = 1;

    public RPS setRps(int rps) {
        if (rps < 0 || rps > 2) throw new IllegalStateException("値が不正です");
        this.rps = rps;
        return this;
    }

    public int getRps() {
        return rps;
    }

    public String getRpsString(){
        if (rps == RPS.ROCK) return "グー";
        if (rps == RPS.SCISSOR) return "チョキ";
        if (rps == RPS.PAPER) return "パー";
        return null;
    }
}
