import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

public class Solver {

    private class Node {
        private final Board curr;
        private final Node prev;
        private final int stepCount;

        public Node(Board curr, Node prev) {
            this.curr = curr;
            this.prev = prev;
            if (prev != null) {
                this.stepCount = prev.stepCount + 1;
            } else {
                this.stepCount = 0;
            }
        }
    }

    private Node solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        Comparator<Node> comparator = new Comparator<>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.curr.manhattan() + o1.stepCount - o2.curr.manhattan() - o2.stepCount;
            }
        };

        MinPQ<Node> nodes = new MinPQ<>(comparator);
        MinPQ<Node> twinNodes = new MinPQ<>(comparator);

        Node initNode = new Node(initial, null);
        Node twinNode = new Node(initial.twin(), null);

        nodes.insert(initNode);
        twinNodes.insert(twinNode);

        Node minNode = nodes.delMin();
        Node twinMinNode = twinNodes.delMin();

        while (!minNode.curr.isGoal() && !twinMinNode.curr.isGoal()) {
            for (Board neighbor :
                    minNode.curr.neighbors()) {
                Board prev = minNode.prev == null ? null : minNode.prev.curr;
                if (!neighbor.equals(prev)) {
                    nodes.insert(new Node(neighbor, minNode));
                }
            }
            for (Board neighbor :
                    twinMinNode.curr.neighbors()) {
                Board prev = twinMinNode.prev == null ? null : twinMinNode.prev.curr;
                if (!neighbor.equals(prev)) {
                    twinNodes.insert(new Node(neighbor, twinMinNode));
                }
            }
            minNode = nodes.delMin();
            twinMinNode = twinNodes.delMin();
        }
        if (minNode.curr.isGoal()) {
            solution = minNode;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solution == null ? -1 : solution.stepCount;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) {
            return null;
        }
        Node tmp = solution;
        Stack<Board> boards = new Stack<>();
        while (tmp != null) {
            boards.push(tmp.curr);
            tmp = tmp.prev;
        }
        return boards;
    }

    // test client (see below)
    public static void main(String[] args) {
// create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
