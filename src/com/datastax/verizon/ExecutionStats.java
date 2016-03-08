package com.datastax.verizon;

import java.util.concurrent.atomic.AtomicLong;

public class ExecutionStats {
	private AtomicLong operationCount;
	private AtomicLong totalDuration;
	
	private String statsName;
	
	public ExecutionStats(String line){
		statsName = line;
		operationCount = new AtomicLong();
		totalDuration = new AtomicLong();
	}
	
	public void updateStats(long durationMillis){
		operationCount.incrementAndGet();
		totalDuration.addAndGet(durationMillis);
	}
	
	public void print(){
		System.out.printf("%30s  %30d  %30d  %30.2f %n", statsName, operationCount.get(), totalDuration.get(), this.getAvgDuration());
	}
	
	public String getName(){
		return statsName;
	}
	
	public double getAvgDuration(){
		return totalDuration.doubleValue()/operationCount.doubleValue();
	}
	
	public long getOperationCount() {
		return operationCount.get();
	}
	public void setOperationCount(long operationCount) {
		this.operationCount.set(operationCount);
	}

	public long getTotalDuration() {
		return totalDuration.get();
	}
	public void setTotalDuration(long totalDuration) {
		this.totalDuration.set(totalDuration);
	}
	
}
