package neuroevolution.def.tic_tac_toe;

public class Match {

    private Player playerA;
    private Player playerB;

    private Board board = new Board();

    public Match(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public int play(){

        while(board.winnerID() == 0 &&  board.possibleMoves().size() > 0){
            playerA.let_npc_move(board);
            if(board.winnerID() == 0 &&  board.possibleMoves().size() > 0){
                playerB.let_npc_move(board);
            }
        }

        int winnerID = board.winnerID();
        return winnerID;

    }

    public void validate(){
        while(board.winnerID() == 0 &&  board.possibleMoves().size() > 0){
            playerA.let_npc_move(board);
            System.out.println(board);
            if(board.winnerID() == 0 &&  board.possibleMoves().size() > 0){
                playerB.let_npc_move(board);
                System.out.println(board);
            }
        }
    }

    public static void main(String[] args){
        new Match(new Player(), new Player()).play();
    }
}
