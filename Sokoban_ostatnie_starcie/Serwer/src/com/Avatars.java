package com;

import java.io.File;

/**
 * Klasa odpowiedzialna za ustawianie ścieżki dostępu do awatarów
 */
public class Avatars {
    /** Scieżka dostępu do folderu przechowującego awatary */
    private static String avatars_path="Pliki/avatars";

    /**
     * Metoda ustawiająca ścieżkę dostępu do awataru
     * @param numberOfAvatar numer wczytywanego obecnie awataru
     * @return metoda zwraca ścieżkę dostępu do odpowiedniego awatara
     */
    public static String load_avatars(int numberOfAvatar)
    {
        String fnameNoExt = null;
        File avtrs= new File(avatars_path);
        File[] files=avtrs.listFiles();

        for (Integer i = 0; i < files.length; i++){
            if(i==numberOfAvatar) {
                fnameNoExt = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
            }
        }
        return avatars_path+"\\"+fnameNoExt+".png";
    }
}
