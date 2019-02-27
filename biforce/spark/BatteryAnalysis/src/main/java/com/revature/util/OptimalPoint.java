package com.revature.util;

import java.io.Serializable;

/**
 * Simple bean to store some information about the cutoff percentage.
 * 
 * @See ModelApplier
 * @author Mason Wegert
 * @author Pil Ju Chun
 */

public class OptimalPoint implements Serializable {

	private static final long serialVersionUID = -5559918855573669288L;

	/**
	 * Enum to describe how optimalPercent was calculated.
	 * ACCURACY = (TP + TN) / Total population
	 * RECALL = TP / (TP + FN)
	 * PRECISION = TP / (TP + FP)
	 * F1_SCORE = 1 / ((RECALL^-1 + PRECISION^-1) / 2)
	 */
	public enum OptimizeType {
		ACCURACY, RECALL, PRECISION, F1_SCORE;
	}
	
	/**
	 * Represents the optimized percent cutoff.
	 */
	private double optimalPercent;

	/**
	 * Represents how the percent cutoff was optimized.
	 */
	private OptimizeType optimalPercentMethod;

	/**
	 * Total number of true positives if optimalPercent is used to split.
	 */
	private long optimalTPCount;
	/**
	 * Total number of false positives if optimalPercent is used to split.
	 */
	private long optimalFPCount;
	/**
	 * Total number of false negatives if optimalPercent is used to split.
	 */
	private long optimalFNCount;
	/**
	 * Total number of true negatives if optimalPercent is used to split.
	 */
	private long optimalTNCount;

	/**
	 * @param optimalPercent
	 * @param optimalPercentMethod
	 * @param optimalTPCount
	 * @param optimalFPCount
	 * @param optimalFNCount
	 * @param optimalTNCount
	 */
	public OptimalPoint(double optimalPercent, OptimizeType optimalPercentMethod, long optimalTPCount,
			long optimalFPCount, long optimalFNCount, long optimalTNCount) {
		super();
		this.optimalPercent = optimalPercent;
		this.optimalPercentMethod = optimalPercentMethod;
		this.optimalTPCount = optimalTPCount;
		this.optimalFPCount = optimalFPCount;
		this.optimalFNCount = optimalFNCount;
		this.optimalTNCount = optimalTNCount;
	}

	/**
	 * @return
	 */
	public double getOptimalPercent() {
		return optimalPercent;
	}

	/**
	 * @param optimalPercent
	 */
	public void setOptimalPercent(double optimalPercent) {
		this.optimalPercent = optimalPercent;
	}

	/**
	 * @return
	 */
	public OptimizeType getOptimalPercentMethod() {
		return optimalPercentMethod;
	}

	/**
	 * @param optimalPercentMethod
	 */
	public void setOptimalPercentMethod(OptimizeType optimalPercentMethod) {
		this.optimalPercentMethod = optimalPercentMethod;
	}

	/**
	 * @return
	 */
	public long getOptimalTPCount() {
		return optimalTPCount;
	}

	/**
	 * @param optimalTPCount
	 */
	public void setOptimalTPCount(long optimalTPCount) {
		this.optimalTPCount = optimalTPCount;
	}

	/**
	 * @return
	 */
	public long getOptimalFPCount() {
		return optimalFPCount;
	}

	/**
	 * @param optimalFPCount
	 */
	public void setOptimalFPCount(long optimalFPCount) {
		this.optimalFPCount = optimalFPCount;
	}

	/**
	 * @return
	 */
	public long getOptimalFNCount() {
		return optimalFNCount;
	}

	/**
	 * @param optimalFNCount
	 */
	public void setOptimalFNCount(long optimalFNCount) {
		this.optimalFNCount = optimalFNCount;
	}

	/**
	 * @return
	 */
	public long getOptimalTNCount() {
		return optimalTNCount;
	}

	/**
	 * @param optimalTNCount
	 */
	public void setOptimalTNCount(long optimalTNCount) {
		this.optimalTNCount = optimalTNCount;
	}
	
	/**
	 * @return
	 */
	public long getCorrectCount() {
		return optimalTPCount + optimalTNCount;
	}
	
	/**
	 * @return total population
	 */
	public long getTotalCount() {
		return optimalTPCount + optimalFPCount + optimalFNCount + optimalTNCount;
	}
	
	/**
	 * @return percent accuracy in decimals
	 */
	public double getAccuracy() {
		return ((double)optimalTPCount + (double)optimalTNCount) / (double)getTotalCount();
	}
	
	/**
	 * @return percent precision in decimals
	 */
	public double getPrecision() {
		return (double)optimalTPCount / ((double)optimalTPCount + (double)optimalFPCount);
	}
	
	/**
	 * @return percent recall in decimals
	 */
	public double getRecall() {
		return (double)optimalTPCount / ((double)optimalTPCount + (double)optimalFNCount);		
	}
	
	/**
	 * @return F1 score
	 */
	public double getF1Score() {
		return 1.0d / ((1/getPrecision() + 1/getRecall()) / 2.0d);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (optimalFNCount ^ (optimalFNCount >>> 32));
		result = prime * result + (int) (optimalFPCount ^ (optimalFPCount >>> 32));
		long temp;
		temp = Double.doubleToLongBits(optimalPercent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((optimalPercentMethod == null) ? 0 : optimalPercentMethod.hashCode());
		result = prime * result + (int) (optimalTNCount ^ (optimalTNCount >>> 32));
		result = prime * result + (int) (optimalTPCount ^ (optimalTPCount >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		OptimalPoint other = (OptimalPoint) obj;
		if (optimalFNCount != other.optimalFNCount) {
			return false;
		}
		if (optimalFPCount != other.optimalFPCount) {
			return false;
		}
		if (Double.doubleToLongBits(optimalPercent) != Double.doubleToLongBits(other.optimalPercent)) {
			return false;
		}
		if (optimalPercentMethod != other.optimalPercentMethod) {
			return false;
		}
		if (optimalTNCount != other.optimalTNCount) {
			return false;
		}
		if (optimalTPCount != other.optimalTPCount) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OptimalPoint [optimalPercent=" + optimalPercent + ", optimalPercentMethod=" + optimalPercentMethod
				+ ", optimalTPCount=" + optimalTPCount + ", optimalFPCount=" + optimalFPCount + ", optimalFNCount="
				+ optimalFNCount + ", optimalTNCount=" + optimalTNCount + "]";
	}

}
