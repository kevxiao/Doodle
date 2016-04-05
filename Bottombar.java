import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bottombar extends Observer {

    // PUBLIC

    public Bottombar(DoodleModel model, Dimension height) {
        super(model);
        this.doodleModel = model;
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.black, (int)Doodle.ratioDPI));
        this.setPreferredSize(height);
        this.setMinimumSize(height);
        this.setMaximumSize(height);

        this.playListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bottombar.this.doodleModel.playDoodle();
                Bottombar.this.playButton.setText("Pause");
                for(ActionListener listener : Bottombar.this.playButton.getActionListeners()) {
                    Bottombar.this.playButton.removeActionListener(listener);
                }
                Bottombar.this.playButton.addActionListener(Bottombar.this.pauseListener);
            }
        };

        this.pauseListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bottombar.this.doodleModel.stopPlaying();
                Bottombar.this.playButton.setText("Play");
                for(ActionListener listener : Bottombar.this.playButton.getActionListeners()) {
                    Bottombar.this.playButton.removeActionListener(listener);
                }
                Bottombar.this.playButton.addActionListener(Bottombar.this.playListener);
            }
        };

        this.initUI();
    }

    public void resize() {
        if(this.getWidth() < this.playButton.getWidth() + this.getPreferredSize().height * 2 + this.startButton.getWidth() + this.endButton.getWidth()) {
            this.remove(this.startButton);
            this.remove(this.endButton);
        } else {
            this.add(this.startButton);
            this.add(this.endButton);
        }
    }

    @Override
    public void update() {
        if(this.slider.getMaximum() != this.doodleModel.getNumPrevStrokes()) {
            this.slider.setMaximum(this.doodleModel.getNumTotalStrokes());
        }
        if(this.slider.getValue() != this.doodleModel.getNumPrevStrokes()) {
            this.slider.setValue(this.doodleModel.getNumPrevStrokes());
            sliderVal = this.slider.getValue();
        }
        if(!this.doodleModel.isPlaying() && this.playButton.getText().equals("Pause")) {
            this.playButton.setText("Play");
            for(ActionListener listener : this.playButton.getActionListeners()) {
                this.playButton.removeActionListener(listener);
            }
            this.playButton.addActionListener(this.playListener);
        }
    }

    // PRIVATE

    private DoodleModel doodleModel;
    private JButton playButton;
    private JSlider slider;
    private JButton startButton;
    private JButton endButton;
    private int sliderVal;

    private ActionListener playListener;
    private ActionListener pauseListener;

    private void initUI() {
        this.playButton = new JButton("Play");
        this.playButton.addActionListener(playListener);
        this.add(this.playButton);

        this.slider = new JSlider(JSlider.HORIZONTAL, 0, 0, 0);
        this.sliderVal = 0;
        this.slider.setMinorTickSpacing(1);
        this.slider.setPaintTicks(true);
        this.slider.setSnapToTicks(true);
        this.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(Bottombar.this.sliderVal != Bottombar.this.slider.getValue()) {
                    Bottombar.this.sliderVal = Bottombar.this.slider.getValue();
                    Bottombar.this.doodleModel.setDoodleStrokes(Bottombar.this.slider.getValue());
                }
            }
        });
        this.add(this.slider);

        this.startButton = new JButton("Start");
        this.startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bottombar.this.slider.setValue(0);
            }
        });
        this.add(this.startButton);

        this.endButton = new JButton("End");
        this.endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bottombar.this.slider.setValue(Bottombar.this.doodleModel.getNumTotalStrokes());
            }
        });
        this.add(this.endButton);
    }
}
