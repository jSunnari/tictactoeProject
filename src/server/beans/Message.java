package server.beans;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 2016-05-24.
 */

public class Message<T> {
    private String command;
    private List<String> commandData;

    public Message(String command){
        this.command = command;
        commandData = new ArrayList<>();
    }

    public Message(String command, T cmdData){
        this.command = command;

        Gson gson = new Gson();
        commandData = new ArrayList<>();
        String outputData = gson.toJson(cmdData);
        commandData.add(outputData);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<String> getCommandData() {
        return commandData;
    }

    public void setCommandData(List<String> commandData) {
        this.commandData = commandData;
    }

}
