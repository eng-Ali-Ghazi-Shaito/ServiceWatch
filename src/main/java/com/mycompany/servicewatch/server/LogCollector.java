package com.mycompany.servicewatch.server;

import com.mycompany.servicewatch.protocol.ProtocolJson;
import com.mycompany.servicewatch.protocol.ProtocolMessage;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.net.ServerSocket;
import com.google.gson.JsonSyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.mycompany.servicewatch.model.MoniterApplication;
import com.mycompany.servicewatch.protocol.MessageType;
import com.mycompany.servicewatch.repository.ApplicationRepository;

import java.sql.SQLException;


public class LogCollector {
    private final ApplicationRepository applicationRepository = new ApplicationRepository();
    
    
public void listen(int port, int readTimeoutMs) throws IOException {
    //create a pool where 2 jobs(connections) can run at the same time
    ExecutorService workers = Executors.newFixedThreadPool(2);

    try (ServerSocket serverSocket = new ServerSocket(port)) {
        System.out.println("ServiceWatch is listening on port " + port);
        //loop so it rexecute the accept() 
        while (true) {
            //wait for someone to connect and return the socket of this conenction
            Socket client = serverSocket.accept();
            //if we do the handleclient inside this method and it took 3 seconds so it can listen for other application since it need 3 seconds to rereach the accept
            workers.execute(() -> handleClient(client, readTimeoutMs));
        }
    } finally {
        workers.shutdown();
    }
}

    
//job to handle one connection 
private void handleClient(Socket client, int readTimeoutMs) {
    //try closes the connection when the try block ends
    try (Socket connection = client) {
         //is the stock or the sales didnt send message for a readTimeoutMs time wait it readTimeoutMs
        connection.setSoTimeout(readTimeoutMs);
        ProtocolMessage message = readMessage(connection);

        if (message.getType() != MessageType.LOG) {
            System.err.println("Expected a LOG message");
            return;
        }

        String serviceId = message.getServiceId();
        if (serviceId == null || serviceId.trim().isEmpty()) {
            System.err.println("The log is missing its serviceId");
            return;
        }

        MoniterApplication application = applicationRepository.findByServiceId(serviceId);
        if (application == null) {
            System.err.println("Unknown serviceId: " + serviceId);
            return;
        }

        System.out.println("Received a log from " + serviceId
                + " with database ID " + application.getId());

    } catch (SQLException e) {
        System.err.println("Could not look up the application: " + e.getMessage());
    } catch (IOException | JsonSyntaxException e) {
        System.err.println("Could not read the message: " + e.getMessage());
    }
}

    private ProtocolMessage readMessage(Socket socket) throws IOException {
        //take the incoming bytes message and convert to readable character
        InputStreamReader textReader = new InputStreamReader(
                socket.getInputStream(),
                StandardCharsets.UTF_8
        );
       //gives us the readline method so we can read a whole line
        BufferedReader reader = new BufferedReader(textReader);
        String line = reader.readLine();

        if (line == null) {
            throw new EOFException("The client disconnected before sending a message");
        }

        return ProtocolJson.fromJson(line);
        //ServiceWatch still needs to save the log in MySQL and send ACK back to Sales through that same socket. The code that handles the whole connection will close it after the reply.
    }
}