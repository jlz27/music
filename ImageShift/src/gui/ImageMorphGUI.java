package gui;

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import algorithms.ContrastMorph;
import algorithms.RandomMorph;
import algorithms.ThreshholdSegment;

public class ImageMorphGUI extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	public static final int IMAGE_WIDTH = 400;
	public static final int IMAGE_HEIGHT = 300;
	private static final int MENU_HEIGHT = 25;

	private ImageComponent randomAlg, contrastAlg, blobAlg, edgeAlg;
	private JFrame fileChooser;
	private JComponent controlsBar;
	JSpinner pixelSpinner;
	JSlider slider;
	
	private File sourceFile, destFile;
	protected int sleepDelay = 5;
	
	public static void main(String[] args) {
		ImageMorphGUI gui = new ImageMorphGUI();
		javax.swing.SwingUtilities.invokeLater(gui);
	}
	
	
	private void createFileChooser() {
		fileChooser = new JFrame();
		fileChooser.setLayout(new GridLayout(3, 2));
		JButton chooseSource = new JButton("Select Source Image");
		final JLabel sourceName = new JLabel("Please Select File", JLabel.CENTER);
		JButton chooseDest = new JButton("Select Destination Image");
		final JLabel destName = new JLabel("Please Select File", JLabel.CENTER);
		final JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("/Users/Jason/Documents/MUSIC1465/project/ImageShift/resources"));
		chooseSource.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int retval = chooser.showOpenDialog(fileChooser);
				if (retval == JFileChooser.APPROVE_OPTION) {
					sourceFile = chooser.getSelectedFile();
					sourceName.setText(sourceFile.getName());
				} else {
					
				}
			}
		});
		
		chooseDest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int retval = chooser.showOpenDialog(fileChooser);
				if (retval == JFileChooser.APPROVE_OPTION) {
					destFile = chooser.getSelectedFile();
					destName.setText(destFile.getName());
				} else {
					
				}
			}
		});
		
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BufferedImage srcImg = null, destImg = null;
				try {
					srcImg = ImageIO.read(sourceFile);
					destImg = ImageIO.read(destFile);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				BufferedImage scaledSrcImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, srcImg.getType());
				Graphics2D g = scaledSrcImg.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.drawImage(srcImg, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 0, 0, srcImg.getWidth(), srcImg.getHeight(), null);
				BufferedImage scaledDestImg = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, srcImg.getType());
				g = scaledDestImg.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.drawImage(destImg, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 0, 0, destImg.getWidth(), destImg.getHeight(), null);
				g.dispose();
				ImageMorphGUI.this.setImages(scaledSrcImg, scaledDestImg);
				fileChooser.setVisible(false);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.setVisible(false);
			}
		});
		fileChooser.add(chooseSource);
		fileChooser.add(sourceName);
		fileChooser.add(chooseDest);
		fileChooser.add(destName);
		fileChooser.add(okButton);
		fileChooser.add(cancelButton);
		fileChooser.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		fileChooser.setResizable(false);
		fileChooser.setSize(400, 100);
	}
	
	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem items = new JMenuItem("New Experiment");
		items.addActionListener(new ExperimentListener());
		file.add(items);
		menuBar.add(file);
		this.setJMenuBar(menuBar);
	}
	
	private void setImages(BufferedImage srcImg, BufferedImage destImg) {
		this.blobAlg.setImage(srcImg, destImg);
		this.contrastAlg.setImage(srcImg, destImg);
		this.randomAlg.setImage(srcImg, destImg);
		this.edgeAlg.setImage(srcImg, destImg);
	}
	
	private void createControlsBar() {
		this.controlsBar = new JPanel();
		this.controlsBar.setLayout(new GridLayout(1,4));
		
		JButton morphButton = new JButton("Morph Pixels");
		JButton morphAutoButton = new JButton("Morph Automatically");
		pixelSpinner = new JSpinner();
		slider = new JSlider(1, 100, 50);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ImageMorphGUI.this.sleepDelay = slider.getValue();
			}
		});
		SpinnerNumberModel model = new SpinnerNumberModel();
		model.setValue(200);
		model.setMinimum(50);
		model.setMaximum(400);
		model.setStepSize(50);
		pixelSpinner.setModel(model);
		MorphListener morphListener = new MorphListener();
		morphButton.addActionListener(morphListener);
		morphAutoButton.addActionListener(morphListener);
		this.controlsBar.add(morphButton);
		this.controlsBar.add(pixelSpinner);
		this.controlsBar.add(morphAutoButton);
		this.controlsBar.add(slider);
	}
	
	private void createSetLayout() {
		GroupLayout layout = new GroupLayout(this.getContentPane());
		ParallelGroup row1 = layout.createParallelGroup();
		ParallelGroup row2 = layout.createParallelGroup();
		ParallelGroup col1 = layout.createParallelGroup();
		ParallelGroup col2 = layout.createParallelGroup();
		
		createControlsBar();
		row1.addComponent(randomAlg).addComponent(blobAlg);
		row2.addComponent(edgeAlg).addComponent(contrastAlg);
		
		col1.addComponent(randomAlg).addComponent(edgeAlg);
		col2.addComponent(blobAlg).addComponent(contrastAlg);
		
		
		
		layout.setHorizontalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup().addGroup(col1).addGroup(col2)).addComponent(this.controlsBar));
		layout.setVerticalGroup(layout.createSequentialGroup().addGroup(row1).addGroup(row2).addComponent(this.controlsBar));
		this.getContentPane().setLayout(layout);
	}
	
	private void createAndShowGUI() {
		createFileChooser();
		createMenuBar();
		
		randomAlg = new ImageComponent(new RandomMorph());
		blobAlg = new ImageComponent(new ThreshholdSegment());
		contrastAlg = new ImageComponent(new ContrastMorph());
		edgeAlg = new ImageComponent(new ThreshholdSegment());
		
		createSetLayout();
		this.setSize(IMAGE_WIDTH*2, IMAGE_HEIGHT*2 + MENU_HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void run() {
		createAndShowGUI();
	}

	class ExperimentListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			fileChooser.setLocationRelativeTo(ImageMorphGUI.this);
			fileChooser.setVisible(true);
		}
		
	}
	
	class MorphListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Morph Pixels")) {
				ImageMorphGUI.this.blobAlg.morphPixels((int)ImageMorphGUI.this.pixelSpinner.getValue());
				ImageMorphGUI.this.randomAlg.morphPixels((int)ImageMorphGUI.this.pixelSpinner.getValue());
				ImageMorphGUI.this.edgeAlg.morphPixels((int)ImageMorphGUI.this.pixelSpinner.getValue());
				ImageMorphGUI.this.contrastAlg.morphPixels((int)ImageMorphGUI.this.pixelSpinner.getValue());
			} else {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						for (int i = 0; i < IMAGE_HEIGHT*IMAGE_WIDTH; ++i) {
							ImageMorphGUI.this.blobAlg.morphPixels(100 + (100 - ImageMorphGUI.this.sleepDelay));
							ImageMorphGUI.this.randomAlg.morphPixels(100 + (100 - ImageMorphGUI.this.sleepDelay));
							ImageMorphGUI.this.edgeAlg.morphPixels(100 + (100 - ImageMorphGUI.this.sleepDelay));
							ImageMorphGUI.this.contrastAlg.morphPixels(100 + (100 - ImageMorphGUI.this.sleepDelay));
							try {
								Thread.sleep(ImageMorphGUI.this.sleepDelay);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}).start();

			}
		}
		
	}
}
