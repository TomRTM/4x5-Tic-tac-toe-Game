package tictactoePackage;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
*       This class is the main class to run the game
*/
public class TictactoeDriver extends JFrame
{       
    //constants
    public static final char EMPTY = 'E';
    public static final char X = 'X';
    public static final char O = 'O';
    public static final int NOWIN = 0;
    public static final int XWIN = 1;//computer wins
    public static final int OWIN = 2;//player wins
    public static final int TIE = 3;
    private static final int BUTTONSIZE = 50;
    private static final int ROW = 4;
    private static final int COLUMN = 5;

    private static JButton screenBoard[][];
    private int selectedOption;//to indicate the game order for human and computer
    private int currentLevel;//to show the current level on the window title

    public GameBoard tictactoe;

    private static Font myfont = new Font("Garamond", Font.BOLD, 50);
        

    //Driver for the tictactoe game, to be created in main
    public TictactoeDriver() {
        int HEIGHT, WIDTH; //size of the screen
        int numSquares;  //number of squares on the board
        int i, j; //array indexes

        numSquares = ROW*COLUMN;

        //Create new board
        tictactoe = new GameBoard();

        //Set up Display
        HEIGHT = (ROW+2) * BUTTONSIZE + 25;
        WIDTH = (COLUMN+2) * BUTTONSIZE;
        HEIGHT = HEIGHT + 100;
        Container pane = getContentPane();
        pane.setLayout(null);
        

        //Create game board (create and add all buttons)
        screenBoard = new JButton[COLUMN][ROW];
        for(i = 0; i < COLUMN; i++) {
            for(j = 0; j < ROW; j++) {
                screenBoard[i][j] = new JButton("");
                screenBoard[i][j].setBackground(Color.white);
                screenBoard[i][j].setSize(BUTTONSIZE,BUTTONSIZE);
                screenBoard[i][j].setLocation(i*BUTTONSIZE + BUTTONSIZE, j*BUTTONSIZE + BUTTONSIZE);
                screenBoard[i][j].addActionListener(new BoardButtonHandler());
                pane.add(screenBoard[i][j]);
            }
        }
        

        //Add restart and Exit buttons
        JButton restart = new JButton("Restart");
        restart.setBackground(Color.white);
        restart.setSize(BUTTONSIZE*2,BUTTONSIZE);
        restart.setLocation(BUTTONSIZE/2, ROW*BUTTONSIZE + BUTTONSIZE*2);
        pane.add(restart);
                

        //Restart button handler
        RestartButtonHandler rbh = new RestartButtonHandler();
        restart.addActionListener(rbh);

        JButton exit = new JButton("Quit");
        exit.setBackground(Color.white);
        exit.setSize(BUTTONSIZE*2,BUTTONSIZE);
        exit.setLocation(WIDTH-BUTTONSIZE*2-BUTTONSIZE/2, ROW*BUTTONSIZE + BUTTONSIZE*2);
        pane.add(exit);

        //Exit button handler
        ExitButtonHandler ebh = new ExitButtonHandler();
        exit.addActionListener(ebh);


        //level selection propmt
        setLevel();
        //Set title of window based on current game level
        currentLevel = tictactoe.getDepth();
        switch(currentLevel){
        case 2: setTitle("Intelligent TicTacToe: Easy");
        	break;
        case 4: setTitle("Intelligent TicTacToe: Medium");
        	break;
        case 6: setTitle("Intelligent TicTacToe: Hard");
        	break;
        default: setTitle("Intelligent TicTacToe");
        	break;
        }
        //Set color of window
        pane.setBackground(new Color(255,122,13));
        //Set size of window
        setSize(WIDTH,HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Set game order
        whoGoFirst();
    }//end TicTacToe constructor
    
    
    //At the beginning of the game, let users select the level of the game
    //There are three levels from easy to hard
    //Each one search a further depth in the minimax tree
    private void setLevel() {
    	int level;
    	String lv1 = "Easy";
    	String lv2 = "Medium";
    	String lv3 = "Hard";
    	
    	Object[] possibleLevels = {"Easy", "Medium", "Hard"};
    	Object levelSelect = JOptionPane.showInputDialog(null, 
    			"Please select a game level.", "Level Selection", JOptionPane.INFORMATION_MESSAGE, 
    			null, possibleLevels, possibleLevels[0]);
    	if(levelSelect.equals(lv1)) {
    		tictactoe.setDepth(2);
    	}
    	else if(levelSelect.equals(lv2)) {
    		tictactoe.setDepth(4);
    	}
    	else if(levelSelect.equals(lv3)) {
    		tictactoe.setDepth(6);
    	}
    	
    }
    
    //After the level selection, the program prompt the message
    //to choose whether the player go first or the computer go first
    //press 'yes', human go first; press 'no', computer go first
    private void whoGoFirst() {
        //select the order to play
        selectedOption = JOptionPane.showConfirmDialog(null, "Do you want to be the first player?", "Game Order Setting", JOptionPane.YES_NO_OPTION);
        switch(selectedOption) {
        	case JOptionPane.YES_OPTION: 
	            break;
        	case JOptionPane.NO_OPTION: 
        		//computer drop one ball in the first round
        		//Pick square, Update Board, check winner all done in this method
                tictactoe.moveOpponent(X);
                //Update button
                screenBoard[tictactoe.getMoveY()][tictactoe.getMoveX()].setText("X");
                screenBoard[tictactoe.getMoveY()][tictactoe.getMoveX()].doClick(100);
	            break;
        }
    }


    //Class to perform appropriate action when a game button is pressed
    private class BoardButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row, column;
            row = -1;
            column = -1;

            //get button pressed
            //Find which of the JButtons was clicked
            JButton find = (JButton)e.getSource();

            //Only take an action if the game is not over
            //Can't affect the board if someone has already won
            if(tictactoe.getWinner() == NOWIN) {
                //Compare this JButton to each in the array until a match is found.
                //Store this button's position in the array
                for(int d = 0; d < COLUMN; d++) {
                    for(int h = 0; h < ROW; h++) {
                        if(screenBoard[d][h].equals(e.getSource())) {
                            column = d;
                            row = h;
                        }
                    }
                }
                //Check to see if it's occupied, do nothing if it is
                if(tictactoe.getTile(row,column) == EMPTY) {
                    //Change text on button to O
                    //Set this button to occupied and insert the O in the array of chars
                    screenBoard[column][row].setText("O");
                    //Pause for a few seconds to show the move
                    screenBoard[column][row].doClick(100);

                    //Update game board
                    tictactoe.setTile(row,column,O);
                    tictactoe.fillSquare();

                    //Check for win based on this new move
                    tictactoe.checkWinner(row,column,O);


                    //AI's turn
                    //AI only moves if there is an empty square left and if there isn't a winner yet
                    if(tictactoe.getWinner() == NOWIN && tictactoe.getEmptySquares() > 0) {
                        //Pick square, Update Board, check winner all done in this method
                        tictactoe.moveOpponent(X);
                        //Update button
                        screenBoard[tictactoe.getMoveY()][tictactoe.getMoveX()].setText("X");
                        screenBoard[tictactoe.getMoveY()][tictactoe.getMoveX()].doClick(100);
                    }
                }
            }//end if


            //If win or tie, pop up a message box
            else if(tictactoe.getWinner() == XWIN) {
                JOptionPane.showMessageDialog(null, "Computer Wins!");
            }
            else if(tictactoe.getWinner() == OWIN) {
                JOptionPane.showMessageDialog(null, "Player Wins!");
            }
            else if(tictactoe.getWinner() == TIE){
                JOptionPane.showMessageDialog(null, "It is a tie.");
            }//TIE
        }//end actionPerformed method
    }//end buttonhandler class

        
        
    //Class to perform appropriate action when Restart button is pressed
    //All arrays are reset, gameover is changed to false, and message is displayed
    private class RestartButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e3) {
            //Clear the screen and game board
            int i,j;
            for(i = 0; i < ROW; i++) {
                for(j = 0; j < COLUMN; j++) {
                    screenBoard[j][i].setText("");
                    tictactoe.setTile(i,j,EMPTY);
                }
            }
            
            //Reset winner and empty all squares
            tictactoe.setEmptySquares(ROW*COLUMN);
            tictactoe.setWinner(NOWIN);
            tictactoe.resetMove();
            //restart the game, a new level selection propmt
            setLevel();
			//Set title of window based on current game level
			currentLevel = tictactoe.getDepth();
			switch(currentLevel){
			case 2: setTitle("Intelligent TicTacToe: Easy");
				break;
			case 4: setTitle("Intelligent TicTacToe: Medium");
				break;
			case 6: setTitle("Intelligent TicTacToe: Hard");
				break;
			default: setTitle("Intelligent TicTacToe");
				break;
			}
            //restart the game, propmt the playing order selection again
            whoGoFirst();
        }
    }
        
    //Class to perform appropriate action when Exit button is pressed
    //All arrays are reset, gameover is changed to false, and message is displayed
    private class ExitButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e3) {
            System.exit(0);
        }
    }

    //Main class, only needs to create a tictactoeDriver object
    public static void main(String[] args) throws IOException {
        TictactoeDriver game = new TictactoeDriver();
    }//end main
        
}// end tictactoeDriver
