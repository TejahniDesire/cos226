import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture; // Defensive copy

    private int[][] color; // Color matrix
    private double[][] energy; // Energy matrix

    /*
    General Thoughts:
    Store the RGB of each pixel in a 2-D array. Calculate and store energyMat
    in a local 2-D array if needed. Update the picture every time when
    removing.
     */

    private class pixel {
        private int x;
        private int y;

        // Construct pixel object
        private pixel(int col, int row) {
            x = col;
            y = row;
        }
    }


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new
                IllegalArgumentException("Null Picture");
        this.picture = new Picture(picture); // Defensive Copy
        color = colorMatrix();
        energy = energyMatrix();
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // private method to get red from 32-bit int of col x, y.
    private int getRed(int x) {
        return ((x >> 16) & 0xFF);
    }

    // private method to get red from 32-bit int of col x, y.
    private int getGreen(int x) {
        return ((x >> 8) & 0xFF);
    }

    // private method to get red from 32-bit int of col x, y.
    private int getBlue(int x) {
        return ((x) & 0xFF);
    }

    private int[][] colorMatrix() {
        // Build new Color matrix
        int[][] newColor = new int[width() + 2][height() + 2];
        for (int col = 1; col <= width(); col++) {
            for (int row = 1; row <= height(); row++) {
                int picCol = col - 1;
                int picRow = row - 1;
                // Copy the outer rows and columns
                newColor[col][row] = picture.getRGB(picCol, picRow);
                if (col == 1) newColor[0][row] = picture.getRGB(width() - 1, picRow);
                if (col == width()) newColor[width() + 1][row] =
                        picture.getRGB(0, picRow);
                if (row == 1) newColor[col][0] = picture.getRGB(picCol, height() - 1);
                if (row == height()) newColor[col][height() + 1]
                        = picture.getRGB(picCol, 0);
            }
        }
        return newColor;
    }

    // Construct new energy Matrix
    public double[][] energyMatrix() {
        int picHeight = height();
        int picWidth = width();
        double[][] newEnergy = new double[picWidth][picHeight]; // energyMat(Col, Row) = e(y,x)
        // color(Col, Row)

        for (int x = 0; x < picWidth; x++)
            for (int y = 0; y < picHeight; y++) {
                int colorCol = x + 1;
                int colorRow = y + 1;

                int rX = getRed(color[colorCol + 1][colorRow]) -
                        getRed(color[colorCol - 1][colorRow]);
                int gX = getGreen(color[colorCol + 1][colorRow]) -
                        getGreen(color[colorCol - 1][colorRow]);
                int bX = getBlue(color[colorCol + 1][colorRow]) -
                        getBlue(color[colorCol - 1][colorRow]);

                int rY = getRed(color[colorCol][colorRow + 1]) -
                        getRed(color[colorCol][colorRow - 1]);
                int gY = getGreen(color[colorCol][colorRow + 1]) -
                        getGreen(color[colorCol][colorRow - 1]);
                int bY = getBlue(color[colorCol][colorRow + 1]) -
                        getBlue(color[colorCol][colorRow - 1]);
                int x2 = rX * rX + gX * gX + bX * bX;
                int y2 = rY * rY + gY * gY + bY * bY;

                newEnergy[x][y] = Math.sqrt(x2 + y2);
            }
        return newEnergy;
    }

    // energyMat of pixel at column x and row y
    public double energy(int x, int y) {
        // Exception
        int colright = width() - 1;
        int rowbottom = height() - 1;
        if (x < 0 || x > colright) throw new
                IllegalArgumentException("Invalid column");
        if (y < 0 || y > rowbottom) throw new
                IllegalArgumentException("Invalid Row");

        return energy[x][y];

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int picWidth = width();
        int picHeight = height();
        double[][] distTo = new double[picWidth][picHeight];
        int[][] edgeTo = new int[picWidth][picHeight];

        for (int k = 0; k < picHeight; k++) {
            distTo[0][k] = energy[0][k];
            edgeTo[0][k] = -1; // Source node is at row -1
        }

        // go through implicit DAG and relax edges in topological order
        for (int i = 1; i < picWidth; i++) {
            for (int k = 0; k < picHeight; k++) {

                // Check left Pixel energy
                if ((distTo[i][k] == 0) ||
                        (distTo[i][k] > distTo[i - 1][k] + energy[i][k])) {
                    distTo[i][k] = distTo[i - 1][k] + energy[i][k];
                    edgeTo[i][k] = k;
                }

                // Check left upper, ensure not on top edge
                if (k != 0) {
                    if ((distTo[i][k] == 0) ||
                            (distTo[i][k] > distTo[i - 1][k - 1] + energy[i][k])) {
                        distTo[i][k] = distTo[i - 1][k - 1] + energy[i][k];
                        edgeTo[i][k] = k - 1;
                    }
                }

                // Check right lower, ensure not on bottom edge
                if (k != picHeight - 1) {
                    if ((distTo[i][k] == 0) ||
                            (distTo[i][k] > distTo[i - 1][k + 1] + energy[i][k])) {
                        distTo[i][k] = distTo[i - 1][k + 1] + energy[i][k];
                        edgeTo[i][k] = k + 1;
                    }
                }
            }
        }

        // find smallest path's end edge
        double champ = Double.POSITIVE_INFINITY;
        int champIndex = -1;
        for (int k = 0; k < picHeight; k++) {
            if (distTo[picWidth - 1][k] < champ) {
                champ = distTo[picWidth - 1][k];
                champIndex = k;
            }
        }

        // creat seam object
        int[] seam = new int[picWidth];
        seam[picWidth - 1] = champIndex;
        for (int i = picWidth - 2; i >= 0; i--) {
            seam[i] = edgeTo[i + 1][seam[i + 1]];
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int picWidth = width();
        int picHeight = height();
        double[][] distTo = new double[picWidth][picHeight];
        int[][] edgeTo = new int[picWidth][picHeight];

        for (int i = 0; i < picWidth; i++) {
            distTo[i][0] = energy[i][0];
            edgeTo[i][0] = -1; // Source node is at row -1
        }

        // go through implicit DAG and relax edges in topological order
        for (int k = 1; k < picHeight; k++) {
            for (int i = 0; i < picWidth; i++) {

                // Check Above Pixel energy
                if ((distTo[i][k] == 0) || (distTo[i][k] >
                        distTo[i][k - 1] + energy[i][k])) {
                    distTo[i][k] = distTo[i][k - 1] + energy[i][k];
                    edgeTo[i][k] = i;
                }

                // Check upper left, ensure not on leftmost edge
                if (i != 0) {
                    if ((distTo[i][k] == 0) || (distTo[i][k] >
                            distTo[i - 1][k - 1] + energy[i][k])) {
                        distTo[i][k] = distTo[i - 1][k - 1] + energy[i][k];
                        edgeTo[i][k] = i - 1;
                    }
                }

                // Check upper right, ensure not on rightmost edge
                if (i != picWidth - 1) {
                    if ((distTo[i][k] == 0) || (distTo[i][k] >
                            distTo[i + 1][k - 1] + energy[i][k])) {
                        distTo[i][k] = distTo[i + 1][k - 1] + energy[i][k];
                        edgeTo[i][k] = i + 1;
                    }
                }
            }
        }

        // find smalled valued path
        double champ = Double.POSITIVE_INFINITY;
        int champIndex = -1;
        for (int i = 0; i < picWidth; i++) {
            if (distTo[i][picHeight - 1] < champ) {
                champ = distTo[i][picHeight - 1];
                champIndex = i;

            }
        }

        int[] seam = new int[picHeight];
        seam[picHeight - 1] = champIndex;

        // creat seam object
        for (int k = picHeight - 2; k >= 0; k--) {
            // StdOut.println(seam[k + 1]);
            seam[k] = edgeTo[seam[k + 1]][k + 1];

        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // Exceptions
        if (seam == null) throw new IllegalArgumentException("Null Input");
        if (seam.length != width()) throw new
                IllegalArgumentException("Wrong Input Length");
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) throw
                    new IllegalArgumentException("Not a Seam");
        }

        int[][] localcolor = new int[width()][height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                localcolor[col][row] = picture.getRGB(col, row);
            }
        }

        // Building the new picture
        picture = new Picture(width(), height() - 1);
        for (int col = 0; col < width(); col++) {
            int newrow = 0;
            for (int row = 0; row < height(); row++) {
                // if (col == seam[row]) continue; // Removing
                // picture.setRGB(col, row, localcolor[col][row]);
                // If row is indicaded for removal, don't set and don't increment newrow
                if (col != seam[row]) {
                    picture.setRGB(col, newrow, localcolor[col][row]);
                    newrow++;
                } // Removing
            }
        }
        color = colorMatrix();
        energy = energyMatrix();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // Exceptions
        if (seam == null) throw new IllegalArgumentException("Null Input");
        if (seam.length != height()) throw new
                IllegalArgumentException("Wrong Input Length");
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) throw
                    new IllegalArgumentException("Not a Seam");
        }

        // Set Color
        // int[][] localcolor = new int[height()][width()];
        // for (int col = 0; col < width(); col++) {
        //     for (int row = 0; row < height(); row++) {
        //         localcolor[row][col] = picture.getRGB(col, row);
        //     }
        // }
        int[][] localcolor = new int[width()][height()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                localcolor[col][row] = picture.getRGB(col, row);
            }
        }

        picture = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            int newcol = 0;
            for (int col = 0; col < width(); col++) {
                // if (col == seam[row]) continue; // Removing
                // picture.setRGB(col, row, localcolor[col][row]);
                // If col is indicaded for removal, don't set and don't increment
                if (col != seam[row]) {
                    picture.setRGB(newcol, row, localcolor[col][row]);
                    newcol++;
                } // Removing
            }
        }
        color = colorMatrix();
        energy = energyMatrix();
    }


    //  unit testing (required)
    public static void main(String[] args) {

    }

}
