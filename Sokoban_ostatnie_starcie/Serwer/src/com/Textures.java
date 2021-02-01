package com;

import java.io.File;

/**
 * Klasa odpowiedzialna za ustawianie ścieżki dostępu do tekstur
 */
public class Textures {
    /** Scieżka dostępu do folderu przechowującego tekstury */
    private static String textures_path="Pliki/textures";

    /**
     * Metoda ustawiająca ścieżkę dostępu do tekstury
     * @param numberOfTexture numer wczytywanej obecnie tekstury
     * @return metoda zwraca ścieżkę dostępu do odpowiedniej tekstury
     */
    public static String load_textures(int numberOfTexture)
    {
        String fnameNoExt = null;
        File avtrs= new File(textures_path);
        File[] files=avtrs.listFiles();

        for (Integer i = 0; i < files.length; i++){
            if(i==numberOfTexture) {
                fnameNoExt = files[i].getName().substring(0, files[i].getName().lastIndexOf("."));
            }
        }
        return textures_path+"\\"+fnameNoExt+".png";
    }
}
