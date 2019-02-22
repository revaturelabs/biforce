package com.revature.util;

import java.io.Serializable;

/**
 * Simple bean to store some information about the cutoff percentage
 * @author Mason Wegert
 */

public class OptimalPoint implements Serializable{

	private static final long serialVersionUID = -5559918855573669288L;
	
	private double optimalPercent;
	private long optimalAccurateCount;
	private long totalCount;
	
	/**
	 * Constructor
	 * @param optimalPercent
	 * @param optimalAccurateCount
	 * @param totalCount
	 */
	public OptimalPoint(double optimalPercent, long optimalAccurateCount, long totalCount) {
		super();
		this.optimalPercent = optimalPercent;
		this.optimalAccurateCount = optimalAccurateCount;
		this.totalCount = totalCount;
	}
	
	/**
	 * Getter method for optimalPercent.
	 * @return
	 */
	public double getOptimalPercent() {
		return optimalPercent;
	}
	/**
	 * Setter method for optimalPercent.
	 * @param optimalPercent
	 */
	public void setOptimalPercent(double optimalPercent) {
		this.optimalPercent = optimalPercent;
	}
	/**
	 * Getter method for optimalAccurateCount.
	 * @return
	 */
	public long getOptimalAccurateCount() {
		return optimalAccurateCount;
	}
	/**
	 * Setter method for optimalAccurateCount.
	 * @param optimalAccurateCount
	 */
	public void setOptimalAccurateCount(long optimalAccurateCount) {
		this.optimalAccurateCount = optimalAccurateCount;
	}
	/**
	 * Getter method for totalCount.
	 * @return
	 */
	public long getTotalCount() {
		return totalCount;
	}
	/**
	 * Setter method for totalCount.
	 * @param totalCount
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (optimalAccurateCount ^ (optimalAccurateCount >>> 32));
		long temp;
		temp = Double.doubleToLongBits(optimalPercent);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (totalCount ^ (totalCount >>> 32));
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OptimalPoint other = (OptimalPoint) obj;
		if (optimalAccurateCount != other.optimalAccurateCount)
			return false;
		if (Double.doubleToLongBits(optimalPercent) != Double.doubleToLongBits(other.optimalPercent))
			return false;
		if (totalCount != other.totalCount)
			return false;
		return true;
	}
	
}
