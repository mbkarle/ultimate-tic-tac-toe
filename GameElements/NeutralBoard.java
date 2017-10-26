package GameElements;

import java.awt.*;

/**
 * Created by student on 10/24/17.
 */
public class NeutralBoard extends Board {

    private DrawMgr parent;
    private int width, height, x, y;
    private boolean active;

    public NeutralBoard(int row, int col, DrawMgr parent) {
        super(row, col);
        this.parent = parent;
        active = false;
        constructorName = "NeutralBoard";
    }

    public void draw(Graphics2D g2){
        width = parent.getWidth() / 3;
        height = parent.getHeight() / 3;
        x = parent.getWidth() / 2 - width/2;
        y = parent.getHeight() / 2 - height/2;
        Color defaultColor = g2.getColor();
        g2.setColor(new Color(166, 166, 166, 220));
        g2.fillRect(0, 0, parent.getWidth(), parent.getHeight());
        g2.setColor(defaultColor);
        g2.drawRect(x, y, width, height);
        for(Square[] squares : getBetaBoard()){
            for(Square square : squares){
                int squareX = x + width * square.getIndices()[0]/3;
                int squareY = y + height * square.getIndices()[1]/3;
                g2.drawRect(squareX, squareY, width/3, height/3);
                if(square.getOwner().equals(Square.owner.player1)){
                    g2.drawImage(DrawMgr.readImg("tictacX.png"), squareX, squareY, width/3, height/3, null);
                }
                else if(square.getOwner().equals(Square.owner.player2)){
                    g2.drawImage(DrawMgr.readImg("tictacO.png"), squareX, squareY, width/3, height/3, null);
                }
            }
        }
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean status){
        active = status;
    }

    public Square getSquare(Point point){
        int[] offset = new int[]{parent.getLocationOnScreen().x, parent.getLocationOnScreen().y};
        int[] offset_coords = {point.x - offset[0] - parent.getWidth()/3, point.y - offset[1] - parent.getHeight()/3};
        int[] divisors = {parent.getWidth() / 9, parent.getHeight() / 9};
        int[] potential_indices = {offset_coords[0] / divisors[0], offset_coords[1] / divisors[1]};
        return getBetaBoard()[potential_indices[1]][potential_indices[0]];
    }

    public Square getRandSquare(){
        int row = (int)Math.floor(Math.random() * getBetaBoard().length);
        int col = (int)Math.floor(Math.random() * getBetaBoard()[0].length);
        return getBetaBoard()[row][col];
    }


}
