import javax.swing.*; 
import java.awt.*; 

public class WordSearchSolver {
    
    private static final int[] ROW_DIRECTIONS = {-1, -1, -1, 0, 0, 1, 1, 1};  
    private static final int[] COL_DIRECTIONS = {-1, 0, 1, -1, 1, -1, 0, 1};  
    private static final boolean[][] visited = new boolean[10][10]; // to check if the cell has already been visited 
    private static JButton[][] buttons = new JButton[10][10]; // for highlighting 
    public static void main(String[] args){
        JFrame frame = new JFrame(); // initialise a new jframe 

        frame.setTitle("Word Search Solver"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setResizable(false); 
        frame.setSize(600,600); 
        frame.setBackground(new Color(0,0,0)); 
        frame.setVisible(true); 
       
        JPanel gridPanel = new JPanel(new GridLayout(10, 10));
        char[][] board = new char[10][10]; 
 
        // generate random board 
        for (int i = 0; i < board.length; i ++){
            for (int j = 0; j < board[i].length; j ++){
                char randomChar = (char) ('A' + (int)(Math.random() * 26));
                board[i][j] = randomChar; 
                JButton btn = new JButton("" + board[i][j]);
                btn.setFont(new Font("Arial", Font.BOLD, 16));
                buttons[i][j] = btn;
                gridPanel.add(btn);
            }

        }

        // word input to be searched in the wordsearch 
        JTextField wordInput = new JTextField(15);
        JButton solveButton = new JButton("Find Word");

        solveButton.addActionListener(e -> {
            String word = wordInput.getText().trim().toUpperCase();
            resetHighlights(); // clear old highlights

            if (findWord(word, board)) {
                System.out.println("Word found: " + word);
            }else{
                JOptionPane.showMessageDialog(frame, "Word not found.");
            } 
        });
        

        // input area 
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Enter word:"));
        controlPanel.add(wordInput);
        controlPanel.add(solveButton);

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    /**
     * Find the first letter of the word and select a direction to search 
     * @param word inputted String 
     * @param board board to search 
     * @return true if the word is in the board 
     */
    public static boolean findWord(String word, char[][] board) {
        int rows = board.length;
        int cols = board[0].length;


        // find the first letter of the word in the board 
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == word.charAt(0)) {
                    for (int d = 0; d < 8; d++) {
                        
                        // clear visited for each new word 
                        clearVisited();

                        int dRow = ROW_DIRECTIONS[d];
                        int dCol = COL_DIRECTIONS[d];


                        // if the word is in this direction, return true 
                        if (solve(i, j, word, 0, board, dRow, dCol)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Backtracking with pruning (stop searching if the word is not in this direction)
     * @param row
     * @param col
     * @param word
     * @param index
     * @param board
     * @param dRow change in the x direction 
     * @param dCol change in the y direction 
     * @return
     */
    public static boolean solve(int row, int col, String word, int index, char[][] board, int dRow, int dCol) {
        int rows = board.length;
        int cols = board[0].length;

        // found the whole word 
        if (index == word.length()) {
            return true;  
        }

        // out of bounds and revisiting the letter, stop searching this direction 
        if (row < 0 || row >= rows || col < 0 || col >= cols || visited[row][col]) {
            return false;
        }

        // if not the next letter in the word, we stop searching this direction 
        if (board[row][col] != word.charAt(index)) {
            return false;
        }

        visited[row][col] = true;
        buttons[row][col].setBackground(Color.YELLOW);  // highlight letter 

        // continue only in selected direction 
        int newRow = row + dRow;
        int newCol = col + dCol;

        // recurse over the rest of the word 
        if (solve(newRow, newCol, word, index + 1, board, dRow, dCol)) {
            return true;
        }

        // if false, we unvisit this letter and backtrack
        visited[row][col] = false;
        buttons[row][col].setBackground(null);  // unhighlight if backtracking
        return false;
    }


    private static void resetHighlights() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                buttons[i][j].setBackground(null);
            }
        }
    }

    private static void clearVisited() {
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j] = false;
            }
        }
    }

}
