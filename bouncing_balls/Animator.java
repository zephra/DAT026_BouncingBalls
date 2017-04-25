package bouncing_balls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import bouncing_balls.Model.Ball;

/**
 * Animated JPanel drawing the bouncing balls. No modifications are needed in this class.
 *
 * @author Simon Robillard
 *
 */
@SuppressWarnings("serial")
public final class Animator extends JPanel implements ActionListener {

	public Animator(int pixelWidth, int pixelHeight, int fps) {
		super(true);
		this.timer = new Timer(1000 / fps, this);
		this.deltaT = 1.0 / fps;
		this.model = new Model(pixelWidth / pixelsPerMeter, pixelHeight / pixelsPerMeter);
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(pixelWidth, pixelHeight));
	}

	/**
	 * Drawing scale
	 */
	private static final double pixelsPerMeter = 200;

	/**
	 * Physical model
	 */
	private Model model;

	/**
	 * Timer that triggers redrawing
	 */
	private Timer timer;

	/**
	 * Time interval between redrawing, also used as time step for the model
	 */
	private double deltaT;

	public void start() {
		timer.start();
	}

	public void stop() {
    	timer.stop();
    }

		private int collisionX = 0;
		private int collisionY = 0;

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// clear the canvas
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		// draw balls
		// g2.setColor(Color.RED);
		for (Ball b : model.balls) {
            if (b == null) break;
            g2.setColor(b.color);
			double x = b.x - b.radius;
			double y = b.y + b.radius;
			// paint balls (y-coordinates are inverted)
			Ellipse2D.Double e = new Ellipse2D.Double(x * pixelsPerMeter, this.getHeight() - (y * pixelsPerMeter),
					b.radius * 2 * pixelsPerMeter, b.radius * 2 * pixelsPerMeter);
			g2.fill(e);

			// drawCollisionAngle(model.balls, g);
			// g.drawLine(0,0, collisionX, collisionY);
			// System.out.println(collisionX + " " + collisionY);
		}
	}

	// private void drawCollisionAngle(Ball[] balls, Graphics g) {
	// 	g.setColor(Color.BLACK);
	//
	// 	for (int i=0; i<balls.length; i++) {
	// 			for (int j=0; j<balls.length; j++) {
	// 					if (i != j) {
	// 							double x1 = balls[i].x;
	// 							double x2 = balls[j].x;
	// 							double y1 = balls[i].y;
	// 							double y2 = balls[j].y;
	// 							double distance = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
	// 							if (distance < balls[i].radius + balls[j].radius) {
	//
	// 									double polarR = rectToPolarR(balls[i].vx, balls[i].vy);
	// 									double polarT = rectToPolarT(balls[i].vx, balls[i].vy);
	// 									double tangentT = rectToPolarT(balls[i].x - balls[j].x, balls[i].y - balls[j].y);
	//
	// 									double collisionAngle = Math.atan2(Math.abs(y2-y1), Math.abs(x2-x1)) + Math.PI;
	//
	// 									collisionX = (int)(balls[i].x * pixelsPerMeter);
	// 									collisionY = (int)(balls[i].y * pixelsPerMeter);
	// 							}
	// 					 }
	// 			}
	// 	 }
	// }
	//
	// public static double rectToPolarR(double x, double y) {
	// 		return Math.sqrt(x*x + y*y);
	// }
	//
	// public static double rectToPolarT(double x, double y) {
	// 		return Math.atan2(y, x);
	// }
	//
	// public static double polarToRectX(double r, double t) {
	// 		return r * Math.cos(t);
	// }
	//
	// public static double polarToRectY(double r, double t) {
	// 		return r * Math.sin(t);
	// }

    @Override
    public void actionPerformed(ActionEvent e) {
    	model.step(deltaT);
    	this.repaint();
    }

	public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Animator anim = new Animator(800, 600, 60);
                JFrame frame = new JFrame("Bouncing balls");
            	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	frame.add(anim);
            	frame.pack();
            	frame.setLocationRelativeTo(null);
            	frame.setVisible(true);
            	anim.start();
            }
        });
    }
}
