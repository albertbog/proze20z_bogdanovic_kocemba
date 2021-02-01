package com.sokoban;

import java.io.*;
import java.util.*;

/**
 * Klasa odpowiedzialna za wczytywanie, udostępnianie oraz zapisywanie do listy najlepszych wyników
 */
public class HScores {
    /**
     * Lista stringów przechowująca listę najlepszych wyników dla trybu online bądź lokalnego w zależności od wyboru użytkownika
     */
    static ArrayList<String> list;
    /** Scieżka dostępu do pliku ranking.txt */
    static final String ranking_path="src/com/sokoban/ranking/ranking.txt";
    /** Zmienna przechowująca tryb gry- lokalny bądź online */
    boolean connectionEstablished;

    /** Metoda zapisująca tryb gry wybrany przez użytkownika
     * @param  connectionE zmienna określająca tryb wybrny przez użytkownika
     */
    public HScores(boolean connectionE)
    {
        connectionEstablished=connectionE;
    }

    /** Metoda, która wywołuje metody wczytujące ranking z udostępnionych danych
     */
    public void loadRanking(){
        if(!connectionEstablished)
        {
            loadRankingFromFile();
        }
        else
        {
            loadRankingFromServer();
        }
    }

    /** Metoda, która wczytuje ranking na podstawie danych uzyskanych z serwera
     */
    private void loadRankingFromServer(){
        String line=AppSettingsFromServer.load_ranking();
        list=new ArrayList<>();
        String[] dd=line.split("/");
        for(int i = 0; i<dd.length; i++)
        {
            list.add(dd[i]);
        }

    }

    /** Metoda, która wczytuje ranking na podstawie danych uzyskanych z plików konfiguracyjnych
     */
    private void loadRankingFromFile(){
        try {
            list=new ArrayList<>();
            BufferedReader File = new BufferedReader(new FileReader(ranking_path));
            String line=File.readLine();
            do {
                list.add(line);
                line=File.readLine();
            }while(line!=null);
            File.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /** Metoda, która zarządza zapisem rankingu do plików konfiguracyjnych
     * @param name nazwa użytkownika
     * @param score wynik
     */
    static void saveScore(String name, int score){
        list.add(name + " " + score);
        list.sort(new MyComparator());
        list.remove(list.size()-1);
        saveRankingInFile();
    }

    /** Klasa wewnętrzna, która porównuje otrzymany wynik z listą najlepszych wyników
     */
    static class MyComparator implements Comparator<String> {
        /** Metoda, która porównuje wyniki graczy
         * @param o1 liczba punktów uzyskanych przez pierwszego gracza
         * @param o2 liczba punktów uzyskanych przez drugiego gracza
         * @return zwraca liczbę określającą zależność porównywanych wartości
         */
        @Override
        public int compare(String o1, String o2){
            Integer a = Integer.parseInt(o1.split(" ")[1]);
            Integer b = Integer.parseInt(o2.split(" ")[1]);
            return -a.compareTo(b);
        }
    }

    /** Metoda, która zapisuje ranking do plików konfiguracyjnych
     */
    private static void saveRankingInFile() {
        try {
            BufferedWriter File = new BufferedWriter(new FileWriter(ranking_path));
            for (String line : list) {
                File.write(line);
                File.newLine();
            }
            File.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

