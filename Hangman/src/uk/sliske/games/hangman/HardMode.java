package uk.sliske.games.hangman;

import java.util.ArrayList;

public class HardMode extends Game {

	private String[] knownChars;

	public HardMode() {
		super();
		knownChars = new String[wordLength];
		for (int i = 0; i < wordLength; i++) {
			knownChars[i] = "__";
		}
	}

	public void handleLetter(char c) {
		String[][] remaining = new String[wordLength + 1][];
		for (int i = 0, j = wordLength + 1; i < j; i++) {
			remaining[i] = possibleIfCharAtIndex(c, i);
		}
		for (String[] s : remaining) {
			System.out.printf("%d ", s.length);
		}
		System.out.println();

		String[] max = {};
		int maxIndex = wordLength;

		for (int i = 0, j = wordLength + 1; i < j; i++) {
			if (remaining[i].length > max.length) {
				max = remaining[i];
				maxIndex = i;
			}
		}

		possibleWords = max;

		if (maxIndex >= wordLength) {
			System.out.printf("word does not contain %c\n", c);
			lives--;
		} else {
			System.out.printf("word has letter %c at postion %d\n", c,
					maxIndex + 1);
			knownChars[maxIndex] = c + "";
		}

	}

	private String[] possibleIfCharAtIndex(char c, int index) {
		if (index >= wordLength || index < 0) {
			return removeFromList(c);
		}
		ArrayList<String> res = new ArrayList<String>();
		for (String s : possibleWords) {
			if (s.charAt(index) == c && (countChars(s, c) == 1)) {
				res.add(s);
			}
		}
		return convertToStringArray(res);
	}

	private int countChars(String s, char c) {
		int i = 0;
		for (char ch : s.toCharArray()) {
			if (ch == c) {
				i++;
			}
		}
		return i;
	}

	public String getWord(boolean gameOver) {
		if (gameOver) {
			if (word != null) {
				return word;
			}
			return possibleWords[random.nextInt(possibleWords.length)];
		}
		if (wordSet) {
			String res = "";
			for (int i = 0; i < wordLength; i++) {
				if (i != 0) {
					res += " ";
				}
				if (guessedLetters.contains(word.charAt(i))) {
					res += word.charAt(i);
				} else {
					res += "__";
				}
			}
			return res;
		} else {
			String res = "";
			for (int i = 0; i < wordLength; i++) {
				if (i != 0) {
					res += " ";
				}
				res += knownChars[i];

			}
			return res;
		}
	}

}
