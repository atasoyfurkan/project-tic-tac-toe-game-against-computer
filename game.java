package assignment;
import java.util.*;
import java.io.*;

public class assignment2 {
	
	public static Scanner console = new Scanner(System.in);
	public static Random random = new Random();
	public static String field = "EEEEEEEEEEEEEEEE"; // this is my gameboard it is consisting of 16 size of array i will change it almost all method so instead of send all methods i decleare it as a global variable
	public static int move, winComputer = 0, winPlayer = 0; // move is for computer understand how it moves and its move is wisely, winComputer is to memorize how much time computer wins and same for winPlayer
	public static String player; // this is for sign of player it is X or O
	public static int turnLoadFile = -1; // it is for understanding loadfile is appropriate or who will start according to load file
	
/*  main method: main method is printing beginning printings and after that it wants to file name if user wants to play in loading game after that main method is calling game method so game method is making other stuffs  */	
	public static void main(String[] args) 
			throws FileNotFoundException {
		
		System.out.println("Welcome to the XOX Game.");
		
		String load;
		do {
			System.out.print("Would you like to load the board from file or create a new one? (L or C) ");
			load = console.nextLine().toUpperCase();
			if(load.equals("L")) {
				String fileName;
				File file;
				do {
					System.out.print("Please enter the file name: ");
					fileName = console.nextLine();
					file = new File(fileName);
				} while(!file.canRead());
				loadFile(file);
			}
		} while(!load.equals("L") && !load.equals("C"));
		if(turnLoadFile == 3) return; // if file is not appropriate turnLoadfile is being equal to 3 so game will be over when file is not appropriate
		
		
		do {
		System.out.print("Enter your symbol: (X or O) ");
		player = console.nextLine().toUpperCase();
		} while(!player.equals("X") && !player.equals("O"));
		System.out.printf("You are player %s and the computer is player %s.\n", player, player.equals("O") ? "X" : "O" );
		
		game();
	}

/* game method: for my code this is the most important part it is running as a main method. It distribute all task to other methods and mainly it is playing game */
	public static void game() {
		
		whoWillStart();
		if(tie() == -1) { // this is for loading file after computer turn is tie and user wants to finish game.
			System.out.printf("You: %d Computer: %d\nThanks for playing!\n", winPlayer, winComputer);
			return;
		}
		if(tie() != 1) yazdir(); // this is for loading file after computer turn is tie and user wants to continue game.
		
		if(kontrol("XXX")) { // it is for if code read file and in the first turn computer wins
			if(!newGameComputer()) {
				System.out.printf("You: %d Computer: %d\nThanks for playing!\n", winPlayer, winComputer);
				return;
			}
		}
		
		while(true) { // this is main while and each turn is playing in this while.
			
			if(tie() == -1) break; // this is for game is tie and user wants to finish game.
			
			int y = -1, x = -1; // this is mainly coordinat of which must be fill.
			boolean flag = false; // this flag is for if user enter wrong input it is being true so code doesn't say enter coordinates.
			do { // this while to understand if input is wrong. if it is true, it is looping until user enter correct input 
				if(flag) System.out.print("Wrong input! Try again:"); 
				else System.out.print("Enter coordinates:"); // only for first step code must print this so after one step this part is not working
				flag = true; 
				
				if(!console.hasNextInt()) { // this if try to understand user enter integer or not
					console.nextLine();
					continue;
				}
				y= console.nextInt(); // same as other if
				if(!console.hasNextInt()) {
					console.nextLine();
					continue;
				}
				x = console.nextInt();
				console.nextLine(); // this is for additional \n or additional wrong input 
				
			}while( (x < 0 || x > 4 || y < 0 || y > 4) || field.charAt((x - 1) + ((y - 1) * 4)) != 'E');
			
			fill(y, x, 'O');
				
			if(kontrol("OOO")) { // this is controlling game is over or not
				yazdir();
				if(newGamePlayer()) continue; // this part for user wants to continue to game.
				else break; // if he don't want this part is working and game will finish
			}
			if(tie() == -1) break; // this is for game is tie and user dont want to play more
			if(tie() == 1) continue; // this is for game is tie and user wants to play more
			
			int doIt = AI(); // Part of computer turn, int this part computer choose wisely its turn
			y = (doIt / 4) + 1;
			x = (doIt % 4) + 1;
			fill(y, x, 'X'); 
			
			if(kontrol("XXX")) { // this is controlling game is over or not
				yazdir();
				if(newGameComputer()) continue; // this part for user wants to continue to game.
				else break; // if he don't want this part is working and game will finish
			}
			yazdir();
		}
		System.out.printf("You: %d Computer: %d\nThanks for playing!\n", winPlayer, winComputer); // this is finishing printing 
	}
	
	/* (if player win)this method for after winning situation occur, player wants to play new game or not. if he wants to play this part making some arrengment on field */ 
	public static boolean newGamePlayer() {
		String answer;
		do {
			System.out.print("You win! Do you want to play again? (Y or N)");
			answer = console.nextLine().toUpperCase();
		} while(!answer.equals("Y") && !answer.equals("N"));
		
		if(answer.equals("Y")) {
			field = "EEEEEEEEEEEEEEEE";
			winPlayer++;
			whoWillStart();
		
			yazdir();
			return true;
		}
		winPlayer++;
		return false;
	}
	
