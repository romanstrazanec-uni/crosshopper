package sk.romanstrazanec.crosshopper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class GameCanvas extends View {
    Paint paint;
    Bitmap pic;
    Bunny bunny;
    Carrot carrot;
    ArrayList<Car> cars;

    int carWidth;
    float maxX, maxY; //maxX=1080 maxY=1776

    // y-nové súradnice, kde sa môžu generovať autá
    float[] leftRoads; // autá z ľavej strany
    float[] rightRoads; // autá z pravej strany

    // aby sa negenerovali autá na tej istej ceste
    int previousLeftRoad = -1;
    int previousRightRoad = -1;
    int previousCarrotLayer = 0; // kde bol pred tým carrot
    float[] layers; // y-nové súradnice, kam môže bunny ísť
    int bunnyLayer; // kde je bunny
    int score;

    public GameCanvas(Context context) {
        super(context);
        paint = new Paint();
        setbg(context);

        leftRoads = new float[]{.269144f * maxY, .347973f * maxY, .573198f * maxY, .797254f * maxY}; //478.0f, 618.0f, 1018.0f, 1418.0f
        rightRoads = new float[]{.094595f * maxY, .184685f * maxY, .488739f * maxY, .713964f * maxY}; // 168.0f, 328.0f, 868.0f, 1268.0f
        layers = new float[]{.038288f * maxY, .111486f * maxY, .195946f * maxY, .280405f * maxY, .359234f * maxY, .432432f * maxY, .5f * maxY, .584459f * maxY, .657658f * maxY, .725225f * maxY, .809685f * maxY, .882883f * maxY};

        carWidth = (int) maxX / 9;
        bunnyLayer = layers.length - 1;
        bunny = new Bunny(maxX / 2, layers[bunnyLayer], (int) (maxY / 35.52f), Color.WHITE);
        carrot = new Carrot(maxX / 2, layers[previousCarrotLayer], Color.rgb(255, 155, 0));
        cars = new ArrayList<>();
        score = 0;
    }

    private void setbg(Context context) {
        Bitmap pozadie = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.background_image));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        maxX = size.x;
        maxY = size.y;
        pic = Bitmap.createScaledBitmap(pozadie, size.x, size.y, false);
    }

    public void update() {
        // ak bunny zasiahnutý
        if (crash()) {
            score = 0;
            bunnyLayer = layers.length - 1;
            previousCarrotLayer = 0;
            carrot.setY(layers[previousCarrotLayer]);
            bunny.setY(layers[bunnyLayer]);
        }

        // ak zoberie mrkvu
        if (bunny.getY() == carrot.getY()) {
            score++;
            carrot.setY(randomCarrotPosition());
        }

        if (cars.size() < 7) {
            if (Math.random() > 0.9) {
                cars.add(new Car(0 - carWidth, randomLeftRoad(), carWidth, (int) (maxY / 35.52),
                        Color.rgb(100 + (int) (Math.random() * 156), 100 + (int) (Math.random() * 156), 100 + (int) (Math.random() * 156)),
                        30 + (int) (Math.random() * 21), 1));
            }
            if (Math.random() > 0.9) {
                cars.add(new Car(maxX, randomRightRoad(), carWidth, (int) (maxY / 35.52),
                        Color.rgb(100 + (int) (Math.random() * 156), 100 + (int) (Math.random() * 156), 100 + (int) (Math.random() * 156)),
                        (30 + (int) (Math.random() * 21)), -1));
            }
        }

        if (cars.size() > 0) {
            Car c;
            for (int i = cars.size() - 1; i >= 0; i--) {
                c = (Car) cars.get(i);
                c.move();
                if (c.getX() > maxX || c.getX() + c.getWidth() < 0) cars.remove(i);
            }
        }
    }

    private float randomLeftRoad() {
        int i;
        do {
            i = (int) (Math.random() * leftRoads.length);
        } while (i == previousLeftRoad);
        previousLeftRoad = i;
        return leftRoads[i];
    }

    private float randomRightRoad() {
        int i;
        do {
            i = (int) (Math.random() * rightRoads.length);
        } while (i == previousRightRoad);
        previousRightRoad = i;
        return rightRoads[i];
    }

    private float randomCarrotPosition() {
        int i;
        do {
            i = (int) (Math.random() * layers.length);
        } while (i == previousCarrotLayer);
        previousCarrotLayer = i;
        return layers[i];
    }

    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();

        if (e == MotionEvent.ACTION_DOWN) {
            if (event.getY() > bunny.getY()) {
                moveBunnyDown();
            } else {
                moveBunnyUp();
            }
            invalidate();
        }
        return true;
    }

    private void moveBunnyUp() {
        if (bunnyLayer - 1 > -1) {
            bunnyLayer--;
            bunny.setY(layers[bunnyLayer]);
        }
    }

    private void moveBunnyDown() {
        if (bunnyLayer + 1 < layers.length) {
            bunnyLayer++;
            bunny.setY(layers[bunnyLayer]);
        }
    }

    private boolean crash() {
        for (int i = 0; i < cars.size(); i++) {
            if (isNear((Car) cars.get(i))) return true;
        }
        return false;
    }

    private boolean isNear(Car c) {
        return (bunny.getY() < c.getY() + c.getHeight() && bunny.getY() > c.getY()) && (bunny.getX() + bunny.getR() > c.getX() && bunny.getX() - bunny.getR() < c.getX() + c.getWidth());
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(pic, 0, 0, paint);
        drawBunny(canvas);
        drawCarrot(canvas);
        drawScoreCounter(canvas);
        for (int i = 0; i < cars.size(); i++) {
            drawCar(canvas, (Car) cars.get(i));
        }
    }

    private void drawBunny(Canvas canvas) {
        paint.setColor(bunny.getColor());
        canvas.drawCircle(bunny.getX(), bunny.getY(), bunny.getR(), paint);
    }

    private void drawCar(Canvas canvas, Car c) {
        paint.setColor(c.getColor());
        canvas.drawRect(c.getX(), c.getY(), c.getX() + c.getWidth(), c.getY() + c.getHeight(), paint);
    }

    private void drawCarrot(Canvas canvas) {
        paint.setColor(carrot.getColor());
        canvas.drawCircle(carrot.getX(), carrot.getY(), (int) (maxY / 59.2), paint);
    }

    private void drawScoreCounter(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawRect((int) (maxY / 177.6), (int) (maxY / 177.6), (int) (maxY / 14.8), (int) (maxY / 25.37), paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize((int) (maxY / 25.37));
        canvas.drawText("" + score, (int) (maxY / 118.4), (int) (maxY / 27.32), paint);
    }
}
