import java.util.ArrayList;

public class Board {
    private final int[][] tiles;
    private final int length;
    private int zeroPosY = -1;
    private int zeroPosX = -1;
    private int hamming = -1;
    private int manhattan = -1;

    // create a tiles from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int[][] tilesCopy = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            System.arraycopy(tiles[i], 0, tilesCopy[i], 0, tiles.length);
        }
        this.tiles = tilesCopy;
        this.length = tiles.length;
    }

    // string representation of this tiles
    public String toString() {
        StringBuilder result = new StringBuilder().append(length).append('\n');
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                result.append(String.format("%2d ", tiles[i][j]));
            }
            result.append("\n");
        }
        return result.toString();
    }

    // tiles dimension n
    public int dimension() {
        return length;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming != -1) {
            return hamming;
        }
        hamming = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                initZeroPos(i, j);
                if (tiles[i][j] != i * length + 1 + j && (i != zeroPosY || j != zeroPosX)) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan != -1) {
            return manhattan;
        }
        manhattan = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                int tile = tiles[i][j];
                if (tile != 0) {
                    manhattan += Math.abs(i - (tile - 1) / length);
                    manhattan += Math.abs(j - (tile - 1) % length);
                }
            }
        }
        return manhattan;
    }

    // is this tiles the goal tiles?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this tiles equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (!(this.getClass() == y.getClass())) {
            return false;
        }
        Board another = (Board) y;
        if (another.length != this.length || another.tiles[0].length != this.tiles[0].length) {
            return false;
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (this.tiles[i][j] != another.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (!isZeroInitialized()) {
            for (int i = 0; i < length; i++) {
                for (int j = 0; j < length; j++) {
                    if (tiles[i][j] == 0) {
                        zeroPosY = i;
                        zeroPosX = j;
                        break;
                    }
                }
            }
        }
        ArrayList<Board> neighbors = new ArrayList<>(8);
        if (zeroPosY != 0) {
            int[][] newTiles = copyTiles();

            swap(newTiles, zeroPosY, zeroPosX, zeroPosY - 1, zeroPosX);
            neighbors.add(new Board(newTiles));
        }
        if (zeroPosY != length - 1) {
            int[][] newTiles = copyTiles();
            swap(newTiles, zeroPosY, zeroPosX, zeroPosY + 1, zeroPosX);
            neighbors.add(new Board(newTiles));
        }
        if (zeroPosX != 0) {
            int[][] newTiles = copyTiles();
            swap(newTiles, zeroPosY, zeroPosX, zeroPosY, zeroPosX - 1);
            neighbors.add(new Board(newTiles));
        }
        if (zeroPosX != length - 1) {
            int[][] newTiles = copyTiles();
            swap(newTiles, zeroPosY, zeroPosX, zeroPosY, zeroPosX + 1);
            neighbors.add(new Board(newTiles));
        }
        return neighbors;
    }

    // a tiles that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] newTiles = copyTiles();
        if (this.tiles[0][0] != 0 && this.tiles[0][1] != 0) {
            swap(newTiles, 0, 0, 0, 1);
        } else {
            swap(newTiles, 1, 0, 1, 1);
        }
        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
                {1, 3},
                {2, 0}
        };
        Board board = new Board(tiles);
        board.manhattan();
        System.out.println(board);
    }

    private boolean isZeroInitialized() {
        return zeroPosY != -1 && zeroPosX != -1;
    }

    private void initZeroPos(int i, int j) {
        if (tiles[i][j] == 0 && !isZeroInitialized()) {
            zeroPosY = i;
            zeroPosX = j;
        }
    }

    private int[][] copyTiles() {
        int[][] newTiles = new int[length][length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(tiles[i], 0, newTiles[i], 0, length);
        }
        return newTiles;
    }

    private void swap(int[][] array, int yPos1, int xPos1, int yPos2, int xPos2) {
        int tmp;
        tmp = array[yPos1][xPos1];
        array[yPos1][xPos1] = array[yPos2][xPos2];
        array[yPos2][xPos2] = tmp;
    }

}