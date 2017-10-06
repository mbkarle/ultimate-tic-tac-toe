

/**
 * Created by student on 10/2/17.
 */
public class Square {




    public enum owner{
        player1, player2, neutral;
    }

    private int rowIndex, colIndex, pixX, pixY;
    private owner thisOwner;
    private Board parentBoard;

    public Square(int row, int col, Board parentBoard){
        thisOwner = owner.neutral;
        rowIndex = row;
        colIndex = col;
        this.parentBoard = parentBoard;

    }

    public owner getOwner(){
        return thisOwner;
    }

    public void setOwner(owner thisOwner){
        this.thisOwner = thisOwner;
    }

    public int[] getIndices(){
        int[] ans = new int[2];
        ans[0] = colIndex;
        ans[1] = rowIndex;
        return ans;
    }

    public int[] getCoords(){
        int[] ans = new int[]{pixX, pixY};
        return ans;
    }

    public void setCoords(int[] coords){
        pixX = coords[0];
        pixY = coords[1];
    }

    public Board getParentBoard(){
        return parentBoard;
    }

}
