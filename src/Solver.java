import java.util.concurrent.ThreadLocalRandom;

public class Solver {

    private Board board;
    private double[][] probabilityBoard;

    public Solver(Board board){
        this.board = board;
        probabilityBoard = new double[board.getWidth()][board.getHeight()];
    }

    public void play() {
        while(anyCellNotFlaggedAndNotOpen()){
            Cell choiceCell = computeLowestProbability(board);
            if(!choiceCell.isFlagged() && !board.setCellOpen(choiceCell.getI(), choiceCell.getJ())){
                System.out.println("You Lose on square: " + choiceCell.getI() + ", " + choiceCell.getJ());
                break;
            }
            System.out.println("Chose: " + choiceCell.getI() + ", " + choiceCell.getJ());
            System.out.println(board.toString());
        }
    }

    private boolean anyCellNotFlaggedAndNotOpen(){
        for(Cell[] cells : board.getCells()){
            for(Cell cell : cells) {
                if(!cell.isFlagged() && !cell.isOpen()){
                    return true;
                }
            }
        }
        return false;
    }

    private Cell computeLowestProbability(Board board) {
        configProbabilityBoard();
        doFlagging();
        return chooseCell();
    }

    private void doFlagging() {
        for(Cell[] cells : board.getCells()){
            for(Cell cell : cells){
                if(hasOpenNeighbor(cell) && !cell.isOpen()) {
                    double thisProbability = computeProbabilityOnClosed(cell);
                    if(thisProbability == 1){
                        board.flag(cell);
                        configProbabilityBoard();
                    }
                }
            }
        }
    }

    private Cell chooseCell(){
        Cell choiceCell = board.getCell(ThreadLocalRandom.current().nextInt(0, board.getWidth()), ThreadLocalRandom.current().nextInt(0, board.getHeight()));

        double lowestProbability = 1;

        for(Cell[] cells : board.getCells()){
            for(Cell cell : cells){
                if(hasOpenNeighbor(cell) && !cell.isOpen()) {
                    double thisProbability = computeProbabilityOnClosed(cell);
                    if(thisProbability < lowestProbability && !cell.isFlagged()){
                        lowestProbability = thisProbability;
                        choiceCell = cell;
                    }
                }
            }
        }

        return choiceCell;
    }

    private double computeProbabilityOnClosed(Cell cell){
        double probability = 0;
        for(Cell neighbor : cell.getNeighbors()){
            if(neighbor.isOpen()){
                double neighborProb = probabilityBoard[neighbor.getI()][neighbor.getJ()];
                if(neighborProb == 0){
                    return 0;
                }
                if(neighborProb > probability){
                    probability = neighborProb;
                }
            }
        }
        return probability;
    }

    private boolean hasOpenNeighbor(Cell cell){
        for(Cell neighbor : cell.getNeighbors()){
            if(neighbor.isOpen()){
                return true;
            }
        }
        return false;
    }

    private void configProbabilityBoard(){
        for(Cell[] cells : board.getCells()){
            for(Cell cell : cells){
                if(cell.isOpen() && cell.getNumMinesAdjacent() != 0) {
                    probabilityBoard[cell.getI()][cell.getJ()] = computeProbabilityOnOpen(cell);
                }
            }
        }
    }

    private double computeProbabilityOnOpen(Cell cell) {
        int numUnopenedNeighbors = 0;
        int numFlagged = 0;
        for(Cell neighbor : cell.getNeighbors()){
            if(!neighbor.isOpen()){
                if(!neighbor.isFlagged()){
                    numUnopenedNeighbors++;
                }
                else{
                    numFlagged++;
                    if(cell.getNumMinesAdjacent() == numFlagged){
                        return 0;
                    }
                }
            }
        }

        return ((double)(cell.getNumMinesAdjacent() - numFlagged) / (double)numUnopenedNeighbors);
    }

}
