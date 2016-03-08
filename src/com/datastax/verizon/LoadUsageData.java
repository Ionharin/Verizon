package com.datastax.verizon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class LoadUsageData {

	public static void main(String[] args) throws InterruptedException {
		//Arguments - number of days to generate data. Defaults to 30 days
		int DAYS = 30;		
		
		DataStaxCluster dse = new DataStaxCluster(new String[]{"node0", "node1", "node2"}, "verizon");
		DataFactory factory = new DataFactory();
		Random r = new Random();
		
		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE, -(DAYS+1));
		
		System.out.println("Getting subscriber data.");
		ArrayList<String> subscribers = dse.getSubscribers();

		System.out.println("Starting data gen.");
		for (int d=0; d<DAYS; d++){
			day.add(Calendar.DATE, 1);
			int records = r.nextInt(subscribers.size()/2);
			
			System.out.println("Generating " + records + " records on " + day.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US) + " " + day.get(Calendar.DATE));
			
			for (int i=0; i<records; i++){
				Usage u = factory.newUsage(subscribers.get(r.nextInt(subscribers.size())), day);
				dse.writeUsage(u);
			}
			
		}
		
		//finish
		System.out.println("Data load complete.");
		//Wait 10 seconds for pending callbacks to complete.
		Thread.sleep(10000);
		dse.printStats();
		System.exit(0);
	}

}
