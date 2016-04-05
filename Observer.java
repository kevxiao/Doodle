import javax.swing.*;

abstract class Observer extends JPanel {

    // PUBLIC

    public Observer(DoodleModel model) {
        model.addObserver(this);
    }

    abstract public void update();
}
