package com.sokoban;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * Klasa odpowiedzialna za czytanie z plikow konfiguracyjnych. Występuje jako Singleton.
 *
 */
public class AppSettings {

    /** Określa początkową ilość kroków*/
    static int numberOfSteps;
    /** Określa ilość wczytywanych poziomów */
    static int numberOfLevels;
    /** Okresla startowa szerokosc okna */
    static int xSize;
    /** Okresla startowa wysokosc okna */
    static int ySize;
    /** Określa poziom, od którego będzie rozpoczęta rozgrywka */
    static int firstLevel;
    /** Określa poziom, którym kończy się rozgrywka */
    static int lastLevel;
    /** Okresla ilosc żyć bohatera */
    static int numberOfLives;
    /** licznik kroków */
    static int stepsCounter;

    static int increaseVelocity;
    /** Scieżka dostępu do pliku config.txt */
    static final String config_path="src/com/sokoban/config/config.txt";
    /** Scieżka dostępu do folderu przechowującego poziomy */
    static final String levels_path="src/com/sokoban/levels/";
    /** Scieżka dostępu do folderu przechowującego tekstury */
    static final String textures_path="src/com/sokoban/textures/";
    /** Scieżka dostępu do folderu przechowującego awatary */
    static final String avatars_path="src/com/sokoban/avatars/";

    /** Przypisanie nazw do wartości wczytywanych tekstur */
    enum Textures {
        Magnet(0),
        Box1(1),
        Box2(2),
        Target(3),
        Evil(4),
        Wall(5),
        End(6);

        private final int index;

        Textures(int index) {
            this.index=index;
        }
    }

    /** Przypisanie nazw do wartości wczytywanych awatarów */
    enum Avatars{
        Boy(0),
        Girl(1),
        Santa(2),
        End(3);

        private final int index;
        Avatars(int index) {
            this.index=index;
        }
    }

    /** tablica przechowująca wszystkie wczytane tekstury */
    static BufferedImage[] localTextures = new BufferedImage[Textures.End.index];
    /** tablica przechowująca wszystkie wczytane awatary */
    static BufferedImage[] localAvatars = new BufferedImage[Avatars.End.index];
    /** tablica przechowująca poziomy gry */
    private Level[] availableLevels;
    /** Informuje czy zostało ustawione połączenie z serwerem*/
    static boolean connectionEstablished=false;
    /** Singleton instacja klasy AppSettings*/
    private static AppSettings single_instance = null;
    /**
     * Zwraca instancję statyczną klasy AppSettings
     * @return zwraca instancję statyczną
     */
    public static AppSettings getInstance()
    {
        if (single_instance == null)
            single_instance = new AppSettings();

        return single_instance;
    }

