package com.sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**Menu główne, zawierające opcje przejścia do gry online lub offline, zasad gry oraz wyjścia*/
public class MainMenu extends IMenu {
    /** Panel przechowujący tytuł menu  */
    private JPanel titlePanel;
    /** Tytuł menu */
    private JLabel titleLabel;

    /**
     * Inicjuje głowne składowe menu
     * @param mainWindow okno podstawowe na którym wyświetlane jest menu
     */
   MainMenu(MainWindow mainWindow){
        super(mainWindow);
        titleLabel=new JLabel();
        titlePanel=new JPanel();
        initPanel();
        initButtons(new String[]{"Game","Server Game", "How To Play?", "EXIT"});
        listenersActivate();

    }

    /**
     * Metoda nadpisana klasy IMenu, inicjująca tytuł i panel menu
     */
    @Override
    public void initPanel(){
        this.setLayout(new GridLayout(2,1));
        this.add(MainWindow.initializeTitlePanel(titlePanel,titleLabel,"Sokoban"));
        menuPanel.setLayout(new BoxLayout(menuPanel,BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0,153,153));
        this.add(menuPanel);

    }
    /**
     *nasłuchuje wybór kolejnego menu przez użytkownika i w zależności od wyboru przechodzi
     *  do menu gry online lub offline, menu zasad gry lub wyjście
     */
    private void listenersActivate(){
        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                resizeBtn();
            }
        });
        if(buttons.get(0).getText().equals("Game")) {
            buttons.get(0).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    MainWindow.menus[MainWindow.Menus.GameMenu.index]=new GameMenu(mainWindow,"Game");
                    MainWindow.switchMenus(MainWindow.Menus.GameMenu,mainWindow);}
            });
        }
        if(buttons.get(1).getText().equals("Server Game")) {
            buttons.get(1).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    MainWindow.menus[MainWindow.Menus.GameMenu.index]=new GameMenu(mainWindow,"Server Game");
                    MainWindow.switchMenus(MainWindow.Menus.GameMenu,mainWindow);}
            });
        }
        if(buttons.get(2).getText().equals("How To Play?")) {
            buttons.get(2).addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
            MainWindow.menus[MainWindow.Menus.HowToPlayMenu.index]=new HowToPlayMenu(mainWindow);
            MainWindow.switchMenus(MainWindow.Menus.HowToPlayMenu,mainWindow);}
            });
        }
       if( buttons.get(3).getText().equals("EXIT")) {
            buttons.get(3).addActionListener(e -> mainWindow.dispose());
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




