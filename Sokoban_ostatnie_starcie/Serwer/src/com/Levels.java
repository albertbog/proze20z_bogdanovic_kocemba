package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Klasa odpowiedzialna za przetworzenie odpowiedniego poziomu dla klienta
 */
public class Levels {
    /** Scieżka dostępu do folderu ze wszystkimi poziomami*/
    static final String Levels_path = "Pliki\\levels";

    /**
     * Metoda odpowiedzialna za wczytanie odpowiedniego poziomu do gry
     * @param numberOfLevel numer wczytywanego obecnie poziomu
     * @return metoda zwraca przetworzoną linię tekstu (String) z zapisaną mapą
     */
    public static String load_levels(Integer numberOfLevel) {
        String data = "";
        try {
            File txtrs = new File(Levels_path);
            File[] files = txtrs.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (i == numberOfLevel) {
                    Scanner myReader = null;
                    try {
                        myReader = new Scanner(files[i]);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    while (myReader.hasNextLine()) {
                        data=data+"/"+myReader.nextLine();
                    }
                    myReader.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
