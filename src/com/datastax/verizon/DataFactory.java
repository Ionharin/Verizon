package com.datastax.verizon;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.github.javafaker.Faker;

public final class DataFactory {
	private final Faker fakedata;
	private Random rand;
	private Date startDate;
	private Date endDate;
	
	public DataFactory(Calendar day){
		fakedata = new Faker();
		rand = new Random();
		
		startDate = day.getTime();
		endDate = new Date();
	}
	
	public VehicleHistory newVehicleHistory(){
		VehicleHistory vh = new VehicleHistory();
		vh.setFleet_id(fakedata.bothify("###"));
		vh.setVin(fakedata.bothify("############"));
		vh.setIdle_time(randomValue());
		vh.updateTime(randomDate());
		return vh;
	}
	
	
	public Date randomDate(){
		return fakedata.date().between(startDate, endDate);
	}
	
	public double randomValue(){
		return rand.nextDouble() * 12;
	}

}
