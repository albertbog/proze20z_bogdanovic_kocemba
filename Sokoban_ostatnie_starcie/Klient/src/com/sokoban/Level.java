package com.sokoban;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**Klasa odpowiedzialna za poziomy */
public class Level  {
    /** Liczba wierszy poziomu */
    public int nRows;
    /** Liczba kolumn poziomu */
    public int nColuns;
    /** Tablica symboli odpowiadajcych obiektom planszy poziomu */
    private ArrayList<String> map;

    /** wszystkie pudła zostały umieszczone */
    boolean hasWon = false;
    /** informuje czy gra została zatrzymana */
    boolean isPaused=false;
     /***/

    /**
     * Inicjuje poziom danymi o obiektach znajdujących się na plaszy oraz liczbie wierszy i kolumn
     * @param map ciąg znaków odpowiadających konkretnym obiektom planszy
     */
    Level(ArrayList<String> map){
        this.map=map;
        nRows=map.size();
        String str=Collections.max(map, Comparator.comparing(String::length));
        nColuns=str.length();
    }
    /**Metoda zwracająca mapę
     * @return zwraca obecną mapę
     */
    public ArrayList<String> getMap(){
        return map;
    }



    /**
     * Pauzuje gre poprzez nadanie zmiennej isPaused wartosci true
     */
    public void pause() {
        isPaused = true;

    }
    /**
     * Wznawia gre poprzez nadanie zmiennej isPaused wartosci false
     */
    public void resume() {
        isPaused = false;
    }


}

/**
 * Klasa odpowiedzialna za sprawdzanie poprawności ruchu bohatera, odpowiada za mechanikę ruchu pudeł
 */
class TAdapter extends KeyAdapter {

    /**
     * Przypisanie kolizji z lewej strony wartości int
     */
    private final int LEFT_COLLISION = 1;
    /**
     * Przypisanie kolizji z prawej strony wartości int
     */
    private final int RIGHT_COLLISION = 2;
    /**
     * Przypisanie kolizji z góry wartości int
     */
    private final int TOP_COLLISION = 3;
    /**
     * Przypisanie kolizji z dołu wartości int
     */
    private final int BOTTOM_COLLISION = 4;
    /**Plansza ilustrująca załadowany poziom*/
    private Drawer.Board board;
    /**Zmienna określająca obecny poziom*/
    Level level;


    /**
     * Inicjuje adapter informacją o obecnym poziomie i planszy na której odbywa się gra
     * @param board plansza ilustrująca poziom, który jest załadowywany
     * @param level zmienna określająca obecny poziom
     */
    TAdapter(Drawer.Board board,Level level){this.board=board; this.level=level;}

    /**
     * Metoda nasłuchująca wykonania ruchu przez użytkownika. Wywołuje metody sprawdzające poprawność wykonanego ruchu. Sprawdza także czy użytkownik wygrał dany poziom.
     * @param e parametr nasłuchujący zdarzenie. W tym przypadku przycik sterowania lub restart
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (level.hasWon) {
            return;
        }

        if(board.playerMoved==false) {
            int key = e.getKeyCode();


            switch (key) {

                case KeyEvent.VK_LEFT:
                    if(checkPlayerCollision(LEFT_COLLISION)){
                        Sokoban.lifesCounter--;
                    }

                    if (checkWallCollision(this.board.player,
                            LEFT_COLLISION)) {
                        return;
                    }

                    if (checkBagCollision(LEFT_COLLISION)) {
                        return;
                    }


                    AppSettings.stepsCounter++;
                    board.changeObjPosition(board.player, -1, 0);


                    break;

                case KeyEvent.VK_RIGHT:
                    if(checkPlayerCollision(RIGHT_COLLISION)){
                        Sokoban.lifesCounter--;
                    }

                    if (checkWallCollision(this.board.player,
                            RIGHT_COLLISION)) {
                        return;
                    }

                    if (checkBagCollision(RIGHT_COLLISION)) {
                        return;
                    }

                    AppSettings.stepsCounter++;
                    board.changeObjPosition(board.player, 1, 0);


                    break;

                case KeyEvent.VK_UP:
                    if(checkPlayerCollision(TOP_COLLISION)){
                        Sokoban.lifesCounter--;
                    }

                    if (checkWallCollision(board.player, TOP_COLLISION)) {
                        return;
                    }

                    if (checkBagCollision(TOP_COLLISION)) {
                        return;
                    }

                    AppSettings.stepsCounter++;
                    board.changeObjPosition(board.player, 0, -1);


                    break;

                case KeyEvent.VK_DOWN:
                    if(checkPlayerCollision(BOTTOM_COLLISION)){
                        Sokoban.lifesCounter--;
                    }

                    if (checkWallCollision(this.board.player, BOTTOM_COLLISION)) {
                        return;
                    }

                    if (checkBagCollision(BOTTOM_COLLISION)) {
                        return;
                    }

                    AppSettings.stepsCounter++;
                    board.changeObjPosition(board.player, 0, 1);


                    break;

                case KeyEvent.VK_R:
                    board.getGraphics().clearRect(0, 0, board.getWidth(), board.getHeight());
                    restartLevel();
                    AppSettings.stepsCounter = 0;
                    Sokoban.restartsCounter++;

                    break;

                default:
                    break;
            }
        }

    }

    /**
     * Metoda pomocnicza sprawdzająca kolizję użytkownika ze ścianą
     * @param actor obiekt, na rzecz którego została wywołana metoda
     * @param type określa kierunek ruchu
     * @return zwraca informację dotyczącą zderzenia ze ścianą
     */
    private boolean checkWallCollision(BoardObject actor, int type) {

        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < board.walls.size(); i++) {

                    BoardObject wall = board.walls.get(i);

                    if (actor.isLeftCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < board.walls.size(); i++) {

                    BoardObject wall = board.walls.get(i);

                    if (actor.isRightCollision(wall)) {
                        return true;
                    }
                }

                return false;

            case TOP_COLLISION:

                for (int i = 0; i < board.walls.size(); i++) {

                    BoardObject wall = board.walls.get(i);

                    if (actor.isTopCollision(wall)) {

                        return true;
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < board.walls.size(); i++) {

                    BoardObject wall = board.walls.get(i);

                    if (actor.isBottomCollision(wall)) {

                        return true;
                    }
                }

                return false;

            default:
                break;
        }

        return false;
    }

