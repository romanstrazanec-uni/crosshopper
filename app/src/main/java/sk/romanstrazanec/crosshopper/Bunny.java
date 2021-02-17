package sk.romanstrazanec.crosshopper;

public class Bunny {
    private float x;
    private float y;
    private int r;
    private int color;

    public Bunny(float x, float y, int r, int color) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public int getColor() {
        return color;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
