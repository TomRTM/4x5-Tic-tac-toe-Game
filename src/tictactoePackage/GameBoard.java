package tictactoePackage;


/*
*       Class to remember the best move during alpha beta pruning
*/
class MoveInfo {
	public static final int NEGATIVE_INFINITY = -100000;
	
	private int moveX;
	private int moveY;
	private int score;
	
	//Constructor
	public MoveInfo() {
	    moveX = -1;
	    moveY = -1;
	    score = NEGATIVE_INFINITY;
	}
	
	//Get X value of best move
	public int getMoveX() {
	    return moveX;
	}
	
	//Set X value of best move
	public void setMoveX(int x) {
	    moveX = x;
	}
	
	//Get Y value of best move
	public int getMoveY() {
	    return moveY;
	}
	
	//Set Y value of best move
	public void setMoveY(int y) {
	    moveY = y;
	}
	
	//Get value of the score associated with this move
	public int getScore() {
	    return score;
	}
	
	//Set value of the score associated with this move
	public void setScore(int s) {
	    score = s;
	}
}// end MoveInfo


/*
*       This class represents the current state of the game
*/
class GameBoard {
	//constants
	public static final int POSITIVE_INFINITY = 100000;
	public static final int NEGATIVE_INFINITY = -100000;
	public static final char EMPTY = 'E';
	public static final char PLAYERX = 'X';//computer
	public static final char PLAYERO = 'O';//player
	public static final int NOWIN = 0;
	public static final int XWIN = 1;//computer wins
	public static final int OWIN = 2;//player wins
	public static final int TIE = 3;
	public static final int ROW = 4;
	public static final int COLUMN = 5;
	public static final int WIN_LENGTH = 4;

    // board fields
    private char[][] board;
    private int emptySquares;
    private int won;
    private int moveX;
    private int moveY;
    private int cutOffDepth;


    //Constructor
    //Creates empty board of size 4 x 5
    public GameBoard() {
        emptySquares = ROW*COLUMN;	
        won = NOWIN;
        board = new char[ROW][COLUMN];
        for(int i = 0; i < ROW ;i++) {
            for(int j = 0; j < COLUMN; j++) {
                board[i][j] = 'E';
            } 
        }
        moveX = -1;
        moveY = -1;
        cutOffDepth = 4;//by default the depth is 4
    }

    //Copy constructor for creating children
    public GameBoard(GameBoard b, int e) {
        emptySquares = e;
        board = new char[ROW][COLUMN];
        moveX = -1;
        moveY = -1;
        for(int i=0;i<ROW;i++) {
            for(int j=0;j<COLUMN;j++) {
                board[i][j] = b.board[i][j];
            }
        }
    }

    
    //Set cutOffDepth
    public void setDepth(int depth) {
    	cutOffDepth = depth;
    }
    
    //Access cutOffDepth
    public int getDepth() {
    	return cutOffDepth;
    }
         
    //Reset best move to null
    public void resetMove() {
        moveX = -1;
        moveY = -1;
    }

    //Access game winner
    public int getWinner() {
        return won;
    }

    //Get X value of the best move
    public int getMoveX() {
        return moveX;
    }

    //Get Y value of the best move
    public int getMoveY() {
        return moveY;
    }

    //Set X value of the best move
    public void setMoveX(int x) {
        moveX = x;
    }

    //Set Y value of the best move
    public void setMoveY(int y) {
        moveY = y;
    }

    //Get number of empty game tiles
    public int getEmptySquares() {
        return emptySquares;
    }

    //Set number of empty game tiles
    public void setEmptySquares(int e) {
        emptySquares = e;
    }

    //A game tile has been filled, decrement empty squares
    public void fillSquare() {
        emptySquares--;
    }
         
    //Set game winner
    //Return true if integer passed is a valid winner code
    //Otherwise, return false
    public boolean setWinner(int w) {
        //Make sure the value is a valid code
        if(w >= NOWIN && w <= TIE) {
            won = w;
            return true;
        }
        else {
            return false;
        }
    }
         

    public void setTile(int x,int y,char val){
        board[x][y] = val;
    }

    //Get the value of a tile on the board
    public char getTile(int x,int y){
        return board[x][y];
    }
         
    //Print Game Board
    public void printBoard() {
        System.out.println("Current Board:");
        for(int i = 0; i < ROW; i++) {
            for(int j = 0; j < COLUMN; j++) {
                System.out.print(board[i][j] + " ");
            }
        System.out.print("\n");
        }
    }
         
