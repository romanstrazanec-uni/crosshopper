package sk.romanstrazanec.crosshopper;

public class Car {
    private float x;
    private float y;
    private int width;
    private int height;
    private int color;
    private int speed;
    private int direction;

    public Car(float x, float y, int width, int height, int color, int speed, int direction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.speed = speed;
        this.direction = direction / Math.abs(direction);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getColor() {
        return color;
    }

    public void move() {
        x += speed * direction;
    }
}
