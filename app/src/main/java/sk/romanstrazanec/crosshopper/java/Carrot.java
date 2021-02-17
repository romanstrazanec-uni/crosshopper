package sk.romanstrazanec.crosshopper.java;

public class Carrot {
    private float x;
    private float y;
    private int color;

    public Carrot(float x, float y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
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
