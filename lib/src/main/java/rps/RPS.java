package rps;

public class RPS {
    private int rps;
    public static int ROCK = 0;
    public static int PAPER = 1;
    public static int SCISSOR = 2;

    public RPS setRps(int rps) {
        if (rps < 0 || rps > 2) throw new IllegalStateException("値が不正です");
        this.rps = rps;
        return this;
    }

    public int getRps() {
        return rps;
    }
}
