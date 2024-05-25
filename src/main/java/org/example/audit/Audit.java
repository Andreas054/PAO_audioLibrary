package org.example.audit;

public class Audit {
    private int id;
    private int userId;
    private String command;
    private boolean rulatOk;

    public Audit(int userId, String command, boolean rulatOk) {
        this.userId = userId;
        this.command = command;
        this.rulatOk = rulatOk;
    }

    public Audit(int id, int userId, String command, boolean rulatOk) {
        this.id = id;
        this.userId = userId;
        this.command = command;
        this.rulatOk = rulatOk;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getCommand() {
        return command;
    }

    public boolean isRulatOk() {
        return rulatOk;
    }
}
