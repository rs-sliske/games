package uk.sliske.games.hangman;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Game {
	private static Game current;
	protected static final Random random = new Random();
	private static String[] dictionary;
	private static int guesses = 13;

	public static boolean hardmode = false;

	public static Game getCurrent() {
		return getCurrent(true);
	}

	private static Game getCurrent(final boolean createIfNull) {
		if (current == null && createIfNull) {
			create();
		}
		return current;
	}

	public static void create() {
		if (hardmode)
			new HardMode();
		else
			new Game();
	}

	protected HashSet<Character> guessedLetters;
	protected String[] possibleWords;
	protected int lives;
	protected int wordLength;
	protected String word;
	protected boolean wordSet = false;
	protected String[] knownChars;

	public Game() {
		this(-1);
	}

	public Game(final int startingLives) {
		gameOver();

		guessedLetters = new HashSet<Character>();

		lives = startingLives;
		if (lives < 0) {
			lives = guesses;
		}
		do {
			wordLength = random.nextInt(10) + 2;
			possibleWords = filterLength();
		} while (possibleWords.length <= 0);

		System.out.printf("starting new game with %d guesses\n", lives);

		knownChars = new String[wordLength];
		for (int i = 0; i < wordLength; i++) {
			knownChars[i] = "__";
		}

		current = this;
	}

	private static String[] getDictionary() {
		if (dictionary != null && dictionary.length > 0) {
			return dictionary;
		} else {

			try {
				List<String> temp = new ArrayList<String>();

				String path = System.getProperty("user.home")
						+ "//sliske games//hangman//dictionary.txt";

				File file = new File(path);
				if (!file.exists()) {
					file = download(
							"http://www-01.sil.org/linguistics/wordlists/english/wordlist/wordsEn.txt",
							path);
				}

				Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					temp.add(scanner.nextLine());
				}
				scanner.close();

				dictionary = convertToStringArray(temp);
				System.out.printf("%d words loaded\n", dictionary.length);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		if (dictionary == null) {
			return new String[] {};
		}
		return dictionary;
	}

	private String[] filterLength() {
		HashSet<String> res = new HashSet<String>();

		for (String s : getDictionary()) {
			String t = stripString(s.toLowerCase());
			if (t.length() == wordLength)
				res.add(t);
		}

		System.out.printf("%d words have the correct length of %d\n",
				res.size(), wordLength);

		return convertToStringArray(res);

	}

	private String stripString(String s) {
		ArrayList<Character> c = new ArrayList<Character>();

		for (char ch : s.toCharArray()) {
			if (Character.isLetter(ch)) {
				c.add(ch);
			}
		}

		Object[] r = c.toArray();
		char[] res = new char[r.length];
		for (int i = 0; i < r.length; i++) {
			res[i] = (char) r[i];
		}
		return new String(res);
	}

	public void guessLetter(char c) {
		if (guessedLetters.contains(c)) {
			System.out.printf("you have already guessed the letter '%c'\n", c);
			return;
		}
		System.out.printf("you have guessed the letter '%c'\n", c);
		guessedLetters.add(c);

		handleLetter(c);
	}

	public void handleLetter(char c) {

		int count = countPossible(c);

		if (!wordSet) {
			if (count > 0) {
				possibleWords = removeFromList(c);
			}
			if (count <= 1) {
				word = possibleWords[0];
				wordSet = true;
				System.out.println(word);
			}
		} else {
			for (int i = 0; i < wordLength; i++) {
				char ch = word.charAt(i);
				if(guessedLetters.contains(ch)){
					knownChars[i] = ch+"";
				}
			}
		}
		if (word == null || !word.contains(c + "")) {
			lives--;
		}

		System.out.printf("there are %d possible answers remaining\n", count);
	}

	public String getWord(boolean gameOver) {
		if (gameOver) {
			if (word != null) {
				return word;
			}
			return possibleWords[random.nextInt(possibleWords.length)];
		}

		String res = "";
		for (int i = 0; i < wordLength; i++) {
			if (i != 0) {
				res += " ";
			}
			res += knownChars[i];

		}
		return res;

	}

	private static void gameOver() {
		boolean won = true;
		Game game = getCurrent(false);
		if (game == null) {
			System.out.println("current game is null");
			return;
		}
		for (char c : game.getWord(true).toCharArray()) {
			if (!game.guessedLetters.contains(c)) {
				won = false;
				break;
			}
		}
		if (won) {
			guesses--;
		} else {
			guesses++;
		}
		if (guesses <= 0) {
			guesses = 0;
		}
		if (guesses > 26) {
			guesses = 26;
		}

	}

	public String[] getUsedChars() {
		String[] res = { "", "" };

		boolean[] first = { true, true };
		Iterator<Character> i = guessedLetters.iterator();
		int c = 0;
		while (i.hasNext()) {
			int j = c < 13 ? 0 : 1;
			if (!first[j]) {
				res[j] += ", ";

			}
			char ch = i.next();

			res[j] += ch;
			first[j] = false;

			c++;
		}
		return res;
	}

	private int countPossible(char c) {
		int r = 0;

		for (String s : possibleWords) {
			if (!s.contains(c + "")) {
				r++;
			}
		}

		return r;
	}

	protected String[] removeFromList(char c) {
		ArrayList<String> res = new ArrayList<String>();
		for (String s : possibleWords) {
			if (!s.contains(c + "")) {
				res.add(s);
			}
		}
		return convertToStringArray(res);

	}

	public int getLives() {
		return lives;
	}

	public static String[] convertToStringArray(Set<String> args) {
		String[] results = new String[args.size()];
		Iterator<String> it = args.iterator();
		int i = 0;
		while (it.hasNext()) {
			results[i] = it.next();
			i++;
		}

		return results;
	}

	public static String[] convertToStringArray(List<String> args) {
		String[] results = new String[args.size()];
		int i = 0;
		for (String s : args) {
			results[i] = s;
			i++;
		}

		return results;
	}

	public static File download(String sourceUrl, String target)
			throws MalformedURLException, IOException {
		URL url = new URL(sourceUrl);

		File file = new File(target);
		file.getParentFile().mkdirs();
		file.createNewFile();
		FileOutputStream writer = new FileOutputStream(file);
		InputStream input = url.openStream();
		int i = 0;
		while ((i = input.read()) >= 0) {
			writer.write(i);
		}
		writer.close();
		input.close();

		return file;
	}

}
