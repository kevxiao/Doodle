import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class DrawArea extends Observer {

    // PUBLIC

    public DrawArea(DoodleModel model, Dimension size) {
        super(model);
        this.doodleModel = model;
        this.previousPoint = null;
        this.playTimer = null;
        this.playTime = 0;
        this.defaultCanvasSize = new Dimension(size);
        this.setBackground(Constants.CANVAS_BACKGROUND_COLOR);
        this.setCanvasSize(size);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                    DrawArea.this.doodleModel.newStroke();
                    if(DrawArea.this.doodleModel.isActualSize()) {
                        DrawArea.this.previousPoint = new Point((int) (e.getPoint().x / Doodle.ratioDPI), (int) (e.getPoint().y / Doodle.ratioDPI));
                    } else {
                        double ratio;
                        if (DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight() < DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth()) {
                            ratio = DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight();
                        } else {
                            ratio = DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth();
                        }
                        DrawArea.this.previousPoint = new Point((int) (e.getPoint().x / Doodle.ratioDPI / ratio), (int) (e.getPoint().y / Doodle.ratioDPI / ratio));
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(DrawArea.this.previousPoint != null) {
                    if(DrawArea.this.doodleModel.isActualSize()) {
                        DrawArea.this.addLine(DrawArea.this.previousPoint, new Point((int) (e.getPoint().x / Doodle.ratioDPI), (int) (e.getPoint().y / Doodle.ratioDPI)));
                    } else {
                        double ratio;
                        if (DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight() < DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth()) {
                            ratio = DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight();
                        } else {
                            ratio = DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth();
                        }
                        DrawArea.this.addLine(DrawArea.this.previousPoint, new Point((int) (e.getPoint().x / Doodle.ratioDPI / ratio), (int) (e.getPoint().y / Doodle.ratioDPI / ratio)));
                    }
                    DrawArea.this.doodleModel.endStroke();
                    DrawArea.this.previousPoint = null;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                    DrawArea.this.doodleModel.newStroke();
                    if(DrawArea.this.doodleModel.isActualSize()) {
                        DrawArea.this.previousPoint = new Point((int) (e.getPoint().x / Doodle.ratioDPI), (int) (e.getPoint().y / Doodle.ratioDPI));
                    } else {
                        double ratio;
                        if (DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight() < DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth()) {
                            ratio = DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight();
                        } else {
                            ratio = DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth();
                        }
                        DrawArea.this.previousPoint = new Point((int) (e.getPoint().x / Doodle.ratioDPI / ratio), (int) (e.getPoint().y / Doodle.ratioDPI / ratio));
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(DrawArea.this.previousPoint != null) {
                    if(DrawArea.this.doodleModel.isActualSize()) {
                        DrawArea.this.addLine(DrawArea.this.previousPoint, new Point((int) (e.getPoint().x / Doodle.ratioDPI), (int) (e.getPoint().y / Doodle.ratioDPI)));
                    } else {
                        double ratio;
                        if (DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight() < DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth()) {
                            ratio = DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight();
                        } else {
                            ratio = DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth();
                        }
                        DrawArea.this.addLine(DrawArea.this.previousPoint, new Point((int) (e.getPoint().x / Doodle.ratioDPI / ratio), (int) (e.getPoint().y / Doodle.ratioDPI / ratio)));
                    }
                    DrawArea.this.doodleModel.endStroke();
                    DrawArea.this.previousPoint = null;
                }
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if(DrawArea.this.previousPoint != null) {
                    Point newPoint;
                    if(DrawArea.this.doodleModel.isActualSize()) {
                        newPoint = new Point((int) (e.getPoint().x / Doodle.ratioDPI), (int) (e.getPoint().y / Doodle.ratioDPI));
                    } else {
                        double ratio;
                        if(DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight() < DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth()) {
                            ratio = DrawArea.this.getSize().getHeight() / DrawArea.this.getCanvasSize().getHeight();
                        } else {
                            ratio = DrawArea.this.getSize().getWidth() / DrawArea.this.getCanvasSize().getWidth();
                        }
                        newPoint = new Point((int) (e.getPoint().x / Doodle.ratioDPI / ratio), (int) (e.getPoint().y / Doodle.ratioDPI / ratio));
                    }
                    DrawArea.this.addLine(DrawArea.this.previousPoint, newPoint);
                    DrawArea.this.previousPoint = newPoint;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
    }

    public Dimension getCanvasSize() {
        return new Dimension(this.canvasSize);
    }

    public BufferedImage getImage() {
        BufferedImage bi;
        if(this.doodleModel.isActualSize()) {
            bi = new BufferedImage(this.getCanvasSize().width, this.getCanvasSize().height, BufferedImage.TYPE_INT_ARGB);
        } else {
            bi = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_ARGB);
        }
        this.paint(bi.getGraphics());
        return bi;
    }

    @Override
    public void update() {
        if(this.doodleModel.isActualSize()) {
            if (this.canvasSize.width < (int) (this.doodleModel.getDoodleSize().width * Doodle.ratioDPI)) {
                this.canvasSize.width = (int) (this.doodleModel.getDoodleSize().width * Doodle.ratioDPI);
                this.setPreferredSize(this.getCanvasSize());
                this.setMaximumSize(this.getCanvasSize());
            }
            if (this.canvasSize.height < (int) (this.doodleModel.getDoodleSize().height * Doodle.ratioDPI)) {
                this.canvasSize.height = (int) (this.doodleModel.getDoodleSize().height * Doodle.ratioDPI);
                this.setPreferredSize(this.getCanvasSize());
                this.setMaximumSize(this.getCanvasSize());
            }
        } else {
            if(this.doodleModel.getDoodleSize().width == 0 || this.doodleModel.getDoodleSize().height == 0) {
                this.setCanvasSize(defaultCanvasSize);
            }
            this.setPreferredSize(new Dimension(0, 0));
        }
        this.revalidate();
        this.repaint();
    }

    // Private

    private DoodleModel doodleModel;
    private Point previousPoint;
    private final Dimension defaultCanvasSize;
    private Dimension canvasSize;
    private Timer playTimer;
    private long playTime;

    private void addLine(Point prevPoint, Point newPoint) {
        this.doodleModel.addLine(prevPoint, newPoint);
    }

    private void paintLine(Graphics2D g2d, Line line) {
        if(this.doodleModel.isActualSize()) {
            g2d.setStroke(new BasicStroke((float) (line.getThickness() * Doodle.ratioDPI), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(line.getColor());
            g2d.drawLine((int) (line.getSrc().x * Doodle.ratioDPI), (int) (line.getSrc().y * Doodle.ratioDPI), (int) (line.getDest().x * Doodle.ratioDPI), (int) (line.getDest().y * Doodle.ratioDPI));
        } else {
            double ratio;
            if(this.getSize().getHeight() / this.getCanvasSize().getHeight() < this.getSize().getWidth() / this.getCanvasSize().getWidth()) {
                ratio = this.getSize().getHeight() / this.getCanvasSize().getHeight();
            } else {
                ratio = this.getSize().getWidth() / this.getCanvasSize().getWidth();
            }
            g2d.setStroke(new BasicStroke((float) (line.getThickness() * Doodle.ratioDPI * ratio), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(line.getColor());
            g2d.drawLine((int) (line.getSrc().x * Doodle.ratioDPI * ratio), (int) (line.getSrc().y * Doodle.ratioDPI * ratio), (int) (line.getDest().x * Doodle.ratioDPI * ratio), (int) (line.getDest().y * Doodle.ratioDPI * ratio));
        }
    }

    private void setCanvasSize(Dimension size) {
        this.canvasSize = size;
    }

    // PROTECTED

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        if(this.doodleModel.willStartPlaying()) {
            if(this.playTimer != null) {
                this.playTimer.cancel();
            }
            this.playTimer = new Timer();
            this.playTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    DrawArea.this.playTime++;
                    DrawArea.this.repaint();
                }
            }, 10, 10);
            if(this.doodleModel.getNumTotalStrokes() > 0) {
                this.doodleModel.setDoodleStrokes(this.doodleModel.getNumPrevStrokes() + 1);
            } else {
                this.doodleModel.stopPlaying();
            }
        }
        if(this.doodleModel.isPlaying()) {
            boolean nextStroke = false;
            for(Stroke stroke : this.doodleModel.getStrokes().subList(0, this.doodleModel.getNumPrevStrokes() - 1)) {
                for(Line line : stroke.getLines()) {
                    this.paintLine(g2d, line);
                }
            }
            Stroke lastStroke = this.doodleModel.getStrokes().get(this.doodleModel.getNumPrevStrokes() - 1);
            for (Line line : lastStroke.getLines()) {
                if (line.getTime() <= this.playTime) {
                    this.paintLine(g2d, line);
                    if (line == lastStroke.getLines().get(lastStroke.getLines().size() - 1)) {
                        nextStroke = true;
                    }
                }
            }
            if(nextStroke) {
                if(this.doodleModel.getNumPrevStrokes() < this.doodleModel.getNumTotalStrokes()) {
                    this.doodleModel.setDoodleStrokes(this.doodleModel.getNumPrevStrokes() + 1);
                    this.playTime = 0;
                } else {
                    this.doodleModel.stopPlaying();
                    this.playTime = 0;
                    this.playTimer.cancel();
                    this.playTimer = null;
                }
            }
        } else {
            if(this.playTimer != null) {
                this.playTimer.cancel();
                this.playTimer = null;
            }
            for(Stroke stroke : this.doodleModel.getStrokes()) {
                for(Line line : stroke.getLines()) {
                    this.paintLine(g2d, line);
                }
            }
            if(this.doodleModel.getCurrentStroke() != null) {
                for(Line line : this.doodleModel.getCurrentStroke().getLines()) {
                    this.paintLine(g2d, line);
                }
            }
        }
    }
}
