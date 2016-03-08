package com.datastax.verizon;

public class LoadAccounts {

	public static void main(String[] args) throws InterruptedException {
		//Arguments - number of records to load. Defaults to 1m accounts;
		int RECORDS = 1000000;		
		
		DataStaxCluster dse = new DataStaxCluster(new String[]{"node0", "node1", "node2"}, "verizon");
		DataFactory factory = new DataFactory();

		if (args.length > 0){
			RECORDS = Integer.parseInt(args[0]);
		}
		
		for (int i=0; i<RECORDS; i++){
			Account a = factory.newAccount();
			dse.writeAccount(a);
			
			for (String subscriber_id: a.getSubscribers()){
				Subscriber s = factory.newSubscriber(a.getAccount_id(), subscriber_id);
				dse.writeSubscriber(s,a);
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
