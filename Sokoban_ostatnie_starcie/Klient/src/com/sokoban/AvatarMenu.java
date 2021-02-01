package com.sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
/**
 * Klasa odpowiedzialna za ilustrację menu wyboru bohatera.
 */
public class AvatarMenu extends IMenu {
    /** Panel przechowujący tytuł menu  */
    JPanel titlePanel;
    /** Tytuł menu */
    JLabel titleLabel;
    /** zmienna przechowująca nazwę użytkownika */
    private String nick;
    /** zmienna sprawdzająca obecność nicku użytkownika */
    private Boolean isEmptyNick=false;


    /**
     * Inicjuje menu wyboru bohatera i wywołuje nasłuchiwacz kliknięcia/wyboru bohatera
     * @param mainWindow okno podstawowe na którym wyświetlane jest menu bohatera
     */
    public AvatarMenu(MainWindow mainWindow) {
        super(mainWindow);
        titleLabel = new JLabel();
        titlePanel = new JPanel();
        initPanel();
        initButtons(AppSettings.localAvatars);
        listenersActivate();

    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca tytuł i panel menu
     */
    @Override
    public void initPanel(){
        this.setLayout(new GridLayout(2,1));
        menuPanel.setLayout(new GridLayout(1,3));
        this.add(MainWindow.initializeTitlePanel(titlePanel,titleLabel,"Choose:"));
        menuPanel.setBackground(new Color(0,153,153));
        this.add(menuPanel);

    }
    /**
     * wywołuje dialog dodania nazwy użytkownika i sprawdza, czy zachowane pole nie jest puste
     */
    private void setNick(){
    nick = JOptionPane.showInputDialog("Enter Nickname:");
    if(nick == null){
        isEmptyNick=true;
        JOptionPane.showMessageDialog(null, "Unfortunately, You can't cancel. Please enter nickname :)");
    }
    else if (nick != null && ("".equals(nick)))
    {
        isEmptyNick=true;
        JOptionPane.showMessageDialog(null, "Empty name is not allowed");
    }
    else{isEmptyNick=false;}
}
    /**
     *nasłuchuje wybór bohatera przez użytkownika, po czym
     * wywołuje kolejne submenu: Sokoban
     */
    private void listenersActivate(){

        menuPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeBtn();
            }
        });
        buttons.get(0).addActionListener(e -> {
            setNick();
            if(!isEmptyNick)
            {
                Sokoban.nick= nick;
                MainWindow.menus[MainWindow.Menus.Sokoban.index]=new Sokoban(mainWindow,0);
                MainWindow.switchMenus(MainWindow.Menus.Sokoban,mainWindow);
            }
        });
        buttons.get(0).addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = buttons.get(0).getSize();
                Image scaled = AppSettings.localAvatars[0].getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
                buttons.get(0).setIcon(new ImageIcon(scaled));
            }
            });

        buttons.get(1).addActionListener(e -> {
            setNick();
            if(!isEmptyNick)
            {
                Sokoban.nick= nick;
                MainWindow.menus[MainWindow.Menus.Sokoban.index]=new Sokoban(mainWindow,1);
                MainWindow.switchMenus(MainWindow.Menus.Sokoban,mainWindow);
            }
        });
        buttons.get(1).addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = buttons.get(1).getSize();
                Image scaled = AppSettings.localAvatars[1].getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
                buttons.get(1).setIcon(new ImageIcon(scaled));
            }
        });
        buttons.get(2).addActionListener(e -> {
            setNick();
            if(!isEmptyNick)
            {
                Sokoban.nick= nick;
                MainWindow.menus[MainWindow.Menus.Sokoban.index]=new Sokoban(mainWindow,2);
                MainWindow.switchMenus(MainWindow.Menus.Sokoban,mainWindow);
            }
        });
        buttons.get(2).addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = buttons.get(2).getSize();
                Image scaled = AppSettings.localAvatars[2].getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
                buttons.get(2).setIcon(new ImageIcon(scaled));
            }
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                MainWindow.resizeTitle(titlePanel,titleLabel);

            }
        });
    }
    /**
     * inicjuje przyciski i ustawia ilustracje bohaterów na przyciskach
     * @param avatars lista ilustracji bohaterów, które należy narysować na przyciskach
     */
    public void initButtons(BufferedImage[] avatars) {
        String[] text=new String[avatars.length];
        for(int i=0;i<avatars.length;i++){
           text[i]= avatars[i].toString();
        }
        super.initButtons(text);
        for(int i=0;i<avatars.length;i++) {
            buttons.get(i).setText(null);
            buttons.get(i).setIcon(new ImageIcon(avatars[i]));

        }
    }
}
