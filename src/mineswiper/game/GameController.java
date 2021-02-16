package mineswiper.game;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class GameController {

    public static GameController mw = new GameController();
    private Game myGame;
    private int boardSize;

    GameController(){
        this.boardSize = 5;
        initGame(5,3);
    }

    GameController(int boardSize, int minesNum){
        this.boardSize = boardSize;
        initGame(boardSize, minesNum);
    }

    private int initGame(int boardSize, int minesNum){ // initializing the game and the board.
        System.out.println("initializing the game.");
        myGame = new Game(boardSize, minesNum);
        myGame.generateMines(minesNum);
        return 0;
    }

    public int[][] watchBoard(){
        // return -10 in a house if not discovered the value yet. i.e. revealed = false.
        int[][] watchBoard = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                watchBoard[i][j] = myGame.watchHouse(i,j);
            }
        }
        return watchBoard;
    }

    public int reveal(int i, int j){ // returns the number of the house, -1 if mine. although it throws the gameFailed exception.
        return myGame.reveal(i,j);
    }

    protected void gameLoop(){ // i think this should be for server-client model. which is not learnt/developed yet.

        while (myGame.gameCondition()==0){ // ==0 means the game hasn't finished yet.

            // watchHouse
            // wait for reveal invocation.

        }

        System.out.print("the game is ");
        if(myGame.gameCondition() == -1)
            System.out.println("LOOOOOOOOOOST !!!!!!!!!");
        else
            System.out.println("won! :D");
    }
}
