package com.sokoban;


/**
 * Klasa odpowiedzialna za konstruowanie komunikatów przesyłanych na serwer.
 * Uzyskaną odpowiedź przekazuje do metod zapisujących otrzymane dane do odpowiednich tablic/zmiennych.
 */
public class AppSettingsFromServer {
    /**
     * Konstuuje komunikat z prośbą o udostępnienie listy najlepszych wyników graczy.
     * Po otrzymaniu odpowiedzi od serwera zwraca pojedynczą linię tekstu typu String, w której zapisane są dane z listy najlepszych wyników.
     * @return zwraca String z listą najlepszych wyników na serwerze
     */
    public static String load_ranking(){
        return new Connection().exchange_messages("Load_ranking");
    }
    /**
     * Konstuuje komunikat z prośbą o udostępnienie listy dostępnych poziomów.
     * Po otrzymaniu odpowiedzi od serwera zwraca pojedynczą linię tekstu typu String, w której zapisany jest wybrany poziom.
     * @param level_number określa wybrany poziom, który obecnie zostanie wczytany
     * @return zwraca String z wybraną mapą
     */
    public static String load_levels(Integer level_number){return new Connection().exchange_messages("Load_levels"+"!"+level_number); }
    /**
     * Konstuuje komunikat z prośbą o udostępnienie początkowych plików konfiguracyjnych.
     * Po otrzymaniu odpowiedzi od serwera zwraca pojedynczą linię tekstu typu String, w której zapisany są odpowiednie dane.
     * @return zwraca String z danymi z plików konfiguracyjnych
     */
    public static String load_config(){return new Connection().exchange_messages("Load_config");}
    /**
     * Konstuuje komunikat z prośbą o udostępnienie listy dostępnych awatarów.
     * Po otrzymaniu odpowiedzi od serwera zwraca tablicę typu byte, w której zapisany jest wybrany awatar.
     * @param avatar_number określa numer wybranego awataru, który obecnie zostanie wczytany
     * @return zwraca tablicę typu byte z wybranym awatarem
     */
    public static byte[] load_avatars(Integer avatar_number){return new Connection().exchange_messages_avatars("Load_avatars"+"!"+avatar_number);}
    /**
     * Konstuuje komunikat z prośbą o udostępnienie listy dostępnych tekstur.
     * Po otrzymaniu odpowiedzi od serwera zwraca tablicę typu byte, w której zapisana jest wybrana tekstura.
     * @param texture_number określa numer wybranej tekstury, która obecnie zostanie wczytana
     * @return zwraca tablicę typu byte z wybraną teksturą
     */
    public static byte[] load_textures(Integer texture_number){return new Connection().exchange_messages_avatars("Load_textures"+"!"+texture_number);}
    /**
     * Konstuuje komunikat z prośbą o możliwe zapisanie uzyskanego wyniku na liście najlepszych wyników serwera.
     * Po otrzymaniu odpowiedzi od serwera zwraca pojedynczą linię tekstu typu String, w której zapisana jest komunikat dotyczący wyniku gracza.
     * @param data linia tekstu zawierająca informację o nazwie użytkownika oraz zdobytej liczby punktów podczas gry
     * @return zwraca linię tekstu typu String przechowującą komunikat dotyczący zapisu wyniku na liście najlepszych wyników serera
     */
    public static String save_ranking(String data) {return new Connection().exchange_messages(data);}
}
