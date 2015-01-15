package com.luxoft.bankapp.command;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ExitCommandTest {

    private static ExitCommand sut;

    @BeforeClass
    public static void createAddAccountCommand() {
        sut = new ExitCommand();
    }

    // test printCommandInfo()
    @Test
    public void testPrintCommandInfo() {
        final String EXPECTED_STRING = "Exit";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        sut.printCommandInfo();
        final String printCommandInfoOutput = byteArrayOutputStream.toString();

        assertEquals("printCommandInfo() method does not produce the expected output",
                EXPECTED_STRING, printCommandInfoOutput);
    }
}