    //Updated to account for winning length and all diagonals
    //When a new move is made, check to see if it is a winning move
    //Set the board winner if there is one
    //Return true if there is a winner or a tie, otherwise false
    public boolean checkWinner(int x, int y, char C) {
        int check = 0;
        int i;
        //Check row
        for(i = 0; i < COLUMN; i++) {
            if(board[x][i] == C){
                check++;
                if(check == WIN_LENGTH){
                    if(C == PLAYERX){
                        setWinner(XWIN);
                    }
                    else if(C == PLAYERO){
                        setWinner(OWIN);
                    }
                    return true;
                }
            }
            else{
                check = 0;
            }
        }//end for
        if(check == WIN_LENGTH){
                if(C == PLAYERX){
                    setWinner(XWIN);
                }
                else if(C == PLAYERO){
                    setWinner(OWIN);
                }               
                return true;
        }//end if
        
        //Check column
        check = 0;
        for(i = 0; i < ROW; i++){
            if(board[i][y] == C){
                check++;
                if(check == WIN_LENGTH){
                    if(C == PLAYERX){
                        setWinner(XWIN);
                    }
                    else if(C == PLAYERO){
                        setWinner(OWIN);
                    }
                    return true;
                }
            }
            else{
                check = 0;
            }
        } 
        if(check == WIN_LENGTH){
            if(C == PLAYERX){
                setWinner(XWIN);
            }
            else if(C == PLAYERO){
                setWinner(OWIN);
            }
            
            return true;
        }
        
        //Need to always check diagonals
        //Check diagonal with negative slope
        int tempx = x;
        int tempy = y;
        while(tempx > 0 && tempy > 0){
            tempx--;
            tempy--;
        }
        
        check = 0;
        while(tempx < ROW && tempy < COLUMN){
            if(board[tempx][tempy] == C){
                check++;
                if(check == WIN_LENGTH){
                    if(C == PLAYERX){
                        setWinner(XWIN);
                    }
                    else if(C == PLAYERO){
                        setWinner(OWIN);
                    }
                    return true;
                }//end if                   
            }//end if
            else {
                check = 0;
            }
            tempx++;
            tempy++;
        }//end while
        
        if(check == WIN_LENGTH){
            if(C == PLAYERX){
                setWinner(XWIN);
            }
            else if(C == PLAYERO){
                setWinner(OWIN);
            }
            
            return true;
        }//end if
        
        //Check diagonal with positive slope
        check = 0;
        tempx = x;
        tempy = y;
        while(tempx > 0 && tempy < COLUMN - 1){       
            tempx--;
            tempy++;
        }
        
        while(tempx < ROW && tempy >= 0){
            if(board[tempx][tempy] == C){
                check++;
                if(check == WIN_LENGTH){
                    if(C == PLAYERX){
                        setWinner(XWIN);
                    }
                    else if(C == PLAYERO){
                        setWinner(OWIN);
                    }
                    return true;
                }//end if                   
            }//end if
            else{
                check = 0;
            }
            tempx++;
            tempy--;                
        }//end while
        
        if(check == WIN_LENGTH){
            if(C == PLAYERX){
                setWinner(XWIN);
            }
            else if(C == PLAYERO){
                setWinner(OWIN);
            }
    
            return true;
        }//end if
            
        //Check for tie
        if(emptySquares == 0){
            setWinner(TIE);
            return true;
        }

        //No winner found
        return false;
    }

        
         
