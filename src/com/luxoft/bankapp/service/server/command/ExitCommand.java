package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;

import java.io.IOException;

public class ExitCommand implements Command {

    private final BankServerInfo bankServerInfo;

    public ExitCommand(BankServerInfo bankServerInfo) {
        this.bankServerInfo = bankServerInfo;
    }

    @Override
    public void execute() throws ClassNotFoundException, IOException {
        ServerThread.sendObject(bankServerInfo.getOut(),
                "Thank you for use our services.\nPress enter to close connection.");
        bankServerInfo.setLogin(false);
    }

    @Override
    public String getCommandInfo() {
        return "Cancel";
    }
}