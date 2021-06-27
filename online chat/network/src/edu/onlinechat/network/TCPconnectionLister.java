package edu.onlinechat.network;

public interface TCPconnectionLister {
void onConnectionReady(TCPconnection tcPconnection);

    @Override
    TCPconnectionLister(TCPconnection tcPconnection);

    void onReceiveString(TCPconnection tcPconnection, String value );
        void onDisconnect(TCPconnection tcPconnection);
        void onException(TCPconnection tcPconnection, Exception e);
}
