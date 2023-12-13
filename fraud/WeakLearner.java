import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {

    private Expert championWeakLeaner; // best weak learner trained
    private int k; // Max dimension number

    // This class keeps track of data points (point dimension k value,
    // point weight, point label) to not lose track when sorting
    private class SortingData implements Comparable<SortingData> {
        double kValue; // dimension value
        double weight; // weight of sample
        int label; // label of sample

        // To keep track of full data point
        public SortingData(
                double storeKValue, double storeWeight, int storeLabel) {
            kValue = storeKValue;
            weight = storeWeight;
            label = storeLabel;
        }

        // Compare method
        public int compareTo(SortingData other) {
            return Double.compare(kValue, other.kValue);
        }
    }

    // Expert to use for predictions
    private class Expert {
        int dp; // Dimension predictor
        double vp; // Value predictor
        int sp; // Sign predictor
        double weight; // Sum of all Experts correct predict

        // Class for weak learners
        public Expert(int dimension, double value, int sign) {
            if (sign != 1 && sign != 0)
                throw new IllegalArgumentException("Sign must be 1 or 0");
            dp = dimension;
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
    public WeakLearner(double[][] input, double[] weights, int[] labels) {
        // Corner Cases
        if (input == null || weights == null || labels == null)
            throw new IllegalArgumentException("Null Argument");
        int n = input.length;

        for (int i = 0; i < n; i++) {
            if (input[i] == null)
                throw new IllegalArgumentException("Null Argument");
        }

        k = input[0].length;

        if ((n != weights.length) || (n != labels.length)) {
            throw new IllegalArgumentException(
                    "Arguments have incompatible dimensions");
        }

        for (int i = 0; i < n; i++) {
            if ((weights[i] < 0) || (labels[i] != 0 && labels[i] != 1))
                throw new IllegalArgumentException(
                        "Weights must be positive and labels must be 1 or 0");
        }
        // K arrays of length n
        SortingData[][] sortingDataArray = new SortingData[k][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                sortingDataArray[j][i] = new SortingData(
                        input[i][j], weights[i], labels[i]);
            }

        }

        Expert[] theNExperts0 = new Expert[n];
        Expert[] theNExperts1 = new Expert[n];
        for (int i = 0; i < k; i++) {
            SortingData[] kColumn = sortingDataArray[i];
            Merge.sort(kColumn);


            // Train the sp = 0, bottom up order--------------------------------
            // establish the total weight of the lowest weak learner
            theNExperts0[0] = new Expert(i, kColumn[0].kValue, 0);
            for (int j = 0; j < n; j++) {
                theNExperts0[0].addCorrectPrediction(
                        kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);
            }

            // move from lowest kValue to highiest, adjusting the higher
            // weak learner (in dp vlaue) as needed
            double lastKValue = kColumn[0].kValue;
            int numberOfLastSameKValue = 0;
            for (int j = 1; j < n; j++) {
                // if the kValue is unchanged, last weak learner
                // weight change is not complete
                if (lastKValue == kColumn[j].kValue) {

                    numberOfLastSameKValue++;

                    theNExperts0[j - numberOfLastSameKValue].assessNewPrediction(
                            kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);

                    double newWeight = theNExperts0[j - numberOfLastSameKValue].weight;

                    for (int v = 1; v < numberOfLastSameKValue; v++) {
                        theNExperts0[j - v].changeWeight(newWeight);
                    }
                    theNExperts0[j] = new Expert(i, kColumn[j].kValue, 0);
                    theNExperts0[j].changeWeight(theNExperts0[j - 1].weight);
                }
                else {
                    numberOfLastSameKValue = 0;
                    theNExperts0[j] = new Expert(i, kColumn[j].kValue, 0);
                    theNExperts0[j].changeWeight(theNExperts0[j - 1].weight);
                    theNExperts0[j].assessNewPrediction(
                            kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);
                }
                lastKValue = kColumn[j].kValue;

            }
            // Train the sp = 1, bottom up order--------------------------------
            // establish the total weight of the highest weak learner
            theNExperts1[0] = new Expert(i, kColumn[0].kValue, 1);

            for (int j = 0; j < n; j++) {
                theNExperts1[0].addCorrectPrediction(
                        kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);
            }
            int numberOfLastSameKValue1 = 0;
            double lastKValue1 = kColumn[0].kValue;
            // move from highest kValue to lowest, adjusting the lower
            // weak learner (in dp value) as needed

            for (int j = 1; j < n; j++) {

                if (lastKValue1 == kColumn[j].kValue) {
                    numberOfLastSameKValue1++;

                    theNExperts1[j - numberOfLastSameKValue1].assessNewPrediction(
                            kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);

                    double newWeight = theNExperts1[
                            j - numberOfLastSameKValue1].weight;

                    for (int v = 1; v < numberOfLastSameKValue1; v++) {
                        theNExperts1[j - v].changeWeight(newWeight);
                    }
                    theNExperts1[j] = new Expert(i, kColumn[j].kValue, 1);
                    theNExperts1[j].changeWeight(theNExperts1[j - 1].weight);
                }
                else {
                    numberOfLastSameKValue1 = 0;
                    theNExperts1[j] = new Expert(i, kColumn[j].kValue, 1);
                    theNExperts1[j].changeWeight(theNExperts1[j - 1].weight);
                    theNExperts1[j].assessNewPrediction(
                            kColumn[j].kValue, kColumn[j].weight, kColumn[j].label);
                }
                lastKValue1 = kColumn[j].kValue;
            }
        }
        championWeakLeaner = new Expert(-1, 0, 0);
        for (int i = 0; i < n; i++) {
            if (championWeakLeaner.weight < theNExperts0[i].weight)
                championWeakLeaner = theNExperts0[i];
            if (championWeakLeaner.weight < theNExperts1[i].weight)
                championWeakLeaner = theNExperts1[i];
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(double[] sample) {
        if (sample == null)
            throw new IllegalArgumentException("Null Argument");
        if (sample.length != k)
            throw new IllegalArgumentException("Incorrect sample length");

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
        // In testData = new In("princeton_training.txt");
        // int n = testData.readInt();
        // int k = testData.readInt();
        // int locationsAmount = 2;
        //
        // double[][] input = new double[n][k];
        // double[] weight = new double[n];
        // int[] correctAnswer = new int[n];
        //
        // double dummy;
        // for (int i = 0; i < k; i++) {
        //     for (int j = 0; j < locationsAmount; j++)
        //         dummy = testData.readDouble();
        // }
        //
        // for (int i = 0; i < n; i++) {
        //     correctAnswer[i] = testData.readInt();
        // }
        //
        // for (int i = 0; i < n; i++) {
        //     for (int j = 0; j < k; j++) {
        //         input[i][j] = testData.readDouble();
        //     }
        //     weight[i] = 1;
        // }
        In testData = new In("testConstructor.txt");
        int n = testData.readInt();
        int k = testData.readInt();

        double[][] input = new double[n][k];
        double[] weight = new double[n];
        int[] correctAnswer = new int[n];

        for (int i = 0; i < n; i++) {
            correctAnswer[i] = testData.readInt();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                input[i][j] = testData.readDouble();
            }
        }
        for (int i = 0; i < n; i++) {
            weight[i] = testData.readDouble();
        }

        StdOut.println("Point 4: (" + input[4][0] + ", " + input[4][1] + ")");
        StdOut.println("Point 4 weight: (" + weight[4] + ")");
        StdOut.println("Point 4 Correct Prediction: (" + correctAnswer[4] + ")");

        WeakLearner testWeakLearner = new WeakLearner(input, weight, correctAnswer);
        StdOut.println("Dimension Predictor: " + testWeakLearner.dimensionPredictor());
        StdOut.println("Value Predictor: " + testWeakLearner.valuePredictor());
        StdOut.println("Sign Predictor: " + testWeakLearner.signPredictor());

        double[] sample = new double[2];
        sample[0] = 4;
        sample[1] = 5;
        StdOut.println("Sample 1 prediction: " + testWeakLearner.predict(sample));

        double[] sample1 = new double[2];
        sample1[0] = 10;
        sample1[1] = 2;
        StdOut.println("Sample 2 prediction: " + testWeakLearner.predict(sample1));

    }
}

