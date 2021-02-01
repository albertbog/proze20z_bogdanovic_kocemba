package com;

import java.io.*;
import java.net.*;

/**
 * Klasa odpowiedzialna za nawiązywanie start serwera oraz nawiązywanie połączenia z użytkownikiem. Obsługuje zadania od klienta. W momencie otrzymania polecenia tworzy nowy wątek.
 */
public class Server {
    /**
     * Zmienna przechowująca numer portu
     */
    private final int port;

    /**
     * Konstruktor, wywołujący metodą odpowiedzialną za zapis numeru portu do zmiennej port
     */
    public Server()
    {
        ConfigLoader.load_port();
        port=ConfigLoader.port;
    }

    /**
     * Serwer oczekuje na zgloszenia od klientow. W momencie gdy pojawi sie klient nastepuje utworzenie nowego watku
     * w ktorym klient jest obslugiwany a serwer dalej oczekuje na kolejne klienty
     */
    public void runServer()
    {
        try {

            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting for connection!");
            while(true)
            {
                Socket socket=serverSocket.accept();
                new Thread(new MyThread(socket)).start();
            }
        }
        catch (IOException exception) {
            System.out.println("Sorry! We could not connect with server!");
        }
    }

    /**
     * Metoda zwracająca numer portu.
     * @return Metoda zwraca numer portu.
     */
    public int return_port()
    {
        return port;
    }
}
