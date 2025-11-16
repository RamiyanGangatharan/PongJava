import java.util.List;

// NOTE: GPT MADE THIS
public class CollisionDetection {

    public static void check(Ball ball, List<Wall> walls, List<Paddle> paddles) {

        // Ball bounding box
        double left   = ball.getPositionX() - ball.getRadius();
        double right  = ball.getPositionX() + ball.getRadius();
        double top    = ball.getPositionY() - ball.getRadius();
        double bottom = ball.getPositionY() + ball.getRadius();

        // -------------------------
        // WALL COLLISIONS
        // -------------------------
        for (Wall w : walls) {
            if (!isOverlap(left, right, top, bottom,
                    w.getPositionX(), w.getPositionY(), w.getWallWidth(), w.getWallHeight())) {
                continue;
            }

            double[] d = computeOverlap(left, right, top, bottom,
                    w.getPositionX(), w.getPositionY(), w.getWallWidth(), w.getWallHeight());

            resolveWallCollision(ball, d);
            return;
        }

        // -------------------------
        // PADDLE COLLISIONS
        // -------------------------
        for (Paddle p : paddles) {
            if (!isOverlap(left, right, top, bottom,
                    p.getPositionX(), p.getPositionY(), p.getPaddleWidth(), p.getPaddleHeight())) {
                continue;
            }

            double[] d = computeOverlap(left, right, top, bottom,
                    p.getPositionX(), p.getPositionY(), p.getPaddleWidth(), p.getPaddleHeight());

            resolvePaddleCollision(ball, p, d);
            return;
        }
    }

    // -------- Helpers --------

    private static boolean isOverlap(
            double left, double right, double top, double bottom,
            int ox, int oy, int ow, int oh
    ) {
        return right > ox &&
                left < ox + ow &&
                bottom > oy &&
                top < oy + oh;
    }

    private static double[] computeOverlap(
            double left, double right, double top, double bottom,
            int ox, int oy, int ow, int oh
    ) {
        double overlapLeft   = Math.abs(right - ox);
        double overlapRight  = Math.abs((ox + ow) - left);
        double overlapTop    = Math.abs(bottom - oy);
        double overlapBottom = Math.abs((oy + oh) - top);

        return new double[] { overlapLeft, overlapRight, overlapTop, overlapBottom };
    }

    private static void resolveWallCollision(Ball ball, double[] d) {
        double overlapLeft = d[0], overlapRight = d[1],
                overlapTop = d[2], overlapBottom = d[3];

        double min = Math.min(Math.min(overlapLeft, overlapRight),
                Math.min(overlapTop, overlapBottom));

        if (min == overlapLeft || min == overlapRight) {
            ball.setVelocityX(-ball.getVelocityX());
        } else {
            ball.setVelocityY(-ball.getVelocityY());
        }
    }

    private static void resolvePaddleCollision(Ball ball, Paddle pad, double[] d) {
        double overlapLeft = d[0], overlapRight = d[1],
                overlapTop = d[2], overlapBottom = d[3];

        double min = Math.min(Math.min(overlapLeft, overlapRight),
                Math.min(overlapTop, overlapBottom));

        if (min == overlapLeft || min == overlapRight) {

            ball.setVelocityX(-ball.getVelocityX());

            double paddleCenter = pad.getPositionY() + pad.getPaddleHeight() / 2.0;
            double distance = ball.getPositionY() - paddleCenter;

            ball.setVelocityY(ball.getVelocityY() + distance * 0.05);

            if (min == overlapLeft)
                ball.setPositionX(pad.getPositionX() - ball.getRadius());
            else
                ball.setPositionX(pad.getPositionX() + pad.getPaddleWidth() + ball.getRadius());

        } else {

            ball.setVelocityY(-ball.getVelocityY());

            if (min == overlapTop)
                ball.setPositionY(pad.getPositionY() - ball.getRadius());
            else
                ball.setPositionY(pad.getPositionY() + pad.getPaddleHeight() + ball.getRadius());
        }
    }
}
