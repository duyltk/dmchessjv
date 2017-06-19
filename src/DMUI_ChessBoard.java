/**
 * Created by bigzero on 6/19/17.
 */
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class DMUI_ChessBoard extends GridPane{
    public DMUI_Square[][] Square = new DMUI_Square[8][8];
    public boolean flagClick = false;
    public DMUI_Square activeSquare = null;
    String ListMove = "";

    public DMUI_ChessBoard()
    {
        super();

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                boolean light = ((x + y) % 2 != 0); // checkerboard space colors
                Square[x][y] = new DMUI_Square(light, x, y);

                this.add(Square[x][y], x, 7 - y);
                final int xVal = x;
                final int yVal = y;

                Square[x][y].setOnAction( e -> onSpaceClick(xVal, yVal) );
            }
        }
    }

    public void onSpaceClick(int x, int y)
    {
        if (Character.isLowerCase(Square[x][y].piece.charAt(0)))
        {
            if (!flagClick)
            {
                Square[x][y].getStyleClass().add("chess-space-active");
                activeSquare = Square[x][y];
                flagClick = true;

                DMChess.flipBoard();

                switch(activeSquare.piece.toUpperCase())
                {
                    case "P":
                        ListMove = DMChess.movePawn((7-y) * 8 +(7-x));
                        break;
                    case "R":
                        ListMove = DMChess.moveRook((7-y) * 8 +(7-x));
                        break;
                    case "B":
                        ListMove = DMChess.moveBishop((7-y) * 8 +(7-x));
                        break;
                    case "K":
                        ListMove = DMChess.moveKnight((7-y) * 8 +(7-x));
                        break;
                    case "Q":
                        ListMove = DMChess.moveQueen((7-y) * 8 +(7-x));
                        break;
                    case "A":
                        ListMove = DMChess.moveKing((7-y) * 8 +(7-x));
                        break;
                }
                DMChess.flipBoard();

                for (int i = 0; i < ListMove.length(); i+=5)
                {
                    if (ListMove.charAt(i + 4) != 'C'){
                        int col = 7-Integer.parseInt(String.valueOf(ListMove.charAt(i + 3)));
                        int row = 7-Integer.parseInt(String.valueOf(ListMove.charAt(i + 2)));

                        if (((col + row) % 2 != 0))
                            Square[col][row].getStyleClass().add("chess-space-hover-light");
                        else
                            Square[col][row].getStyleClass().add("chess-space-hover-dark");
                    }
                    else {
                        //Colume Previous King, Rook => Next King, Rook
                        int colRook = 7-Integer.parseInt(String.valueOf(ListMove.charAt(i + 1)));
                        int colKing = 7-Integer.parseInt(String.valueOf(ListMove.charAt(i)));

                        if (colRook % 2 == 0){
                            Square[colKing][0].getStyleClass().add("chess-space-hover-dark");
                        }
                        else
                            Square[colRook][0].getStyleClass().add("chess-space-hover-light");

                    }
                }
            }
            else {
                if (Square[x][y] == activeSquare)
                {
                    activeSquare.getStyleClass().removeAll("chess-space-active");
                    activeSquare = null;
                    flagClick = false;
                    ListMove = "";
                }
                else if(Square[x][y].piece.equals("r") && activeSquare.piece.equals("a")){
                    int updateRook = 0, updateKing = 0;
                    String applyMoveKing = "";
                    if (x == 0) {
                        if (DMChess.castlingLLong) {
                            applyMoveKing = "3754C";
                            updateRook = 2;
                            updateKing = 3;
                        }
                    } else {
                        if (DMChess.castlingLShort){
                            applyMoveKing = "3012C";
                            updateRook = 5;
                            updateKing = 6;
                        }
                    }
                    if (applyMoveKing.length() != 0){
                        DMChess.flipBoard();
                        DMChess.applyMove(applyMoveKing);
                        DMChess.flipBoard();
                        DMChess.drawBoard();
                        activeSquare.setPathGraphic();
                        Square[x][y].setPathGraphic();
                        Square[updateKing][0].setPathGraphic();
                        Square[updateRook][0].setPathGraphic();

                        flagClick = false;
                    }
                }
            }
        }
        else {
            if (flagClick){
                for (int i = 0; i < ListMove.length(); i+= 5)
                {
                    int col = 7-Integer.parseInt(String.valueOf(ListMove.charAt(i + 3)));
                    int row = 7-Integer.parseInt(String.valueOf(ListMove.charAt(i + 2)));
                    if (x == col && y == row){
                        DMChess.flipBoard();
                        DMChess.applyMove(ListMove.substring(i, i + 5));
                        DMChess.flipBoard();
                        activeSquare.setGraphic(new ImageView());
                        activeSquare.getStyleClass().removeAll("chess-space-active");
                        Square[x][y].piece = activeSquare.piece;
                        activeSquare.piece = " ";
                        Square[x][y].setPathGraphic();
                        DMChess.drawBoard();
                        flagClick = false;
                    }
                }
            }
        }
        if (!flagClick)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    Square[i][j].getStyleClass().removeAll("chess-space-hover-light");
                    Square[i][j].getStyleClass().removeAll("chess-space-hover-dark");
                }
            }
        }
    }
}