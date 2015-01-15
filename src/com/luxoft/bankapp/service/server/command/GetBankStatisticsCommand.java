package com.luxoft.bankapp.service.server.command;

import com.luxoft.bankapp.model.info.BankInfo;
import com.luxoft.bankapp.model.info.BankInfoCalc;
import com.luxoft.bankapp.service.server.BankServerInfo;
import com.luxoft.bankapp.service.server.ServerThread;

import java.io.IOException;

public class GetBankStatisticsCommand implements Command {

    private final BankServerInfo bankServerInfo;

    public GetBankStatisticsCommand(BankServerInfo bankServerInfo) {
        this.bankServerInfo = bankServerInfo;
    }

    @Override
    public void execute() throws ClassNotFoundException, IOException {
        BankInfo bankInfo = new BankInfo();
        bankInfo.setName(BankInfoCalc.getName(bankServerInfo.getCurrentBank()));
        bankInfo.setNumberOfClients(BankInfoCalc.calcNumberOfClients(bankServerInfo
                .getCurrentBank()));
        bankInfo.setNumberOfAccounts(BankInfoCalc.calcNumberOfAccounts(bankServerInfo
                .getCurrentBank()));
        bankInfo.setClientsSet(BankInfoCalc.getClientsSet(bankServerInfo.getCurrentBank()));
        bankInfo.setDepositSum(BankInfoCalc.calcDepositSum(bankServerInfo.getCurrentBank()));
        bankInfo.setCreditSum(BankInfoCalc.calcCreditSum(bankServerInfo.getCurrentBank()));
        ServerThread.sendObject(bankServerInfo.getOut(), bankInfo);
        ServerThread
                .sendObject(bankServerInfo.getOut(),
                        "Bank statistics was successfully sent.\nDo you want to perform other operations? " +
                                "(Yes/yes/No/no)");
        new ContinueCommand(bankServerInfo).execute();
    }

    @Override
    public String getCommandInfo() {
        return "Get the bank statistics";
    }
}