package com;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class main {
    public static void main(String[] args)
        {
            Server ser= new Server();
            try {
                new ConfigSaver(ser.return_port(), InetAddress.getLocalHost());
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            ser.runServer();
        }
}
