package com.sokoban;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**Klasa bazowa odpowiedzialna za główne cechy ( takie jak panel główny menu oraz przyciski) dziedziczących klas pochodnych */
public class IMenu extends JPanel {
    /**Lista zawieranych w menu przycisków*/
    protected ArrayList<JButton> buttons;
    /**Panel na którym są ilustrowane podstawowe cechy menu*/
    protected JPanel menuPanel;
    /**Okno główne, do którego są dodawane(na którym są ilustrowane) klasy pochodne menu */
    protected MainWindow mainWindow;


    /**
     * Inicjuje głowne składowe każdego menu
     * @param mainWindow okno podstawowe na którym wyświetlane jest menu
     */
    public IMenu(MainWindow mainWindow) {
        this.mainWindow=mainWindow;
        menuPanel= new JPanel();
        buttons=new ArrayList<>();
    }
    /**
     * Metoda inicjująca tytuł i panel menu
     */
    public void initPanel(){}
    /**
     * Metoda inicjująca przyciski menu
     * @param text lista tytułów przycisków, które należy dodać do menu
     */
    public void initButtons(String[] text){

        for (String s : text) {
            JButton button = new JButton(s);
            button.setAlignmentX(CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(menuPanel.getWidth() / 2, menuPanel.getHeight() / 2));
            buttons.add(button);
            menuPanel.add(button);

        }
        menuPanel.revalidate();
    }
    /**
     * Metoda umożliwiająca zmianę wielkości przycisków w zależności od wielkości ekranu
     */
    public void resizeBtn(){

        for(JButton button:buttons)
        {
            button.setMaximumSize(new Dimension(this.getWidth()/2,this.getHeight()/2));
        }
        menuPanel.revalidate();
    }

}
