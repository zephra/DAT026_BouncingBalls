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
        // double[] xCoords = new double[balls.length];
        // double[] yCoords = new double[balls.length];

        // TODO this method implements one step of simulation with a step deltaT
        for (int i=0; i<balls.length; i++) {
            Ball b = balls[i];
            if (b == null) break;

            // gravitation
            b.vy += -G; // TODO: account for mass!

            // detect collision with the border
            if (b.x < b.radius && b.vx < 0
                        || b.x > areaWidth - b.radius && b.vx > 0) {
                b.vx *= -DAMPENING; // change direction of ball
            }
            if (b.y < b.radius && b.vy < 0
                        || b.y > areaHeight - b.radius && b.vy > 0) {
                b.vy *= -DAMPENING;
            }

            // xCoords[i] = b.x;
            // yCoords[i] = b.y;

            // compute new position according to the speed of the ball
            b.x += deltaT * b.vx;
            b.y += deltaT * b.vy;
        }

        for (int i=0; i<balls.length; i++) {
            for (int j=i+1; j<balls.length; j++) {
                // if (j > i) {
                    double x1 = balls[i].x;
                    double x2 = balls[j].x;
                    double y1 = balls[i].y;
                    double y2 = balls[j].y;
                    double distance = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
                    if (distance < balls[i].radius + balls[j].radius) {

                        double polarR = rectToPolarR(balls[i].vx, balls[i].vy);
                        double polarT = rectToPolarT(balls[i].vx, balls[i].vy);
                        double polarR2 = rectToPolarR(balls[j].vx, balls[j].vy);
                        double polarT2 = rectToPolarT(balls[j].vx, balls[j].vy);
                        // double tangentT = rectToPolarT(balls[i].x - balls[j].x, balls[i].y - balls[j].y);

                        // polarT += Math.PI;
                        // double alfa = polarT - tangentT;
                        // polarT += 2 * alfa;

                // --- Calculations:

                // x = r cos(alfa)
                // xt = r cos(alfa - beta)
                // y = r sin(alfa)
                // yt = r sin(alfa - beta)

                // v1 = v2 - R
                // v2 = v1 + R

                //     m1 v1 + m2 (v1 + R) = I
                // =>  m1 v1 + m2 v1 + m2 R = I
                // =>  v1 (m1 + m2) = I - m2 R
                // =>  v1 = (I - m2 R) / (m1 + m2)


                //     m1 (v2 - R) + m2 v2 = I
                // =>  m1 v2 - m1 R + m2 v2 = I
                // =>  v2 (m1 + m2) = I + m1 R
                // =>  v2 = (I + m1 R) / (m1 + m2)

                        double beta = rectToPolarT(x2 - x1, y2 - y1);

                        double u1 = polarToRectX(polarR, polarT - beta);
                        double u1y = polarToRectY(polarR, polarT - beta);
                        double u2 = polarToRectX(polarR2, polarT2 - beta);
                        double u2y = polarToRectY(polarR2, polarT2 - beta);

                        double m1 = balls[i].mass;
                        double m2 = balls[j].mass;

                        // System.out.println("r1: "+polarR+", v1: "+v1+", v1y: "+v1y+" comb: "+rectToPolarR(v1, v1y));
                        // System.out.println("r2: "+polarR2+", v2: "+v2+", v2y: "+v2y);

                        double bigI = m1 * u1 + m2 * u2;
                        double bigR = -(u2 - u1);

                        double v1 = (bigI - m2 * bigR) / (m1 + m2);
                        double v2 = (bigI + m1 * bigR) / (m1 + m2);

                        polarR = rectToPolarR(v1, u1y);
                        polarT = rectToPolarT(v1, u1y);
                        polarR2 = rectToPolarR(v2, u2y);
                        polarT2 = rectToPolarT(v2, u2y);

                        // double collisionAngle = Math.atan2(Math.abs(y2-y1), Math.abs(x2-x1));
                        // double normalAngle = Math.atan2(Math.abs(y2-y1), Math.abs(x2-x1)) + Math.PI/2;
                        // if (x1 < x2 && y1 < y2) {
                        //   collisionAngle += Math.PI;
                        // } else if (x1 < x2) {
                        //   collisionAngle += Math.PI/2;
                        // } else if (y1 < y2) {
                        //   collisionAngle += 3*Math.PI/2;
                        // }
                        // polarT += normalAngle - Math.PI;
                        // polarT2 += normalAngle - Math.PI;

                        // if (polarT > Math.PI) {
                        //     polarT -= Math.PI * 2;
                        // } else if (polarT < -Math.PI) {
                        //     polarT += Math.PI * 2;
                        // }

                        // polarT += Math.PI / 2;
                        // polarT2 += Math.PI / 2;

                        // polarT = collisionAngle;
                        // polarT2 = collisionAngle + Math.PI;

                        // System.out.println("angle: "+collisionAngle);

                        System.out.println("Ball " + i);
                        System.out.println("Before\t\tR: " + rectToPolarR(balls[i].vx, balls[i].vy) + "\tT: " + rectToPolarT(balls[i].vx, balls[i].vy));

                        balls[i].vx = polarToRectX(polarR, polarT);
                        balls[i].vy = polarToRectY(polarR, polarT);
                        balls[j].vx = polarToRectX(polarR2, polarT2);
                        balls[j].vy = polarToRectY(polarR2, polarT2);

                        System.out.println("After\t\tR: " + rectToPolarR(balls[i].vx, balls[i].vy) + "\tT: " + rectToPolarT(balls[i].vx, balls[i].vy));
                        // System.out.println("Collision\t" + collisionAngle);
                        System.out.println();

                    }
                // }
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
