package com.luxoft.bankapp.model;

import com.luxoft.bankapp.service.ClientRegistrationListener;

import java.text.SimpleDateFormat;
import java.util.*;

public class Bank implements Report {
    private static final String NAME = "name";
    private final Set<Client> clientsSet = new TreeSet<>();
    @NoDB
    private final List<ClientRegistrationListener> listeners = new ArrayList<>();
    @NoDB
    private final Map<String, Client> clients = new TreeMap<>();
    @NoDB
    private int id;
    private String name;


    /**
     * Constructs a new bank with the specified name
     *
     * @param name the bank's name
     * @throws java.lang.NullPointerException if the specified name is null
     */
    public Bank(String name) {
        this.name = name;

        class EmailNotificationListener implements ClientRegistrationListener {
            @Override
            public void onClientAdded(Client c) {
                System.out.println("Notification email for client " + c.getName() + " to be sent.");
            }
        }

        class PrintClientListener implements ClientRegistrationListener {
            @Override
            public void onClientAdded(Client c) {
                c.printReport();
            }
        }

        class DebugListener implements ClientRegistrationListener {
            @Override
            public void onClientAdded(Client c) {
                System.out.println("Name: " + c.getName() + ", current time: "
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".\n");
            }
        }

        class AddClientToClientsSetListener implements ClientRegistrationListener {

            @Override
            public void onClientAdded(Client c) {
                clientsSet.add(c);
            }
        }

        class AddClientToClientsMapListener implements ClientRegistrationListener {
            @Override
            public void onClientAdded(Client c) {
                clients.put(c.getName(), c);
            }
        }

        addClientRegistrationListener(new EmailNotificationListener());
        addClientRegistrationListener(new PrintClientListener());
        addClientRegistrationListener(new DebugListener());
        addClientRegistrationListener(new AddClientToClientsSetListener());
        addClientRegistrationListener(new AddClientToClientsMapListener());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Client> getClients() {
        return clients;
    }

    public void parseFeed(Map<String, String> feed) {
        String clientName = feed.get(NAME);
        Client client = clients.get(clientName);
        if (client == null) {
            client = new Client(clientName);
            clients.put(clientName, client);
        }
        client.parseFeed(feed);
    }

    public void addClient(Client client) {
        for (ClientRegistrationListener listener : listeners) {
            listener.onClientAdded(client);
        }
    }

    public void removeClient(Client client) {
        clientsSet.remove(client);
        clients.remove(client.getName());
    }

    private void addClientRegistrationListener(ClientRegistrationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void printReport() {
        System.out.println("Bank name: " + name + "\n\nList of clients: \n");
        for (Client client : clients.values()) {
            client.printReport();
            System.out.println();
        }
    }
}