package bouncing_balls;

import java.awt.Color;

/**
 * The physics model.
 *
 * This class is where you should implement your bouncing balls model.
 *
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 *
 * @author Simon Robillard
 *
 */
class Model {

    final double G = 9.82 * 0.01;
    final double DAMPENING = 0.98;

    double areaWidth, areaHeight;

    Ball [] balls;

    Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;

        // Initialize the model with a few balls
        int i = 0;
        balls = new Ball[2];
        balls[i++] = new Ball(width / 3, height * 0.6, 1.0, 0, 0.4, Color.RED);
        balls[i++] = new Ball(2 * width / 3, height * 0.3, -1.6, 0, 0.3, Color.GREEN);
        // balls[i++] = new Ball(width / 4, height * 0.9, 0.8, 0, 0.2, Color.BLUE);
    }

    void step(double deltaT) {
        double[] xCoords = new double[balls.length];
        double[] yCoords = new double[balls.length];

        // TODO this method implements one step of simulation with a step deltaT
        for (int i=0; i<balls.length; i++) {
            Ball b = balls[i];
            if (b == null) break;

            // gravitation
            b.vy += -G;

            // detect collision with the border
            if (b.x < b.radius && b.vx < 0
                        || b.x > areaWidth - b.radius && b.vx > 0) {
                b.vx *= -DAMPENING; // change direction of ball
            }
            if (b.y < b.radius && b.vy < 0
                        || b.y > areaHeight - b.radius && b.vy > 0) {
                b.vy *= -DAMPENING;
            }

            xCoords[i] = b.x;
            yCoords[i] = b.y;

            // compute new position according to the speed of the ball
            b.x += deltaT * b.vx;
            b.y += deltaT * b.vy;
        }

        for (int i=0; i<balls.length; i++) {
            for (int j=0; j<balls.length; j++) {
                if (i != j) {
                    double distance = Math.sqrt(Math.pow((balls[i].x - balls[j].x),2) + Math.pow((balls[i].y - balls[j].y),2));
                    if (distance <= balls[i].radius + balls[j].radius) {
                        balls[i].vx = -balls[i].vx;
                        balls[i].vy = -balls[i].vy;
                    }
                }
            }
        }

        // System.out.println("Ball 0 vertical speed: "+balls[0].vy);
    }

    /**
     * Simple inner class describing balls.
     */
    class Ball {

        Ball(double x, double y, double vx, double vy, double r, Color c) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = r;
            this.mass = 4/3 * Math.PI * Math.pow(r, 3); // Volume of a sphere
            this.color = c;
        }

        /**
         * Position, speed, and radius of the ball. You may wish to add other attributes.
         */
        double x, y, vx, vy, radius, mass;
        Color color;
    }
}
