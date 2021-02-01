package com.sokoban;


import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;


/**klasa dziedzicząca po BoardObject odpowiedzialna za przeciwników*/
public class Evil extends BoardObject {


    /**
     * Określa czy przeciwnik się poruszył
     */
    private boolean monsterMoved =false;
    /**Plansza ilustrująca załadowany poziom*/
     Drawer.Board board;

    /**
     * predkosc w plaszczyznie x
     */
     int velocityX;
    /**
     * predkosc w plaszczyznie y
     */
    int velocityY;
    /**
     * przyśpieszenie w plaszczyznie x
     */
    public  int increaseVelocityX;
    /**
     * przyśpieszenie w plaszczyznie y
     */
    public int increaseVelocityY ;

    /**
     * Określa końcowy punkt X podczas poruszania się przeciwnika
     */
    int endX;
    /**
     * Określa końcowy punkt Y podczas poruszania się przeciwnika
     */
    int endY;

    EAdapter adapter;

    /**
     * Inicjuje atrybuty objektu planszy
     * @param a obrazek obiektu
     * @param b połozenie górne w płaszczyźnie x i y
     * @param c nazwa obiektu
     * @param row położenie w wierszu
     * @param column położenie w kolumnie
     * @param board plansza ilustrująca załadowany poziom
     */
    Evil(BufferedImage a, Point2D b, String c, int row, int column, Drawer.Board board) {
        super(a, b, c, row, column);
        this.board=board;
        endX=(int)this.point2D.getX();
        endY=(int)this.point2D.getY();
        adapter = new EAdapter(this, board);


    }


    /**
     * Metoda umożliwiająca losowy ruch przeciwników na mapie
     */
    void move(){
    int[] keys = new int[]{KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,KeyEvent.VK_UP, KeyEvent.VK_DOWN }; // Your list of KeyEvent character constants here, adapt as desired.
// Start optional for loop here if you want more than 1 random character
    int randomValue = ThreadLocalRandom.current().nextInt(0, keys.length);
    adapter.keyPressed(randomValue);


}
    /**
     * Metoda umożliwiająca losowy ruch przeciwników na mapie
     * @param obj obiekt, na rzecz którego jest wywoływana metoda
     * @param x przesunięcie w osi X
     * @param y przesunięcie w osi Y
     */
    void changeObjPosition(BoardObject obj,int x, int y){
            this.column=obj.column+x;
            this.row=obj.row+y;
            this.velocityX=x;
            this.velocityY=y;


    };

    /**
     * Metoda przesuwająca potwora do odpowiedniego punktu
     * @param monster obiekt, na rzecz którego jest wywoływana metoda
     */
     void moveMonster(Evil monster) {


             if((int)monster.point2D.getX()==monster.endX &&(int)monster.point2D.getY()==monster.endY)
             {monster.move();
             monster.endX = (int) (monster.point2D.getX() + (monster.velocityX * board.width / board.columns));
             monster.endY = (int) (monster.point2D.getY() + (monster.velocityY * board.height / board.rows));}



                 monster.point2D = new Point2D.Double(monster.velocityX * increaseVelocityX + monster.point2D.getX(), velocityY * increaseVelocityY + monster.point2D.getY());
                 if(abs(monster.point2D.getX()-monster.endX)<monster.increaseVelocityX && abs(monster.point2D.getY()-monster.endY)<monster.increaseVelocityY) {
                     monster.point2D = new Point2D.Double(endX , endY);
                     monster.velocityX=0;
                     monster.velocityY=0;
                 }


         }
     }





/**
 * Klasa odpowiedzialna za sprawdzanie poprawności ruchu przeciwników, odpowiada za mechanikę ruchu
 */

class EAdapter {

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
    /**
     * Przypisanie przemieszeniu w lewą stronę wartości int
     */
    private final int VK_LEFT =0;
    /**
     * Przypisanie przemieszeniu w prawą stronę wartości int
     */
    private final int VK_RIGHT =1;
    /**
     * Przypisanie przemieszeniu do góry wartości int
     */
    private final int VK_UP =2;
    /**
     * Przypisanie przemieszeniu do dołu wartości int
     */
    private final int VK_DOWN =3;
    /**Zmienna określająca potwora*/
    Evil monster;
    /**Plansza ilustrująca załadowany poziom*/
    Drawer.Board board;

