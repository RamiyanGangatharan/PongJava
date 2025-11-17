import java.util.List;

/**
 * Utility class responsible for detecting and resolving collisions between the
 * ball, walls, and paddles within the game world.
 *
 * <p>This class provides AABB-style collision tests against rectangular objects
 * and applies appropriate velocity and positional corrections to the ball
 * when a collision occurs.</p>
 */
public class CollisionDetection {

    /**
     * Checks the ball against all walls and paddles, resolving the first detected
     * collision. Wall collisions simply invert the velocity, while paddle
     * collisions add spin based on impact position.
     *
     * @param ball     the ball to check for collisions
     * @param walls    all wall objects in the scene
     * @param paddles  all paddle objects in the scene
     */
    public static void check(Ball ball, List<Wall> walls, List<Paddle> paddles) {

        // Ball bounding box
        double left   = ball.getPositionX() - ball.getRadius();
        double right  = ball.getPositionX() + ball.getRadius();
        double top    = ball.getPositionY() - ball.getRadius();
        double bottom = ball.getPositionY() + ball.getRadius();

        // -------------------------
        // WALL COLLISIONS
        // -------------------------
        for (Wall wall : walls) {
            if (!isOverlap(
                    left, right,
                    top, bottom,
                    wall.getPositionX(), wall.getPositionY(),
                    wall.getWallWidth(), wall.getWallHeight()
            )) { continue; }

            double[] d = computeOverlap(
                    left, right,
                    top, bottom,
                    wall.getPositionX(), wall.getPositionY(),
                    wall.getWallWidth(), wall.getWallHeight()
            );

            resolveWallCollision(ball, d);
            return;
        }

        // -------------------------
        // PADDLE COLLISIONS
        // -------------------------
        for (Paddle paddle : paddles) {
            if (!isOverlap(
                    left, right,
                    top, bottom,
                    paddle.getPositionX(), paddle.getPositionY(),
                    paddle.getPaddleWidth(), paddle.getPaddleHeight()
            )) { continue; }

            double[] d = computeOverlap(
                    left, right,
                    top, bottom,
                    paddle.getPositionX(), paddle.getPositionY(),
                    paddle.getPaddleWidth(), paddle.getPaddleHeight()
            );

            resolvePaddleCollision(ball, paddle, d);
            return;
        }
    }

    // -------- Helpers --------

    /**
     * Determines whether two axis-aligned bounding boxes overlap.
     *
     * @param left         left edge of the first box
     * @param right        right edge of the first box
     * @param top          top edge of the first box
     * @param bottom       bottom edge of the first box
     * @param overlapX     x-position of the second box
     * @param overlapY     y-position of the second box
     * @param overlapWidth width of the second box
     * @param overlapHeight height of the second box
     * @return {@code true} if the boxes overlap; otherwise {@code false}
     */
    private static boolean isOverlap(
            double left, double right,
            double top, double bottom,
            int overlapX, int overlapY,
            int overlapWidth, int overlapHeight
    )
    { return right > overlapX && left < overlapX + overlapWidth && bottom > overlapY && top < overlapY + overlapHeight; }

    /**
     * Computes the penetration depths between two overlapping axis-aligned boxes.
     * The resulting array contains four values in the order:
     * <pre>
     * [overlapLeft, overlapRight, overlapTop, overlapBottom]
     * </pre>
     *
     * @param left         left edge of the first box
     * @param right        right edge of the first box
     * @param top          top edge of the first box
     * @param bottom       bottom edge of the first box
     * @param overlapX     x-position of the second box
     * @param overlapY     y-position of the second box
     * @param overlapWidth width of the second box
     * @param overlapHeight height of the second box
     * @return an array containing the penetration distances on each side
     */
    private static double[] computeOverlap(
            double left, double right,
            double top, double bottom,
            int overlapX, int overlapY,
            int overlapWidth, int overlapHeight
    )
    {
        double overlapLeft   = Math.abs(right - overlapX);
        double overlapRight  = Math.abs((overlapX + overlapWidth) - left);
        double overlapTop    = Math.abs(bottom - overlapY);
        double overlapBottom = Math.abs((overlapY + overlapHeight) - top);

        return new double[] { overlapLeft, overlapRight, overlapTop, overlapBottom };
    }

    /**
     * Resolves a collision between the ball and a wall by inverting the
     * appropriate component of the ball's velocity.
     *
     * @param ball the ball involved in the collision
     * @param penetrationDistance penetration distances (left, right, top, bottom)
     */
    private static void resolveWallCollision(Ball ball, double[] penetrationDistance) {
        double overlapLeft = penetrationDistance[0];
        double overlapRight = penetrationDistance[1];
        double overlapTop = penetrationDistance[2];
        double overlapBottom = penetrationDistance[3];
        double min = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

        if (min == overlapLeft || min == overlapRight) { ball.setVelocityX(-ball.getVelocityX()); }
        else  { ball.setVelocityY(-ball.getVelocityY()); }
    }

    /**
     * Resolves a collision between the ball and a paddle. Horizontal impacts
     * reverse horizontal velocity and add spin, while vertical impacts reverse
     * vertical velocity.
     *
     * @param ball the ball involved in the collision
     * @param pad  the paddle that was hit
     * @param penetrationDistance   penetration distances (left, right, top, bottom)
     */
    private static void resolvePaddleCollision(Ball ball, Paddle pad, double[] penetrationDistance) {
        double overlapLeft = penetrationDistance[0];
        double overlapRight = penetrationDistance[1];
        double overlapTop = penetrationDistance[2];
        double overlapBottom = penetrationDistance[3];
        double min = Math.min(Math.min(overlapLeft, overlapRight), Math.min(overlapTop, overlapBottom));

        if (min == overlapLeft || min == overlapRight) {

            ball.setVelocityX(-ball.getVelocityX());

            double paddleCenter = pad.getPositionY() + pad.getPaddleHeight() / 2.0;
            double distance = ball.getPositionY() - paddleCenter;

            ball.setVelocityY(ball.getVelocityY() + distance * 0.05);

            if (min == overlapLeft) { ball.setPositionX(pad.getPositionX() - ball.getRadius()); }
            else { ball.setPositionX(pad.getPositionX() + pad.getPaddleWidth() + ball.getRadius()); }

        }
        else {
            ball.setVelocityY(-ball.getVelocityY());
            if (min == overlapTop) { ball.setPositionY(pad.getPositionY() - ball.getRadius()); }
            else { ball.setPositionY(pad.getPositionY() + pad.getPaddleHeight() + ball.getRadius()); }
        }
    }
}
