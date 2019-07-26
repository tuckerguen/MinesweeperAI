import javax.swing.*;
import java.awt.*;

public class Minesweeper {

    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;
    private static final int NUM_MINES = 99;
    private static final boolean RUN_AI = true;
    private static Board board = new Board(WIDTH, HEIGHT, NUM_MINES);

    public static void main(String[] args) {
        initGui();
        if(RUN_AI){
            runAI();
        }
    }

    private static void runAI() {
        Solver solver = new Solver(board);
        solver.play();
        board.openFullBoard();
        System.out.println(board.printWithMines());
    }

    private static void initGui() {
        JFrame f = new JFrame();
        f.setSize(WIDTH * 22, HEIGHT * 22);
        f.setResizable(false);
        f.setLayout(new GridLayout(WIDTH, HEIGHT));

        for(Cell[] cells : board.getCells()){
            for(Cell cell : cells){
                f.add(cell);
            }
        }

        f.setVisible(true);
    }
}
