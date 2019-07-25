import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Board {

    private static final int CLOSED = 6;
    private static final int FLAG = 7;
    private static final int MINE = 8;

    private int width;
    private int height;
    private double numMines;
    private Cell[][] cells;

    private Map<Integer, Image> images = new HashMap<>();

    public Board(int width, int height, int numMines) {
        this.width = width;
        this.height = height;
        this.numMines = numMines;
        getImages();
        initBoard();
    }

    /**
     * Initialize cells and cells.neighbors
     * Run once on startup, use resetBoard()
     * for repeat games
     */
    private void initBoard(){
        setCells(new Cell[width][height]);
        for(int i =0; i < width; i++){
            for(int j = 0; j < height; j++){
                Cell cell = new Cell(i, j);
                cell.setIcon(new ImageIcon(images.get(CLOSED)));
                cell.addMouseListener(new MSMouseListener());
                setCell(i, j , cell);
            }
        }
        initNeighbors();
        setMines();
        computeNeighbors();
    }

    private void initNeighbors(){
        for(int i =0; i < getCells().length; i++){
            for(int j = 0; j < getCells()[i].length; j++) {
                ArrayList<Cell> neighbors = getNeighbors(i, j);
                getCell(i, j).setNeighbors(neighbors);
            }
        }
    }

    private void computeNeighbors(){
        for(int i =0; i < getWidth(); i++){
            for(int j = 0; j < getHeight(); j++) {
                getCell(i, j).computeNeighbors();
            }
        }
    }

    private ArrayList<Cell> getNeighbors(int i, int j){
        ArrayList<Cell> neighbors = new ArrayList<>();

        for(int di = -1; di <= 1; di++){
            for(int dj = -1; dj <= 1; dj++){
                if(isWithinBoard(i + di, j + dj) && !(di == 0 && dj == 0)){
                    neighbors.add(getCell(i + di, j + dj));
                }
            }
        }
        return neighbors;
    }

    private boolean isWithinBoard(int i, int j){
        return i > -1 && i < getWidth() && j > -1 && j < getHeight();
    }

    private void setMines(){
        int randI = ThreadLocalRandom.current().nextInt(0, getWidth());
        int randJ = ThreadLocalRandom.current().nextInt(0, getHeight());

        for(int i = 0; i < numMines; i++){
            while(getCell(randI, randJ).hasMine()){
                randI = ThreadLocalRandom.current().nextInt(0, getWidth());
                randJ = ThreadLocalRandom.current().nextInt(0, getHeight());
            }
            getCell(randI, randJ).setMine(true);
        }
    }

    public boolean setCellOpen(int i, int j){
        Cell cell = getCell(i, j);
        if(cell.hasMine()){
            cell.setIcon(new ImageIcon(images.get(MINE)));
            return false;
        }
        else {
            cell.setOpen(true);
            cell.setIcon(new ImageIcon(images.get(cell.getNumMinesAdjacent())));
            if(cell.getNumMinesAdjacent() == 0){
                openNeighbors(cell);
            }
            return true;
        }
    }

    private void openNeighbors(Cell cell){
        for(Cell neighbor : cell.getNeighbors()){
            if(!neighbor.hasMine() && !neighbor.isOpen()){
                setCellOpen(neighbor.getI(), neighbor.getJ());
            }
        }
    }

    public void flag(Cell cell){
        if(!cell.isOpen()){
            if(cell.isFlagged()){
                cell.setFlagged(false);
                cell.setIcon(new ImageIcon(images.get(CLOSED)));
            }
            else{
                cell.setFlagged(true);
                cell.setIcon(new ImageIcon(images.get(FLAG)));
            }
        }
    }

    private void resetBoard(){

    }

    public void openFullBoard(){
        StringBuilder sb = new StringBuilder();
        for(Cell[] cells : getCells()){
            for(Cell cell : cells){
                cell.setOpen(true);
            }
        }
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(Cell[] cells : getCells()){
            for(Cell cell : cells){
                if(cell.isOpen()){
                    sb.append("<");
                    sb.append(cell.getNumMinesAdjacent()).append("> ");
                }
                else if(cell.isFlagged()){
                    sb.append("!   ");
                }
                else{
                    sb.append("O   ");
                }
            }
            sb.append(" \n");
        }
        return sb.toString();
    }

    public String printWithMines(){
        StringBuilder sb = new StringBuilder();

        for(Cell[] cells : getCells()){
            for(Cell cell : cells){
                if(cell.hasMine()){
                    sb.append(" M  ");
                }
                else if(cell.isOpen()) {
                    sb.append("<");
                    sb.append(cell.getNumMinesAdjacent()).append("> ");
                }
                else{
                    sb.append("O   ");
                }
            }
            sb.append(" \n");
        }
        return sb.toString();
    }

    private void getImages(){
        for(int i = 0; i < 6; i++){
            images.put(i, getScaledImage("images/" + i + ".png"));
        }
        images.put(CLOSED, getScaledImage("images/closed.png"));
        images.put(FLAG, getScaledImage("images/flag.png"));
        images.put(MINE, getScaledImage("images/mine.jpg"));

    }

    private class MSMouseListener implements MouseListener {
        public void mouseClicked(MouseEvent e) {
            Cell cell = (Cell)e.getSource();
            if (SwingUtilities.isRightMouseButton(e)) {
                handleRightClick(cell);
            }
            else {
                handleLeftClick(cell);
            }
        }

        private void handleRightClick(Cell cell){
            flag(cell);
        }

        private void handleLeftClick(Cell cell) {
            if(!cell.isOpen() && !cell.isFlagged()){
                setCellOpen(cell.getI(), cell.getJ());
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }

    private static Image getScaledImage(String imageString) {
        try {
            Image image = ImageIO.read(Minesweeper.class.getResource(imageString));
            return image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Cell[][] getCells() {
        return cells;
    }

    private void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    private void setCell(int i, int j, Cell cell){
        getCells()[i][j] = cell;
    }

    public Cell getCell(int i, int j){
        return getCells()[i][j];
    }

    public double getNumMines() {
        return numMines;
    }

    public void setNumMines(double numMines) {
        this.numMines = numMines;
    }

}
