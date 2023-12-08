import edu.princeton.cs.algs4.Picture;

public class test {
    public static void main(String[] args) {
        int width = 32000;
        int height = 2000;
        Picture pic = SCUtility.randomPicture(width, height);
        pic.save("Random_" + width + "_" + height + ".jpeg");

    }
}
