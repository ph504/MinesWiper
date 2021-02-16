package mineswiper.game;

class Game {
    private int [][] board; // -1 --> mine, number of mines otherwise.
    private boolean [][] revealed;

    Game(int boardSize, int minesNum){
        board = new int [boardSize][boardSize];
        revealed = new boolean[boardSize][boardSize];
    }

    void generateMines(int minesNum) {
        boolean [][] isSet = new boolean[board.length][board.length];
        for (int i = 0; i < minesNum; i++) {
            int randI = (int)(Math.random()*board.length);
            int randJ = (int)(Math.random()*board.length);
            if(isSet[randI][randJ]){
                i--;
                continue;
            }
            board[randI][randJ] = -1;
            updateNeighbors(randI,randJ, new GenUpdator(this),isSet);
            isSet[randI][randJ] = true;
        }
    }

    private void updateNeighbors(int i, int j, Updator updator, boolean[][] ds){ // ds is the revealed array / isSet array.
        int m = 0;
        int n = 0;
        // corners.
        if(i == 0 && j == 0){
            m = i+1;
            n = j+1;
            if (board[m][n]!=-1)
                updator.operate(m,n,board);
            m = i;
            n = j+1;
            if(board[m][n]!=-1)
                updator.operate(m,n,board);
            m = i+1;
            n = j;
            if(board[i+1][j]!=-1)
                updator.operate(m,n,board);

        }
        else if(i == 0 && j == ds.length-1){
            m = i+1;
            n = j-1;
            if (board[i+1][j-1]!=-1)
                updator.operate(m,n,board);
            m = i;
            n = j-1;
            if(board[i][j-1]!=-1)
                updator.operate(m,n,board);
            m = i+1;
            n = j;
            if(board[i+1][j]!=-1)
                updator.operate(m,n,board);
        }
        else if(i == ds.length-1 && j == 0){
            m = i-1;
            n = j+1;
            if (board[i-1][j+1]!=-1)
                updator.operate(m,n,board);
            m = i;
            n = j+1;
            if(board[i][j+1]!=-1)
                updator.operate(m,n,board);
            m = i-1;
            n = j;
            if(board[i-1][j]!=-1)
                updator.operate(m,n,board);
        }
        else if(i == ds.length-1 && j == ds.length-1){
            m = i-1;
            n = j-1;
            if (board[i-1][j-1]!=-1)
                updator.operate(m,n,board);
            m = i;
            n = j-1;
            if(board[m][n]!=-1)
                updator.operate(m,n,board);
            m = i-1;
            n = j;
            if(board[m][n]!=-1)
                updator.operate(m,n,board);
        }
        // non-corners.
        else if (!ds[i][j]) // if not revealed or if not isSet --> update
            for (int k = -1; k < 2; k++)
                for (int l = -1; l < 2; l++)
                    if(board[i+k][j+l]!=-1)
                        updator.operate(i+k,j+l,board);




    }

    int gameCondition(){ // if every non-mine house is revealed the game is won, if a mine is revealed the game is lost.
        boolean notFinished = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(!revealed[i][j])
                    notFinished = true;
                else if(board[i][j]==-1)
                    return -1; // game lost
            }
        }

        return notFinished?0:1; // if the game is not finished it will return 0.
    }

    int getHouse(int i, int j){
        return board[i][j];
    }

    int watchHouse(int i, int j){
        return revealed[i][j]?board[i][j]:-10;
    }

    int reveal(int i, int j){
        revealed[i][j] = true;
        // if(board[i][j]==-1) throw new GameFailed();
        if(board[i][j]==0) { // recursively call the reveal function until it reveals the area which is empty
            updateNeighbors(i, j, new RevealUpdator(this), revealed);
        }
        return board[i][j];
    }

    @Override
    public String toString() {
        return "board : \n" + board + "the revealed map : \n" + revealed + "\n";

    }
}

interface Updator{ // strategy pattern.
    void operate(int i,int j, int[][]board);
}

class GenUpdator implements Updator{ // when the board is generated.
    private Game game;

    GenUpdator(Game game){
        this.game = game;
    }
    @Override
    public void operate(int i, int j, int[][]board) {
        ++board[i][j];
    }
}

class RevealUpdator implements Updator{ // when we want to reveal some parts of the board.

    private Game game;

    RevealUpdator(Game game){
        this.game = game;
    }
    @Override
    public void operate(int i, int j, int[][]board) {
        game.reveal(i, j);
    }
}
