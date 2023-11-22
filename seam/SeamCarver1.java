import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

public class SeamCarver1 {
    private Picture picture; // Defensive copy


    /*
    General Thoughts:
    Store the RGB of each pixel in a 2-D array. Calculate and store energy
    in a local 2-D array if needed. Update the picture every time when
    removing.
     */


    // create a seam carver object based on the given picture
    public SeamCarver1(Picture picture) {
        if (picture == null) throw new
                IllegalArgumentException("Null Picture");
        this.picture = new Picture(picture); // Defensive Copy
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(picture);
        return pic;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // private method to get red from 32-bit int.
    private int getred(int rgb) {
        return ((rgb >> 16) & 0xFF);
    }

    // private method to get red from 32-bit int.
    private int getgreen(int rgb) {
        return ((rgb >> 8) & 0xFF);
    }

    // private method to get red from 32-bit int.
    private int getblue(int rgb) {
        return ((rgb) & 0xFF);
    }


    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        // Exception
        int colright = width() - 1;
        int rowbottom = height() - 1;
        if (x < 0 || x > colright) throw new
                IllegalArgumentException("Invalid column");
        if (y < 0 || y > rowbottom) throw new
                IllegalArgumentException("Invalid Row");

        // Build Color matrix
        int[][] color = new int[height() + 2][width() + 2];
        for (int col = 1; col <= width(); col++) {
            for (int row = 1; row <= height(); row++) {
                // Copy the outer rows and columns
                color[row][col] = picture.getRGB(col - 1, row - 1);
            }
        }

        // Copy the leftmost and rightmost columns for rolling.
        for (int row = 1; row <= height(); row++) {
            color[row][0] = color[row][width()];
            color[row][width() + 1] = color[row][1];
        }

        // Copy the up and bottom rows for rolling.
        for (int col = 1; col <= width(); col++) {
            color[0][col] = color[height()][col];
            color[height() + 1][col] = color[1][col];
        }

        int rX = getred(color[y + 1][x]) - getred(color[y + 1][x + 2]);
        int gX = getgreen(color[y + 1][x]) - getgreen(color[y + 1][x + 2]);
        int bX = getblue(color[y + 1][x]) - getblue(color[y + 1][x + 2]);
        int rY = getred(color[y][x + 1]) - getred(color[y + 2][x + 1]);
        int gY = getgreen(color[y][x + 1]) - getgreen(color[y + 2][x + 1]);
        int bY = getblue(color[y][x + 1]) - getblue(color[y + 2][x + 1]);
        int x2 = rX * rX + gX * gX + bX * bX;
        int y2 = rY * rY + gY * gY + bY * bY;
        return (Math.sqrt(x2 + y2));
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
            if (seam[i] >= height() || seam[i] < 0) throw new
                    IllegalArgumentException("Not a Seam");
        }
        if (seam[seam.length - 1] >= height() || seam[seam.length - 1] < 0)
            throw new IllegalArgumentException("Not a Seam");

        // Set Color
        int[][] color = new int[height()][width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                color[row][col] = picture.getRGB(col, row);
            }
        }

        // Building the new picture H
        picture = new Picture(width(), height() - 1);
        for (int col = 0; col < width(); col++) {
            int newrow = 0;
            for (int row = 0; row < height(); row++) {
                if (row != seam[col]) {
                    picture.setRGB(col, newrow, color[row][col]);
                    newrow++;
                }
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
            if (seam[i] >= width() || seam[i] < 0) throw new
                    IllegalArgumentException("Not a Seam");
        }
        if (seam[seam.length - 1] >= width() || seam[seam.length - 1] < 0)
            throw new IllegalArgumentException("Not a Seam");

        // Set Color
        int[][] color = new int[height()][width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                color[row][col] = picture.getRGB(col, row);
            }
        }

        // Building the new picture
        picture = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            int newcol = 0;
            for (int col = 0; col < width(); col++) {
                if (col == seam[row]) {
                    picture.setRGB(col, row, color[row][newcol]);
                    newcol++;
                }
            }
        }
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture testp = new Picture(2, 2);
        testp.set(0, 0, new Color(1, 2, 3));
        testp.set(0, 1, new Color(2, 3, 4));
        testp.set(1, 0, new Color(3, 4, 5));
        testp.set(1, 1, new Color(4, 5, 6));
        SeamCarver1 test = new SeamCarver1(testp);
        StdOut.println(test.energy(1, 0)); // 0
        StdOut.println(test.width()); // 2
        StdOut.println(test.height()); // 2
        int[] seamh = new int[2];
        int[] seamv = new int[1];
        seamv[0] = 1;
        test.removeHorizontalSeam(seamh);
        test.removeVerticalSeam(seamv);
        Picture testp2 = test.picture();
        StdOut.println(testp2.get(0, 0));
    }
}
