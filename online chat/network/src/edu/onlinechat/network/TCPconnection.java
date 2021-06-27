package edu.onlinechat.network;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPconnection{
    private final Socket socket;
    private final Thread rxThread;
    private final TCPconnectionLister eventListner;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPconnection(TCPconnectionLister eventListner, String ipAddr, int port) throws IOException{
            this (eventListner, new Socket (ipAddr,port));
        }

    public TCPconnection (TCPconnectionLister eventListner, Socket socket) throws IOException {
        this.eventListner = eventListner;
            this.socket = socket;
            socket.getInputStream();
             in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Charset.forName("UTF-8")));
            rxThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        eventListner.onConnectionReady(TCPconnection.this);
                        while (!rxThread.isInterrupted()){
                            String msg = in.readLine();
                            eventListner.onReceiveString(TCPconnection.this,msg);

                        }
                    String msg = in.readLine();
                } catch (IOException e) {
                        eventListner.onException(TCPconnection.this, e);
                    } finally {
                    eventListner.onDisconnect(TCPconnection.this);
                        }
                    }
            });
            rxThread.start();

    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();
        } catch (IOException e) {
            eventListner.onException(TCPconnection.this, e);
            disconnect();
        }
    }
    public synchronized void disconnect(){
        rxThread.interrupt();
        try{
            socket.close();
        }catch (IOException e) {
            eventListner.onException(TCPconnection.this, e);
        }
    }

        @Override
        public String toString() {
            return "TCPconnection: " + socket.getInetAddress()+": " + socket.getPort();
        }
}




