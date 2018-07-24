package neuroevolution.def.tic_tac_toe;

import java.util.ArrayList;

public class Board {

    private int[][] fields;
    private int current_id;

    public Board(){
        reset();
    }

    public void reset(){
        fields = new int[3][3];
        current_id = 1;
    }

    public void place(int x, int y) {
        fields[x][y] = current_id;
        current_id = -current_id;
    }

    public int winnerID(){
        for(int i = 0; i < 3; i++){
            int val = fields[i][0];
            boolean win = true;
            for(int n = 0; n < 3; n++){
                if(fields[i][n] != val) win = false;
            }
            if(win) return val;
        }

        for(int i = 0; i < 3; i++){
            int val = fields[0][i];
            boolean win = true;
            for(int n = 0; n < 3; n++){
                if(fields[n][i] != val) win = false;
            }
            if(win) return val;
        }
        if(fields[0][0] == fields[1][1] && fields[1][1] == fields[2][2]) return fields[0][0];
        if(fields[0][2] == fields[1][1] && fields[1][1] == fields[2][0]) return fields[0][2];
        return 0;
    }

    public ArrayList<int[]> possibleMoves(){
        ArrayList<int[]> out = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int n = 0; n < 3; n++){
                if(fields[i][n] == 0){
                    out.add(new int[]{i,n});
                }
            }
        }
        return out;
    }

    public Board copyBoard(){
        Board b = new Board();
        for (int i = 0; i < 3; i++) {
            for (int n = 0; n < 3; n++) {
                b.fields[i][n] = this.fields[i][n];
            }
        }
        b.current_id = this.current_id;
        return b;
    }

    public String toString() {
        String out = "#####\n";
        for(int i = 0; i < 3; i++){
            out += "#";
            for(int n = 0; n < 3; n++){
                out += fields[n][i] != 0? (fields[n][i] == 1? "x":"o"):(" ");
            }
            out += "#\n";
        }
        return out + "#####\n";
    }

    public int[][] getFields() {
        return fields;
    }

    public void setFields(int[][] fields) {
        this.fields = fields;
    }

    public int getCurrent_id() {
        return current_id;
    }

    public void setCurrent_id(int current_id) {
        this.current_id = current_id;
    }
}
