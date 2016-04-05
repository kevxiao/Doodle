import java.awt.*;

public class Constants {

    // PUBLIC

    public static final int DEFAULT_DPI = 96;
    public static final double MIN_WIDTH = 200;
    public static final double MIN_HEIGHT = 180;
    public static final double DEFAULT_DIMENTIONS_RATIO = 1.5;
    public static final Color DEFAULT_LINE_COLOR = Color.black;
    public static final double DEFAULT_LINE_THICKNESS = 3;
    public static final double DEFAULT_CANVAS_WIDTH = 400;
    public static final double DEFAULT_CANVAS_HEIGHT = 300;
    public static final Color CANVAS_BACKGROUND_COLOR = Color.white;

    // PRIVATE

    // prevent creating an instance of this class
    private Constants(){
        throw new AssertionError();
    }
}
