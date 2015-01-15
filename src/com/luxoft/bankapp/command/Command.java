package com.luxoft.bankapp.command;

public interface Command {
    void execute();

    void printCommandInfo();
}