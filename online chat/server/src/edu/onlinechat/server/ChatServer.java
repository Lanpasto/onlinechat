package edu.onlinechat.server;

import edu.onlinechat.network.TCPconnection;
import edu.onlinechat.network.TCPconnectionLister;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPconnectionLister {

    public static void main (String[] args){
        new ChatServer();
    }

    private final ArrayList<TCPconnection> connections = new ArrayList<>();

    private ChatServer (){
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(8189)){
            while (true) {
                try {
                      new TCPconnection(this,serverSocket.accept() );
                }catch (IOException e){
                    System.out.println("TCPconnection exception:" + e);
                }
            }
        }   catch (IOException e) {
            throw  new RuntimeException(e);
        }
    }


    @Override
    public synchronized onConnectionReady(TCPconnection tcPconnection) {
        connections.add(tcPconnection);
        sendToAllConnection("Client connection: " + tcPconnection);
    }

    @Override
    public synchronized void onReceiveString(TCPconnection tcPconnection, String value) {
        sendToAllConnection(value);
    }

    @Override
    public synchronized void onDisconnect(TCPconnection tcPconnection) {
            connections.remove(tcPconnection);
        sendToAllConnection("Client disconnection: " + tcPconnection);
    }

    @Override
    public synchronized void onException(TCPconnection tcPconnection, Exception e) {
        System.out.println("TCPconnection exception: " + e );
    }
    private  void sendToAllConnection(String Value){
            System.out.println(value);
            final int cnt = connections.size()
            for (int i = 0; i <cnt ;i++) connections.get(i).sendString(value);

    }
}
