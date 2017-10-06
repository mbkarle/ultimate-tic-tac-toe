import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
    private boolean player1Turn;
    Point mouseCoords;
    ArrayList<Square> squaresList;
    ArrayList<Square> changedSquares;
    Board corresponding_board;
    private Square.owner winner;
   // Graphics2D graphics2D;


    public DrawMgr(){

        addMouseListener(this);
        setFocusable(true);
        initialize_game();
    }

    public void paintComponent(Graphics g){
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
        for(Square[] squares : connectedSquares){
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

    public Square getSquare(Point point){

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
//                System.out.println("Square's coords" + Arrays.toString(square.getCoords()));
//                System.out.println("Mouse rounded coords" + Arrays.toString(potential_coords));
//                System.out.println("We have a match");
//                System.out.println();
                return ans;
            }
            else if(i == squaresList.size()){
                System.out.println("Square's coords" + Arrays.toString(square.getCoords()));
                System.out.println("Mouse rounded coords" + Arrays.toString(potential_coords));
                System.out.println("no match");
            }
        }
        return new Square(0, 0, new Board(0, 0));
    }

    public void check_full_win(Graphics2D g2){
        for (int i = 0; i < 3; i++) {
            if (!boardPrime[i][1].getOwner().equals(Square.owner.neutral)) {
                if (boardPrime[i][1].getOwner().equals(boardPrime[i][2].getOwner()) && boardPrime[i][1].getOwner().equals(boardPrime[i][0].getOwner())) {
                    winner = boardPrime[i][1].getOwner();
                    System.out.println("This mans won " + winner);
                    break;
                }
            } else if (!boardPrime[1][i].getOwner().equals(Square.owner.neutral)) {
                if (boardPrime[1][i].getOwner().equals(boardPrime[2][i].getOwner()) && boardPrime[1][i].getOwner().equals(boardPrime[0][i].getOwner())) {
                    winner = boardPrime[1][i].getOwner();
                    System.out.println("This mans won " + winner);
                    break;
                }
            } else if (!boardPrime[1][1].getOwner().equals(Square.owner.neutral)) {
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
                g2.drawString("Player 1 wins!", getWidth() / 2 - 60, getHeight() / 2);

            }
            else if(winner.equals(Square.owner.player2)){
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);
                g2.drawString("Player 2 wins!", getWidth() / 2 - 60, getHeight() / 2);

            }
        }

    }

    public void connect_squares(Square squareA, Square squareB){
        Square[] ans = new Square[]{squareA, squareB};
        connectedSquares.add(ans);

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
        else{
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
