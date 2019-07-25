import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Minesweeper {

    private static final int WIDTH = 24;
    private static final int HEIGHT = 24;
    private static final int NUM_MINES = 99;


    public static void main(String[] args) throws InterruptedException{
        JFrame f = new JFrame();//creating instance of JFrame

        Board board = new Board(WIDTH, HEIGHT, NUM_MINES);
        for(Cell[] cells : board.getCells()){
            for(Cell cell : cells){
                f.add(cell);
            }
        }
        f.setSize(WIDTH * 20, HEIGHT * 20);
        f.setResizable(false);
        f.setLayout(new GridLayout(WIDTH, HEIGHT));
        f.setVisible(true);

        Solver solver = new Solver(board);
        solver.play();
        board.openFullBoard();
        System.out.println(board.printWithMines());
    }





}
