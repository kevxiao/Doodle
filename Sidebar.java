import javafx.geometry.Side;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class Sidebar extends Observer{

    // PUBLIC

    public Sidebar(DoodleModel model, Dimension width) {
        super(model);
        this.doodleModel = model;
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black, (int)Doodle.ratioDPI));
        this.setPreferredSize(width);
        this.setMinimumSize(width);
        this.setMaximumSize(width);
        this.initUI();
    }

    public void resize() {
        if(this.getHeight() < this.colorPickerButton.getPreferredSize().height + this.colorLabel.getPreferredSize().height + this.thicknessLabel.getPreferredSize().height + this.thicknessSlider.getPreferredSize().height + this.getPreferredSize().width * 7 / 5) {
            this.remove(this.colorChooser);
            this.remove(this.colorLabel);
            this.colorPickerButton.setText("Pick Color");
        } else {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.anchor = GridBagConstraints.PAGE_START;
            this.add(this.colorLabel, c);

            c.gridy = 1;
            this.add(this.colorChooser, c);

            this.colorPickerButton.setText("More Colors");
        }
    }

    @Override
    public void update() {
        this.colorChooser.setColor(doodleModel.getLineColor());
    }

    // PRIVATE

    private DoodleModel doodleModel;
    private JColorChooser colorChooser;
    private CustomChooserPanel chooserPanel;
    private JButton colorPickerButton;
    private JLabel colorLabel;
    private JLabel thicknessLabel;
    private JSlider thicknessSlider;

    private void initUI() {
        this.colorChooser = new JColorChooser(Color.black);
        this.colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Sidebar.this.doodleModel.setLineColor(Sidebar.this.colorChooser.getColor());
            }
        });
        this.colorPickerButton = new JButton("More Colors");
        this.colorPickerButton.setBackground(Color.white);
        this.colorPickerButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, (int)Doodle.ratioDPI), BorderFactory.createLineBorder(Color.white, (int)(3 * Doodle.ratioDPI))));
        this.colorPickerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(SwingUtilities.getWindowAncestor(Sidebar.this), "Color Picker", Sidebar.this.doodleModel.getLineColor());
                if(newColor != null) {
                    Sidebar.this.doodleModel.setLineColor(newColor);
                    colorPickerButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, (int)Doodle.ratioDPI), BorderFactory.createLineBorder(newColor, (int)(3 * Doodle.ratioDPI))));
                    Sidebar.this.chooserPanel.deselect();
                }
            }
        });
        this.colorChooser.setPreviewPanel(new JPanel());
        AbstractColorChooserPanel[] oldPanels = colorChooser.getChooserPanels();
        for (AbstractColorChooserPanel panel : oldPanels) {
            this.colorChooser.removeChooserPanel(panel);
        }

        this.chooserPanel = new CustomChooserPanel();

        this.colorChooser.addChooserPanel(chooserPanel);
        this.colorChooser.setPreferredSize(this.getPreferredSize());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        this.colorLabel = new JLabel("Pick a Color");
        this.colorLabel.setPreferredSize(new Dimension(this.getPreferredSize().width, (int)(20 * Doodle.ratioDPI)));
        this.add(this.colorLabel, c);

        c.gridy = 1;
        c.ipady = (int)(2 * Doodle.ratioDPI);
        this.add(this.colorChooser, c);

        c.gridy = 2;
        //c.weighty = 1;
        this.colorPickerButton.setPreferredSize(new Dimension(this.getPreferredSize().width, (int)(30 * Doodle.ratioDPI)));
        this.add(this.colorPickerButton, c);

        c.gridy = 3;
        this.thicknessLabel = new JLabel("Line Thickness");
        this.thicknessLabel.setPreferredSize(new Dimension(this.getPreferredSize().width, (int)(20 * Doodle.ratioDPI)));
        this.add(this.thicknessLabel, c);

        c.gridy = 4;
        c.weighty = 1;
        this.thicknessSlider = new JSlider(JSlider.HORIZONTAL, 1, 21, (int)Constants.DEFAULT_LINE_THICKNESS);
        this.thicknessSlider.setMajorTickSpacing(5);
        this.thicknessSlider.setMinorTickSpacing(1);
        this.thicknessSlider.setPaintTicks(true);
        this.thicknessSlider.setSnapToTicks(true);
        this.thicknessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Sidebar.this.doodleModel.setLineThickness(Sidebar.this.thicknessSlider.getValue());
            }
        });
        this.thicknessSlider.setPreferredSize(new Dimension(this.getPreferredSize().width, (int)(30 * Doodle.ratioDPI)));
        this.add(this.thicknessSlider, c);
    }

    private class CustomChooserPanel extends AbstractColorChooserPanel {
        @Override
        public void updateChooser() {

        }

        @Override
        protected void buildChooser() {
            this.setLayout(new GridLayout(7, 5));
            this.selectButton(addButton("Black", Color.black));
            addButton("Gray1", Color.darkGray);
            addButton("Gray2", Color.gray);
            addButton("Gray3", Color.lightGray);
            addButton("White", Color.white);
            addButton("Red1", new Color(105, 0, 0));
            addButton("Red2", new Color(180, 0, 0));
            addButton("Red3", new Color(255, 0, 0));
            addButton("Red4", new Color(255, 75, 75));
            addButton("Red5", new Color(255, 150, 150));
            addButton("Yellow1", new Color(75, 30, 0));
            addButton("Yellow2", new Color(255, 100, 0));
            addButton("Yellow3", new Color(255, 185, 0));
            addButton("Yellow4", new Color(255, 255, 0));
            addButton("Yellow5", new Color(255, 255, 175));
            addButton("Green1", new Color(0, 105, 0));
            addButton("Green2", new Color(0, 180, 0));
            addButton("Green3", new Color(0, 255, 0));
            addButton("Green4", new Color(75, 255, 75));
            addButton("Green5", new Color(150, 255, 105));
            addButton("Cyan1", new Color(0, 75, 75));
            addButton("Cyan2", new Color(0, 165, 165));
            addButton("Cyan3", new Color(0, 255, 255));
            addButton("Cyan4", new Color(100, 255, 255));
            addButton("Cyan5", new Color(200, 255, 255));
            addButton("Blue1", new Color(0, 0, 105));
            addButton("Blue2", new Color(0, 0, 180));
            addButton("Blue3", new Color(0, 0, 255));
            addButton("Blue4", new Color(75, 75, 255));
            addButton("Blue5", new Color(150, 150, 255));
            addButton("Purple1", new Color(30, 0, 75));
            addButton("Purple2", new Color(100, 0, 180));
            addButton("Purple3", new Color(150, 0, 240));
            addButton("Purple4", new Color(255, 0, 255));
            addButton("Purple5", new Color(255, 175, 255));
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        @Override
        public Icon getSmallDisplayIcon() {
            return null;
        }

        @Override
        public Icon getLargeDisplayIcon() {
            return null;
        }

        public void deselect() {
            if(selectedBtn != null) {
                selectedBtn.setBorder(null);
                selectedBtn = null;
            }
        }

        // PRIVATE

        private JButton selectedBtn;

        private JButton addButton(String name, Color color) {
            JButton button = new JButton(name);
            button.setBackground(color);
            button.setBorder(null);
            button.setAction(new AbstractAction() {
                public void actionPerformed(ActionEvent evt) {
                    selectButton((JButton)evt.getSource());
                    getColorSelectionModel().setSelectedColor(selectedBtn.getBackground());
                }
            });
            button.setPreferredSize(new Dimension(Sidebar.this.getPreferredSize().width / 5, Sidebar.this.getPreferredSize().width / 5));
            this.add(button);
            return button;
        }

        private void selectButton(JButton button) {
            if(selectedBtn != null) {
                selectedBtn.setBorder(null);
            }
            colorPickerButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, (int)Doodle.ratioDPI), BorderFactory.createLineBorder(Color.white, (int)(3 * Doodle.ratioDPI))));
            selectedBtn = button;
            selectedBtn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black, (int)Doodle.ratioDPI), BorderFactory.createLineBorder(Color.white, (int)Doodle.ratioDPI)));
        }
    }
}
