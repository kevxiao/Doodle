import java.awt.*;
import java.io.Serializable;

public class Line implements Serializable{

    // PUBLIC

    public Line(Point a, Point b, Color c, double th, long t) {
        this.src = a;
        this.dest = b;
        this.color = c;
        this.thickness = th;
        this.time = t;
    }

    public Point getSrc() {
        return new Point(this.src);
    }

    public Point getDest() {
        return new Point(this.dest);
    }

    public Color getColor() {
        return new Color(this.color.getRGB());
    }

    public double getThickness() {
        return this.thickness;
    }

    public long getTime() {
        return this.time;
    }

    // PRIVATE

    private Point src;
    private Point dest;
    private Color color;
    private double thickness;
    private long time;
}