     //Evaluate board for a player
     //This is calculated for both players, and the final value of the board
     //is calculated by subtracting the points of the opponent from the points
     //of the current player
     //Need to award points even if there are X's and O's in the same row/col/diag
     public int evaluation(char player)
     {
        char opponent;
        int utilityPlayer = 0, utilityOpponent = 0;
        int i,j,k,m;
        //to count how many consecutive balls in one line (row, column, diagonal)
        //Note: empty tiles would be counted valid for both player and opponent
        int lengthPlayer = 0, lengthOpponent = 0;
        //to count balls to see which pattern it matched (1-in-line, 2-in-line, 3-in-line)
        int countPlayer = 0, countOpponent = 0;
        int oneInLinePly = 0,
            twoInLinePly = 0,
            threeInLinePly = 0,
            winForPly = 0, //four in a line
            oneInLineOpp = 0,
            twoInLineOpp = 0,
            threeInLineOpp = 0,
            winForOpp = 0; //four in a line
        int startRow, startCol;
     
            
        //Figure out what player you are evaluating for
        //and who the opponent is
        if(player == PLAYERX){
            opponent = PLAYERO;
        }
        else{
            opponent = PLAYERX;
        }
                
        
        //System.out.println("Check Rows");
        //Do each row
        for(i = 0; i < ROW; i++)
        {
            //Reset lengths for each new row
            lengthPlayer = 0;
            lengthOpponent = 0;
            
            for(j = 0; j < COLUMN; j++){
                if(board[i][j] == EMPTY){
                    lengthPlayer++;
                    lengthOpponent++;
                }
                else if(board[i][j] == player){
                    lengthPlayer++;
                    
                    //Check opponent's section
                    //Only give points if big enough to win in
                    if(lengthOpponent >= WIN_LENGTH){                                                               
                            
                        k = j-1;                                    
                        countOpponent = 0;
                        while(lengthOpponent > 0){
                            //Count number of total squares
                            if(board[i][k] == opponent){
                                countOpponent++;
                            } 

                            k--;
                            lengthOpponent--;
                        }//end while
                        if(countOpponent==1){
                        	oneInLineOpp++;
                        }
                        else if(countOpponent==2){
                        	twoInLineOpp++;
                        }
                        else if(countOpponent==3){
                        	threeInLineOpp++;
                        }
                        else if(countOpponent>=4){
                            winForOpp++;
                        }

                        utilityOpponent = utilityOpponent + oneInLineOpp*1 + twoInLineOpp*10 + threeInLineOpp*100 + winForOpp*1000;
                        
                    }//end if
                    
                    //Reset opponent's section
                    lengthOpponent = 0;
                    oneInLineOpp = 0;
                    twoInLineOpp = 0;
                    threeInLineOpp = 0;
                    winForOpp = 0;
                    
                }//end else if for player

                else if(board[i][j] == opponent){
                    lengthOpponent++;  
                    
                    if(lengthPlayer >= WIN_LENGTH){                                   
                        k = j-1;                                    
                        countPlayer = 0;
                        while(lengthPlayer > 0){
                            //Count number of total squares
                            if(board[i][k] == player){
                                countPlayer++;
                            }  

                            k--;
                            lengthPlayer--;
                        }//end while
                        if(countPlayer==1){
                            oneInLinePly++;
                        }
                        else if(countPlayer==2){
                            twoInLinePly++;
                        }
                        else if(countPlayer==3){
                            threeInLinePly++;
                        }
                        else if(countPlayer>=4){
                            winForPly++;
                        }

                        utilityPlayer = utilityPlayer + oneInLinePly*1 + twoInLinePly*10 + threeInLinePly*100 + winForPly*1000;
                        
                    }//end if
                    
                    //Reset player's section
                    lengthPlayer = 0;
                    oneInLinePly = 0;
                    twoInLinePly = 0;
                    threeInLinePly = 0;
                    winForPly = 0;
                    
                }//end "else if" for opponent
            }//end for (one row)   
            

            if(lengthPlayer > lengthOpponent)
            {
                if(lengthPlayer >= WIN_LENGTH){                                   
                    k = j-1;                                    
                    countPlayer = 0;
                    while(lengthPlayer > 0){
                        //Count number of total squares
                        if(board[i][k] == player){
                            countPlayer++;
                        }  

                        k--;
                        lengthPlayer--;
                    }//end while
                    if(countPlayer==1){
                        oneInLinePly++;
                    }
                    else if(countPlayer==2){
                        twoInLinePly++;
                    }
                    else if(countPlayer==3){
                        threeInLinePly++;
                    }
                    else if(countPlayer>=4){
                        winForPly++;
                    }

                    utilityPlayer = utilityPlayer + oneInLinePly*1 + twoInLinePly*10 + threeInLinePly*100 + winForPly*1000;
                    
                }//end if

                //Reset player's section    
                lengthPlayer = 0;
                oneInLinePly = 0;
                twoInLinePly = 0;
                threeInLinePly = 0;
                winForPly = 0;
            }
            else
            {
                if(lengthOpponent >= WIN_LENGTH){                                                               
                        
                    k = j-1;                                    
                    countOpponent = 0;
                    while(lengthOpponent > 0){
                        //Count number of total squares
                        if(board[i][k] == opponent){
                            countOpponent++;
                        }    

                        k--;
                        lengthOpponent--;
                    }//end while
                    if(countOpponent==1){
                        oneInLineOpp++;
                    }
                    else if(countOpponent==2){
                        twoInLineOpp++;
                    }
                    else if(countOpponent==3){
                        threeInLineOpp++;
                    }
                    else if(countOpponent>=4){
                        winForOpp++;
                    }

                    utilityOpponent = utilityOpponent + oneInLineOpp*1 + twoInLineOpp*10 + threeInLineOpp*100 + winForOpp*1000;
                                  
                }//end if

                //Reset opponent's section
                lengthOpponent = 0;
                oneInLineOpp = 0;
                twoInLineOpp = 0;
                threeInLineOpp = 0;
                winForOpp = 0;
            }
        }//end for (i row)
            

        //======================================================================================================
        //System.out.println("Check Columns");
        //Do each column
        for(i = 0; i < COLUMN; i++) {
            //Reset lengths for each new column
            lengthPlayer = 0;
            lengthOpponent = 0;
            
            for(j = 0; j < ROW; j++) {
                if(board[j][i] == EMPTY) {
                    lengthPlayer++;
                    lengthOpponent++;
                }
                else if(board[j][i] == player) {
                    lengthPlayer++;

                    //Check opponent's section
                    //Only give points if big enough to win in
                    if(lengthOpponent >= WIN_LENGTH){                                                               
                            
                        k = j-1;                                    
                        countOpponent = 0;
                        while(lengthOpponent > 0){
                            //Count number of total squares
                            if(board[k][i] == opponent){
                                countOpponent++;
                            } 

                            k--;
                            lengthOpponent--;
                        }//end while
                        if(countOpponent==1){
                            oneInLineOpp++;
                        }
                        else if(countOpponent==2){
                            twoInLineOpp++;
                        }
                        else if(countOpponent==3){
                            threeInLineOpp++;
                        }
                        else if(countOpponent>=4){
                            winForOpp++;
                        }

                        utilityOpponent = utilityOpponent + oneInLineOpp*1 + twoInLineOpp*10 + threeInLineOpp*100 + winForOpp*1000; 

                    }//end if
                    
                    //Reset opponent's section
                    lengthOpponent = 0;
                    oneInLineOpp = 0;
                    twoInLineOpp = 0;
                    threeInLineOpp = 0;
                    winForOpp = 0;                                       
                }//end else if for player
                else {
                    lengthOpponent++;
                    
                    if(lengthPlayer >= WIN_LENGTH) {
                        k = j-1;                                    
                        countPlayer = 0;
                        while(lengthPlayer > 0) {
                            //Count number of total squares
                            if(board[k][i] == player) {
                                countPlayer++;
                            }  

                            k--;
                            lengthPlayer--;
                        }//end while
                        if(countPlayer==1){
                            oneInLinePly++;
                        }
                        else if(countPlayer==2){
                            twoInLinePly++;
                        }
                        else if(countPlayer==3){
                            threeInLinePly++;
                        }
                        else if(countPlayer>=4){
                            winForPly++;
                        }

                        utilityPlayer = utilityPlayer + oneInLinePly*1 + twoInLinePly*10 + threeInLinePly*100 + winForPly*1000;
                                                                                               
                    }//end if

                    //Reset player's section 
                    lengthPlayer = 0;
                    oneInLinePly = 0;
                    twoInLinePly = 0;
                    threeInLinePly = 0;
                    winForPly = 0;
                }//end else
            }//end for (one column)
            
            if(lengthPlayer > lengthOpponent)
            {
                    if(lengthPlayer >= WIN_LENGTH) {
                        k = j-1;                                    
                        countPlayer = 0;
                        while(lengthPlayer > 0){
                            //Count number of total squares
                            if(board[k][i] == player){
                                countPlayer++;
                            }  

                            k--;
                            lengthPlayer--;
                        }//end while
                        if(countPlayer==1){
                            oneInLinePly++;
                        }
                        else if(countPlayer==2){
                            twoInLinePly++;
                        }
                        else if(countPlayer==3){
                            threeInLinePly++;
                        }
                        else if(countPlayer>=4){
                            winForPly++;
                        }

                        utilityPlayer = utilityPlayer + oneInLinePly*1 + twoInLinePly*10 + threeInLinePly*100 + winForPly*1000;
                                                                                             
                    }//end if
                    
                    //Reset player's section 
                    lengthPlayer = 0;
                    oneInLinePly = 0;
                    twoInLinePly = 0;
                    threeInLinePly = 0;
                    winForPly = 0;
            }
            else
            {
                    if(lengthOpponent >= WIN_LENGTH)
                            {
                                k = j-1;                                    
                                countOpponent = 0;
                                while(lengthOpponent > 0){
                                    //Count number of total squares
                                    if(board[k][i] == opponent){
                                        countOpponent++;
                                    } 

                                    k--;
                                    lengthOpponent--;
                                }//end while
                                if(countOpponent==1){
                                    oneInLineOpp++;
                                }
                                else if(countOpponent==2){
                                    twoInLineOpp++;
                                }
                                else if(countOpponent==3){
                                    threeInLineOpp++;
                                }
                                else if(countOpponent>=4){
                                    winForOpp++;
                                }

                                utilityOpponent = utilityOpponent + oneInLineOpp*1 + twoInLineOpp*10 + threeInLineOpp*100 + winForOpp*1000;
                                    
                            }//end if
                            
                            //Reset opponent's section
                            lengthOpponent = 0;
                            oneInLineOpp = 0;
                            twoInLineOpp = 0;
                            threeInLineOpp = 0;
                            winForOpp = 0;

            }//end else
                
        }//end for (i column)
            
            

        //======================================================================================================
        //System.out.println("Check Positive Diagonals");
        //Do Positive slope diagonals************************************************************
        //for the next line, arg2
        //assume that: int x = Math.min(ROW, COLUMN)
        //then it should be like: for(i=0; i<x*2;i++)
        //since the max winlength could only be the min size of the board
        for(i = 0; i < ROW*2; i++)
        {
            lengthPlayer = 0;
            lengthOpponent = 0;
            //Only check if diagonal is big enough
            if(i >= WIN_LENGTH-1 && i < (COLUMN*2 - WIN_LENGTH)) { // 3 <= i < 6

                //for the next positive diagonal
                if(i > ROW-1) { //here i>3
                    startRow = ROW-1;// =3
                    startCol = i%(ROW-1);// here i=4                               
                }
                else { //for the first positive diagonal
                    startRow = i;//at this time, i=3
                    startCol = 0;
                }
            
                while(startRow > 0 && startCol < COLUMN) {
                    if(board[startRow][startCol] == EMPTY) {
                        lengthPlayer++;
                        lengthOpponent++;
                    }
                    else if(board[startRow][startCol] == player) {
                        lengthPlayer++;
                        
                        //Check opponent's section
                        //Only give points if big enough to win in
                        if(lengthOpponent >= WIN_LENGTH) {
                        
                            //Start checking at square before this one on the diagonal
                            k = startRow+1;
                            m = startCol-1;
                            countOpponent = 0;

                            while(lengthOpponent > 0)
                            {
                                //Count number of total squares
                                if(board[k][m] == opponent) {
                                    countOpponent++;
                                }
                                
                                k++;
                                m--;
                                lengthOpponent--;
                            }//end while

                            if(countOpponent==1){
                                oneInLineOpp++;
                            }
                            else if(countOpponent==2){
                                twoInLineOpp++;
                            }
                            else if(countOpponent==3){
                                threeInLineOpp++;
                            }
                            else if(countOpponent>=4){
                                winForOpp++;
                            }

                            utilityOpponent = utilityOpponent + oneInLineOpp*1 + twoInLineOpp*10 + threeInLineOpp*100 + winForOpp*1000;
                                                                                        
                        }//end if
                            
                        //Reset opponent's section
                        lengthOpponent = 0;
                        oneInLineOpp = 0;
                        twoInLineOpp = 0;
                        threeInLineOpp = 0;
                        winForOpp = 0;
                    }
                    else {
                        lengthOpponent++;
                        
                        if(lengthPlayer >= WIN_LENGTH) {
                            k = startRow+1;
                            m = startCol-1;
                            countPlayer = 0;

                            while(lengthPlayer > 0) {
                                //Count number of total squares
                                if(board[k][m] == player) {
                                    countPlayer++;
                                }
                                
                                k++;
                                m--;
                                lengthPlayer--;
                            }//end while
                            if(countPlayer==1){
                                oneInLinePly++;
                            }
                            else if(countPlayer==2){
                                twoInLinePly++;
                            }
                            else if(countPlayer==3){
                                threeInLinePly++;
                            }
                            else if(countPlayer>=4){
                                winForPly++;
                            }

                            utilityPlayer = utilityPlayer + oneInLinePly*1 + twoInLinePly*10 + threeInLinePly*100 + winForPly*1000;
                                                                                      
                        }//end if
                            
                        lengthPlayer = 0;
                        oneInLinePly = 0;
                        twoInLinePly = 0;
                        threeInLinePly = 0;
                        winForPly = 0;
                    }//end else
                    //to the next cell in same line            
                    startRow--;
                    startCol++;
                }//end while
                    
                if(lengthPlayer > lengthOpponent) {
                    if(lengthPlayer >= WIN_LENGTH) {
                        k = startRow+1;
                        m = startCol-1;
                        countPlayer = 0;

                        while(lengthPlayer > 0) {
                            //Count number of total squares
                            if(board[k][m] == player){
                                countPlayer++;
                            }
                            
                            k++;
                            m--;
                            lengthPlayer--;
                        }//end while
                        if(countPlayer==1){
                            oneInLinePly++;
                        }
                        else if(countPlayer==2){
                            twoInLinePly++;
                        }
                        else if(countPlayer==3){
                            threeInLinePly++;
                        }
                        else if(countPlayer>=4){
                            winForPly++;
                        }

                        utilityPlayer = utilityPlayer + oneInLinePly*1 + twoInLinePly*10 + threeInLinePly*100 + winForPly*1000;
                        
                    }//end if
                                
                    lengthPlayer = 0;
                    oneInLinePly = 0;
                    twoInLinePly = 0;
                    threeInLinePly = 0;
                    winForPly = 0;
                }
                else
                {
                    if(lengthOpponent >= WIN_LENGTH) {
                            
                        //Start checking at square before this one on the diagonal
                        k = startRow+1;
                        m = startCol-1;
                        countOpponent = 0;

                        while(lengthOpponent > 0) {
                            //Count number of total squares
                            if(board[k][m] == opponent) {
                                countOpponent++;
                            }
                            
                            k++;
                            m--;
                            lengthOpponent--;
                        }//end while

                        if(countOpponent==1){
                            oneInLineOpp++;
                        }
                        else if(countOpponent==2){
                            twoInLineOpp++;
                        }
                        else if(countOpponent==3){
                            threeInLineOpp++;
                        }
                        else if(countOpponent>=4){
                            winForOpp++;
                        }

                        utilityOpponent = utilityOpponent + oneInLineOpp*1 + twoInLineOpp*10 + threeInLineOpp*100 + winForOpp*1000;                                                   
                                    
                    }//end if
                            
                    //Reset opponent's section
                    lengthOpponent = 0;
                    oneInLineOpp = 0;
                    twoInLineOpp = 0;
                    threeInLineOpp = 0;
                    winForOpp = 0;
                }//end else 
            }//end if (to check if the diagonal is big enough)                       
        }//end for
            



        //System.out.println("Check Negative Diagonals");
        //Do Negative Slope Diagonals *****************************************************
        for(i = 0; i < COLUMN*2; i++)
        {
            lengthPlayer = 0;
            lengthOpponent = 0;
            //Only check if diagonal is big enough
            if(i >= WIN_LENGTH-1 && i < (ROW*2 - WIN_LENGTH)) { // 3 <= i < 4, here means only i=3 is valid
                //for the next negative diagonal
                if(i > ROW-1) { //here i>3, but in 4*5 is impossible for this condition
                    startRow = 0;
                    startCol = i%(ROW-1);
                }
                else { //for the first negative diagonal
                    startCol = 0;
                    startRow = (ROW-1) - i;// here i=3, thus value is 0
                }

                
                while(startRow < ROW && startCol < COLUMN) {
                    if(board[startRow][startCol] == EMPTY) {
                        lengthPlayer++;
                        lengthOpponent++;
                    }
                    else if(board[startRow][startCol] == player) {
                        lengthPlayer++;
                        
                        //Check opponent's section
                        //Only give points if big enough to win in
                        if(lengthOpponent >= WIN_LENGTH) {             
                            //Start checking at square before this one on the diagonal
                            k = startRow-1;
                            m = startCol-1;                                            
                            countOpponent = 0;

                            while(lengthOpponent > 0) {
                                //Count number of total squares
                                if(board[k][m] == opponent) {
                                    countOpponent++;
                                }
                                
                                k--;
                                m--;
                                lengthOpponent--;
                            }//end while
                            if(countOpponent==1){
                                oneInLineOpp++;
                            }
                            else if(countOpponent==2){
                                twoInLineOpp++;
                            }
                            else if(countOpponent==3){
                                threeInLineOpp++;
                            }
                            else if(countOpponent>=4){
                                winForOpp++;
                            }

                            utilityOpponent = utilityOpponent + oneInLineOpp*1 + twoInLineOpp*10 + threeInLineOpp*100 + winForOpp*1000;
                                                                                                                                                                
                        }//end if
                        
                        //Reset opponent's section
                        lengthOpponent = 0;
                        oneInLineOpp = 0;
                        twoInLineOpp = 0;
                        threeInLineOpp = 0;
                        winForOpp = 0;
                    }
                    else {
                        lengthOpponent++;
                        
                        if(lengthPlayer >= WIN_LENGTH) {
                            //Start checking at square before this one
                            //i stays the same, same row
                            //k is the end of the section
                            k = startRow-1;
                            m = startCol-1;                                           
                            countPlayer = 0;
                            while(lengthPlayer > 0) {
                                //Count number of total squares
                                if(board[k][m] == player) {
                                    countPlayer++;
                                }
                                
                                k--;
                                m--;
                                lengthPlayer--;
                            }//end while
                            if(countPlayer==1){
                                oneInLinePly++;
                            }
                            else if(countPlayer==2){
                                twoInLinePly++;
                            }
                            else if(countPlayer==3){
                                threeInLinePly++;
                            }
                            else if(countPlayer>=4){
                                winForPly++;
                            }

                            utilityPlayer = utilityPlayer + oneInLinePly*1 + twoInLinePly*10 + threeInLinePly*100 + winForPly*1000;
                                                                                                                                                                                              
                        }//end if
                        
                        lengthPlayer = 0;
                        oneInLinePly = 0;
                        twoInLinePly = 0;
                        threeInLinePly = 0;
                        winForPly = 0;
                    }//end else
            
            
                    startRow++;
                    startCol++;
                }//end while
                
                if(lengthPlayer > lengthOpponent) {
                    if(lengthPlayer >= WIN_LENGTH) {
                        //Start checking at square before this one
                        //i stays the same, same row
                        //k is the end of the section
                        k = startRow-1;
                        m = startCol-1;                                           
                        countPlayer = 0;
                        while(lengthPlayer > 0) {
                            //Count number of total squares
                            if(board[k][m] == player) {
                                countPlayer++;
                            }
                            
                            k--;
                            m--;
                            lengthPlayer--;
                        }//end while
                        if(countPlayer==1){
                            oneInLinePly++;
                        }
                        else if(countPlayer==2){
                            twoInLinePly++;
                        }
                        else if(countPlayer==3){
                            threeInLinePly++;
                        }
                        else if(countPlayer>=4){
                            winForPly++;
                        }

                        utilityPlayer = utilityPlayer + oneInLinePly*1 + twoInLinePly*10 + threeInLinePly*100 + winForPly*1000;
                                                                                                                                                                                       
                    }//end if
                    
                    lengthPlayer = 0;
                    oneInLinePly = 0;
                    twoInLinePly = 0;
                    threeInLinePly = 0;
                    winForPly = 0;
                }
                else {
                    if(lengthOpponent >= WIN_LENGTH) {             
                        //Start checking at square before this one on the diagonal
                        k = startRow-1;
                        m = startCol-1;                                            
                        countOpponent = 0;

                        while(lengthOpponent > 0) {
                            //Count number of total squares
                            if(board[k][m] == opponent) {
                                countOpponent++;
                            }
                            
                            k--;
                            m--;
                            lengthOpponent--;
                        }//end while
                        if(countOpponent==1){
                            oneInLineOpp++;
                        }
                        else if(countOpponent==2){
                            twoInLineOpp++;
                        }
                        else if(countOpponent==3){
                            threeInLineOpp++;
                        }
                        else if(countOpponent>=4){
                            winForOpp++;
                        }

                        utilityOpponent = utilityOpponent + oneInLineOpp*1 + twoInLineOpp*10 + threeInLineOpp*100 + winForOpp*1000;
                                                                                                                                                           
                    }//end if
                    
                    //Reset opponent's section
                    lengthOpponent = 0;
                    oneInLineOpp = 0;
                    twoInLineOpp = 0;
                    threeInLineOpp = 0;
                    winForOpp = 0;
                }//end else                       
            }//end if (to check if the diagonal is big enough)
        }//end for

        //System.out.println("Return from evaluation function");
        return utilityPlayer - utilityOpponent;
    }//end evaluation function





//=======================================================================================================      
         
