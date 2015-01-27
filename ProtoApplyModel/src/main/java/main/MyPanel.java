package main;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

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
	JButton go;
	JTextArea textArea;
	JLabel picLabel;

	public MyPanel() {
		go = new JButton("Select Image");

		textArea = new JTextArea("", 15, 50);
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		picLabel = new JLabel("");

		go.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("../"));
				chooser.setDialogTitle("choosertitle");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					String result = "";
					try {
						textArea.setText("Processing .. ");

						picLabel.setIcon(new ImageIcon(
								getScaledImage(ImageIO.read(chooser.getSelectedFile()), 100, 100)));

						result = DetectModelComputation.detectModelForAllTerms(chooser.getSelectedFile().getPath());

						System.out.println(result.toString());

					} catch (Exception e1) {
						result = e1.toString();
					}

					textArea.setText(result.toString());
				} else {
					textArea.setText("No Selection ");
				}

			}
		});

		add(go);
		add(picLabel);
		add(textArea);
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
