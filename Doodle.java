import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Doodle extends JFrame implements ClipboardOwner{

    // PUBLIC

    public static final double ratioDPI = Toolkit.getDefaultToolkit().getScreenResolution() / Constants.DEFAULT_DPI;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Doodle();
            }
        });
    }

    public Doodle() {
        this.setTitle("Doodle");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setResizable(true);

        this.setPreferredSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().width / Constants.DEFAULT_DIMENTIONS_RATIO), (int)(Toolkit.getDefaultToolkit().getScreenSize().height / Constants.DEFAULT_DIMENTIONS_RATIO)));
        this.setMinimumSize(new Dimension((int)(Doodle.ratioDPI * Constants.MIN_WIDTH), (int)(Doodle.ratioDPI * Constants.MIN_HEIGHT)));

        Font menuFont = (Font)UIManager.get("Menu.font");
        Font f = new Font(menuFont.getFontName(), menuFont.getStyle(), (int)(menuFont.getSize() * Doodle.ratioDPI));
        Color color = new Color(240, 240, 240);
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while(keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if(value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
            if(value != null && value instanceof javax.swing.plaf.ColorUIResource && key.toString().contains(".background")) {
                UIManager.put(key, color);
            }
        }
        UIManager.put("ColorChooser.swatchesRecentSwatchSize", new Dimension((int)(10 * Doodle.ratioDPI), (int)(10 * Doodle.ratioDPI)));
        UIManager.put("ColorChooser.swatchesSwatchSize", new Dimension((int)(10 * Doodle.ratioDPI), (int)(10 * Doodle.ratioDPI)));

        this.doodleModel = new DoodleModel();
        this.initUI();
    }

    public void lostOwnership( Clipboard clip, Transferable trans ) {

    }

    // PRIVATE

    private DoodleModel doodleModel;
    private Sidebar sidebar;
    private Bottombar bottombar;
    private DrawArea drawArea;

    private void initUI() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem itemNew = new JMenuItem("New");
        itemNew.setMnemonic(KeyEvent.VK_N);
        itemNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Doodle.this.doodleModel.hasChanged()) {
                    int result = JOptionPane.showConfirmDialog(Doodle.this, "Discard current changes?", "Discard", JOptionPane.YES_NO_OPTION);
                    if(result != 0) {
                        return;
                    }
                }
                Doodle.this.doodleModel.resetDoodle();
            }
        });
        fileMenu.add(itemNew);
        JMenuItem itemOpen = new JMenuItem("Open");
        itemOpen.setMnemonic(KeyEvent.VK_O);
        itemOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Doodle.this.doodleModel.hasChanged()) {
                    int result = JOptionPane.showConfirmDialog(Doodle.this, "Discard current changes?", "Discard", JOptionPane.YES_NO_OPTION);
                    if(result != 0) {
                        return;
                    }
                }
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text/Doodle Files (.txt/.doodle)", "txt", "doodle"));
                int returnVal = fileChooser.showOpenDialog(Doodle.this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooser.getSelectedFile().getName();
                    if(filename.substring(filename.lastIndexOf("."), filename.length()).equals(".txt")) {
                        File file = fileChooser.getSelectedFile();
                        try {
                            Doodle.this.doodleModel.resetDoodle();
                            Scanner scanner = new Scanner(file);
                            int numStrokes = scanner.nextInt();
                            int numLines;
                            Stroke newStroke;
                            for (int i = 0; i < numStrokes; ++i) {
                                Doodle.this.doodleModel.newStroke();
                                numLines = scanner.nextInt();
                                for (int j = 0; j < numLines; ++j) {
                                    Doodle.this.doodleModel.addLine(new Line(new Point((int) scanner.nextDouble(), (int) scanner.nextDouble()), new Point((int) scanner.nextDouble(), (int) scanner.nextDouble()), new Color(scanner.nextInt()), scanner.nextDouble(), scanner.nextLong()));
                                }
                                Doodle.this.doodleModel.endStroke();
                            }
                            int numFutureStrokes = scanner.nextInt();
                            for (int i = 0; i < numFutureStrokes; ++i) {
                                Doodle.this.doodleModel.newStroke();
                                numLines = scanner.nextInt();
                                for (int j = 0; j < numLines; ++j) {
                                    Doodle.this.doodleModel.addLine(new Line(new Point((int) scanner.nextDouble(), (int) scanner.nextDouble()), new Point((int) scanner.nextDouble(), (int) scanner.nextDouble()), new Color(scanner.nextInt()), scanner.nextDouble(), scanner.nextLong()));
                                }
                                Doodle.this.doodleModel.endStroke();
                            }
                            Doodle.this.doodleModel.setDoodleStrokes(numStrokes);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    } else if(filename.substring(filename.lastIndexOf("."), filename.length()).equals(".doodle")) {
                        try {
                            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()));
                            @SuppressWarnings("unchecked")
                            ArrayList<Stroke> strokes = (ArrayList<Stroke>) oin.readObject();
                            @SuppressWarnings("unchecked")
                            ArrayList<Stroke> futureStrokes = (ArrayList<Stroke>) oin.readObject();
                            for(Stroke stroke : strokes) {
                                Doodle.this.doodleModel.newStroke();
                                for(Line line : stroke.getLines()) {
                                    Doodle.this.doodleModel.addLine(line);
                                }
                                Doodle.this.doodleModel.endStroke();
                            }
                            for(Stroke stroke : futureStrokes) {
                                Doodle.this.doodleModel.newStroke();
                                for(Line line : stroke.getLines()) {
                                    Doodle.this.doodleModel.addLine(line);
                                }
                                Doodle.this.doodleModel.endStroke();
                            }
                            Doodle.this.doodleModel.setDoodleStrokes(strokes.size());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        fileMenu.add(itemOpen);
        JMenuItem itemSave = new JMenuItem("Save");
        itemSave.setMnemonic(KeyEvent.VK_S);
        itemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Doodle File (.doodle)", "doodle"));
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text File (.txt)", "txt"));
                int returnVal = fileChooser.showOpenDialog(Doodle.this);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        String filename = fileChooser.getSelectedFile().getName();
                        if(((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0].equals("txt")) {
                            FileWriter fileWriter;
                            if(filename.lastIndexOf(".") != -1 && filename.substring(filename.lastIndexOf("."), filename.length()).equals("." + ((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0])) {
                                fileWriter = new FileWriter(fileChooser.getSelectedFile());
                            } else {
                                fileWriter = new FileWriter(fileChooser.getSelectedFile() + "." + ((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0]);
                            }
                            int numStrokes = Doodle.this.doodleModel.getNumPrevStrokes();
                            String output = Integer.toString(numStrokes) + " ";
                            for (Stroke stroke : Doodle.this.doodleModel.getStrokes()) {
                                output += Integer.toString(stroke.getLines().size()) + " ";
                                for (Line line : stroke.getLines()) {
                                    output += Double.toString(line.getSrc().getX()) + " " + Double.toString(line.getSrc().getY())
                                            + " " + Double.toString(line.getDest().getX()) + " " + Double.toString(line.getDest().getY())
                                            + " " + Integer.toString(line.getColor().getRGB()) + " " + Double.toString(line.getThickness())
                                            + " " + Long.toString(line.getTime()) + " ";
                                }
                            }
                            output += "\n";
                            numStrokes = Doodle.this.doodleModel.getNumTotalStrokes() - Doodle.this.doodleModel.getNumPrevStrokes();
                            output += Integer.toString(numStrokes) + " ";
                            for (Stroke stroke : Doodle.this.doodleModel.getFutureStrokes()) {
                                output += Integer.toString(stroke.getLines().size()) + " ";
                                for (Line line : stroke.getLines()) {
                                    output += Double.toString(line.getSrc().getX()) + " " + Double.toString(line.getSrc().getY())
                                            + " " + Double.toString(line.getDest().getX()) + " " + Double.toString(line.getDest().getY())
                                            + " " + Integer.toString(line.getColor().getRGB()) + " " + Double.toString(line.getThickness())
                                            + " " + Long.toString(line.getTime()) + " ";
                                }
                            }
                            fileWriter.write(output);
                            fileWriter.close();
                        } else if (((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0].equals("doodle")) {
                            try {
                                FileOutputStream fout;
                                if(filename.lastIndexOf(".") != -1 && filename.substring(filename.lastIndexOf("."), filename.length()).equals("." + ((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0])) {
                                    fout = new FileOutputStream(fileChooser.getSelectedFile());
                                } else {
                                    fout = new FileOutputStream(fileChooser.getSelectedFile() + "." + ((FileNameExtensionFilter)fileChooser.getFileFilter()).getExtensions()[0]);
                                }
                                ObjectOutputStream oout = new ObjectOutputStream(fout);
                                oout.writeObject(Doodle.this.doodleModel.getStrokes());
                                oout.writeObject(Doodle.this.doodleModel.getFutureStrokes());
                                oout.close();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
        fileMenu.add(itemSave);
        fileMenu.addSeparator();
        JMenuItem itemExit = new JMenuItem("Exit");
        itemExit.setMnemonic(KeyEvent.VK_X);
        itemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Doodle.this.doodleModel.hasChanged()) {
                    int result = JOptionPane.showConfirmDialog(Doodle.this, "Discard current changes?", "Discard", JOptionPane.YES_NO_OPTION);
                    if(result != 0) {
                        return;
                    }
                }
                Doodle.this.dispatchEvent(new WindowEvent(Doodle.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        fileMenu.add(itemExit);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        JMenuItem itemCopy = new JMenuItem("Copy");
        itemCopy.setMnemonic(KeyEvent.VK_C);
        editMenu.add(itemCopy);
        menuBar.add(editMenu);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        JMenuItem radioItemFull = new JRadioButtonMenuItem("Full Size");
        radioItemFull.setMnemonic(KeyEvent.VK_U);
        viewMenu.add(radioItemFull);
        JMenuItem radioItemFit = new JRadioButtonMenuItem("Fit Window");
        radioItemFit.setMnemonic(KeyEvent.VK_I);
        viewMenu.add(radioItemFit);
        radioItemFull.setSelected(true);
        radioItemFull.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radioItemFull.setSelected(true);
                radioItemFit.setSelected(false);
                Doodle.this.doodleModel.setActualSize(true);
            }
        });
        radioItemFit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                radioItemFit.setSelected(true);
                radioItemFull.setSelected(false);
                Doodle.this.doodleModel.setActualSize(false);
            }
        });
        menuBar.add(viewMenu);

        JPanel layoutPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.VERTICAL;

        this.sidebar = new Sidebar(this.doodleModel, new Dimension(this.getMinimumSize().width / 2, 0));
        layoutPanel.add(this.sidebar, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weighty = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.fill = GridBagConstraints.HORIZONTAL;

        this.bottombar = new Bottombar(this.doodleModel, new Dimension(0, this.getMinimumSize().height / 4));
        layoutPanel.add(this.bottombar, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        this.drawArea = new DrawArea(this.doodleModel, new Dimension((int)(Constants.DEFAULT_CANVAS_WIDTH * Doodle.ratioDPI), (int)(Constants.DEFAULT_CANVAS_HEIGHT * Doodle.ratioDPI)));
        this.drawArea.setPreferredSize(this.drawArea.getCanvasSize());
        this.drawArea.setMaximumSize(this.drawArea.getCanvasSize());
        JScrollPane scrollPane = new JScrollPane(drawArea);
        layoutPanel.add(scrollPane, c);

        itemCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage bi = Doodle.this.drawArea.getImage();
                TransferableImage transfer = new TransferableImage( bi );
                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                clip.setContents(transfer, Doodle.this);
            }
        });

        this.addComponentListener(new ComponentAdapter()
        {
            public void componentResized(ComponentEvent evt) {
                if(Doodle.this.sidebar != null) {
                    sidebar.resize();
                    bottombar.resize();
                }
            }
        });

        this.setJMenuBar(menuBar);
        this.add(layoutPanel);
        this.pack();
        this.setVisible(true);
    }

    private class TransferableImage implements Transferable {

        Image i;

        public TransferableImage( Image i ) {
            this.i = i;
        }

        @Override
        public Object getTransferData( DataFlavor flavor )
                throws UnsupportedFlavorException, IOException {
            if ( flavor.equals( DataFlavor.imageFlavor ) && i != null ) {
                return i;
            }
            else {
                throw new UnsupportedFlavorException( flavor );
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[ 1 ];
            flavors[ 0 ] = DataFlavor.imageFlavor;
            return flavors;
        }

        @Override
        public boolean isDataFlavorSupported( DataFlavor flavor ) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for(DataFlavor flav : flavors ) {
                if ( flavor.equals( flav ) ) {
                    return true;
                }
            }

            return false;
        }
    }
}
