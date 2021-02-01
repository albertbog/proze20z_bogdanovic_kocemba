package com.sokoban;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Klasa odpowiedzialna za połączenie z serwerem. Wysyła do serwera odpowiednie komunikaty oraz odbiera odpowiedzi generowane przez serwer.
 *
 */
public class Connection {
    /** Przechowuje numer portu serwera */
    private static int port;
    /** Przechowuje nazwę hostu serwera */
    private static String host;
    /** Przechowuje informację o sockecie */
    private static Socket serverSocket;
    /** Scieżka dostępu do pliku przechowującego dane dotyczące portu i hosta serwera */
    static final String config_server_path = "src/com/sokoban/config/DaneWyjscioweSerwera.txt";

    /**
     * Odczytuje z plików konfiguracyjnych numer portu i nazwę hosta serwera
     */
    public void load_data() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(config_server_path));
            port = Integer.parseInt(bufferedReader.readLine());
            host = ((bufferedReader.readLine()).split("/")[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda odpowiedzialna za nawiązanie połączenia pomiędzy klientem a serwerem na podstawie otrzymanego numeru portu i nazwy hosta
     * @return Zwraca informację czy udało połączyć się z serwerem
     */
    public boolean try_to_connect() {
        try {
            serverSocket = new Socket(host, port);
            return true;
        } catch (UnknownHostException e) {
            System.out.println("Sorry! We could not connect!");
            return false;
        } catch (IOException exception) {
            return false;
        }
    }

    /**
     * Odpowiada za komunikację na linii serwer-klient. Za jej pomocą są przesyłane informacje dotyczące wszystkich plików konfiguracyjnych, map, listy najlepszych wyników. Zwraca pojedynczą linię tekstu typu String
     * @param command komenda, która określa prośbę wystosowaną do serwera
     * @return zwraca linię tekstu, która jest odpowiedzią serwera na wysłaną komendę
     */
    public String exchange_messages(String command) {
        String line = null;
        DataOutputStream dataOutputStream= null;
        try {
            serverSocket = new Socket(host,port);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            dataOutputStream = new DataOutputStream(serverSocket.getOutputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            dataOutputStream.writeUTF(command);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            dataOutputStream.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        InputStream is = null;
        try {
            is = serverSocket.getInputStream();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            line = br.readLine();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        try {
            serverSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return line;
    }

    /**
     * Odpowiada za komunikację na linii serwer-klient. Za jej pomocą są przesyłane informacje dotyczące wszystkich elementów graficznych gry. Zwraca tablicę typu byte
     * @param  command komenda, która określa prośbę wystosowaną do serwera
     * @return zwraca tablicę typu byte, która jest odpowiedzią serwera na wysłaną komendę
     */
    public byte[] exchange_messages_avatars(String command) {
        byte[] buffer = new byte[0];
        try {
            serverSocket = new Socket(host,port);
            DataOutputStream dataOutputStream=new DataOutputStream(serverSocket.getOutputStream());
            dataOutputStream.writeUTF(command);
            dataOutputStream.flush();

            ObjectInputStream objectInputStream= new ObjectInputStream(serverSocket.getInputStream());
            try {
                buffer= (byte[]) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            serverSocket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return buffer;
    }
}
