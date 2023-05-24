public class EQ {
    private static Piece[][] ChessBoard = new Piece[8][8];

    public static void main(String[] args) throws Exception {
        //Populate chessboard with emptey spaces
        for (int row = 0; row < ChessBoard.length; row++) {
            for (int column = 0; column < ChessBoard[0].length; column++) {
                ChessBoard[row][column] = new Piece();
            }
        }

        //Find solution
        placePiece(true);
        printBoard();
    }

    public static void placePiece(boolean printProgress) throws InterruptedException {
        if(isBoardEmpty()) {
            //Randomly place a queen in each column
            for (int column = 0, row = 0; column < ChessBoard.length; column++, row++) {
                ChessBoard[row][column].setType(true);
            }
        }

        //Randomly choose two columns and swap them if the solution hasn't been reached
        if (!solutionFound()) {
            //Gets two random column
            int rndFirstColumn = rndNumber(ChessBoard[0].length);
            int rndSecondColumn = rndNumber(ChessBoard[0].length, rndFirstColumn);

            //Swaps the two columns
            swapPieces(
                findQueenInColumn(rndFirstColumn), 
                rndFirstColumn, 
                findQueenInColumn(rndFirstColumn),
                rndSecondColumn);
            swapPieces(
                findQueenInColumn(rndSecondColumn), 
                rndSecondColumn, 
                findQueenInColumn(rndSecondColumn),
                rndFirstColumn);

            //Prints the current layout of the chessboard to the console if wanted
            if (printProgress) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                printBoard();
                Thread.sleep(20);
            }

            //Since no solution was found, re-call placePiece (recursively)
            placePiece(printProgress);
        }

    }      

    //Returns random number from zero to length-1
    public static int rndNumber(int length) {
        double num = Math.random()*length;
        return (int) num;
    }
    //Returns random number from zero to length-1 that isnt equal to "reRollIf"
    public static int rndNumber(int length, int reRollIf) {
        double num = Math.random()*length;
        if ((int) num != reRollIf) return (int) num;
        else return rndNumber(length, reRollIf);
    }

    //Resturns the row a queen in a given column is located in, if there is no queen it returns -1
    public static int findQueenInColumn(int column) {
        for (int row = 0; row < ChessBoard.length; row++) {
            if (ChessBoard[row][column].isQueen())
                return row;
        }
        return -1;
    }

    //Returns true if the board contains no queens
    public static boolean isBoardEmpty() {
        for (int row = 0; row < ChessBoard.length; row++) {
            for (int column = 0; column < ChessBoard[0].length; column++) {
                if (ChessBoard[row][column].isQueen())
                    return false;
            }
        }
        return true;
    }

    //Returns true if the two perameters are within the lengths and width of the board
    public static boolean isIndexInBounds(int row, int column) {
        if (row > ChessBoard.length - 1 || column > ChessBoard[0].length - 1)
            return false;
        if (row < 0 || column < 0)
            return false;
        return true;
    }

    //Returns true if the solution to eight queens is reached
    public static boolean solutionFound() {
        for (int column = 0; column < ChessBoard.length; column++) {
            if (!isQueenInColumnValid(column))
                return false;
        }
        return true;
    }

    //Returns true if the given spot on the board (acting as a queen) would conflict with another queen's position as a possible move
    public static boolean isInConflict(int r, int c) {
        //Check diagonal with slope 1/1 (+,+)
        int col = c + 1;
        for (int row = r - 1; row >= 0; row--) {
            if (isIndexInBounds(row, col)) {
                if (ChessBoard[row][col].isQueen())
                    return true;
            }
            col++;
        }
        //Check diagonal with slope -1/-1 (-,-)
        col = c - 1;
        for (int row = r + 1; row < ChessBoard.length; row++) {
            if (isIndexInBounds(row, col)) {
                if (ChessBoard[row][col].isQueen())
                    return true;
            }
            col--;
        }
        //Check diagonal with slope -1/1 (-,+)
        col = c - 1;
        for (int row = r - 1; row >= 0; row--) {
            if (isIndexInBounds(row, col)) {
                if (ChessBoard[row][col].isQueen())
                    return true;
            }
            col--;
        }
        //Check diagonal with slope 1/-1 (+,-)
        col = c + 1;
        for (int row = r + 1; row < ChessBoard.length; row++) {
            if (isIndexInBounds(row, col)) {
                if (ChessBoard[row][col].isQueen())
                    return true;
            }
            col++;                
        }
        //Check vertical
        for (int row = 0; row < ChessBoard.length; row++) {
            if (ChessBoard[row][c].isQueen() && ChessBoard[row][c] != ChessBoard[r][c])
                return true;
        }
        //Check horizontal
        for (int column = 0; column < ChessBoard[0].length; column++) {
            if (ChessBoard[r][column].isQueen() && ChessBoard[r][column] != ChessBoard[r][c])
                return true;
        }
        return false;
    }

    //Returns true if the queen in a given column does not conflict with another queen's position as one of its possible moves
    public static boolean isQueenInColumnValid(int column) {
        for (int row = 0; row < ChessBoard.length; row++) {
            if (ChessBoard[row][column].isQueen() && isInConflict(row, column))
                return false;
        }
        return true;
    }

    //Swaps two pieces on the board
    public static void swapPieces(int currentRow, int currentColumn, int newRow, int newColumn) {
        boolean currentIsQueen = ChessBoard[currentRow][currentColumn].isQueen();
        boolean newIsQueen = ChessBoard[newRow][newColumn].isQueen();
        ChessBoard[currentRow][currentColumn].setType(newIsQueen);
        ChessBoard[newRow][newColumn].setType(currentIsQueen);
    }

    //Prints the current layout of the board
    public static void printBoard() {
        if (solutionFound())
        {
            System.out.println("\nSolution Found!");
        }
        System.out.println("-----------------");
        for (int row = 0; row < ChessBoard.length; row++) {
            for (int column = 0; column < ChessBoard[0].length; column++) {
                if (column == 0)
                    System.out.print("|");
                if (!ChessBoard[row][column].isQueen())
                    System.out.print(" |");
                else if (ChessBoard[row][column].isQueen())
                    System.out.print("X|");
                if (column == ChessBoard[0].length - 1 && row != ChessBoard.length - 1)
                    System.out.println();
            }
        }
        System.out.println("\n-----------------");
    }
}
