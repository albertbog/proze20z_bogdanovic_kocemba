package com.sokoban;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Klasa odpowiedzialna za ilustrację
 * menu zawierającego przejście do menu wyboru bohatera i menu najlepszych wyników
 */
public class GameMenu extends IMenu {
    /** Panel przechowujący tytuł menu  */
    private JPanel titlePanel;
    /** Tytuł menu */
    private JLabel titleLabel;
    /** zmienna odpowiedzialna za sprawdzenie wybranego typu gry: online lub offline */
    private String type;
    /** Informuje czy zostało ustawione połączenie z serwerem*/
    private static Boolean connectionEstablished=false;

    /**
     * Inicjuje menu Game lub Serwer Game w zależności od wyboru użytkownika.
     * W przypadku wyboru Serwer Game następuje sprawdzenie połączenia z serwerem.
     * @param mainWindow okno podstawowe na którym wyświetlane jest menu Game lub Serwer Game
     * @param gameType wybrany przez użytkownika typ gry: online lub offline
     */
    GameMenu(MainWindow mainWindow,String gameType){
        super(mainWindow);
        if(gameType.equals("Game") || gameType.equals("Server Game")) {
            type = gameType;
            if(gameType.equals("Server Game")) {
                JOptionPane.showMessageDialog(super.mainWindow, "Connection is checking...", "Checking connection",
                        JOptionPane.INFORMATION_MESSAGE);
                Connection connection = new Connection();
                connection.load_data();
                connectionEstablished = connection.try_to_connect();
            }
            if(gameType.equals("Server Game") && !connectionEstablished)
            {JOptionPane.showMessageDialog(super.mainWindow, "Can't establish server connection... Server Game mod changed to Offline Game mod", "Warning",
                    JOptionPane.WARNING_MESSAGE);
                type="Game";
            }
            else if(gameType.equals("Server Game") && connectionEstablished)
            {
                JOptionPane.showMessageDialog(super.mainWindow, "Welcome in Server Game mod", "Server Game mod",
                        JOptionPane.INFORMATION_MESSAGE);
                type="Server Game";
            }
            titleLabel=new JLabel();
            titlePanel=new JPanel();
            initPanel();
            initButtons(new String[]{"Start", "High Score","Main Menu"});
            listenersActivate();
        }
        else {
            MainWindow.switchMenus(MainWindow.Menus.MainMenu,mainWindow);
            System.err.println("misspelled gameType");
        }

    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca tytuł i panel menu
     */
    @Override
    public void initPanel(){
        this.setLayout(new GridLayout(2,1));
        this.add(MainWindow.initializeTitlePanel(titlePanel,titleLabel,type));
        menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0,153,153));
        this.add(menuPanel);

    }
    /**
     *nasłuchuje wybór kolejnego menu przez użytkownika i w zależności od wyboru przechodzi
     *  do menu wyboru bohatera, najlepszych wyników lub powrotu do menu podstawowego
     */
    private void listenersActivate(){
        menuPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBtn();
            }
        });
        if(buttons.get(0).getText().equals("Start")) {
            AppSettings.getInstance().load(connectionEstablished);
            MainWindow.menus[MainWindow.Menus.AvatarMenu.index]=new AvatarMenu(mainWindow);
            buttons.get(0).addActionListener(e -> MainWindow.switchMenus(MainWindow.Menus.AvatarMenu,mainWindow));
        }
        if(buttons.get(1).getText().equals("High Score")) {
            HScores score=new HScores(connectionEstablished);
            score.loadRanking();
            MainWindow.menus[MainWindow.Menus.HighScoresMenu.index]=new HighScoresMenu(mainWindow);
            buttons.get(1).addActionListener(e -> MainWindow.switchMenus(MainWindow.Menus.HighScoresMenu,mainWindow));
        }
        if(buttons.get(2).getText().equals("Main Menu")) {
            connectionEstablished=false;
            buttons.get(2).addActionListener(e -> MainWindow.switchMenus(MainWindow.Menus.MainMenu,mainWindow));
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                MainWindow.resizeTitle(titlePanel,titleLabel);

            }
        });
    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca przyciski menu
     * @param text lista tytułów przycisków, które należy dodać do menu
     */
    @Override
    public void initButtons(String[] text) {
        super.initButtons(text);
    }


}
