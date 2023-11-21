import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture; // Defensive copy
    private int[][] color; // Color matrix
    private Double[][] energyMat; // Energy matrix

    /*
    General Thoughts:
    Store the RGB of each pixel in a 2-D array. Calculate and store energyMat
    in a local 2-D array if needed. Update the picture every time when
    removing.
     */


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new
                IllegalArgumentException("Null Picture");
        this.picture = new Picture(picture); // Defensive Copy
        colorMatrix();
        energyMatrix();
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

    // Build color matrix
    // private void colorMatrix() {
    //     // Build Color matrix
    //     color = new int[height() + 2][width() + 2];
    //     for (int col = 1; col <= width(); col++) {
    //         for (int row = 1; row <= height(); row++) {
    //             int picCol = col - 1;
    //             int picRow = row - 1;
    //             // Copy the outer rows and columns
    //             color[row][col] = picture.getRGB(picCol, picRow);
    //             if (col == 1) color[row][0] = picture.getRGB(width() - 1, picRow);
    //             if (col == width()) color[row][width() + 1] =
    //                     picture.getRGB(0, picRow);
    //             if (row == 1) color[0][col] = picture.getRGB(picCol, height() - 1);
    //             if (row == height()) color[height() + 1][col]
    //                     = picture.getRGB(picCol, 0);
    //         }
    //     }
    // }
    private void colorMatrix() {
        // Build Color matrix
        color = new int[width() + 2][height() + 2];
        for (int col = 1; col <= width(); col++) {
            for (int row = 1; row <= height(); row++) {
                int picCol = col - 1;
                int picRow = row - 1;
                // Copy the outer rows and columns
                color[col][row] = picture.getRGB(picCol, picRow);
                if (col == 1) color[0][row] = picture.getRGB(width() - 1, picRow);
                if (col == width()) color[width() + 1][row] =
                        picture.getRGB(0, picRow);
                if (row == 1) color[col][0] = picture.getRGB(picCol, height() - 1);
                if (row == height()) color[col][height() + 1]
                        = picture.getRGB(picCol, 0);
            }
        }
    }

    // Construct energy Matrix
    public void energyMatrix() {
        int picHeight = height();
        int picWidth = width();
        energyMat = new Double[picWidth][picHeight]; // energyMat(Col, Row) = e(y,x)
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

                energyMat[x][y] = Math.sqrt(x2 + y2);
            }
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

        return energyMat[x][y];

        // // Build Color matrix
        // int[][] color = new int[height() + 2][width() + 2];
        // for (int col = 1; col <= width(); col++) {
        //     for (int row = 1; row <= height(); row++) {
        //         // Copy the outer rows and columns
        //         color[row][col] = picture.getRGB(col - 1, row - 1);
        //         if (col == 1) color[row][0] = picture.getRGB(width() - 1, row - 1);
        //         if (col == width()) color[row][width() + 1] =
        //                 picture.getRGB(0, row - 1);
        //         if (row == 1) color[0][col] = picture.getRGB(col - 1, height() - 1);
        //         if (row == height()) color[height() + 1][col]
        //                 = picture.getRGB(col - 1, 0);
        //     }
        // }
        // int rX = getRed(color[y + 1][x]) - getRed(color[y + 1][x + 2]);
        // int gX = getGreen(color[y + 1][x]) - getGreen(color[y + 1][x + 2]);
        // int bX = getBlue(color[y + 1][x]) - getBlue(color[y + 1][x + 2]);
        // int rY = getRed(color[y][x + 1]) - getRed(color[y + 2][x + 1]);
        // int gY = getGreen(color[y][x + 1]) - getGreen(color[y + 2][x + 1]);
        // int bY = getBlue(color[y][x + 1]) - getBlue(color[y + 2][x + 1]);
        // int x2 = rX * rX + gX * gX + bX * bX;
        // int y2 = rY * rY + gY * gY + bY * bY;
        // return (Math.sqrt(x2 + y2));
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
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

        // Set Color
        int[][] color = new int[height()][width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                color[row][col] = picture.getRGB(col, row);
            }
        }

        // Building the new picture
        picture = new Picture(width(), height() - 1);
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if (row == seam[col]) continue; // Removing
                picture.setRGB(col, row, color[col][row]);
            }
        }
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
        int[][] color = new int[height()][width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                color[row][col] = picture.getRGB(col, row);
            }
        }

        // Building the new picture
        picture = new Picture(width(), height() - 1);
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                if (col == seam[row]) continue; // Removing
                picture.setRGB(col, row, color[col][row]);
            }
        }
    }

    //  unit testing (required)
    public static void main(String[] args) {

    }

}
