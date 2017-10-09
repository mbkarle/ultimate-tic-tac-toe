import javax.swing.*;
import java.awt.*;

/**
 * Created by student on 10/2/17.
 */
public class Frame extends JFrame {

    private Menu menu = new Menu(this);
    private DrawMgr panel = new DrawMgr();
    private Tutorial tutorial;

    public Frame(){
        enter_menu();
        setSize(810, 810);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        revalidate();
    }

    public void start_game(){
        menu.removeButtons();
        remove(menu);
        menu.setFocusable(false);
        setLayout(new BorderLayout());
        add(panel);
        panel.requestFocus();
        panel.revalidate();
        panel.repaint();
    }

    public void start_tutorial(){
        tutorial = new Tutorial(this);
        menu.removeButtons();
        remove(menu);
        add(tutorial);
        tutorial.requestFocus();
        tutorial.revalidate();
        revalidate();
    }

    public void enter_menu(){

        add(menu);
        menu.addButtons();
      //  menu.repaint();
    }

}