    /**
     * Tworzy tablicę wszystkich wczytanych tekstur dla gry offline lub gry sieciowej
     */
    private void load_textures() {
        if (!connectionEstablished) {
            File txtrs = new File(textures_path);
            File[] files = txtrs.listFiles();
            for (int i = 0; i < files.length; i++) {
                try {
                    BufferedImage img;
                    img = ImageIO.read(files[i]);
                    String fnameNoExt = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
                    localTextures[Textures.valueOf(fnameNoExt).index] = img;// scale it the smooth way

                } catch (IOException e) {
                    System.out.println("No such texture\n");
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                byte[] respond = AppSettingsFromServer.load_textures(i);
                try {
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(respond));
                    localTextures[i] = img;// scale it the smooth way
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * Tworzy tablicę wszystkich wczytanych awatarów dla gry offline lub gry sieciowej
     */
    private void load_avatars(){
        if(!connectionEstablished) {
            File avtrs = new File(avatars_path);
            File[] files = avtrs.listFiles();
            for (int i = 0; i < files.length; i++) {
                try {
                    BufferedImage img;
                    img = ImageIO.read(files[i]);
                    String fnameNoExt = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
                    localAvatars[Avatars.valueOf(fnameNoExt).index] = img;// scale it the smooth way

                } catch (IOException e) {
                    System.out.println("No such avatar\n");
                }
            }
        }
        else
        {
            for(int i=0; i<3;i++)
            {
                byte[] respond=AppSettingsFromServer.load_avatars(i);
                try {
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(respond));
                    localAvatars[i] = img;// scale it the smooth way
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    /**
     * Tworzy tablicę wszystkich wczytanych poziomów dla gry offline lub gry sieciowej
     */
    private void load_levels(){
        availableLevels = new Level[numberOfLevels];
        if(!connectionEstablished) {
            try {
                File txtrs = new File(levels_path);
                File[] files = txtrs.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if(i>=firstLevel && i<=firstLevel+numberOfLevels-1) {
                        Scanner myReader = new Scanner(files[i]);
                        ArrayList<String> data = new ArrayList<>();
                        while (myReader.hasNextLine()) {
                            data.add(myReader.nextLine());
                        }
                        availableLevels[i - firstLevel] = new Level(data);
                        myReader.close();
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
                e.printStackTrace();
            }
        }

        else {
            for (int i = 0; i < numberOfLevels; i++) {
                String respond = AppSettingsFromServer.load_levels(firstLevel+i);
                String[] array = respond.split("/");
                ArrayList<String> data = new ArrayList<>();
                for (int b = 1; b < array.length; b++) {
                    if(array[b]!=null) {
                        data.add(array[b]);
                    }
                }
                availableLevels[i]= new Level(data);
            }
        }
    }

    /**
     * Wczytuje z udostępnionych plików konfiguracyjnych atrybuty gry. Wywołuje metodę zapisującą dane z plików konfiguracyjnych do odpowiednich zmiennych
     */
    private void load_config(){
        if(!connectionEstablished) {
            java.util.List<String> list = new ArrayList<>();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(config_path));
                String line = bufferedReader.readLine();
                do {
                    list.add(line);
                    line = bufferedReader.readLine();
                } while (line != null);
                save_config(list);
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                e.printStackTrace();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        else{
            String respond=AppSettingsFromServer.load_config();
            String[] array=respond.split("/");
            List<String> list = new ArrayList<>(Arrays.asList(array));
            save_config(list);
        }
    }

    /**
     * Zapisuje otrzymane atrybuty do odpowiednich zmiennych. Modyfikuje zmienne w celu możliwości personalizacji gry.
     * @param list lista Stringów, które zostaną zapisane do odpowiednich zmiennych
     */
    private void save_config(List<String> list){
        numberOfLives=Integer.parseInt(list.get(0));
        xSize=Integer.parseInt(list.get(1));
        ySize=Integer.parseInt(list.get(2));
        numberOfSteps=Integer.parseInt(list.get(3));
        numberOfLevels=Integer.parseInt(list.get(4));
        firstLevel=Integer.parseInt(list.get(5));
        lastLevel=Integer.parseInt(list.get(6));
        increaseVelocity=Integer.parseInt((list.get(7)));
        check_config();
    }

    /**
     * Sprawdza poprawność wczytanych danych dotyczących ilości poziomów zadeklarowanych w plikach konfiguracyjnych
     */
    private void check_config()
    {
        try {
            if (lastLevel - firstLevel != numberOfLevels - 1) {
                throw new Exception("Check config!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Funkcja wywołująca wszystkie funkcje, które wczytują parametry z plików konfiguracyjnych oraz zapisują dane w odpowiednie miejsca
     * @param connectionE określa czy zostało nawiązane połączenie z serwerem, czy użytkownik wybrał grę offline
     */
    public void load(boolean connectionE){
        connectionEstablished=connectionE;
        load_config();
        load_levels();
        load_textures();
        load_avatars();
    }

    /**
     * Funkcja zwracająca szerokość ekranu aplikacji
     * @return zwraca szerokość ekranu aplikacji
     */
    public Integer ScreenWidth(){ return xSize;}
    /**
     * Funkcja zwracająca wysokość ekranu aplikacji
     * @return zwraca wysokość ekranu aplikacji
     */
    public Integer ScreenHeight(){return ySize;}
    /**
     * Funkcja zwracająca odpowiedni poziom z tablicy wszystkich dostępnych poziomów
     * @param  index numer wybranej planszy gry
     * @return zwraca odpowiedni poziom z tablicy wszystkich dostępnych poziomów
     */
    public Level getAvailableLevel(int index){return availableLevels[index];}
    /**
     * Funkcja zwracająca odpowiednią teksturę z tablicy wszystkich dostępnych tekstur
     * @param name nazwa wybranej tekstury
     * @return zwraca odpowiednią teksturę z tablicy wszystkich dostępnych tekstur
     */
    public BufferedImage getLocalTexture(Textures name){return localTextures[name.index];}
    /**
     * Funkcja zwracająca odpowiedni awatar z tablicy wszystkich dostępnych awatarów
     * @param index numer wybranego awataru do wczytania
     * @return zwraca odpowiedni awatar z tablicy wszystkich dostępnych awatarów
     */
    public BufferedImage getLocalAvatar(int index){return localAvatars[index];}

}
