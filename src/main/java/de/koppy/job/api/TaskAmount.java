package de.koppy.job.api;

import java.util.ArrayList;
import java.util.List;

public class TaskAmount {

    private List<Integer> taskammounts = new ArrayList<Integer>();

    public TaskAmount() { }

    /*
     * return error when index out of bound.
     * make sure to register enough taskamounts beforehand
     */
    public int getTaskAmount(int index) {
        return this.taskammounts.get(index);
    }

    public void setTaskAmount(int index, int value) {
        this.taskammounts.set(index, value);
    }

    public void addTaskAmount(int index, int amount) {
        int bal = getTaskAmount(index);
        bal = bal + amount;
        setTaskAmount(index, bal);
    }

    public void removeTaskAmount(int index, int amount) {
        int bal = getTaskAmount(index);
        bal = bal - amount;
        setTaskAmount(index, bal);
    }

    public void registerTaskAmount(int amount) {
        taskammounts.clear();
        for(int i=0; i<amount; i++) {
            taskammounts.add(0);
        }
    }

}