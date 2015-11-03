package uk.sliske.games.hangman;

import java.awt.Font;
import java.awt.Label;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import uk.sliske.games.hangman.ui.Window;

public class Hangman {

	private Window window;

	public Hangman() {

		window = new Window();
		window.setVisible(true);
	}

	public static void main(String[] args) {
		JFrame loading = createLoadingWindow();
		loading.setVisible(true);
		if (args.length == 1) {
			try {
				int i = Integer.parseInt(args[0]);
				new Game(i);
			} catch (Exception e) {

			}

		}
		new Hangman();
		loading.dispose();
	}

	private static JFrame createLoadingWindow() {
		JLabel wordLabel;
		Label row1;
		JFrame frame = new JFrame("Sliske's Evil Hangman");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);

		wordLabel = new JLabel("");
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		wordLabel.setBounds(10, 11, 414, 58);
		wordLabel.setText("Loading Dictionary");
		frame.add(wordLabel);

		row1 = new Label("");
		row1.setAlignment(Label.CENTER);
		row1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		row1.setBounds(10, 129, 414, 22);
		row1.setText("Please Wait");
		frame.add(row1);

		return frame;
	}

}
