import java.io.Serializable;
import java.util.ArrayList;

public class Stroke implements Serializable{

    // PUBLIC

    public Stroke() {
        this.lines = new ArrayList<>();
    }

    public ArrayList<Line> getLines() {
        return this.lines;
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    // PRIVATE

    private ArrayList<Line> lines;
}
