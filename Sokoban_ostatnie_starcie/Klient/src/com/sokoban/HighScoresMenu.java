package com.sokoban;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
/**
 * Klasa odpowiedzialna za interfejs listy najlepszych wyników
 */
public class HighScoresMenu extends IMenu {
    /** przechowuje nazwy kolumn tabeli najlepszych wyników */
    private static final String column[]={"Rank", "Name", "Score"};
    /** przechowuje model tabeli z zainicjowanymi nazwami kolumn */
    private DefaultTableModel model=new DefaultTableModel(column,0);
    /** Tablica przechowujaca najlepsze wyniki w formacie "Rank-Name-Score" */
    private JTable jt=new JTable(model);
    /**
     * Inicjuje menu najlepszych wyników wraz z listą najlepszych wyników.
     * @param mainWindow okno podstawowe na którym wyświetlane jest menu najlepszych wyników
     */
    public HighScoresMenu(MainWindow mainWindow){
        super(mainWindow);
        initPanel();
        initTable(HScores.list);
        initButtons(new String[]{"Back"});
        listenersActivate();
    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca panel menu
     */
    @Override
    public void initPanel() {
        this.setBackground(new Color(0,153,153));
        this.setLayout(new GridLayout(1,0));
        menuPanel.setLayout(new GridLayout(1,0));
        menuPanel.setBackground(new Color(0,153,153));
    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca przyciski menu
     * @param text lista tytułów przycisków, które należy dodać do menu
     */
    @Override
    public void initButtons(String[] text){
        super.initButtons(text);
    }
    /**
     * Metoda inicjująca tabelę listą najlepszych wyników
     * @param list lista najlepszych wyników wczytywana z pliku konfiguracyjnego
     */
    public void initTable(ArrayList<String> list){
        for(int i=0;i<list.size();i=i+1)
        {
            model.addRow(new String[]{String.valueOf(i+1), list.get(i).split(" ")[0], list.get(i).split(" ")[1]});
        }
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setLayout(new BorderLayout(5,5));
        add(new JScrollPane(jt), BorderLayout.CENTER);
        add(menuPanel, BorderLayout.PAGE_END);
    }
    /**
     *nasłuchuje wywołanie przycisku back po czym
     * wywołuje submenu GameMenu
     */
    private void listenersActivate(){
        menuPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBtn();
            }
        });
        if(buttons.get(0).getText().equals("Back")) {
            buttons.get(0).addActionListener(e -> MainWindow.switchMenus(MainWindow.Menus.GameMenu,mainWindow));
        }
    }
}