    //AI makes move
    public int moveOpponent(char c) {        
        //Object to keep track of the best move and its value
        //in the alpha-beta function
        MoveInfo finalMove = new MoveInfo();

        //Call alpha-beta function
        //current depth setting is 4. Try to make it configurable by users(different level selection)
        //easy:2 steps. medium:4 steps. high:6 steps
        int val = alphaBeta('O', cutOffDepth, this, finalMove);
              
        //Get best move
        moveX = finalMove.getMoveX();
        moveY = finalMove.getMoveY();
        
        //Set the move on the board
        setTile(moveX,moveY,c);
        fillSquare();
                           
        //System.out.println("*******FINAL MOVE*******");//test
        //printBoard();//test

        //Check for a winner based on this move
        checkWinner(moveX,moveY,c);

        return getWinner();
    }
         
         
        
    //Alpha-beta pruning function 
    public static int alphaBeta(char c, int depth, GameBoard board, MoveInfo lastMove) {
        int value = maxValue(c, depth, board, lastMove, NEGATIVE_INFINITY, POSITIVE_INFINITY);
        return value;
    }

    //Minimax function for max nodes
    public static int maxValue(char c, int depth, GameBoard board, MoveInfo finalMove, int alpha, int beta) {
        //Determine who our opponent is
        char next;
        if (c == PLAYERO){
            next = PLAYERX;
        }
        else{
            next = PLAYERO;
        }

        //IF GAME IS OVER in current board position, return board value
        if(board.getWinner() == OWIN){
            return POSITIVE_INFINITY;
        }
        else if(board.getWinner() == XWIN){
            return NEGATIVE_INFINITY;
        }
        else if(board.getWinner() == TIE){
            return 0;
        }
        else if(depth <=0){
            return board.evaluation(c);
        }
        
        int v = NEGATIVE_INFINITY;
        
        //For all children (all legal moves for the player from the current board)
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 5; j++) {
                //Make sure this is a legal move, square is not occupied
                if(board.getTile(i,j) == EMPTY) {
                    //Create and set child node
                    GameBoard child = new GameBoard(board, board.getEmptySquares());
                    child.setTile(i,j,c);
                    child.fillSquare();
                    child.checkWinner(i,j,c);
                                        
                    //Value of this node
                    int score = minValue(next,depth-1,child,finalMove,alpha,beta);                   
                    
                    if(score > v){
                        v = score;
                    }
                    
                    //Found a better move?
                    if(v > alpha){
                        alpha = v;
                        finalMove.setMoveX(i);
                        finalMove.setMoveY(j);
                        finalMove.setScore(v);
                    }
                                            
                    //Found a better move??
                    if(v >= beta){
                        return v;
                    }                            
                }//end large if
            }//end for j
        }//end for i
        return v;
            
    }//end maxvalue


    //Minimax function for min nodes
    public static int minValue(char c, int depth, GameBoard board,MoveInfo finalMove, int alpha, int beta) {
        //Determine who our opponent is
        char next;
        if (c == PLAYERO){
            next = PLAYERX;
        }
        else{
            next = PLAYERO;
        }
        
        //IF GAME IS OVER in current board position, return board value
        if(board.getWinner() == XWIN) {
            return NEGATIVE_INFINITY;
        }
        else if(board.getWinner() == OWIN) {
            return POSITIVE_INFINITY;
        }
        else if(board.getWinner() == TIE) {
            return 0;
        }
        else if(depth <=0) {
            return board.evaluation(next);
        }
        
        int v = POSITIVE_INFINITY;
        
        //For all children (all legal moves for the player from the current board)
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 5; j++) {
                //Make sure this is a legal move, square is not occupied
                if(board.getTile(i,j) == EMPTY) {
                    //Create and set child node
                    GameBoard child = new GameBoard(board, board.getEmptySquares());
                    child.setTile(i,j,c);
                    child.fillSquare();
                    child.checkWinner(i,j,c);                    
                    
                    //Value of this node
                    int score = maxValue(next,depth-1,child,finalMove,alpha,beta);                    
                    
                    if(score < v) {
                        v = score;
                    }
                    
                    if(v < beta) {
                        beta = v;
                    }
                    
                    if(v <= alpha) {
                        //Best move for X
                        //child.printBoard();
                        //System.out.println("MINVALUEScore: " + score + " Alpha:" + alpha);
                        //System.out.println("New Best move:" + i + " " + j);
                        return v;
                    }
                }//end large if
            }//end for j
        }//end for i
                        
        return v;      
    }//end minvalue

}//end GameBoard
