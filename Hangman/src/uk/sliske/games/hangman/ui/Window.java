package uk.sliske.games.hangman.ui;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import uk.sliske.games.hangman.Game;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Window extends JFrame {
	private static final long serialVersionUID = 6782120844725251459L;

	private JPanel contentPane;
	private JTextField textField;
	private JLabel wordLabel;
	private JLabel lives;
	private Label row0;
	private Label row1;

	public Window(){
		createWindow();
		refreshData();
	}
		
	private final void createWindow(){
		setTitle("Sliske's Evil Hangman");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		wordLabel = new JLabel("");
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
		wordLabel.setBounds(10, 11, 414, 58);
		contentPane.add(wordLabel);

		JButton newGame = new JButton("New Game");
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Game();
				refreshData();
			}
		});
		newGame.setBounds(237, 183, 124, 50);
		contentPane.add(newGame);

		lives = new JLabel("New label");
		lives.setHorizontalAlignment(SwingConstants.CENTER);
		lives.setFont(new Font("Tahoma", Font.PLAIN, 29));
		lives.setBounds(27, 183, 83, 50);
		contentPane.add(lives);

		row0 = new Label("");
		row0.setAlignment(Label.CENTER);
		row0.setFont(new Font("Tahoma", Font.PLAIN, 20));
		row0.setBounds(10, 101, 414, 22);
		contentPane.add(row0);

		row1 = new Label("");
		row1.setAlignment(Label.CENTER);
		row1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		row1.setBounds(10, 129, 414, 22);
		contentPane.add(row1);

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				textField.setText("");
				Game.getCurrent().guessLetter(e.getKeyChar());
				refreshData();
			}
		});
		textField.setBounds(141, 183, 86, 50);
		contentPane.add(textField);
		textField.setColumns(10);
		
	}

	public void refreshData() {
		Game game = Game.getCurrent();
		int l = game.getLives();
		lives.setText(l + "");
		String word = game.getWord(l <= 0);
		wordLabel.setText(word);
		String[] used = game.getUsedChars();
		if (word.contains("_")) {
			row0.setText(used[0]);
			row1.setText(used[1]);
		} else {
			row0.setText("Well Done");
			row1.setText("You Won");
		}

		textField.setText("");

	}
}
