import javax.swing.*;

/**
 * Created by student on 10/2/17.
 */
public class Frame extends JFrame {

    private DrawMgr panel = new DrawMgr();

    public Frame(){
        add(panel);
        setSize(810, 810);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
