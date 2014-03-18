package timerpack.timer;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by MIKESCHER on 18.03.14.
 */
public class MovingPixelPoint {
    public Point<Integer> TARGET;

    public Point<Double> CURRENT;

    private double velocity = 0;

    public MovingPixelPoint(int x, int y) {
        TARGET = new Point<Integer>(x, y);
        CURRENT = new Point<Double>(new Double(x), new Double(y));
    }

    public MovingPixelPoint(Point<Integer> p) {
        TARGET = new Point<Integer>(p.x, p.y);
        CURRENT = new Point<Double>(new Double(p.x), new Double(p.y));
    }

    public RectF getRect(double size) {
        return new RectF((float)(CURRENT.x.doubleValue() * size), (float)(CURRENT.y.doubleValue() * size), (float)(CURRENT.x * size + size), (float)(CURRENT.y * size + size));
    }

    public void setTarget(Point<Integer> p, int duration) {
        double distance = Math.hypot(TARGET.x - p.x, TARGET.y - p.y);

        velocity = distance / duration;

        TARGET.x = p.x;
        TARGET.y = p.y;
    }

    public void tick(int delta) {
        if (velocity == 0) return;

        double distance = Math.hypot(TARGET.x - CURRENT.x, TARGET.y - CURRENT.y);

        double move = delta * velocity;

        if (distance < move) {
            CURRENT.x = new Double(TARGET.x.intValue());
            CURRENT.y = new Double(TARGET.y.intValue());
            velocity = 0;
        } else {
            double x = ((TARGET.x - CURRENT.x) / distance) * move;
            double y = ((TARGET.y - CURRENT.y) / distance) * move;

            CURRENT.x += x;
            CURRENT.y += y;
        }

    }

    public String getString() {
        return "(" + CURRENT.x + "|" + CURRENT.y + ") - (" + TARGET.x + "|" + TARGET.y + ")";
    }
}