	/* (if computer win)this method for after winning situation occur, player wants to play new game or not. if he wants to play this part making some arrengment on field */ 
	public static boolean newGameComputer() {
		String answer;
		do {
			System.out.println("Computer wins! Do you want to play again? (Y or N)");
			answer = console.nextLine().toUpperCase();
		} while(!answer.equals("Y") && !answer.equals("N"));
			
		if(answer.equals("Y")) {
			field = "EEEEEEEEEEEEEEEE";
			winComputer++;		
			whoWillStart();		
			yazdir();
			return true;
		}
		winComputer++;
		return false;
	}
	
	/* this method mainly for understand who will start and after that it prints what it should to be. additionaly, if computer start, it is playing in this code to first turn  */
	public static void whoWillStart() {
		
		int turn = random.nextInt(2);
		if(turnLoadFile != -1) {	
			if(player.equals("X")) {// part for making reverse because computer is "X" and player is "O" in this code.
				 field = makeReverse(); 
				 turn = turnLoadFile == 0 ? 1 : 0;
			}
			if(player.equals("O")) {
				turn = turnLoadFile;
			}
			turnLoadFile = -1;
		}
		if(turn % 2 == 0) {
			System.out.println("You will start:");
		}
		else {
			System.out.println("Computer will start:");
			
			int doIt, x ,y;
			doIt = AI(); // doIt integer for to understand which moving will be wisely
			y = (doIt / 4) + 1;
			x = (doIt % 4) + 1;
			
			fill(y, x, 'X');		
		}
	}
	
	/* this method for printing gameboard */
	public static void yazdir() {
		
		String newField = "";
		
		if(player.equals("X")) { // Code always store computer as "X" and player as "O" so to make both possibility available i am making reverse in this part.
			newField = makeReverse();
		}
		else {
			newField = field;
		}
		
		for(int i = 0; i < 4; i++) { // this is for printing
			System.out.print("| ");
			for(int j = 0; j < 4; j++) {
				if(newField.charAt(j + i * 4) == 'E') System.out.print(" " + " | ");
				else System.out.print(newField.charAt(j + i * 4) + " | ");
			}
			System.out.println();
		}
	}
	/* this method for filling gameboard */
	public static void fill(int y, int x, char sign) {
		
		String newField = field.substring(0, (x - 1) + (y -1) * 4 ) + sign + field.substring((x - 1) + (y -1) * 4 + 1, 16);
		
		field = newField;
	}

	
	/* AI method: this method for computer understand which moving is the best for him and in this part it is making the most correct move */
	public static int AI() {
		
		int doIt = -1, x, y;
		
		if(kontrol("XXE")) return move; // part for win if there is proper way
		if(kontrol("EXX")) return move;
		if(kontrol("XEX")) return move;
		
		if(kontrol("OOE")) return move; // part for blocking player
		if(kontrol("EOO")) return move;
		if(kontrol("OEO")) return move;
		
		if(kontrol("EOEE")) return move; // part for blocking player to win after two turn
		if(kontrol("EEOE")) return move;
		
		if(kontrol("EXEE")) return move; // part for guranteeing winning after two win
		if(kontrol("EEXE")) return move;
			
		if(kontrol("EXE")) return move; // part for creating a change to win
		if(kontrol("XEE")) return move;
		if(kontrol("EEX")) return move;
					
		do { // part for choosing main square place			
			x = random.nextInt(2) + 1;
			y = random.nextInt(2) + 1;
			
			doIt = x + y * 4;
			
		} while(field.charAt(doIt) != 'E');
		if(doIt != -1) return doIt;
		
		
		do { // part for choosing completely random
			doIt = random.nextInt(16);
			
		} while(field.charAt(doIt) != 'E');
		
		return doIt;
	}
	
	
	/*kontrol method: this method is very important for my code and it is sending correct coordinat to innerControl method to construct winning string place and according to innerControl answer it sending true or false*/
	public static Boolean kontrol(String control) {
		
		for(int j = 0; j < 4; j++) {
			if(innerControl(control, j * 4, 1, 4)) return true; // creating winning string horizantally
		}
		
		for(int i = 0; i < 4; i++) {
			if(innerControl(control, i, 4, 4)) { // creating winning string vertically
				
				return true;
			}
		}
		if(innerControl(control, 1, 5, 3)) return true; // creating winning string diagnotically
		if(innerControl(control, 0, 5 ,4)) return true;
		if(innerControl(control, 4, 5, 3)) return true;
		if(innerControl(control, 2, 3, 3)) return true;
		if(innerControl(control, 3, 3, 4)) return true;
		if(innerControl(control, 7, 3, 3)) return true;
		
		return false;
	}
	
