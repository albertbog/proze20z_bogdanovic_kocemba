package com.sokoban;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.lang.Math.abs;

/**Klasa odpowiedzialna za planszę*/
public class Drawer {

    /**Okno, w którym są rysowane elementy menu gry i plansza*/
    private final MainWindow mainW;
    /**Obecny załadowany poziom*/
    Level level;
    /**Plansza ilustrująca załadowany poziom*/
    Board board;
    /**Menu zawierające podstawowe informcje bądź funkcjonalności procesu gry (Gra)*/
    Sokoban soko;


    /**Przypisuje podstawowe objekty na rzecz inicjacji głównego menu z planszą gry
     * @param soko menu gry
     * @param mainWindow okno główne
     * @param lvl obecny poziom
     */
    Drawer(Sokoban soko,MainWindow mainWindow, Level lvl) {
        this.mainW = mainWindow;
        this.level= lvl;
        this.soko=soko;


    }
    /** wywołuje metodę inicjującą planszę
     * @return panel planszy
     * */
    public JPanel draw() {
        return draw_grid();
    }
    /** inicjuje planszę i dodaje ją do podstawy
     * @return zwraca podstawę na której jest ilustrowana plansza
     * */
    public BoardBase draw_grid() {
        BoardBase boardBase = new BoardBase();
        board=new Board();
        board.start();
        boardBase.add(board);
        return boardBase;
    }

    /**Klasa wewnętrzna odpowiedzialna za animację/ilustrowanie planszy gry*/
    class Board extends Canvas implements ActionListener {
        /**szerokość planszy*/
        int width;
        /**wysokość planszy*/
        int height;
        /**liczba wierszy planszy*/
        int rows;
        /**liczba kolumn planszy*/
        int columns;
        /**tablica przechowująca obiekty ilustrowane na plaszy */
        ArrayList<BoardObject> world;
        /**tablica przechowująca obiekty typu ściana*/
        ArrayList<BoardObject> walls;
        /**tablica przechowująca obiekty typu pudełka*/
        ArrayList<BoardObject> boxes;
        /**tablica przechowująca obiekty typu cel*/
        ArrayList<BoardObject> targets;
        /**tablica przechowująca obiekty typu zgredek*/
        ArrayList<Evil> monsters;
        /**obiekt głównego bohatera*/
        BoardObject player;
        /**obiekt zawierający ilustrację mapy ze ścianami i celami stworzony w celu zmniejszenia nakładu rysowania poszczególnych ścian i celi*/
        BoardObject map;
        /** obiekt slużący do animacji */
        BufferStrategy bs = null;
        /** Informuje czy bohater został poruszony */
        public boolean playerMoved =false;
        /**referencja na pudełko przesuwane*/
        BoardObject box;
        /** Informuje czy pudełoko przez bohatera zostało poruszone */
        public boolean boxMoved =false;
        /** predkosc w plaszczyznie x */
        int velocityX=0;
        /** predkosc w plaszczyznie y */
        int velocityY=0;
        /** przyśpieszenie w plaszczyznie x */
        int increaseVelocityX;
        /** przyśpieszenie w plaszczyznie y */
        int increaseVelocityY;
        /** Licznik odpowiedzialny za ruch animacji */
        Timer timer = new Timer(100,this);



        /**Inicjuje planszę, jej wymiary, nasłuchiwacz ruchu bohatera
         */
        Board() {
            setSize(width = mainW.getWidth()/2, height = mainW.getHeight()/2);
            rows = level.nRows;
            columns = level.nColuns;
            addKeyListener(new TAdapter(this,level));


        }

    /**Metoda odpowiedzialna za renderowanie/animację
     * Zmienia pozycję bohatera wraz z pudełkiem w wyniku czego powstaje animacja. Sprawdza, czy gracz osiągnął koniec poziomu i
     * wywołuje metodę wywołującą kolejny poziom.
     * @param e parametr nasłuchujący zdarzenie. W tym przypadku zdarzeniem jest zmiana stanu licznika
     * w parze z informacją o stanie ruchu bohatera
     */
        @Override
        public void actionPerformed(ActionEvent e) {
            bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(2);
                return;
            }
            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            for(Evil monster:monsters) {
                monster.moveMonster(monster);
            }
            draw(g);
            bs.show();


