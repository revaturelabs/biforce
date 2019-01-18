package com.revature.spark;

/**
 * This is a wrapper class for the 3 values returned by an indicator.
 * Percentage chance to fail, the sample size, and an explanation string.
 */

public class AnalyticResult {
	private double percentage;
	private int sampleSize;
	private String explanation;
	
	public AnalyticResult(double percentage, int sampleSize, String explanation) {
		super();
		this.percentage = percentage;
		this.sampleSize = sampleSize;
		this.explanation = explanation;
	}
	
	/*
	 * Boiler plate
	 */

	public double getPercentage() {
		return percentage;
	}

	public int getSampleSize() {
		return sampleSize;
	}

	public String getExplanation() {
		return explanation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((explanation == null) ? 0 : explanation.hashCode());
		long temp;
		temp = Double.doubleToLongBits(percentage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + sampleSize;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnalyticResult other = (AnalyticResult) obj;
		if (explanation == null) {
			if (other.explanation != null)
				return false;
		} else if (!explanation.equals(other.explanation))
			return false;
		if (Double.doubleToLongBits(percentage) != Double.doubleToLongBits(other.percentage))
			return false;
		if (sampleSize != other.sampleSize)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AnalyticResult [percentage=" + percentage + ", sampleSize=" + sampleSize + ", explanation="
				+ explanation + "]";
	}
}
