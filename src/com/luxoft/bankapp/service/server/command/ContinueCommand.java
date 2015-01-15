package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;
import com.luxoft.bankapp.validator.Validator;

import java.io.IOException;

public class ContinueCommand implements Command {

    private final BankServerInfo bankServerInfo;

    public ContinueCommand(BankServerInfo bankServerInfo) {
        this.bankServerInfo = bankServerInfo;
    }

    @Override
    public void execute() throws ClassNotFoundException, IOException {
        String message = (String) bankServerInfo.getIn().readObject();
        while (Validator.yesNoValidator(message)) {
            System.out.println(message);
            ServerThread.sendObject(bankServerInfo.getOut(),
                    "You entered wrong value. Please try again.");
            message = (String) bankServerInfo.getIn().readObject();
        }
        if ("No".equalsIgnoreCase(message)) {
            bankServerInfo.getRemoteOfficeCommands().get("5").execute();
        }
    }

    @Override
    public String getCommandInfo() {
        return null;
    }
}