package com.luxoft.bankapp.command;

import com.luxoft.bankapp.service.BankCommander;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.joda.time.format.DateTimeFormat.mediumDateTime;

public class ExitCommand implements Command {

    private static final Logger CLIENTS_LOGGER = Logger.getLogger("LogClients." + ExitCommand.class.getName());

    @Override
    public void execute() {
        logDisconnectionTime();
        System.exit(0);
    }

    private void logDisconnectionTime() {
        DateTimeFormatter dateTimeFormatter = mediumDateTime();
        PeriodFormatter periodFormatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(" day", " days")
                .appendSeparator(" ")
                .appendHours()
                .appendSeparator(":")
                .appendMinutes().minimumPrintedDigits(2)
                .appendSeparator(":")
                .appendSeconds().minimumPrintedDigits(2)
                .toFormatter();
        DateTime disconnectionTime = new DateTime();
        Period period = new Period(BankCommander.connectionTime, disconnectionTime);
        CLIENTS_LOGGER.log(Level.INFO, "User disconnected from the Bank Commander" +
                "\nDisconnection time: " + dateTimeFormatter.print(disconnectionTime)
                + "\nConnection duration: " + periodFormatter.print(period));
    }

    @Override
    public void printCommandInfo() {
        System.out.print("Exit");
    }
}