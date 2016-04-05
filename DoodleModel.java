import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DoodleModel extends Thread {

    // PUBLIC

    public DoodleModel() {
        this.strokes = new ArrayList<>();
        this.futureStrokes = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.lineColor = Constants.DEFAULT_LINE_COLOR;
        this.lineThickness = Constants.DEFAULT_LINE_THICKNESS;
        this.doodleSize = new Dimension(0, 0);
        this.curStroke = null;
        this.strokeTimer = null;
        this.strokeTime = 0;
        this.actualSize = true;
        this.notifyObservers();
    }

    public void addObserver(Observer obs) {
        this.observers.add(obs);
    }

    public int getNumPrevStrokes() {
        return this.strokes.size();
    }

    public int getNumTotalStrokes() {
        return this.strokes.size() + this.futureStrokes.size();
    }

    public ArrayList<Stroke> getStrokes() {
        return this.strokes;
    }

    public ArrayList<Stroke> getFutureStrokes() {
        return this.futureStrokes;
    }

    public Stroke getCurrentStroke() {
        return this.curStroke;
    }

    public boolean hasChanged() {
        return !this.strokes.isEmpty();
    }

    public Dimension getDoodleSize() {
        return new Dimension(this.doodleSize);
    }

    public boolean isActualSize() {
        return this.actualSize;
    }

    public Color getLineColor() {
        return this.lineColor;
    }

    public double getLineThickness() {
        return this.lineThickness;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public boolean willStartPlaying() {
        boolean temp = this.startPlay;
        if(this.startPlay) {
            this.startPlay = false;
        }
        return temp;
    }

    public void setActualSize(boolean actual) {
        this.actualSize = actual;
        this.notifyObservers();
    }

    public void setLineColor(Color color) {
        this.lineColor = color;
        this.notifyObservers();
    }

    public void setLineThickness(double thickness) {
        this.lineThickness = thickness;
        this.notifyObservers();
    }

    public void newStroke() {
        this.curStroke = new Stroke();
        this.strokeTime = 0;
        if(this.strokeTimer != null) {
            this.strokeTimer.cancel();
        }
        this.strokeTimer = new Timer();
        this.strokeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DoodleModel.this.strokeTime++;
            }
        }, 10, 10);
        this.notifyObservers();
    }

    public void endStroke() {
        this.strokes.add(curStroke);
        this.curStroke = null;
        if(this.strokeTimer != null) {
            this.strokeTimer.cancel();
            this.strokeTimer = null;
        }
        this.notifyObservers();
    }

    public void playDoodle() {
        if(!this.playing && this.getNumTotalStrokes() > 0) {
            if(futureStrokes.isEmpty()) {
                this.setDoodleStrokes(0);
            }
            this.playing = true;
            this.startPlay = true;
            this.notifyObservers();
        }
    }

    public void stopPlaying() {
        this.playing = false;
        this.notifyObservers();
    }

    public void setDoodleStrokes(int strokeNum) {
        if(this.strokes.size() > strokeNum) {
            ArrayList<Stroke> tempStrokes = new ArrayList<>(this.strokes.subList(strokeNum, this.strokes.size()));
            tempStrokes.addAll(this.futureStrokes);
            this.strokes = new ArrayList<>(this.strokes.subList(0, strokeNum));
            this.futureStrokes = tempStrokes;
        } else if (this.strokes.size() < strokeNum) {
            int numStrokes = this.strokes.size();
            this.strokes.addAll(this.futureStrokes.subList(0, strokeNum - numStrokes));
            this.futureStrokes = new ArrayList<>(this.futureStrokes.subList(strokeNum - numStrokes, this.futureStrokes.size()));
        }
        this.notifyObservers();
    }

    public void addLine(Point prevPoint, Point newPoint) {
        this.addLine(new Line(prevPoint, newPoint, this.getLineColor(), this.getLineThickness(), this.strokeTime));
    }

    public void addLine(Line line) {
        if(this.curStroke != null) {
            this.stopPlaying();
            this.curStroke.addLine(line);
            if(!this.futureStrokes.isEmpty()) {
                this.futureStrokes = new ArrayList<>();
            }
            if(line.getSrc().x + Constants.DEFAULT_CANVAS_WIDTH / 3 > doodleSize.width) {
                doodleSize.width = line.getSrc().x + (int)(Constants.DEFAULT_CANVAS_WIDTH / 3);
            }
            if(line.getSrc().y + Constants.DEFAULT_CANVAS_HEIGHT / 3 > doodleSize.height) {
                doodleSize.height = line.getSrc().y + (int)(Constants.DEFAULT_CANVAS_HEIGHT / 3);
            }
            if(line.getDest().x + Constants.DEFAULT_CANVAS_WIDTH / 3 > doodleSize.width) {
                doodleSize.width = line.getDest().x + (int)(Constants.DEFAULT_CANVAS_WIDTH / 3);
            }
            if(line.getDest().y + Constants.DEFAULT_CANVAS_HEIGHT / 3 > doodleSize.height) {
                doodleSize.height = line.getDest().y + (int)(Constants.DEFAULT_CANVAS_HEIGHT / 3);
            }
            this.notifyObservers();
        }
    }

    public void resetDoodle() {
        this.strokes = new ArrayList<>();
        this.futureStrokes = new ArrayList<>();
        this.curStroke = null;
        this.doodleSize = new Dimension(0, 0);
        this.notifyObservers();
    }

    // PRIVATE

    private ArrayList<Observer> observers;
    private ArrayList<Stroke> strokes;
    private ArrayList<Stroke> futureStrokes;
    private Stroke curStroke;
    private Color lineColor;
    private double lineThickness;
    private Dimension doodleSize;
    private Timer strokeTimer;
    private long strokeTime;
    private boolean playing;
    private boolean startPlay;
    private boolean actualSize;

    private void notifyObservers() {
        for(Observer obs : this.observers) {
            obs.update();
        }
    }
}
