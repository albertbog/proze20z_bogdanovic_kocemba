package com.sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
    /**
     * Klasa odpowiedzialna za obiekty planszy
     */

    class BoardObject extends JLabel {
        /**Obrazek objektu*/
        BufferedImage image;
        /** polozenie górne w plaszczyxnie x i y*/
        Point2D point2D;
        /** położenie w wierszu*/
        int row;
        /** położenie w kolumnie*/
        int column;
        /** typ/nazwa objektu*/
        String type;
        /** Inicjuje atrybuty objektu planszy
         * @param a obrazek obiektu
         * @param b połozenie górne w płaszczyźnie x i y
         * @param c nazwa obiektu
         * @param row położenie w wierszu
         * @param column położenie w kolumnie
         */
        BoardObject(BufferedImage a, Point2D b,String c, int row, int column)
        {

            image=a;
            point2D=b;
            type=c;
            this.row=row;
            this.column=column;

        }

        /** Metoda odpowiedzialna za rysowanie objektu na planszy
         * @param g pole graficzne planszy
         * @param board plansza na której obiekt ma być narysowany
         * */
        public void draw(Graphics2D g,Drawer.Board board) {
            if (image != null)
                g.drawImage(board.resize(image, board.getWidth() / board.columns, board.getHeight() / board.rows), (int) point2D.getX(), (int) point2D.getY(), null);
        }
        /** metoda sprawdzająca kolizję po lewej stronie od objektu planszy
         * @param obj objekt z którym jest porównywana kolizja
         * @return zwraca wynik sprawdzenia kolizji
         * */
        public boolean isLeftCollision(BoardObject obj) {

            return this.column - 1 == obj.column && this.row == obj.row;
        }
        /** metoda sprawdzająca kolizję po prawej stronie od objektu planszy
         * @param obj objekt z którym jest porównywana kolizja
         *             @return zwraca wynik sprawdzenia kolizji
         * */
        public boolean isRightCollision(BoardObject obj) {

            return this.column + 1 == obj.column && this.row == obj.row;
        }
        /** metoda sprawdzająca kolizję u góry od objektu planszy
         * @param obj objekt z którym jest porównywana kolizja
         *             @return zwraca wynik sprawdzenia kolizji
         * */
        public boolean isTopCollision(BoardObject obj) {

            return this.row - 1 == obj.row && this.column == obj.column;
        }
        /** metoda sprawdzająca kolizję u dołu od objektu planszy
         * @param obj objekt z którym jest porównywana kolizja
         *             @return zwraca wynik sprawdzenia kolizji
         * */
        public boolean isBottomCollision(BoardObject obj) {

            return this.row + 1 == obj.row && this.column == obj.column;
        }

    }
