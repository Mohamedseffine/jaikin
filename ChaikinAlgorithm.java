import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ChaikinAlgorithm extends JPanel implements KeyListener, MouseListener {
    private List<Point> controlPoints = new ArrayList<>();
    private List<Point> currentPoints = new ArrayList<>();
    private int currentStep = 0;
    private boolean animationRunning = false;
    private javax.swing.Timer animationTimer; 
    private final int POINT_RADIUS = 5;
    private final int ANIMATION_DELAY = 1000; 
    
    public ChaikinAlgorithm() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        addMouseListener(this);
        setFocusable(true);
        addKeyListener(this);
        
        animationTimer = new javax.swing.Timer(ANIMATION_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (animationRunning) {
                    nextAnimationStep();
                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.BLUE);
        for (Point p : controlPoints) {
            g2d.fillOval(p.x - POINT_RADIUS, p.y - POINT_RADIUS, 
                         POINT_RADIUS * 2, POINT_RADIUS * 2);
        }
        
        if (!currentPoints.isEmpty()) {
            g2d.setColor(Color.RED);
            for (int i = 0; i < currentPoints.size() - 1; i++) {
                Point p1 = currentPoints.get(i);
                Point p2 = currentPoints.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            
            g2d.setColor(Color.GREEN);
            for (Point p : currentPoints) {
                g2d.fillOval(p.x - POINT_RADIUS/2, p.y - POINT_RADIUS/2, 
                            POINT_RADIUS, POINT_RADIUS);
            }
        }
        
        g2d.setColor(Color.BLACK);
        g2d.drawString("Step: " + currentStep, 10, 20);
        g2d.drawString("Control Points: " + controlPoints.size(), 10, 40);
        g2d.drawString("Instructions:", 10, getHeight() - 60);
        g2d.drawString("Left Click: Add control point", 10, getHeight() - 40);
        g2d.drawString("Enter: Start/Continue animation", 10, getHeight() - 20);
        g2d.drawString("Escape: Exit", 10, getHeight() - 80);
        
        if (controlPoints.isEmpty()) {
            g2d.setColor(Color.RED);
            g2d.drawString("Please draw points by clicking on the canvas", 
                          getWidth()/2 - 100, getHeight()/2);
        }
    }
    
    private void startAnimation() {
        if (controlPoints.size() < 2) {
            return; 
        }
        
        if (!animationRunning) {
            currentPoints = new ArrayList<>(controlPoints);
            currentStep = 0;
            animationRunning = true;
            animationTimer.start();
        }
    }
    
    private void nextAnimationStep() {
        if (currentStep >= 7) {
            currentPoints = new ArrayList<>(controlPoints);
            currentStep = 0;
        } else {
            currentPoints = chaikinStep(currentPoints);
            currentStep++;
        }
        repaint();
    }
    
    private List<Point> chaikinStep(List<Point> points) {
        List<Point> newPoints = new ArrayList<>();
        
        if (points.size() < 2) {
            return new ArrayList<>(points);
        }
        
        newPoints.add(points.get(0));
        
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            
            Point q1 = new Point(
                (3 * p1.x + p2.x) / 4,
                (3 * p1.y + p2.y) / 4
            );
            
            Point q2 = new Point(
                (p1.x + 3 * p2.x) / 4,
                (p1.y + 3 * p2.y) / 4
            );
            
            newPoints.add(q1);
            newPoints.add(q2);
        }
        
        newPoints.add(points.get(points.size() - 1));
        
        return newPoints;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && !animationRunning ) {
            controlPoints.add(e.getPoint());
            currentPoints = new ArrayList<>(controlPoints);
            currentStep = 0;
            animationTimer.stop();
            repaint();
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                startAnimation();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_DELETE:
                animationRunning = false;
                controlPoints.clear();
                currentPoints.clear();
                currentStep = 0;
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chaikin's Algorithm Animation");
            ChaikinAlgorithm panel = new ChaikinAlgorithm();
            
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            panel.requestFocusInWindow();
        });
    }
}