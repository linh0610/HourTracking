package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Represents a bouncing box animation panel.
 */
public class BouncingBox extends JPanel implements ActionListener {
    private int boxSize = 90;
    private int cordX = 0;
    private int cordY = 0;
    private int dx = 3;
    private int dy = 3;
    private Timer timer;

    /**
     * Constructs a BouncingBox panel.
     * Effects: Initializes the panel with preferred dimensions, background color, and starts the animation timer.
     */
    public BouncingBox() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        timer = new Timer(10, this);
        timer.start();
    }

    /**
     * Paints the bouncing box on the panel.
     * Modifies: g
     * Effects: Draws the bouncing box image at its current coordinates.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon imageIcon = new ImageIcon("./data/DVD-logo.png");
        Image image = imageIcon.getImage();
        g.drawImage(image, cordX, cordY, boxSize, 100, this);
    }

    /**
     * Handles the animation of the bouncing box.
     * Modifies: cordX, cordY, dx, dy
     * Effects: Updates the coordinates of the bouncing box and changes direction when hitting the panel boundaries.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        cordX += dx;
        cordY += dy;
        if (cordX <= 0 || cordX + boxSize >= getWidth()) {
            dx = -dx;
        }
        if (cordY <= 0 || cordY + boxSize >= getHeight()) {
            dy = -dy;
        }
        repaint();
    }
}