    /**
     * Metoda pomocnicza sprawdzająca niedozwolone przesuwanie bloków (można przesuwać tylko jedną skrzynkę)
     * @param type określa kierunek ruchu
     * @return zwraca informację dotyczącą poprawności ruchu skrzyń
     */
    private boolean checkBagCollision(int type) {


               switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < board.boxes.size(); i++) {

                    BoardObject box = board.boxes.get(i);

                    if (board.player.isLeftCollision(box)) {

                        for (int j = 0; j <board.boxes.size(); j++) {

                            BoardObject item = board.boxes.get(j);

                            if (!box.equals(item)) {

                                if (box.isLeftCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(box, LEFT_COLLISION)) {
                                return true;
                            }
                        }

                        board.changeObjPosition(box,-1,0);

                        isCompleted();
                    }
                }

                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < board.boxes.size(); i++) {

                    BoardObject box  = board.boxes.get(i);
                    BoardObject prev = box;

                    if (board.player.isRightCollision(box)) {

                        for (int j = 0; j < board.boxes.size(); j++) {

                            BoardObject item = board.boxes.get(j);

                            if (!box.equals(item)) {

                                if (box.isRightCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(box, RIGHT_COLLISION)) {
                                return true;
                            }
                        }

                        board.changeObjPosition(box,1,0);

                        isCompleted();
                    }
                }
                return false;

            case TOP_COLLISION:

                for (int i = 0; i < board.boxes.size(); i++) {

                    BoardObject box = board.boxes.get(i);
                    BoardObject prev = box;

                    if (board.player.isTopCollision(box)) {

                        for (int j = 0; j < board.boxes.size(); j++) {

                            BoardObject item = board.boxes.get(j);

                            if (!box.equals(item)) {

                                if (box.isTopCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(box, TOP_COLLISION)) {
                                return true;
                            }
                        }

                        board.changeObjPosition(box,0,-1);

                        isCompleted();
                    }
                }

                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < board.boxes.size(); i++) {

                    BoardObject box = board.boxes.get(i);
                    BoardObject prev = box;

                    if (board.player.isBottomCollision(box)) {

                        for (int j = 0; j < board.boxes.size(); j++) {

                            BoardObject item = board.boxes.get(j);

                            if (!box.equals(item)) {

                                if (box.isBottomCollision(item)) {
                                    return true;
                                }
                            }

                            if (checkWallCollision(box,BOTTOM_COLLISION)) {

                                return true;
                            }
                        }

                        board.changeObjPosition(box,0,1);
                        isCompleted();
                    }
                }

                break;

            default:
                break;
        }

        return false;
    }

    /**
     * Metoda pomocnicza sprawdzająca kolizję gracza z przeciwnikem
     * @param type określa kierunek ruchu
     * @return zwraca informację dotyczącą zderzenia gracza z przeciwnikiem
     */
    private boolean checkPlayerCollision(int type){
        BoardObject player = board.player;
        switch (type) {

            case LEFT_COLLISION:
                for(Evil monster : board.monsters)
                if (monster.isLeftCollision(player)) {

                    return true;
                }
                return false;

            case RIGHT_COLLISION:
                for(Evil monster : board.monsters)
                if (monster.isRightCollision(player)) {

                    return true;
                }
                return false;

            case TOP_COLLISION:
                for(Evil monster : board.monsters)
                if (monster.isTopCollision(player)) {

                    return true;
                }
                return false;
            case BOTTOM_COLLISION:
                for(Evil monster : board.monsters)
                if (monster.isBottomCollision(player)) {

                    return true;
                }
                return false;

            default:
                break;
        }

        return false;
    }

    /**
     * Metoda określająca wykonanie poziomu
     */
    public void isCompleted() {

        int nOfBags = board.boxes.size();
        int finishedBags = 0;

        for (int i = 0; i < nOfBags; i++) {

            BoardObject box = board.boxes.get(i);

            for (int j = 0; j < nOfBags; j++) {

                BoardObject target =  board.targets.get(j);

                if (box.row == target.row && box.column == target.column) {

                    finishedBags += 1;
                }
            }
        }

        if (finishedBags == nOfBags) {

            System.out.println("Congrats\n");
            level.hasWon = true;
            Sokoban.allSteps=Sokoban.allSteps+AppSettings.stepsCounter;
            Sokoban.numOfPassedLevels++;
            AppSettings.stepsCounter=-1;
            Sokoban.currentLevel++;
        }
    }

    /**
     * Metoda pozwalająca zrestartować dany poziom
     */
    private void restartLevel() {

        board.clearAll();
        board.initWorld();
        board.repaint();

        if (level.hasWon) {
            level.hasWon = false;
        }
    }

}

