import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by student on 10/6/17.
 */
public class Menu extends JPanel {

    private JButton startOfflineGameButton, tutorialButton;
    private Frame parentFrame;
    private ArrayList<JButton> buttonsList = new ArrayList<>();
    private ArrayList<Component> rigidAreas = new ArrayList<>();
    private ActionListener startOfflineGame = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            parentFrame.start_game();
        }
    };

    public Menu(Frame parentFrame){
        this.parentFrame = parentFrame;
      //  startOfflineGameButton.setBounds(100, 100, 200, 100);
        startOfflineGameButton = new JButton("Start Game");
        startOfflineGameButton.addActionListener(startOfflineGame);
        buttonsList.add(startOfflineGameButton);
        tutorialButton = new JButton("How to Play");
        tutorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.start_tutorial();
            }
        });
        buttonsList.add(tutorialButton);

    }

    public void addButtons(){
        BoxLayout bl = new BoxLayout(parentFrame.getContentPane(), BoxLayout.Y_AXIS);
        parentFrame.setLayout(bl);
        for(JButton button : buttonsList){
            parentFrame.add(button);
            button.setFont(new Font("TimesRoman", Font.BOLD, 32));
            button.setPreferredSize(new Dimension(200, 100));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

        }

        parentFrame.revalidate();
      //  parentFrame.repaint();
    }

    public void removeButtons(){
        for(JButton button : buttonsList){
            parentFrame.remove(button);
        }
        for(Component rigidArea : rigidAreas){
            remove(rigidArea);
            parentFrame.remove(rigidArea);
            parentFrame.getContentPane().remove(rigidArea);
        }
    }

    public void paintComponent(Graphics g){
        for(Component comp : rigidAreas){
            if(comp.getName().equals("rA")){
                parentFrame.remove(comp);
            }
        }
        g.setFont(new Font("TimesRoman", Font.BOLD, 64));
        g.drawString("Ultimate Tic Tac Toe", getWidth() / 2 - 300, getHeight()/2);
        Component rA = Box.createRigidArea(new Dimension(getWidth() / 8, getHeight() / 8));
        rA.setPreferredSize(new Dimension(getWidth() / 8, getHeight() / 8));
        rA.setName("rA");
        parentFrame.getContentPane().add(rA);
        parentFrame.revalidate();
        rigidAreas.add(rA);
    }
}
