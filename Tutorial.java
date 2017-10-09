import GameElements.DrawMgr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by student on 10/6/17.
 */
public class Tutorial extends JPanel {

    private String[] instructions  = {
            "Ultimate Tic Tac Toe is a strategic spin on the classic game",
            "It is comprised of a nine by nine tic tac toe grid made up of inner tic tac toe grids",
            "Players seek to win inner grids in order to win a square on the outer grid",
            "The game is won when someone gets three in a row in the outer grid.",
            "Gameplay revolves around a single mechanic: when a player plays in a box in an inner grid, their opponent must go in the corresponding box of the outer grid",
            "For instance, if a player plays in any bottom right inner grid box, the next player must play in the bottom right grid.",
            "The corresponding grid will be highlighted as a guide."
    };
    private JButton nextButton;
    private JButton endButton;
    private Frame parentFrame;
    private int stringCount = 0;
    public Tutorial(Frame parentFrame){
        this.parentFrame = parentFrame;
        BoxLayout bl = new BoxLayout(parentFrame.getContentPane(), BoxLayout.Y_AXIS);
        parentFrame.setLayout(bl);
        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(stringCount < instructions.length - 1){
                    stringCount++;
                    repaint();
                }
                else{
                    end_tutorial();
                }
            }
        });
        endButton = new JButton("End Tutorial");
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(GameElements.DrawMgr.getAllComponents(parentFrame));
                end_tutorial();
            }
        });

        parentFrame.add(nextButton);
        parentFrame.add(endButton);
        parentFrame.revalidate();
    }

    public void paintComponent(Graphics g){
        DrawMgr.drawCenteredString(g, instructions[stringCount], 24, this);
    }

    public void end_tutorial(){
        parentFrame.remove(nextButton);
        parentFrame.remove(endButton);
        parentFrame.remove(this);
        parentFrame.enter_menu();
    }
}
