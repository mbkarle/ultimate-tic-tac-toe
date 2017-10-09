package GameElements;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by student on 10/2/17.
 */
public class DrawMgr extends JPanel implements MouseListener{
    private Board[][] boardPrime = new Board[3][3];
    private ArrayList<Square[]> connectedSquares;
    private int boxWidth, boxHeight;
    private boolean player1Turn, paused;
    Point mouseCoords;
    ArrayList<Square> squaresList;
    ArrayList<Square> changedSquares;
    Board corresponding_board, last_corresponding;
    private Square.owner winner;
    private JButton pauseButton, undoButton;

   // Graphics2D graphics2D;


    public DrawMgr(){

        addMouseListener(this);
        setFocusable(true);
        initialize_game(); //add mouse listener and begin game!

        setLayout(null);   //add pause button

        pauseButton = new JButton("||");
        pauseButton.setBounds(5, 5, 20, 20);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(paused){
                    remove(undoButton);
                    revalidate();
                }
                paused = !paused;
                repaint();
            }
        });
        add(pauseButton);
        revalidate();

        undoButton = new JButton("Undo Last Move");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo_last_move();
            }
        });
    }

    public void paintComponent(Graphics g){ //handles all drawing
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for(Square square : changedSquares){
            Square.owner squareOwner = square.getOwner();
            if(squareOwner.equals(Square.owner.player1)){
                g2.drawImage(readImg("tictacX.png"), square.getCoords()[0], square.getCoords()[1], boxWidth / 3, boxHeight / 3, null);
            }
            else if(squareOwner.equals(Square.owner.player2)){
                g2.drawImage(readImg("tictacO.png"), square.getCoords()[0], square.getCoords()[1], boxWidth / 3, boxHeight / 3, null);
            }
            else{
                System.out.println("This square is neutral");
            }
        }
        drawBoard(g2);
        if(paused){
            drawPauseMenu(g2);
        }
    }

    public void initialize_game(){ //starts and resets game
        squaresList = new ArrayList<>();
        changedSquares = new ArrayList<>();
        connectedSquares = new ArrayList<>();
        winner = Square.owner.neutral;
        player1Turn = true;
        corresponding_board = null;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardPrime[i][j] = new Board(i, j);
                boardPrime[i][j].setThisMgr(this);
            }
        }

    }

    public void drawBoard(Graphics2D g2){ // draws board overlay
   //     graphics2D = g2;
        boxWidth = (int)Math.floor(getWidth() / 3);
        boxHeight = (int)Math.floor(getHeight() / 3);
        for(Board[] boards : boardPrime){
            for(Board board : boards){
                g2.setStroke(new BasicStroke(5));
                int boardX = board.getIndices()[0] * boxWidth;
                int boardY = board.getIndices()[1] * boxHeight;
                if(board.equals(corresponding_board)){
                    if(player1Turn){ //setting color based on player
                        g2.setColor(new Color(240, 105, 0, 127));
                    }
                    else{
                        g2.setColor(new Color(14, 0, 240, 127));
                    }

                    g2.fillRect(boardX, boardY, boxWidth, boxHeight);
                    g2.setColor(Color.black);
                }
                g2.drawRect(boardX, boardY, boxWidth, boxHeight);
                for(Square[] squares : board.getBetaBoard()){
                    for(Square square : squares){
                        int squareX = boardX + square.getIndices()[0] * boxWidth /3;
                        int squareY = boardY + square.getIndices()[1] * boxHeight / 3;
                        square.setCoords(new int[]{squareX, squareY});
                        g2.setStroke(new BasicStroke(1));
                        g2.drawRect(squareX, squareY, boxWidth / 3, boxHeight / 3);
                        if(squaresList.size() < 81){
                            squaresList.add(square);
                        }

                    }
                }
            }
        }
        for(Square[] squares : connectedSquares){ //connects any squares that were part of a winning three-in-a-row set
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.BLACK);
            int centerX = boxWidth / 6;
            int centerY = boxHeight / 6;
            g2.drawLine(squares[0].getCoords()[0] + centerX, squares[0].getCoords()[1] + centerY, squares[1].getCoords()[0] + centerX, squares[1].getCoords()[1] + centerY);
        }
        if(changedSquares.size() > 15){
            check_full_win(g2);
        }

    }

    public static BufferedImage readImg(String fileName){ //reads file to produce a buffered image
        BufferedImage img = null;
        String filePath = "/Users/student/IdeaProjects/Ultimate Tic Tac Toe/src/ImageAssets/" + fileName;
        File file = new File(filePath);
        try{
            img = ImageIO.read(file);
        }
        catch(IOException e){
            System.out.println(e);
        }
        return img;
    }

    public Square getSquare(Point point){ //returns the square which a given point is on; used for mouse location

        int[] offset = new int[]{getLocationOnScreen().x, getLocationOnScreen().y};
        int[] offset_coords = new int[]{point.x - offset[0], point.y - offset[1]};
        double x_divisor = getWidth() / 9;
        double y_divisor = getHeight() / 9;
        int[] potential_coords = new int[]{(int)(x_divisor * Math.floor(offset_coords[0] / x_divisor)), (int)(y_divisor * Math.floor(offset_coords[1] / y_divisor))};
        Square ans;
        int i = 0;
        for(Square square : squaresList){
            i++;
            if(Math.abs(square.getCoords()[0] - potential_coords[0]) < 10 && Math.abs(square.getCoords()[1] - potential_coords[1]) < 10){
                ans = square;
//                System.out.println("GameElements.Square's coords" + Arrays.toString(square.getCoords()));
//                System.out.println("Mouse rounded coords" + Arrays.toString(potential_coords));
//                System.out.println("We have a match");
//                System.out.println();
                return ans;
            }
            else if(i == squaresList.size()){
                System.out.println("GameElements.Square's coords" + Arrays.toString(square.getCoords()));
                System.out.println("Mouse rounded coords" + Arrays.toString(potential_coords));
                System.out.println("no match");
            }
        }
        return new Square(0, 0, new Board(0, 0));
    }

    public void check_full_win(Graphics2D g2){
        for (int i = 0; i < 3; i++) {
            if (!boardPrime[i][1].getOwner().equals(Square.owner.neutral)) { //checks each square in 2nd column
                if (boardPrime[i][1].getOwner().equals(boardPrime[i][2].getOwner()) && boardPrime[i][1].getOwner().equals(boardPrime[i][0].getOwner())) {
                    winner = boardPrime[i][1].getOwner();
                    System.out.println("This mans won " + winner);
                    break;
                }
            }
            if (!boardPrime[1][i].getOwner().equals(Square.owner.neutral)) { //checks each square in 2nd row
                if (boardPrime[1][i].getOwner().equals(boardPrime[2][i].getOwner()) && boardPrime[1][i].getOwner().equals(boardPrime[0][i].getOwner())) {
                    winner = boardPrime[1][i].getOwner();
                    System.out.println("This mans won " + winner);
                    break;
                }
            }
            if (!boardPrime[1][1].getOwner().equals(Square.owner.neutral)) { //checks diagonals
                if (boardPrime[2][2].getOwner().equals(boardPrime[1][1].getOwner()) && boardPrime[0][0].getOwner().equals(boardPrime[1][1].getOwner()) || boardPrime[0][2].getOwner().equals(boardPrime[1][1].getOwner()) && boardPrime[2][0].getOwner().equals(boardPrime[1][1].getOwner())) {
                    winner = boardPrime[1][1].getOwner();
                    System.out.println("This mans won " + winner);
                    break;
                }
            }
        }
        g2.setFont(new Font("TimesRoman", Font.BOLD, 48));
        if(changedSquares.size() > 16){
            if(winner.equals(Square.owner.player1)){
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);
                drawCenteredString(g2, "Player 1 wins!", 48, this);

            }
            else if(winner.equals(Square.owner.player2)){
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);
                drawCenteredString(g2, "Player 2 wins!", 48, this);

            }
        }

    }

    public void connect_squares(Square squareA, Square squareB){
        Square[] ans = new Square[]{squareA, squareB};
        connectedSquares.add(ans);

    }

    public void drawPauseMenu(Graphics2D g2){
        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setFont(new Font("TimesRoman", Font.PLAIN, 36));
        g2.setColor(Color.BLACK);
        drawCenteredString(g2, "Game Paused", 36, this);
        int buttonWidth = getWidth() / 4;
        int buttonHeight = getHeight() / 18;
        undoButton.setBounds(getWidth() / 2 - buttonWidth/2, getHeight() / 2, buttonWidth, buttonHeight);
        add(undoButton);
        revalidate();
        //g2.drawRect(undoButton.getX(), undoButton.getY(), undoButton.getWidth(), undoButton.getHeight());
    }

    public void undo_last_move(){
        if(changedSquares.size() > 0){
            player1Turn = !player1Turn;
            Square last_move = changedSquares.get(changedSquares.size() - 1);
            if(connectedSquares.size() > 0){ //check if square removed was part of winning three

                Square[] last_connected = connectedSquares.get(connectedSquares.size() - 1);
                int[] coordsBetween = {last_connected[1].getCoords()[0] - last_connected[0].getCoords()[0], last_connected[1].getCoords()[1] - last_connected[0].getCoords()[1]};
                if(last_move.equals(last_connected[0]) || last_move.equals(last_connected[1]) || last_move.equals(getSquare(new Point(coordsBetween[0], coordsBetween[1])))){
                    connectedSquares.remove(last_connected);
                    last_move.getParentBoard().setThisOwner(Square.owner.neutral);
                }

            }
            last_move.setOwner(Square.owner.neutral);
            corresponding_board = last_corresponding;
            changedSquares.remove(last_move);
            repaint();
        }

    }

    //helpful static methods for formatting GUI
    public static String join(String separator, String[] stringArray){ //why the nut is this not a default method of strings
        String ans = "";
        for(int i = 0; i < stringArray.length; i++){
            ans += stringArray[i];
            if(i < stringArray.length - 1){
                ans += separator;
            }
        }
        return ans;
    }

    public static void drawCenteredString(Graphics g, String string, int fontSize, JPanel panel){
        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        if(string.length() > fontSize * fontSize / 20){ //wraps text
            String[] stringArray = string.split(" ");
            String[] stringArray1 = Arrays.copyOfRange(stringArray, 0, stringArray.length / 2);
            String[] stringArray2 = Arrays.copyOfRange(stringArray, stringArray.length / 2, stringArray.length);
            g.drawString(DrawMgr.join(" ", stringArray1), panel.getWidth() / 2 - fontSize/9 * (string.length() + 30), panel.getHeight()/2);
            g.drawString(DrawMgr.join(" ", stringArray2), panel.getWidth() / 2 - fontSize/9 * (string.length() + 30), panel.getHeight()/2 + fontSize * 2);
        }
        else{
            g.drawString(string, panel.getWidth() / 2 - fontSize/10 * string.length(), panel.getHeight() / 2);
        }
    }

    public static ArrayList<Component> getAllComponents(final Container c) { //mostly for debugging
        Component[] comps = c.getComponents();
        ArrayList<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!winner.equals(Square.owner.neutral)){
            initialize_game();
            repaint();
        }
        else if(!paused){
            mouseCoords = MouseInfo.getPointerInfo().getLocation();
            Square.owner curr_owner = Square.owner.neutral;
            if(player1Turn){
                curr_owner = Square.owner.player1;
            }
            else{
                curr_owner = Square.owner.player2;
            }
            Square squareClicked = getSquare(mouseCoords);
            if(squareClicked.getOwner().equals(Square.owner.neutral) && squareClicked.getParentBoard().equals(corresponding_board) || changedSquares.size() == 0){
                squareClicked.setOwner(curr_owner);
                changedSquares.add(squareClicked);
                if((changedSquares.size() > 1)){
                    last_corresponding = corresponding_board;
                    corresponding_board.check_win();
                }
                corresponding_board = boardPrime[squareClicked.getIndices()[1]][squareClicked.getIndices()[0]];
                repaint();

                player1Turn = !player1Turn;
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
