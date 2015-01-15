package com.luxoft.bankapp.service.server;

import com.luxoft.bankapp.exception.AccountNotExistsException;
import com.luxoft.bankapp.exception.ClientNotExistsException;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.service.ClientService;
import com.luxoft.bankapp.service.server.mock.BankClientMock;
import com.luxoft.bankapp.service.server.mock.BankClientMockCallable;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class BankServerThreadedTest {

    private static final String TEST_CLIENT_NAME = "John Smith";
    private static final int WITHDRAWAL_AMOUNT = 20;
    private static final int CLIENTS_NUMBER = 1000;
    private static final int POOL_SIZE = 100;

    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + BankServerThreadedTest.class.getName());
    public static final String SAVING = "saving";

    // test with runnable mock
    @Ignore
    @Test
    public void testBankClientMock() {
        double initialBalance;
        BankServerThreaded server = new BankServerThreaded();
        ClientService clientService = server.getClientService();
        server.run();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        try {
            Client client = clientService.getClient(TEST_CLIENT_NAME);
            initialBalance = clientService.getBalance(clientService.getAccount(client, SAVING));
            BankClientMock bankClientMock = new BankClientMock(client);
            Thread[] threads = new Thread[CLIENTS_NUMBER];
            for (int i = 0; i < CLIENTS_NUMBER; i++) {
                threads[i] = new Thread(bankClientMock);
                threads[i].start();
                System.out.println("new");
            }
            for (Thread thread : threads) {
                thread.join();
            }

            server.shutdown();
            Thread.sleep(1000);

            assertEquals(initialBalance - WITHDRAWAL_AMOUNT * CLIENTS_NUMBER,
                    clientService.getBalance(clientService.getAccount(client, SAVING)), 0f);

        } catch (ClientNotExistsException | AccountNotExistsException | InterruptedException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }


    // test with callable mock
    @Ignore
    @Test
    public void bankClientMockCallableTest() {
        double initialBalance;
        BankServerThreaded server = new BankServerThreaded();
        ClientService clientDAO = server.getClientService();
        server.run();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }

        try {
            Client client = clientDAO.getClient(TEST_CLIENT_NAME);
            initialBalance = clientDAO.getBalance(clientDAO.getAccount(client, SAVING));

            ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
            List<Long> clientServiceTimeList = new ArrayList<>();
            for (int i = 0; i < CLIENTS_NUMBER; i++) {
                Future<Long> future = executor.submit(new BankClientMockCallable(client));
                clientServiceTimeList.add(future.get());
                try {
                    executor.awaitTermination(1000, TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            }
            server.shutdown();

            System.out.println("Average time of client service = "
                    + calcAverage(clientServiceTimeList) + " us");

            assertEquals(initialBalance - WITHDRAWAL_AMOUNT * CLIENTS_NUMBER,
                    clientDAO.getBalance(clientDAO.getAccount(client, SAVING)), 0f);
        } catch (ClientNotExistsException | AccountNotExistsException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public long calcAverage(List<Long> future) {
        Long sum = 0L;
        for (Long times : future) {
            sum += times;
        }
        return sum / future.size() / 1000;
    }

}