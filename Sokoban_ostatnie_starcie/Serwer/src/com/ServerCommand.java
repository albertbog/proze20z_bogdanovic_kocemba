package com;

/**
 * Klasa odpowiedzialna za obsługę poleceń od użytkownika
 */
public class ServerCommand {

    /**
     * Metoda odpowiedzialna za obsługę poleceń od użytkownika
     * @param command tablica String z odpowiednimi komendami przesłanymi od użytkownika
     * @return odpowiedź zwracana przez serwer lub ścieżka dostępu do danego pliku (w zależności od komendy użytej przez gracza)
     */
    public static String do_command_string(String[] command)
    {
        String respond;

        switch (command[0])
        {
            case "Load_ranking":
                respond= Ranking.load_ranking();
                break;
            case "Load_levels":
                respond= Levels.load_levels(Integer.valueOf(command[1]));
                break;
            case "Load_config":
                respond=ConfigLoader.load_config();
                break;
            case "Load_avatars":
                respond=Avatars.load_avatars(Integer.valueOf(command[1]));
                System.out.println(respond);
                break;

            case "Load_textures":
                respond=Textures.load_textures(Integer.valueOf(command[1]));
                System.out.println(respond);
                break;
            case "Save_ranking":
                respond=Ranking.save_ranking((String) command[1]);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + command);
        }
        return respond;
    }

}
