package com.sokoban;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Klasa zarządzająca całą grą Sokoban
 *
 */
public class Sokoban extends IMenu  {
    /** Przechowuje informacje dotyczącą wybranego awataru przez użytkownika*/
    public static int choosenAvatarInd;
    /** Informuje czy gra jest zapauzowana, true - zapauzowana */
    private boolean pauseButtonPressed = false;
    /** Przechowuje numer obecnego poziomu */
    public static int currentLevel=0;
    /**  Przechowuje liczbę poziomów, które użytkownik przeszedł*/
    public static  int numOfPassedLevels=0;
    /** Przechowuje liczbę użytych restartów*/
    public static int restartsCounter=0;
    /** Przechowuje liczbę wszystkich kroków wykonanych przez użytkownika*/
    public static int allSteps=0;

    /** Przechowuje nick gracza */
    public static String nick;
    /** Przechowuje wynik gracza w trakcie rozgrywki*/
    private int score;
    /**obiekt pomocniczy odpowiedzialny za tworzenie planszy*/
    private Drawer drawer;
    /**obiekt pomocniczy informujący o obecnym poziomie*/
    private Level lvl;
    /**Panel z informacjá o liczbie kroków i żyć*/
    JPanel info;
    /** Komponent przechowujący liczbę kroków. Potem liczba kroków zostanie wyświetlona użytkownikowi */
    private JLabel steps;
    /** Komponent przechowujący liczbę żyć. Potem liczba żyć zostanie wyświetlona użytkownikowi */
    private JLabel lifes;
    /** Licznik kroków */
    public int stepsCounter=AppSettings.stepsCounter;
    /** Przechowuje liczbę pozostałych żyć */
    public static int lifesCounter=AppSettings.numberOfLives;

    /**Przypisuje podstawowe obiekty na rzecz inicjacji menu gry
     * @param w okno podstawowe na którym wyświetlane jest menu
     * @param choosenAvatar informacja dotyczącą wybranego awataru przez użytkownika
     */
    Sokoban(MainWindow w,int choosenAvatar){
        super(w);
        choosenAvatarInd=choosenAvatar;
        initButtons(new String[]{"Pause", "Game Menu"});
        initPanel();


    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca panel
     */
    @Override
    public void initPanel(){
        lvl = AppSettings.getInstance().getAvailableLevel(currentLevel);
        drawer= new Drawer(this,mainWindow,lvl );
        this.setLayout(new BorderLayout());
        Box box = new Box(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createVerticalGlue());
        box.add(drawer.draw());
        box.add(Box.createVerticalGlue());
        drawer.board.setFocusable(true);
        add(box);
        menuPanel.setLayout(new GridLayout(5,1));
        menuPanel.setBackground(new Color(0,153,153));
        listenersActivate();
        info = new JPanel(new GridLayout(3,1));
        steps = new JLabel("Steps: "+AppSettings.stepsCounter);
        lifes=new JLabel("Lifes: "+lifesCounter);
        info.add(steps);info.add(lifes);
        menuPanel.add(info);
        info.setBackground(new Color(0,153,153));
        add(menuPanel,BorderLayout.EAST);
        this.setBackground(new Color(0,153,153));

    }
    /**
     * Metoda nadpisana klasy IMenu, inicjująca przyciski menu
     * @param text lista tytułów przycisków, które należy dodać do menu
     */
    @Override
    public void initButtons(String[] text) {
    super.initButtons(text);
    }
    /**
     *nasłuchuje wybór przycisku użytkownika i w zależności zatrzybuje/uruchamia stan gry dla pause lub wraca do menu gry
     */
    private void listenersActivate(){
        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                resizeBtn();
                menuPanel.setPreferredSize(new Dimension(mainWindow.getWidth()/7,mainWindow.getHeight()));
            }
        });

        drawer.board.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                steps.setText("Steps: "+AppSettings.stepsCounter);
                Sokoban.allSteps=Sokoban.allSteps+AppSettings.stepsCounter;
                lifes.setText("Lifes: "+lifesCounter);
                if(lifesCounter<=0){
                    drawer.board.clearAll();
                    score = 100000*numOfPassedLevels/(3*restartsCounter+allSteps+20*(AppSettings.numberOfLives-lifesCounter));
                    if(AppSettings.connectionEstablished) {
                        String line = AppSettingsFromServer.save_ranking("Save_ranking" + "!" + nick + "/" + score);
                        JOptionPane.showMessageDialog(null, "Congratulations!\n Your Score is :  " + score + "\n" + line);
                    }
                    else {
                        HScores.saveScore(nick,score);
                        JOptionPane.showMessageDialog(null, "Congratulations!\n Your Score is :  " + score);
                    }

                    resetCounters();
                    MainWindow.menus[MainWindow.Menus.MainMenu.index]=new MainMenu(mainWindow);
                    MainWindow.switchMenus(MainWindow.Menus.GameMenu,mainWindow);
                    drawer.board.timer.stop();
                }
                info.repaint();
            }
        });
        buttons.get(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(buttons.get(0).getText()=="Pause")
                {lvl.pause();
                    drawer.board.timer.stop();
                buttons.get(0).setText("Resume");
                drawer.board.setFocusable(false);
                System.out.println("isPaused\n");}
                else{
                    buttons.get(0).setText("Pause");
                    drawer.board.timer.restart();
                    drawer.board.setFocusable(true);
                    drawer.board.requestFocus();
                    lvl.resume();
                }

            }
        });
        buttons.get(1).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    resetCounters();
                    MainWindow.menus[MainWindow.Menus.MainMenu.index]=new MainMenu(mainWindow);
                    MainWindow.switchMenus(MainWindow.Menus.GameMenu,mainWindow);
                    drawer.board.timer.stop();
                    //clearData();
            }
        });



    }

    /**
     * Wczytuje kolejny poziom, jesli gracz dotarl do konca wyswietla menu z koncowym wynikiem i przesyła wynik do klasy odpowiedzialnej za zapis danych do listy najlepszych wyników
     */
    public void loadNextLevel() {

        if (currentLevel < AppSettings.numberOfLevels) {
            buttons.removeAll(buttons);
            initButtons(new String[]{"Pause", "Main Menu"});
            initPanel();
            repaint();
            revalidate();

        }
        else {
            drawer.board.clearAll();
            score = 100000*numOfPassedLevels/(3*restartsCounter+allSteps+20*(AppSettings.numberOfLives-lifesCounter));
            if(AppSettings.connectionEstablished) {
                String line = AppSettingsFromServer.save_ranking("Save_ranking" + "!" + nick + "/" + score);
                JOptionPane.showMessageDialog(null, "Congratulations!\n Your Score is :  " + score + "\n" + line);
            }
            else {
                HScores.saveScore(nick,score);
                JOptionPane.showMessageDialog(null, "Congratulations!\n Your Score is :  " + score);
            }
            resetCounters();
            MainWindow.menus[MainWindow.Menus.MainMenu.index]=new MainMenu(mainWindow);
            MainWindow.switchMenus(MainWindow.Menus.GameMenu,mainWindow);
            drawer.board.timer.stop();
        }
   }

    /**
     * Odpowiada za restart wszystkich parametrów gry przy zakończeniu rozgrywki
     */
    private void resetCounters(){
        currentLevel=0;
        nick="";
        stepsCounter=0;
        lifesCounter=AppSettings.numberOfLives;
        numOfPassedLevels=0;
        restartsCounter=0;
        allSteps=0;
        AppSettings.stepsCounter=0;

    }


}