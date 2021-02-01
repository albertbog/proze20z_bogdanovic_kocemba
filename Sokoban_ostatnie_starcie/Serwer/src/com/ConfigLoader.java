package com;

import java.io.*;

/**
 * Klasa odpowiedzialna za odczyt danych znajdujących się w plikach konfiguracyjnych serwera.
 */
public class ConfigLoader {
    /** Zmienna przechowująca numer portu*/
    static int port;
    /** Linia tekstu, która zostanie wysłana do klienta. Zawiera dane konfiguracyjne z serwera*/
    static String list;
    /** Scieżka dostępu do pliku txt zawierającego numer portu*/
    static final String Port_path="Pliki\\Port.txt";
    /** Scieżka dostępu do pliku txt zawierającego dane konfiguracyjne*/
    static final String Config_path="Pliki\\config.txt";

    /**
     * Metoda odpowiedzialna za wczytanie numeru portu do serwera
     */
    public static void load_port() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Port_path));
            port = Integer.parseInt(bufferedReader.readLine());
            bufferedReader.close();
        }
        catch (IOException exception)
        {
            System.out.println("Something went wrong!");
            System.err.println(exception);
        }
    }

    /**
     * Metoda odpowiedzialna za wczytanie danych konfiguracyjnych z serwera
     * @return funkcja zwraca dane konfiguracyjne przekazywane do klienta
     */
    public static String load_config()
    {
        try {
            list="";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Config_path));
            String line=bufferedReader.readLine();
            do {
                list=list+line+"/";
                line=bufferedReader.readLine();
            }while(line!=null);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
