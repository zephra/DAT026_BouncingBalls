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
                    if (distance < balls[i].radius + balls[j].radius) {

                        double polarR = rectToPolarR(balls[i].vx, balls[i].vy);
                        double polarT = rectToPolarT(balls[i].vx, balls[i].vy);
                        double tangentT = rectToPolarT(balls[i].x - balls[j].x, balls[i].y - balls[j].y);
                        // polarT += Math.PI;
                        double alfa = polarT - tangentT;
                        polarT += 2 * alfa;
                        double newX = polarToRectX(polarR, polarT);
                        double newY = polarToRectY(polarR, polarT);

                        double collisionAngle;

                        System.out.println("Ball " + i);
                        System.out.println("Before\tR: " + rectToPolarR(balls[i].vx, balls[i].vy) + "\tT: " + rectToPolarT(balls[i].vx, balls[i].vy));

                        balls[i].vx = newX;
                        balls[i].vy = newY;

                        System.out.println("After\tR: " + rectToPolarR(balls[i].vx, balls[i].vy) + "\tT: " + rectToPolarT(balls[i].vx, balls[i].vy));
                        System.out.println();

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

    // class Polar {
    //     double r, t;
    //     public Polar(double r, double t) {
    //         this.r = r;
    //         this.t = t;
    //     }
    // }

    // class Rect {
    //     double x, y;
    //     public Rect(double x, double y) {

    //     }
    // }

    public static double rectToPolarR(double x, double y) {
        return Math.sqrt(x*x + y*y);
    }

    public static double rectToPolarT(double x, double y) {
        return Math.atan2(y, x);
    }

    public static double polarToRectX(double r, double t) {
        return r * Math.cos(t);
    }

    public static double polarToRectY(double r, double t) {
        return r * Math.sin(t);
    }
}
