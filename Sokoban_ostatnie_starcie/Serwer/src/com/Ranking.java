package com;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Klasa odpowiedzialna za wczytywanie, udostępnianie oraz zapisywanie do listy najlepszych wyników
 */
public class Ranking {
    /** Scieżka dostępu do pliku ranking.txt */
    static final String Ranking_path="Pliki\\Ranking.txt";
    /**
     * Linia tekstu, która zostanie udostępniona użytkownikowi
     */
    static String list;
    /**
     * Lista stringów przechowująca listę najlepszych wyników
     */
    static ArrayList<String> rankingList;

    /** Metoda, która wczytuje ranking do programu oraz zwraca linię tekstu z zapisanym rankingiem
     * @return metoda zwraca linię tekstu z zapisanym rankingiem
     */
    public static String load_ranking()
    {
        try {
            list= "";
            BufferedReader File = new BufferedReader(new FileReader(Ranking_path));
            String line=File.readLine();
            do {
                list=list+line+"/";
                line=File.readLine();
            }while(line!=null);
            File.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    /** Metoda, która zarządza zapisem rankingu do serwera oraz zwraca komunikat dotyczący umieszczenia gracza na liście najlepszych wyników
     * @param data nazwa użytkownika i wynik punktowy, który może być wpisany na listę najlepszych wyników
     * @return metoda zwraca linię tekstu z odpowiedzią dla klienta
     */
    public static String save_ranking(String data)
    {
        String name=data.split("/")[0];
        Integer score=Integer.parseInt(data.split("/")[1]);
        System.out.println(name);
        String respond;
        loadRankingFromFile();
        rankingList.add(name + " " + String.valueOf(score));
        rankingList.sort(new MyComparator());
        if((rankingList.get(rankingList.size()-1))!=name + " " + String.valueOf(score)){
            respond="Huuura! Jestes najlepszy!";
        }
        else{
            respond="Niestety! Są lepsi od Ciebie. Probuj dalej!";
        }
        rankingList.remove(rankingList.size()-1);
        saveRankingInFile();
        return respond;
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

    /** Metoda, która zapisuje ranking do plików konfiguracyjnych dostępnych na serwerze
     */
    private static void saveRankingInFile() {
        try {
            BufferedWriter File = new BufferedWriter(new FileWriter(Ranking_path));
            for (String line : rankingList) {
                File.write(line);
                File.newLine();
            }
            File.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /** Metoda, która wczytuje z serwera
     */
    private static void loadRankingFromFile(){
        try {
            rankingList=new ArrayList<>();
            BufferedReader File = new BufferedReader(new FileReader(Ranking_path));
            String line=File.readLine();
            do {
                rankingList.add(line);
                line=File.readLine();
            }while(line!=null);
            File.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
