package com.sokoban;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa odpowiedzialna za czytanie z plikow konfiguracyjnych w celu ustalenia wstępnej wielkości okna. Występuje jako Singleton.
 */
public class AppSettingsMainMenu {
    /** Scieżka dostępu do pliku config_main_menu.txt */
    static final String config_main_menu="src/com/sokoban/config/config_main_menu.txt";
    /** Okresla startowa szerokosc okna */
    static Integer xSize;
    /** Okresla startowa wysokosc okna */
    static Integer ySize;

    /** Singleton instacja klasy AppSettingsMainMenu*/
    private static AppSettingsMainMenu single_instance = null;

    private AppSettingsMainMenu(){ load();}

    /**
     * Zwraca instancję statyczną klasy AppSettings
     * @return zwraca instancję statyczną
     */
    public static AppSettingsMainMenu getInstance()
    {
        if (single_instance == null)
            single_instance = new AppSettingsMainMenu();

        return single_instance;
    }

    /**
     * Funkcja odpowiadająca za wywołanie funkcji wczytującej dane początkowe dotyczące rozmiaru okien
     */
    public void load(){
        load_config();
    }

    /**
     * Wczytuje z udostępnionych plików konfiguracyjnych atrybuty okna gry. Wywołuje metodę zapisującą dane z plików konfiguracyjnych do odpowiednich zmiennych
     */
    private void load_config(){
        List<String> list = new ArrayList<String>();
        try{
            BufferedReader bufferedReader=new BufferedReader(new FileReader(config_main_menu));
            String line=bufferedReader.readLine();
            do {
                list.add(line);
                line= bufferedReader.readLine();
            }while(line!=null);
            save_config(list);
            bufferedReader.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Zapisuje otrzymane atrybuty do odpowiednich zmiennych. Modyfikuje zmienne w celu możliwości personalizacji gry.
     * @param list lista Stringów, które zostaną zapisane do odpowiednich zmiennych
     */
    private void save_config(List<String> list){
        xSize=Integer.parseInt(list.get(0));
        ySize=Integer.parseInt(list.get(1));
    }

    /**
     * Funkcja zwracająca szerokość ekranu aplikacji
     * @return zwraca szerokość ekranu aplikacji
     */
    public Integer ScreenWidth(){return xSize;}
    /**
     * Funkcja zwracająca wysokość ekranu aplikacji
     * @return zwraca wysokość ekranu aplikacji
     */
    public Integer ScreenHeight(){return ySize;}
}
