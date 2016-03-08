package com.datastax.verizon;

import java.util.ArrayList;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;

public class DataStaxCluster {
	private String[] nodes;
	private String keyspace;
	private Cluster cluster;
	private Session session;
	
	private StatsFutureSet futures;


	//Prepared Statements
	private PreparedStatement psWriteAccount;
	private PreparedStatement psWriteSubscriber;
	private PreparedStatement psWriteAccountByEmail;
	private PreparedStatement psWriteAccountByPhone;
	private PreparedStatement psWriteUsage;
	
	
	public DataStaxCluster(String[] nodes, String keyspace){
		this.setNodes(nodes);
		this.setKeyspace(keyspace);
		
		futures = new StatsFutureSet(20, 100000, 100000);
		
		connect();
		prepare();
	}
	
	public void printStats(){
		futures.printStats();
	}
	
	private void connect(){
		Builder builder = Cluster.builder();
		builder.addContactPoints(nodes);
		cluster = builder.build();
		cluster.getConfiguration().getSocketOptions().setReadTimeoutMillis(100000);
		//cluster.getConfiguration().getPoolingOptions().s
		cluster.getConfiguration().getQueryOptions().setConsistencyLevel(ConsistencyLevel.ONE);
		session = cluster.connect(keyspace);		
	}
	
	private void prepare(){
		psWriteAccount = session.prepare("insert into account(account_id, first_name, last_name, name_prefix, name_suffix, street_address, city, state, zip, email, subscribers) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		psWriteSubscriber = session.prepare("insert into subscriber(account_id, subscriber_id, phone_number) values(?, ?, ?)");
		psWriteAccountByEmail = session.prepare("insert into account_by_email(email, account_id, first_name, last_name) values(?, ?, ?, ?)");
		psWriteAccountByPhone = session.prepare("insert into account_by_phone(phone_number, account_id, first_name, last_name) values(?, ?, ?, ?)");
		psWriteUsage = session.prepare("insert into data_usage(subscriber_id, date, url, year, month, day, hour, bytes_up, bytes_dn, bytes_total) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
	}
	
	public void writeAccount(Account a){
		futures.add(session.executeAsync(psWriteAccount.bind(a.getAccount_id(), a.getFirst_name(), a.getLast_name(), a.getName_prefix(), a.getName_suffix(), a.getStreet_address(), a.getCity(), a.getState(), a.getZip(), a.getEmail_address(), a.getSubscribers())), "write_account");
		futures.add(session.executeAsync(psWriteAccountByEmail.bind(a.getEmail_address(), a.getAccount_id(), a.getFirst_name(), a.getLast_name())), "write_account_by_email");
	}
	
	public void writeSubscriber(Subscriber s, Account a){
		futures.add(session.executeAsync(psWriteSubscriber.bind(s.getAccount_id(), s.getSubscriber_id(), s.getPhone_number())), "write_subscriber");
		futures.add(session.executeAsync(psWriteAccountByPhone.bind(s.getPhone_number(), s.getAccount_id(), a.getFirst_name(), a.getLast_name())), "write_account_by_phone");
	}
	
	public void writeUsage(Usage u){
		futures.add(session.executeAsync(psWriteUsage.bind(u.getSubscriber_id(), u.getDate(), u.getUrl(), u.getYear(), u.getMonth(), u.getDay(), u.getHour(), u.getBytes_up(), u.getBytes_dn(), u.getBytes_total())), "write_data_usage");
	}
	
	public ArrayList<String> getSubscribers(){
		ArrayList<String> subscribers = new ArrayList<String>();
		ResultSet results = session.execute("select subscriber_id from subscriber");
		
		for (Row row : results){
			subscribers.add(row.getString("subscriber_id"));
		}
		
		return subscribers;
	}
	

	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}	
}
