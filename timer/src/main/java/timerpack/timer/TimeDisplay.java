package timerpack.timer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by MIKESCHER on 18.03.14.
 */
public class TimeDisplay extends View {

    public int Hours = 0;
    public int Minutes = 0;
    public int Seconds = 0;

    private Bitmap[] digits = new Bitmap[11];

    private TimeDisplayDigit[] display = new TimeDisplayDigit[8];

    private int delays[] = {500, 500, 0, 500, 500, 0, 500, 500};

    public TimeDisplay(Context context) {
        super(context);

        init();
    }

    public TimeDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        Drawable d = getResources().getDrawable(R.drawable.digits);

        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.digits);

        for (int i = 0; i < 11; i++) {
            digits[i] = Bitmap.createBitmap(b, i*8, 0, 8, 12);
        }

        for (int i = 0; i < 8; i++) {
            display[i] = new TimeDisplayDigit(digits, (i - 2) % 3 == 0 ? 10 : 0, delays[i]);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("T4E", "TimeDisplay KeyDown");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int comp_w = getWidth();
        int comp_h = getHeight();

        double targetRatio = ((8*8)/(12.0));
        double actualRatio = comp_w / (comp_h*1.0);

        int w, h, x, y;
        if (targetRatio > actualRatio) {
            x = 0;

            w = comp_w;
            h = (int)(w / targetRatio);

            y = (comp_w - w) / 2;
        } else {
            y = 0;

            h = comp_h;
            w = (int)(targetRatio * h);

            x = (comp_w - w) / 2;
        }

        double scale = h / 12.0;


        for (int i = 0; i < 8; i++) {
            display[i].tick();

            Rect r = getDigitRect(i, scale, x, y);
            display[i].draw(canvas, r.left, r.top, r.width());
        }

/*
        canvas.drawBitmap(digits[Hours/10], null, getDigitRect(0, scale, x, y), null);
        canvas.drawBitmap(digits[Hours%10], null, getDigitRect(1, scale, x, y), null);

        canvas.drawBitmap(digits[10], null, getDigitRect(2, scale, x, y), null);

        canvas.drawBitmap(digits[Minutes/10], null, getDigitRect(3, scale, x, y), null);
        canvas.drawBitmap(digits[Minutes%10], null, getDigitRect(4, scale, x, y), null);

        canvas.drawBitmap(digits[10], null, getDigitRect(5, scale, x, y), null);

        canvas.drawBitmap(digits[Seconds/10], null, getDigitRect(6, scale, x, y), null);
        canvas.drawBitmap(digits[Seconds%10], null, getDigitRect(7, scale, x, y), null);
*/

        invalidate();
    }

    private Rect getDigitRect(int idx, double scale, int x, int y) {
        return new Rect(
                x + (int)(8 * idx * scale),
                y + 0,
                x + (int)((8 * idx + 8) * scale),
                y + (int)(12*scale));
    }

    public void setTime(int h, int m, int s) {
        if (Hours != h) {
            Hours = h;

            display[0].set(Hours / 10);
            display[1].set(Hours % 10);
        }

        if (Minutes != m) {
            Minutes = m;

            display[3].set(Minutes / 10);
            display[4].set(Minutes % 10);
        }

        if (Seconds != s) {
            Seconds = s;

            display[6].set(Seconds / 10);
            display[7].set(Seconds % 10);
        }
    }
}
