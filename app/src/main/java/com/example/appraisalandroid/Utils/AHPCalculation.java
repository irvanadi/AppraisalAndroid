package com.example.appraisalandroid.Utils;

import com.example.appraisalandroid.Models.Topic;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import java.util.ArrayList;

public class AHPCalculation {

    /**
     * Random Consistency Index
     */
    protected static double RI[] = {0.0, 0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59};

    /**
     * The matrix
     */
    protected Array2DRowRealMatrix mtx;

    /**
     * The Array of Total Variabel
     */
    protected double totalMatrix[];

    /**
     * The Array of pVektor
     */
    protected double pVektor[];

    /**
     * The Array of Bobot (Weight)
     */
    protected double bobot[];

    /**
     * The Array of Eigen Value
     */
    protected double pointEigenValue[];

    /**
     * The matrix normalisasi
     */
    protected Array2DRowRealMatrix normalisasi;

    /**
     * Contains
     */
    protected double pairwiseComparisonArray[];

    /**
     * Number of alternatives
     */
    protected int nrAlternatives;

    /**
     * The resulting weights/priorities
     */
    protected double weights[];

    /**
     * Corresponds to the weights
     */
    protected String labels[] = null;

    /**
     *
     */
    protected EigenDecomposition evd;

    /**
     * Convenience array, i.e. comparisonIndices[length=NumberOfPairwiseComparisons][2]
     * Contains minimum number of comparisons.
     */
    protected int[][] comparisonIndices;

    /**
     * Index of the greatest Eigenvalue/ -vector
     */
    protected int evIdx = 0; // index of actual eigenvalue/-vector

    /**
     * Index of the CI
     */
    protected double cIndex;

    /**
     * Index of the CR
     */
    protected double cRatio;

    /**
     * Index of the Total Eigen Value
     */
    protected double totalEV;

    protected double geoMean;

    public void Calculate(ArrayList topics){
        this.nrAlternatives = topics.size();
        mtx = new Array2DRowRealMatrix(nrAlternatives, nrAlternatives);
        normalisasi = new Array2DRowRealMatrix(nrAlternatives, nrAlternatives);
        totalMatrix = new double[nrAlternatives];
        weights = new double[nrAlternatives];
        pVektor = new double[nrAlternatives];
        bobot = new double[nrAlternatives];
        pointEigenValue = new double[nrAlternatives];

        pairwiseComparisonArray = new double[getNrOfPairwiseComparisons()];
        comparisonIndices = new int[getNrOfPairwiseComparisons()][];
        for (int i = 0; i < getNrOfPairwiseComparisons(); i++) {
            comparisonIndices[i] = new int[2];
        }

        // only need diagonal 1, but set everything to 1.0
        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = 0; col < nrAlternatives; col++) {
                mtx.setEntry(row, col, 1.0);
            }
        }
        int comp = getNrOfPairwiseComparisons();
        double compArray[] = getPairwiseComparisonArray();
    }

    public void setPairwiseComparisonArray(double topicValue[]){
        int i = 0;
        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = row + 1; col < nrAlternatives; col++) {
                // System.out.println(row + "/" + col + "=" + a[i]);
                mtx.setEntry(row, col, topicValue[i]);
                mtx.setEntry(col, row, 1.0 / mtx.getEntry(row, col));
                comparisonIndices[i][0] = row;
                comparisonIndices[i][1] = col;
                i++;
            }
        }

        for (int row = 0; row < nrAlternatives; row++){
            for (int col = 0; col < nrAlternatives; col++) {
                totalMatrix[row] += mtx.getEntry(col,row);
            }
        }

        normalisasi = mtx;

        for (int row = 0; row < nrAlternatives; row++){
            for (int col = 0; col < nrAlternatives; col++) {
                normalisasi.setEntry(col,row, mtx.getEntry(col,row) / totalMatrix[row]);
                System.out.println(normalisasi.getEntry(col,row));
            }
        }

        for (int col = 0; col < nrAlternatives; col++){
            for (int row = 0; row < nrAlternatives; row++) {
                pVektor[col] += normalisasi.getEntry(col,row);
            }
        }

        for (int k = 0; k < nrAlternatives; k++){
            bobot[k] = pVektor[k] / nrAlternatives;
            pointEigenValue[k] = bobot[k] * totalMatrix[k];
            totalEV += pointEigenValue[k];
        }

        cIndex = (totalEV - nrAlternatives) / (nrAlternatives - 1);
        cRatio = cIndex / RI[nrAlternatives -1];
    }

    public int getNrOfPairwiseComparisons() {
        return ((nrAlternatives - 1) * nrAlternatives) / 2;
    }

    public double[] getPairwiseComparisonArray() {
        return pairwiseComparisonArray;
    }
}
