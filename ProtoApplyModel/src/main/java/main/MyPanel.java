package main;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MyPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	JButton imagePathBtn, allModels, selectedModels;
	JTextArea textArea;
	JLabel picLabel;

	String imagePath;

	public MyPanel() {
		imagePathBtn = new JButton("Select Image");
		allModels = new JButton("Compute the best term");
		selectedModels = new JButton("Apply selected models");

		textArea = new JTextArea("", 15, 50);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		picLabel = new JLabel("");

		imagePathBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("../"));
				chooser.setDialogTitle("choosertitle");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					try {
						picLabel.setIcon(new ImageIcon(
								getScaledImage(ImageIO.read(chooser.getSelectedFile()), 100, 100)));
					} catch (IOException e1) {
						textArea.setText("Exception : " + e1);
					}

					imagePath = chooser.getSelectedFile().getPath();
					textArea.setText("Image Path : " + imagePath);

				} else {
					textArea.setText("No Selection ");
				}

			}
		});

		allModels.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					textArea.setText("Processing ... ");

					textArea.setText(DetectModelComputation.detectModelForAllTerms(imagePath));
				} catch (Exception e1) {
					textArea.setText("Exception : " + e1);
				}
			}
		});

		selectedModels.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					textArea.setText("Processing ... ");

					textArea.setText(DetectModelComputation.computeProbabilitiesForModels(imagePath));
				} catch (Exception e1) {
					textArea.setText("Exception : " + e1);
				}
			}
		});

		add(imagePathBtn);
		add(allModels);
		add(selectedModels);
		add(picLabel);
		add(scrollPane);
		setVisible(true);
	}

	private BufferedImage getScaledImage(BufferedImage srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}
}
