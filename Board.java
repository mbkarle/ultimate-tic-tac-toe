import java.awt.*;

/**
 * Created by student on 10/2/17.
 */
public class Board {

    private Square[][] betaBoard = new Square[3][3];
    private int rowIndex;
    private int colIndex;
    Square.owner thisOwner = Square.owner.neutral;
    DrawMgr thisMgr;

    public Board(int row, int col){

        rowIndex = row;
        colIndex = col;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                betaBoard[i][j] = new Square(i, j, this);
            }
        }
    }

    public Square[][] getBetaBoard(){
        return betaBoard;
    }

    public int[] getIndices(){
        int[] ans = new int[2];
        ans[0] = colIndex;
        ans[1] = rowIndex;
        return ans;
    }

    public void check_win() {
        if (thisOwner.equals(Square.owner.neutral)) {
            for (int i = 0; i < 3; i++) {
                if (!betaBoard[i][1].getOwner().equals(Square.owner.neutral)) {
                    if (betaBoard[i][1].getOwner().equals(betaBoard[i][2].getOwner()) && betaBoard[i][1].getOwner().equals(betaBoard[i][0].getOwner())) {
                        thisOwner = betaBoard[i][1].getOwner();
                        thisMgr.connect_squares(betaBoard[i][2], betaBoard[i][0]);
                        System.out.println("Square belongs to " + thisOwner);
                        break;
                    }
                } else if (!betaBoard[1][i].getOwner().equals(Square.owner.neutral)) {
                    if (betaBoard[1][i].getOwner().equals(betaBoard[2][i].getOwner()) && betaBoard[1][i].getOwner().equals(betaBoard[0][i].getOwner())) {
                        thisOwner = betaBoard[1][i].getOwner();
                        thisMgr.connect_squares(betaBoard[2][i], betaBoard[0][i]);
                        System.out.println("Square belongs to " + thisOwner);
                        break;
                    }
                } else if (!betaBoard[1][1].getOwner().equals(Square.owner.neutral)) {
                    if (betaBoard[2][2].getOwner().equals(betaBoard[1][1].getOwner()) && betaBoard[0][0].getOwner().equals(betaBoard[1][1].getOwner())) {
                        thisOwner = betaBoard[1][1].getOwner();
                        thisMgr.connect_squares(betaBoard[2][2], betaBoard[0][0]);
                        System.out.println("Square belongs to " + thisOwner);
                        break;
                    }
                    else if(betaBoard[0][2].getOwner().equals(betaBoard[1][1].getOwner()) && betaBoard[2][0].getOwner().equals(betaBoard[1][1].getOwner())){
                        thisOwner = betaBoard[1][1].getOwner();
                        thisMgr.connect_squares(betaBoard[2][0], betaBoard[0][2]);
                        System.out.println("Square belongs to " + thisOwner);
                        break;
                    }
                }
            }
        }
    }

    public Square.owner getOwner(){
        return thisOwner;
    }

    public void setThisMgr(DrawMgr drawMgr){
        thisMgr = drawMgr;
    }
}
