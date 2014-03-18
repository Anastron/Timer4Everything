package timerpack.timer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by MIKESCHER on 18.03.14.
 */
public class TimeDisplayDigit {
    private static final int ACCURACY = 2;

    private static final int WIDTH = 8 * ACCURACY;
    private static final int HEIGHT = 12 * ACCURACY;

    private int currentNumber = 0;

    private boolean[][][] maps = new boolean[11][WIDTH][HEIGHT];
    private List<Point<Integer>>[] activemaps = new List[11];

    private int[] pixelCount = new int[11];

    private MovingPixelPoint[] pixel;

    private int TRANSFORM_SPEED;

    private long lastTick;

    public TimeDisplayDigit(Bitmap[] digits, int init, int transdu) {
        int maxPixCount = 0;

        lastTick = System.currentTimeMillis();

        TRANSFORM_SPEED = transdu;

        currentNumber = init;

        for (int dig = 0; dig < 11; dig++) {
            pixelCount[dig] = 0;
            activemaps[dig] = new ArrayList<Point<Integer>>();

            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    int bx = (x*8) / WIDTH;
                    int by = (y*12) / HEIGHT;

                    maps[dig][x][y] = Color.alpha( digits[dig].getPixel(bx, by) ) > 0;

                    if (maps[dig][x][y]) {
                        pixelCount[dig]++;
                        activemaps[dig].add(new Point<Integer>(x, y));
                    }
                }
            }

            maxPixCount = Math.max(maxPixCount, pixelCount[dig]);
        }

        pixel = new MovingPixelPoint[maxPixCount];

        for (int i = 0; i < pixel.length; i++) {
            pixel[i] = new MovingPixelPoint(activemaps[currentNumber].get(i % activemaps[currentNumber].size()));
        }
    }

    public void draw(Canvas c, int x, int y, int wid) {
        double px_size = wid / (WIDTH*1.0);

        Paint p = new Paint();
        p.setColor(Color.WHITE);


        for (int i = 0; i < pixel.length; i++) {
            RectF r = pixel[i].getRect(px_size);
            r.offset(x, y);
            c.drawRect(r, p);
        }
    }

    public void tick() {
        int delta =(int)(System.currentTimeMillis() - lastTick);
        lastTick += delta;

        for (int i = 0; i < pixel.length; i++) {
            pixel[i].tick(delta);
        }
    }

    public void set(int v) {
        if (v == currentNumber) return;

        currentNumber = v;

        Collections.shuffle(activemaps[currentNumber]);

        for (int i = 0; i < pixel.length; i++) {
            pixel[i].setTarget(activemaps[currentNumber].get(i % activemaps[currentNumber].size()), TRANSFORM_SPEED);
        }
    }
}