	/*this method taking some coordinat and from them, it is constructing strings and in this string there is winning situation horizontally, vertically, or diagonally after that it is comparing winning string and control string which is instant view of our field. 
	 * If there is situation which one user must be win it is sending true if it is not it is sending false*/
	public static Boolean innerControl(String control, int i, int artis, int n) {
		
		String part = "";		
		for(int j = 0; j < n; j++) {
			part += field.charAt(i + j * artis);
		}
		
		if(part.indexOf(control) != -1) {
			
			if(part.indexOf("OOE") != -1)  //  it is making control for "OOEE" and "OOE"
				move = i + (part.indexOf("OOE") + 2) * artis; // to know where computer must put sign
			
			if(part.indexOf("EOO") != -1)  // it is making control for "EEOO" and "EOO"
				move = i + part.indexOf("EOO") * artis; // to know where computer must put sign
			
			if(part.indexOf("OEO") != -1)
				move = i + (part.indexOf("OEO") + 1) * artis; // to know where computer must put sign
			
			
			if(part.indexOf("XXE") != -1) 
				move = i + (part.indexOf("XXE") + 2) * artis;
			
			if(part.indexOf("EXX") != -1) 
				move = i + part.indexOf("EXX") * artis;
			
			if(part.indexOf("XEX") != -1) 
				move = i + (part.indexOf("XEX") + 1) * artis;
			
			
			if(part.indexOf("EOEE") != -1) 
				move = i + (part.indexOf("EOEE") + 2) * artis;
			
			if(part.indexOf("EEOE") != -1) 
				move = i + (part.indexOf("EEOE") + 1) * artis;
			
			
			if(part.indexOf("EXEE") != -1)
				move = i + (part.indexOf("EXEE") + 2) * artis;
			
			if(part.indexOf("EEXE") != -1)
				move = i + (part.indexOf("EEXE") + 1) * artis;
			
			
			if(part.indexOf("EXE") != -1) 
				move = i + part.indexOf("EXE") * artis;
			
			if(part.indexOf("XEE") != -1) 
				move = i + (part.indexOf("XEE") + 1) * artis;
			
			if(part.indexOf("EEX") != -1) 
				move = i + (part.indexOf("EEX") + 1) * artis;
			
			
			return true;
		}
		
		return false;
	}

	/*loadFile method: this method is taking file and it is filling our gameboard and after that it makes some comparison for file input is appropriate or not if it is notappropriate.
	 * game will be over. Besides, this method understands which side will start to game according to number of X and O if it is equal randomly it is deciding.  */
	public static void loadFile(File file) 
			throws FileNotFoundException {
		
		Scanner input = new Scanner(file);
		int counterI = 0, counterX = 0, counterO = 0;
		String newField = "", temp = "";
		
		while(input.hasNext()) { // to take input from file
			String index = input.next();
			
			if(temp.equals(index) && !temp.equals("|")) { // for consecutive word for wrong file input
				System.out.println("This file is not appropriate!");
				turnLoadFile = 3;
				return;
			}
			
			if(index.equals("O") || index.equals("X") || index.equals("E")) { // part for counting number of X O I and creating newField string
				newField += index;

				if(index.equals("O")) counterO++;
				if(index.equals("X")) counterX++;
			}
			else if(index.equals("|")) counterI++;
			else {
				System.out.println("This file is not appropriate!"); 
				turnLoadFile = 3;
				return;	
			}
			temp = index;
		}
		field = newField;
		
		
		if(counterO == counterX) {
			turnLoadFile = random.nextInt(2);
		}
		if(counterO > counterX) {
			turnLoadFile = 1;
		}
		if(counterO < counterX) {
			turnLoadFile = 0;
		}
		
		if(Math.abs(counterO - counterX) > 1 || field.indexOf("E") == -1 
		   || kontrol("XXX") || kontrol("OOO") || newField.length() != 16 || counterI != 20) { // part for understand file is not approprieto for some reason
			System.out.println("This file is not appropriate!");
			turnLoadFile = 3;
			return;
		}
		System.out.println("Load successful.");
		
	}
	
	/* makeReverse method: in my code, to make things easy, computer is always X and player is always O and only for printing part it must make reverse according to user choose of sign and
	 * this method is making reverse for this */ 
	public static String makeReverse() {
		
		String newField = "";
		for(int i = 0; i < 16; i++) {
			if(field.charAt(i) == 'X') newField += "O";
			if(field.charAt(i) == 'O') newField += "X";
			if(field.charAt(i) == 'E') newField += "E";
		}
		return newField;	
	}
	
	
	/* tie method: this method understand game is tie or not. If it is tie method wants from user to decide do you want to play again or not and after that it is making same arrengment for next game  */
	public static int tie() {
		
		if(field.indexOf("E") == -1) {	// if for understand game is tie 
			yazdir();
			
			String answer;
			do {
				System.out.print("It's tie! Do you want to play again? (Y or N)");
				answer = console.nextLine().toUpperCase();
			} while(!answer.equals("Y") && !answer.equals("N"));
			
			if(answer.equals("Y")) {
				field = "EEEEEEEEEEEEEEEE";
				whoWillStart();
			
				return 1;
			}
			if(answer.equals("N")) {
				return -1;
			}
		}
		return 0;
	}
}