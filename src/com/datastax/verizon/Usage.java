package com.datastax.verizon;

import java.util.Date;

public class Usage {
	private String subscriber_id;
	private Date date;
	private String url;
	private int year;
	private int month;
	private int day;
	private int hour;
	private double bytes_up;
	private double bytes_dn;
	
	
	public String getSubscriber_id() {
		return subscriber_id;
	}
	public void setSubscriber_id(String subscriber_id) {
		this.subscriber_id = subscriber_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public double getBytes_up() {
		return bytes_up;
	}
	public void setBytes_up(double bytes_up) {
		this.bytes_up = bytes_up;
	}
	public double getBytes_dn() {
		return bytes_dn;
	}
	public void setBytes_dn(double bytes_dn) {
		this.bytes_dn = bytes_dn;
	}
	public double getBytes_total() {
		return bytes_up + bytes_dn;
	}


}
