package com.datastax.verizon;

import java.util.Calendar;
import java.util.Random;

import com.github.javafaker.Faker;

public final class DataFactory {
	private final Faker fakedata;
	private Random rand;
	
	public DataFactory(){
		fakedata = new Faker();
		rand = new Random();
	}
	
	public Account newAccount(){
		//Verizon Account Number 785828394-00001
		//"#########-0000#"
		Account a = new Account();
		a.setAccount_id(fakedata.numerify("#########-0000#"));
		a.setCity(fakedata.address().city());
		a.setEmail_address(fakedata.internet().emailAddress());
		a.setFirst_name(fakedata.name().firstName());
		a.setLast_name(fakedata.name().lastName());
		a.setName_prefix(fakedata.name().prefix());
		a.setName_suffix(fakedata.name().suffix());
		a.setState(fakedata.address().state());
		a.setStreet_address(fakedata.address().streetAddress(false));
		a.setZip(fakedata.address().zipCode());
		
		//Generate subscribers
		//How many? (Not too many)
		int numsubs = (int)Math.floor(Math.log(rand.nextDouble()+.000000001)/-1);
		//At least 1 sub per account
		if (0 == numsubs) numsubs = 1;
		
		for (int i=0; i<numsubs; i++){
			//IMEI format 35 780502 398494 2
			//"##-######-######-#"	
			a.addSub(fakedata.numerify("##-######-######-#"));
		}
		return a;
	}
	
	private double getBytes(){
		return (Math.log(1-rand.nextDouble())/-1)*1000;
	}
	
	public Subscriber newSubscriber(String account_id, String subscriber_id){
		Subscriber s = new Subscriber();
		s.setAccount_id(account_id);
		s.setSubscriber_id(subscriber_id);
		s.setPhone_number(fakedata.phoneNumber().phoneNumber());
		return s;
	}
	
	public Usage newUsage(String subscriber_id, Calendar day){
		Usage u = new Usage();
		u.setSubscriber_id(subscriber_id);
		u.setBytes_dn(getBytes());
		u.setBytes_up(getBytes());
		u.setUrl(fakedata.internet().url());
		
		//randomize the timestamp
		day.set(Calendar.HOUR_OF_DAY, rand.nextInt(24));
		day.set(Calendar.MINUTE, rand.nextInt(60));
		day.set(Calendar.SECOND, rand.nextInt(60));
		day.set(Calendar.MILLISECOND, rand.nextInt(1000));
		
		int year = day.get(Calendar.YEAR);
		int month = year * 100 + day.get(Calendar.MONTH)+1;
		int date = month * 100 + day.get(Calendar.DATE);
		int hour = date * 100 + day.get(Calendar.HOUR_OF_DAY);
		
		u.setDate(day.getTime());
		u.setYear(year);
		u.setMonth(month);
		u.setDay(date);
		u.setHour(hour);

		return u;
	}
}
