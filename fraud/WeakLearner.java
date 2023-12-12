import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {

    private Expert championWeakLeaner; // best weak learner trained

    // This class keeps track of data points (point dimension k value,
    // point weight, point label) to not lose track when sorting
    private class sortingData implements Comparable<sortingData> {
        Double kValue;
        double weight;
        int label;

        public sortingData(
                double storeKValue, double storeWeight, int storeLabel) {
            kValue = storeKValue;
            weight = storeWeight;
            label = storeLabel;
        }

        public int compareTo(sortingData other) {
            return kValue.compareTo(other.kValue);
        }
    }

    // Expert to use for predictions
    private class Expert {
        int dp; // Dimension predictor
        double vp; // Value predictor
        int sp; // Sign predictor
        double weight; // Sum of all Experts correct predict

        public Expert(int dimension, double value, int sign) {
            if (sign != 1 && sign != 0)
                throw new IllegalArgumentException("Sign must be 1 or 0");
            vp = value;
            sp = sign;
            weight = 0.0;
        }

        // Add correctly labeled point's weight to total weight but don't
        // subtract incorrect labeled weights (only use for first weak learner
        public void addCorrectPrediction(
                double kTest, double testWeight, int testLabel) {
            int prediction = predict(kTest);
            if (prediction == testLabel)
                changeWeight(weight + testWeight);

        }

        // Alter established weight to new prediction
        public void assessNewPrediction(
                double kTest, double testWeight, int testLabel) {
            int prediction = predict(kTest);
            if (prediction != testLabel)
                changeWeight(weight - testWeight);
            else
                changeWeight(weight + testWeight);
        }

        // Given a test kValue, return a prediction
        public int predict(double kValue) {
            if (sp == 0) {
                if (kValue <= vp)
                    return 0;
                else
                    return 1;
            }
            else {
                if (kValue <= vp)
                    return 1;
                else
                    return 0;

            }
        }

        // Change weight of expert
        public void changeWeight(double newWeight) {
            weight = newWeight;
        }
    }

    // train the weak learner
    // TODO: The iterations to higher k value is not properly adressing cases
    //  in which there are 2 or more locations on the same k value
    public WeakLearner(double[][] input, double[] weights, int[] labels) {
        int n = input.length;
        int k = input[0].length;
        // K arrays of length n
        sortingData[][] sortingDataArray = new sortingData[k][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                sortingDataArray[j][i] = new sortingData(
                        input[i][j], weights[i], labels[i]);
            }
        }

        for (int i = 0; i < k; i++) {
            StdOut.println("I Value: " + i);
            sortingData[] kColumn = sortingDataArray[i];
            Merge.sort(kColumn); // nlogn work

            // Train the sp = 0, bottum up order--------------------------------
            // establish the total weight of the lowest weak learner
            Expert[] theNExperts = new Expert[n];
            theNExperts[0] = new Expert(i, kColumn[0].kValue, 0);
            StdOut.println("First Learner kValue :" + kColumn[0].kValue);

            for (int j = 0; j < n; j++) {
                theNExperts[0].addCorrectPrediction(
                        kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);
            }
            StdOut.println("Total Weight: " + theNExperts[0].weight);
            championWeakLeaner = theNExperts[0];

            // move from lowest kValue to highiest, adjusting the higher
            // weak learner (in dp vlaue) as needed
            for (int j = 1; j < n; j++) {
                theNExperts[j] = new Expert(i, kColumn[j].kValue, 0);
                theNExperts[j].changeWeight(theNExperts[j - 1].weight);
                theNExperts[j].assessNewPrediction(
                        kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);
                StdOut.println("Next learner Weight: " + theNExperts[j].weight);
                if (championWeakLeaner.weight < theNExperts[j].weight)
                    championWeakLeaner = theNExperts[j];
            }

            // Train the sp = 1, top down order--------------------------------
            // establish the total weight of the highest weak learner
            Expert[] theNExperts1 = new Expert[n];
            int maxExpert = n - 1;
            theNExperts1[maxExpert] = new Expert(i, kColumn[maxExpert].kValue, 1);

            for (int j = maxExpert; j > -1; j--) {
                theNExperts1[maxExpert].addCorrectPrediction(
                        kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);
            }
            if (championWeakLeaner.weight < theNExperts1[maxExpert].weight)
                championWeakLeaner = theNExperts1[maxExpert];

            // move from highest kValue to lowest, adjusting the lower
            // weak learner (in dp value) as needed
            for (int j = n - 2; j > -1; j--) {
                theNExperts1[j] = new Expert(i, kColumn[j].kValue, 1);
                theNExperts1[j].changeWeight(theNExperts1[j + 1].weight);
                theNExperts1[j].assessNewPrediction(
                        kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);
                if (championWeakLeaner.weight < theNExperts1[j].weight)
                    championWeakLeaner = theNExperts1[j];
            }
        }

    }

    // return the prediction of the learner for a new sample
    public int predict(double[] sample) {
        return championWeakLeaner.predict(sample[championWeakLeaner.dp]);
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return championWeakLeaner.dp;
    }

    //
    // return the value the learner uses to separate the data
    public double valuePredictor() {
        return championWeakLeaner.vp;
    }

    //
    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return championWeakLeaner.sp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // Test Data Initialization
        In testData = new In("testConstructor.txt");
        int n = testData.readInt();
        int k = testData.readInt();

        double[][] input = new double[n][k];
        double[] weight = new double[n];
        int[] correctAnswer = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++)
                input[i][j] = testData.readDouble();
        }

        for (int i = 0; i < n; i++) {
            weight[i] = testData.readDouble();
        }
        for (int i = 0; i < n; i++) {
            correctAnswer[i] = testData.readInt();
        }

        StdOut.println("Point 4: (" + input[4][0] + ", " + input[4][1] + ")");
        StdOut.println("Point 4 weight: (" + weight[4] + ")");
        StdOut.println("Point 4 Correct Prediction: (" + correctAnswer[4] + ")");

        WeakLearner testWeakLearner = new WeakLearner(input, weight, correctAnswer);
        StdOut.println("Dimension Predictor: " + testWeakLearner.dimensionPredictor());
        StdOut.println("Value Predictor: " + testWeakLearner.valuePredictor());
        StdOut.println("Sign Predictor: " + testWeakLearner.signPredictor());


    }
}

