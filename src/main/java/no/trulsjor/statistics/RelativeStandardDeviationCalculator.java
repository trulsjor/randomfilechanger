package no.trulsjor.statistics;

import java.text.MessageFormat;

/**
 * Small util for calculating relative standard deviation for a list of numbers
 * 
 * @author trulsjor@gmail.com
 * 
 */
public class RelativeStandardDeviationCalculator {

    public static String getAsPercentageText(int[] numbers) {
	return MessageFormat.format("{0,number,#.##%}", calculate(numbers));
    }

    public static double calculate(int[] numbers) {
	int sum = getSum(numbers);
	double average = sum / numbers.length;
	double diffsSquared = getDiffsSquared(numbers, average);
	double standarddeviation = Math.sqrt(diffsSquared / (numbers.length - 1));
	double relativeStandardDeviation = (standarddeviation / average);
	return relativeStandardDeviation;
    }

    private static double getDiffsSquared(int[] numbers, double average) {
	double pows = 0;
	for (int i = 0; i < numbers.length; i++) {
	    pows += Math.pow((numbers[i] - average), 2);
	}
	return pows;
    }

    private static int getSum(int[] numbers) {
	int sum = 0;
	for (int i = 0; i < numbers.length; i++) {
	    sum += numbers[i];
	}
	return sum;
    }

}
