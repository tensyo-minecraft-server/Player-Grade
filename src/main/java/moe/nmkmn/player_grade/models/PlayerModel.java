package moe.nmkmn.player_grade.models;

public class PlayerModel {
    private String UUID;
    private int votesCast;
    private double balance;
    private long playTime;
    private long blockDestruction;

    public PlayerModel(String UUID, int votesCast, long playTime, double balance, long blockDestruction) {
        this.UUID = UUID;
        this.votesCast = votesCast;
        this.playTime = playTime;
        this.balance = balance;
        this.blockDestruction = blockDestruction;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public int getVotesCast() {
        return votesCast;
    }

    public void setVotesCast(int votesCast) {
        this.votesCast = votesCast;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getBlockDestruction() {
        return blockDestruction;
    }

    public void setBlockDestruction(long blockDestruction) {
        this.blockDestruction = blockDestruction;
    }
}
