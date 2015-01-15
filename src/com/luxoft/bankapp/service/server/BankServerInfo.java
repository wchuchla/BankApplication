package com.luxoft.bankapp.service.server;

import com.luxoft.bankapp.model.Account;
import com.luxoft.bankapp.model.Bank;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.ClientService;
import com.luxoft.bankapp.service.server.command.Command;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class BankServerInfo {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Bank currentBank;
    private Client currentClient;
    private Account currentAccount;
    private ClientService clientService;
    private boolean login;
    private Map<String, Command> atmCommands;
    private Map<String, Command> remoteOfficeCommands;

    public BankServerInfo(ObjectOutputStream out, ObjectInputStream in, Bank currentBank,
                          ClientService clientService, Map<String, Command> atmCommandsMap,
                          Map<String, Command> remoteOfficeCommandsMap) {
        this.out = out;
        this.in = in;
        this.currentBank = currentBank;
        this.clientService = clientService;
        this.login = false;
        this.atmCommands = atmCommandsMap;
        this.remoteOfficeCommands = remoteOfficeCommandsMap;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public Bank getCurrentBank() {
        return currentBank;
    }

    public void setCurrentBank(Bank currentBank) {
        this.currentBank = currentBank;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public Map<String, Command> getAtmCommands() {
        return atmCommands;
    }

    public void setAtmCommands(Map<String, Command> atmCommands) {
        this.atmCommands = atmCommands;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public Map<String, Command> getRemoteOfficeCommands() {
        return remoteOfficeCommands;
    }

    public void setRemoteOfficeCommands(Map<String, Command> remoteOfficeCommands) {
        this.remoteOfficeCommands = remoteOfficeCommands;
    }
}