            if(playerMoved) {
                int endX = (int) (player.point2D.getX() + (velocityX * board.width / board.columns));
                int endY = (int) (player.point2D.getY() + (velocityY * board.height / board.rows));

                int semiEndX =endX-(abs(((int)player.point2D.getX()-endX))%increaseVelocityX)*velocityX;
                int semiEndY =endY-(abs(((int)player.point2D.getY()-endY))%increaseVelocityY)*velocityY;


                while (player.point2D.getX() != semiEndX || player.point2D.getY() != semiEndY) {

                    player.point2D = new Point2D.Double(velocityX*increaseVelocityX + player.point2D.getX(), velocityY*increaseVelocityY + player.point2D.getY());
                    if(boxMoved)
                    {
                        box.point2D =  new Point2D.Double(velocityX*increaseVelocityX + box.point2D.getX(), velocityY*increaseVelocityY + box.point2D.getY());
                    }
                    for(Evil monster:monsters) {
                        monster.moveMonster(monster);
                    }

                    draw(g);
                    bs.show();
                }

                player.point2D = new Point2D.Double(endX, endY);
                if(boxMoved)
                {
                    box.point2D =  new Point2D.Double(endX-semiEndX + box.point2D.getX(), endY-semiEndY + box.point2D.getY());
                }
                for(Evil monster:monsters) {
                    monster.moveMonster(monster);
                }
                draw(g);
                bs.show();

                draw(g);

                bs.show();
            }
            if(level.hasWon)
            {
                level.hasWon=false;
                timer.stop();
                JOptionPane.showMessageDialog(this, "move to the next level","Congrats",1);
                soko.removeAll();
                soko.menuPanel.removeAll();
                soko.loadNextLevel();
            }
            playerMoved =false;
            boxMoved=false;
        }




        /**Inicjuje tekstury planszy z bohaterem oraz rozpoczyna licznik
         */
        public void start() {

            initWorld();
            timer.start();

        }
        /**Metoda odpowiedzialna za rysowanie obiektów planszy: mapy, pudeł i bohatera
         * @param g pole graficzne planszy
         * */
        public void draw(Graphics2D g) {

            g.drawImage(resize(map.image, board.getWidth(), board.getHeight()), (int) map.point2D.getX(), (int) map.point2D.getY(), this);
            player.draw(g,board);
            for (BoardObject actor : boxes)
                g.drawImage(board.resize(actor.image, board.getWidth() / board.columns, board.getHeight() / board.rows), (int) actor.point2D.getX(), (int) actor.point2D.getY(), null);
            for (Evil actor : monsters)
                g.drawImage(board.resize(actor.image, board.getWidth() / board.columns, board.getHeight() / board.rows), (int) actor.point2D.getX(), (int) actor.point2D.getY(), null);


        }
        /**Metoda odpowiedzialna za modyfikację pozycji bohatera i pudła
         * @param obj objekt którego pozycja jest modyfikowana
         * @param x kierunek modyfikacji na osi x
         * @param y kierunek modyfikacji na osi y
         */
        void changeObjPosition(BoardObject obj,int x, int y){
            if(obj.type=="Player"){
                obj.column=obj.column+x;
                obj.row=obj.row+y;
                velocityX=x;
                velocityY=y;
                playerMoved =true;

            }
            if(obj.type=="Box1")
            {

                obj.column=obj.column+x;
                obj.row=obj.row+y;
                box=obj;
                board.boxMoved=true;
            }

        };




        /**metoda nadpisana odpowiedzialna za ustawienie preferowanych rozmiarów planszy
         */
        @Override public Dimension getPreferredSize() {
            System.out.println("Wywolano getPreferredSize");
            return new Dimension(mainW.getWidth()/2, mainW.getHeight()/2);
        }

