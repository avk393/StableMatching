/**
 * Created by tkalbar on 3/2/19.
 */

import java.util.Arrays;

public class TestCase {
    int[][] image;
    int intensityWeight;
    int prunedWeight;

    public TestCase(int[][] image, int intensity, int pruned){
        this.image = image;
        this.intensityWeight = intensity;
        this.prunedWeight = pruned;
    }

    public int[][] getImage() {
        return image;
    }

    public void setImage(int[][] image) {
        this.image = image;
    }

    public int getIntensityWeight() {
        return intensityWeight;
    }

    public void setIntensityWeight(int intensity) {
        this.intensityWeight = intensity;
    }

    public int getPrunedWeight() {
        return prunedWeight;
    }

    public void setPrunedWeight(int pruned) {
        this.prunedWeight = pruned;
    }

    private String imageToStringByRow() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<image.length; ++i) {
            sb.append("    ");
            sb.append(Arrays.toString(image[i]));
            sb.append('\n');
        }
        return sb.toString();
    }

    public void check(int intensityResult, int prunedResult){
        boolean intensityFail = false;
        boolean prunedFail = false;
        
        System.out.println("Testing input image below: \n\n" + imageToStringByRow());

        if (!(this.intensityWeight == intensityResult)) {
            intensityFail = true;
            String msg = "Intensity graph test failed.\n";
            String expected = "   Expected: " + this.intensityWeight + "\n";
            String actual = "   Actual: " + intensityResult;
            System.out.println(msg + expected + actual);
        } else {
            System.out.println("Intensity graph test passed.");
        }

        if (!(this.prunedWeight == prunedResult)) {
            prunedFail = true;
            String msg = "Pruned graph test failed.\n";
            String expected = "   Expected: " + this.prunedWeight + "\n";
            String actual = "   Actual: " + prunedResult;
            System.out.println(msg + expected + actual);
        } else {
            System.out.println("Pruned graph test passed.");
        }

        System.out.println();
    }
}
