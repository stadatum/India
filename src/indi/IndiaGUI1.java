/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indi;

import java.awt.FlowLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Stadatum
 */
public class IndiaGUI1 implements Runnable {

    // Mat is converted to this image;
    Image img2display;

    public void setImg2display(Image img2display) {
        this.img2display = img2display;
    }

    public IndiaGUI1() {

    }

    public void run() {
        //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
        ImageIcon icon = new ImageIcon(img2display);
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        //frame.setLayout(new BoxLayout(frame, BoxLayout.X_AXIS));
        frame.setSize(img2display.getWidth(null) + 50, img2display.getHeight(null) + 50);

        JLabel lbl = new JLabel("Image from Mat is ");
        lbl.setIcon(icon);

        frame.add(lbl);
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
