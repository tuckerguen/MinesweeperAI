import javax.swing.*;
import java.util.ArrayList;

public class Cell extends JButton {
    private boolean isMine;
    private boolean flagged;
    private boolean open;
    private int i;
    private int j;
    private int numMinesAdjacent;
    private ArrayList<Cell> neighbors;

    public Cell(int x, int j){
        this.i = x;
        this.j = j;
        isMine = false;
        open = false;
        numMinesAdjacent = 0;
        neighbors = new ArrayList<>();
    }

    public void computeNeighbors(){
        for(Cell cell : neighbors){
            if(cell.hasMine()){
                numMinesAdjacent++;
            }
        }
    }

    public int getNumMinesAdjacent() {
        if(isOpen()){
            return numMinesAdjacent;
        }
        else {
            return -1;
        }
    }

    public void setNumMinesAdjacent(int numMinesAdjacent) {
        this.numMinesAdjacent = numMinesAdjacent;
    }

    public boolean hasMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        this.isMine = mine;
    }

    public ArrayList<Cell> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}
