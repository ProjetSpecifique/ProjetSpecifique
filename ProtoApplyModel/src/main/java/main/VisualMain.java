package main;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class VisualMain {

	public static void main(String[] args) {
//		MyEvaluator.logResults = false;

		JFrame frame = new JFrame("Simple Visual Swing");
		MyPanel panel = new MyPanel();

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.getContentPane().add(panel, "Center");
		frame.setSize(new Dimension(600, 500));
		frame.setVisible(true);

	}
}
