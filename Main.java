import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JFrame {
    private BoidManager boidManager;
    private Canvas canvas;

    public Main(double maxWidth, double maxHeight) {
        this.boidManager = new BoidManager(
            50, // number of boids
            maxWidth, // max width
            maxHeight, // max height
            100, // visible radius
            0.005, // steering factor
            0.05, // alignment factor
            50, // collision radius
            0.05 // collision avoidance factor
        );
        this.canvas = new Canvas();

        this.setTitle("Boid Simulation");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize((int) Math.round(maxWidth), (int) Math.round(maxHeight));
        this.setResizable(false);
        this.add(canvas);
        this.setVisible(true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBoids();
                repaint();
            }
        }, 0, 20); // 1 update every 20 milliseconds

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Check if Cmd (or Ctrl) + W keys are pressed
                if ((e.getKeyCode() == KeyEvent.VK_W) && ((e.getModifiersEx() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()) != 0)) {
                    // Close the window
                    dispose();
                    System.exit(0);
                }
            }
        });
    }

    // Add KeyListener to the JFrame

    private void updateBoids() {
        boidManager.update();
    }

    public void repaint() {
        this.canvas.repaint();
    }

    class Canvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            ArrayList<Boid> boids = boidManager.getBoids();
            for (int i = 0; i < boids.size(); i++) {
                int x = (int) Math.round(boids.get(i).posX);
                int y = (int) Math.round(boids.get(i).posY);
                double velocityMagnitude = Math.sqrt(boids.get(i).velocityX * boids.get(i).velocityX + boids.get(i).velocityY * boids.get(i).velocityY);
                double scale = 20;
                double scaledVelocityX = boids.get(i).velocityX * scale / velocityMagnitude;
                double scaledVelocityY = boids.get(i).velocityY * scale / velocityMagnitude;
                g.setColor(Color.WHITE);
                int[] polygonX = {
                    (int) Math.round(x-scaledVelocityY/2-scaledVelocityX/2),
                    (int) Math.round(x),
                    (int) Math.round(x+scaledVelocityY/2-scaledVelocityX/2),
                    (int) Math.round(x+scaledVelocityX)
                };
                int[] polygonY = {
                    (int) Math.round(y+scaledVelocityX/2-scaledVelocityY/2),
                    (int) Math.round(y),
                    (int) Math.round(y-scaledVelocityX/2-scaledVelocityY/2),
                    (int) Math.round(y+scaledVelocityY)
                };
                g.fillPolygon(polygonX, polygonY, 4);
                g.setColor(Color.GRAY);
                g.drawPolygon(polygonX, polygonY, 4);
                // g.drawOval(x-10, y-10, 20, 20);
                // g.setColor(Color.LIGHT_GRAY);
                // g.drawOval(x-50, y-50, 100, 100);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main(1000, 750);
            }
        });
    }
}