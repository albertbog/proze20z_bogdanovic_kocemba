package com.sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;


/**
 * Klasa ilustrująca menu zasad gry
 *
 */
public class HowToPlayMenu extends IMenu {
    /** rysunek z zasadami gry*/
    private ImageIcon iconHtp;
    /** Pole do którego jest dodawana ilustracja z zasadami gry*/
    private JLabel iconLabel;


    /**
     * Inicjuje menu zasad gry
     * @param mainWindow okno podstawowe na którym wyświetlane jest menu zasad gry
     */
    HowToPlayMenu(MainWindow mainWindow) {
        super(mainWindow);
        try {
            initImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initPanel();
        initButtons(new String[]{"Back"});
        listenersActivate();

    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca panel menu
     */
    @Override
    public void initPanel() {
        this.setLayout(new BorderLayout());
        this.add(menuPanel);
    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca przyciski menu
     * @param text lista tytułów przycisków, które należy dodać do menu
     */
    @Override
    public void initButtons(String[] text) {
        super.initButtons(text);
        menuPanel.remove(buttons.get(0));
        this.add(buttons.get(0),BorderLayout.SOUTH);



    }
    /**
     *nasłuchuje wywołanie przycisku back po czym
     * wywołuje submenu MainMenu
     */
    private void listenersActivate(){
        buttons.get(0).addActionListener(e -> MainWindow.switchMenus(MainWindow.Menus.MainMenu,mainWindow));

        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                resizeIcon();resizeBtn();
            }
        });
    }
    /**Metoda wczytująca ilustrację zasad gry
     * @throws IOException w przypadku braku możliwości wczytania obrazku intrukcji gry
     * */
    public void initImage() throws IOException {
        iconLabel=new JLabel();
        iconHtp = new ImageIcon(getClass().getResource("img/sokobanHTP.png"));
        Image img =  iconHtp.getImage().getScaledInstance(mainWindow.getWidth(), mainWindow.getHeight(),
              Image.SCALE_SMOOTH);
       iconHtp = new ImageIcon(img);
        iconLabel=new JLabel(iconHtp);
        menuPanel.add(iconLabel);

    }
    /**Metoda ustawiająca preferowaną wielkość ilustracji zasad gry w zależności
     * od wielkości okna */
    private void resizeIcon(){
        menuPanel.remove(iconLabel);
        try {
            initImage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
