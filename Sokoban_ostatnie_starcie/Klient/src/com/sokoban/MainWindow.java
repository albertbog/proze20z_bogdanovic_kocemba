package com.sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Klasa odpowiedzialna za tworzenie ramki i obsługe zdarzen pochodzących z graficznego
 * interfejsu użytkownika
 */

public class MainWindow extends JFrame {
    /** Tablica przechowująca stany okna Menu */
    public static IMenu [] menus = new IMenu[Menus.End.index];

    /** Przypisanie numerom stanów okna Menu nazw*/
enum Menus {
        MainMenu(0),
        HowToPlayMenu(1),
        GameMenu(2),
        HighScoresMenu(3),
        AvatarMenu(4),
        Sokoban(5),
        End(6);

        public int index;

        Menus(int index) {
            this.index=index;
        }

}

    /**
     * Inicjuje głowne okno menu
     */
    public MainWindow() {
        super("Sokoban");
        menus[Menus.MainMenu.index]=new MainMenu(this);
        initializeScreen();
        MainWindow.switchMenus(Menus.MainMenu,this);

    }


    /** Metoda odpowiedzialna za inicjalizację ekranu o odpowiednich parametrach*/
    public void initializeScreen(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(AppSettingsMainMenu.getInstance().ScreenWidth(), AppSettingsMainMenu.getInstance().ScreenHeight());
        this.getContentPane().setBackground(new Color(0,153,153));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    /** Metoda odpowiedzialna za inicjację tytułowego panelu aplikacji
     * @param titleLabel komponent, w którym ma zostać napisany tekst
     * @param titlePanel panel, na którym ma zostać dokonana zmiana
     * @param text tekst, który ma zostać wyświetlony w oknie
     * @return metoda zwraca panel, w którym została dokonana zmiana
     */
    public static JPanel initializeTitlePanel(JPanel titlePanel, JLabel titleLabel, String text ) {
        titleLabel.setText(text);
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(0,153,153));
        titleLabel.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 12));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;

    }
    /** Metoda odpowiedzialna za zmianę wielkości napisu
     * @param tLabel komponent, w którym ma zostać zmieniony tekst
     * @param tPanel panel, na którym ma zostać dokonana zmiana
     */
    public static void resizeTitle(JPanel tPanel, JLabel tLabel){
        tLabel.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, tPanel.getWidth()/10));
        tPanel.repaint();
    }

    /** Metoda odpowiedzialna za zmianę menu
     * @param menu nazwa nowego menu
     * @param mainW kontener, w którym zmieniane są elementy
     */
    public static void switchMenus(Menus menu, JFrame mainW){
        mainW.setContentPane(menus[menu.index]);
        mainW.repaint();
        mainW.revalidate();

    }
}
