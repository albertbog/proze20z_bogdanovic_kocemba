package com;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Klasa odpowiedzialna za zapis danych dotyczących numeru portu oraz nazwy hosta. Dane te są udostępniane użytkownikowi w celu umożliwienia połączenia z serwerem.
 */
public class ConfigSaver {
    /** Scieżka dostępu do pliku, gdzie będą zapisywane dane dotyczące numeru portu i nazwy hosta*/
    static String Data_path="DaneWyjscioweSerwera.txt";
    /**
     * Konstruktor, wywołujący metodą odpowiedzialną za zapis numeru portu i nazwy hosta
     * @param port numer portu do zapisu
     * @param localHost nazwa hosta do zapsiu
     */
    public ConfigSaver(int port, InetAddress localHost)
    {
        save_data_server(port, localHost);
    }

    /**
     * Metoda odpowiedzialna za zapis numeru portu i nazwy hosta do odpowiedniego pliku txt
     * @param port numer portu do zapisu
     * @param localHost nazwa hosta do zapisu
     */
    public void save_data_server(int port, InetAddress localHost)
    {
        try {
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(Data_path));
            bufferedWriter.write(String.valueOf(port));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(localHost));
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