        /**metoda odpowiedzialna za rysowanie(przerysowywanie) planszy.
         * @param g pole graficzne planszy
         */
        @Override
        public void paint(Graphics g) {
            width = getSize().width;
            height = getSize().height;
            for(Evil monster: monsters)
            {
                monster.endX = (monster.column* width/columns );
                monster.endY =  (monster.row* height/rows );
                monster.point2D = new Point2D.Double(monster.endX, monster.endY);
                monster.increaseVelocityX=AppSettings.getInstance().increaseVelocity*board.width/AppSettings.getInstance().ScreenWidth();
                monster.increaseVelocityY=AppSettings.getInstance().increaseVelocity*board.height/AppSettings.getInstance().ScreenHeight();
            }
            increaseVelocityX=AppSettings.getInstance().increaseVelocity*2*board.width/AppSettings.getInstance().ScreenWidth();
            increaseVelocityY=AppSettings.getInstance().increaseVelocity*2*board.height/AppSettings.getInstance().ScreenHeight();
            playerMoved=false;
            boxMoved=false;
            updateWorld(g);
            if(!level.isPaused){timer.restart();}





        }
        /**inicjuje objekty rysowane na planszy*/
        public void initWorld() {

            walls = new ArrayList<>();
            boxes = new ArrayList<>();
            targets = new ArrayList<>();
            monsters = new ArrayList<>();
            world = new ArrayList<>();


            for (int r = 0; r < rows; r++)
                for (int c=0; c < columns; c++) {

                    char item = level.getMap().get(r).charAt(c);

                    switch (item) {

                        case '#':
                            walls.add(new BoardObject(AppSettings.getInstance().getLocalTexture(AppSettings.Textures.Wall),
                                    new Point2D.Float(c*getWidth()/columns,r*getHeight()/rows),AppSettings.Textures.Wall.name(),r,c));
                            break;

                        case '$':
                            boxes.add(new BoardObject(AppSettings.getInstance().getLocalTexture(AppSettings.Textures.Box1),
                                    new Point2D.Float(c*getWidth()/columns,r*getHeight()/rows),AppSettings.Textures.Box1.name(),r,c));
                            break;

                        case '.':
                            targets.add(new BoardObject(AppSettings.getInstance().getLocalTexture(AppSettings.Textures.Target),
                                    new Point2D.Float(c*getWidth()/columns,r*getHeight()/rows),AppSettings.Textures.Target.name(),r,c));
                            break;

                        case '@':
                            player=new BoardObject(AppSettings.getInstance().getLocalAvatar(Sokoban.choosenAvatarInd),
                                    new Point2D.Float(c*getWidth()/columns,r*getHeight()/rows),"Player",r,c);

                            break;

                        case '^':

                                monsters.add(new Evil(AppSettings.getInstance().getLocalTexture(AppSettings.Textures.Evil),
                                        new Point2D.Float(c*getWidth()/columns,r*getHeight()/rows),AppSettings.Textures.Evil.name(),r,c,board));

                            break;


                        default:
                            break;
                    }
                }

            world.addAll(walls);
            world.addAll(targets);
            world.addAll(boxes);
            world.addAll(monsters);
            world.add(player);

            BufferedImage img = new BufferedImage(board.getWidth(),board.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D mapG = img.createGraphics();
            mapG.fillRect(0,0,board.getWidth(),board.getHeight());
            for(int i = 0; i < walls.size()+ targets.size(); i++) {
                BoardObject item = world.get(i);
                item.point2D = new Point2D.Float(item.column * getWidth() / columns, item.row * getHeight() / rows);
                mapG.drawImage(resize(item.image, getWidth() / columns, getHeight() / rows), (int) item.point2D.getX(), (int) item.point2D.getY(), this);
            }

            map = new BoardObject(img,new Point2D.Double(0,0),"map", 0,0);


        }
        /**odnawia rozmiary objektów rysowanych na planszy i je przerysowuje*
         * @param g pole graficzne planszy
         */

        public void updateWorld(Graphics g) {

            g.drawImage(resize(map.image, board.getWidth(), board.getHeight()), (int) map.point2D.getX(), (int) map.point2D.getY(), this);

            for (int i = walls.size()+ targets.size(); i < world.size(); i++) {
                BoardObject item = world.get(i);
                item.point2D = new Point2D.Float(item.column * getWidth() / columns, item.row * getHeight() / rows);
                g.drawImage(resize(item.image, getWidth() / columns, getHeight() / rows), (int) item.point2D.getX(), (int) item.point2D.getY(), this);

            }

        }
        /**metoda odpowiedzialna za zmianę wymiarów rysowanych objektów planszy
         * @param img przrysowywany obrazek
         * @param newW nowa szerokość obrazka
         * @param newH nowa wysokość obrazka
         * @return metoda zwraca wyskalowany obraz elementów planszy
         */
        public Image resize(BufferedImage img, int newW, int newH) {
            Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            return tmp;
        }
        /**usuwa wszystkie objekty planszy z kontenerów*/
        public void clearAll(){

            walls.clear();
            boxes.clear();
            targets.clear();
            monsters.clear();
            world.clear();
        }

    }

    /**klasa odpowiedzialna za bazę na której jest rozmieszczona plansza*/
    class BoardBase extends JPanel {

        /** metoda nadpisana zwracająca minimalne wymiary bazy planszy*/
           @Override
        public Dimension getMinimumSize() {
            return new Dimension((level.nColuns/ level.nRows)*mainW.getWidth()/2,(level.nColuns/ level.nRows)*mainW.getHeight()/2);
        }
        /** metoda nadpisana zwracająca maksymalne wymiary bazy planszy*/
        @Override
        public Dimension getMaximumSize() {
            return new Dimension((level.nColuns/ level.nRows)*mainW.getWidth()/2,(level.nColuns/ level.nRows)*mainW.getHeight()/2);
        }
        /** metoda nadpisana zwracająca preferowane wymiary bazy planszy*/
        @Override
        public Dimension getPreferredSize() {
            return new Dimension((level.nColuns/ level.nRows)*mainW.getWidth()/2,(level.nColuns/ level.nRows)*mainW.getHeight()/2);
        }

    }

}