    /**Przypisuje podstawowe obiekty na rzecz inicjacji odpowiednich zmiennych
     * @param moster zmienna określająca potwora
     * @param board plansza ilustrująca załadowany poziom
     */
    EAdapter(Evil moster, Drawer.Board board){this.monster=moster;
        this.board = board;
    }

    /**
     * Metoda sprawdzająca poprawność ruchu przeciwników (kolizję ze ścianą, z blokami oraz graczem) oraz określająca mechanikę gry
     * @param key zmienna określająca kierunek ruchu
     */
    public void keyPressed(int key) {

            switch (key) {

                case VK_LEFT:
                    if(checkPlayerCollision(LEFT_COLLISION)){
                        Sokoban.lifesCounter--;
                    }

                    if (checkWallCollision(monster,
                            LEFT_COLLISION)) {
                        return;
                    }

                    if (checkBagCollision(LEFT_COLLISION)) {
                        return;
                    }

                    monster.changeObjPosition(monster,-1,0);



                    break;

                case VK_RIGHT:
                    if(checkPlayerCollision(RIGHT_COLLISION)){
                        Sokoban.lifesCounter--;
                    }
                    if (checkWallCollision(monster,
                            RIGHT_COLLISION)) {
                        return;
                    }

                    if (checkBagCollision(RIGHT_COLLISION)) {
                        return;
                    }

                    monster.changeObjPosition(monster,1,0);


                    break;

                case VK_UP:
                    if(checkPlayerCollision(TOP_COLLISION)){
                        Sokoban.lifesCounter--;
                    }

                    if (checkWallCollision(monster, TOP_COLLISION)) {
                        return;
                    }

                    if (checkBagCollision(TOP_COLLISION)) {
                        return;
                    }


                    monster.changeObjPosition(monster,0,-1);


                    break;

                case VK_DOWN:
                    if(checkPlayerCollision(BOTTOM_COLLISION)){
                        Sokoban.lifesCounter--;
                    }

                    if (checkWallCollision(monster, BOTTOM_COLLISION)) {
                        return;
                    }

                    if (checkBagCollision(BOTTOM_COLLISION)) {
                        return;
                    }

                    monster.changeObjPosition(monster,0,1);
                    break;


                default:
                    break;
            }

    }

    /**
     * Metoda pomocnicza sprawdzająca kolizję ze ścianą
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
     * Metoda pomocnicza sprawdzająca kolizję ze skrzynką
     * @param type określa kierunek ruchu
     * @return zwraca informację dotyczącą zderzenia ze ścianą
     */
    private boolean checkBagCollision(int type) {


        switch (type) {

            case LEFT_COLLISION:

                for (int i = 0; i < board.boxes.size(); i++) {

                    BoardObject box = board.boxes.get(i);

                    if (monster.isLeftCollision(box)) {

                                    return true;
                                }
                        }
                return false;

            case RIGHT_COLLISION:

                for (int i = 0; i < board.boxes.size(); i++) {

                    BoardObject box = board.boxes.get(i);

                    if (monster.isRightCollision(box)) {

                        return true;
                    }
                }
                return false;

            case TOP_COLLISION:
                for (int i = 0; i < board.boxes.size(); i++) {

                    BoardObject box = board.boxes.get(i);

                    if (monster.isTopCollision(box)) {

                        return true;
                    }
                }
                return false;

            case BOTTOM_COLLISION:

                for (int i = 0; i < board.boxes.size(); i++) {

                    BoardObject box = board.boxes.get(i);

                    if (monster.isBottomCollision(box)) {

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
     * Metoda pomocnicza sprawdzająca kolizję z graczem
     * @param type określa kierunek ruchu
     * @return zwraca informację dotyczącą zderzenia ze ścianą
     */
    private boolean checkPlayerCollision(int type){
        BoardObject player = board.player;
        switch (type) {

            case LEFT_COLLISION:

                    if (monster.isLeftCollision(player)) {

                        return true;
                    }
                return false;

            case RIGHT_COLLISION:

                if (monster.isRightCollision(player)) {

                    return true;
                }
                return false;

            case TOP_COLLISION:
                if (monster.isTopCollision(player)) {

                    return true;
                }
                return false;
            case BOTTOM_COLLISION:

                if (monster.isBottomCollision(player)) {

                    return true;
                }
                return false;

            default:
                break;
        }

        return false;
    }

}