package com.datastax.verizon;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.Mapper.Option;
import com.datastax.driver.mapping.MappingManager;

public class DataStaxCluster {
	private String[] nodes;
	private String keyspace;
	private Cluster cluster;
	private Session session;
	
	private StatsFutureSet futures;
	
	//Mappers
	private Mapper<VehicleHistory> vehicleHistory;
	
	public DataStaxCluster(String[] nodes, String keyspace){
		this.setNodes(nodes);
		this.setKeyspace(keyspace);
		
		futures = new StatsFutureSet(10, 100000, 100000);
		
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
		//cluster.getConfiguration().getPoolingOptions();
		cluster.getConfiguration().getQueryOptions().setConsistencyLevel(ConsistencyLevel.ONE);
		session = cluster.connect(keyspace);		
	}
	
	private void prepare(){
		vehicleHistory = new MappingManager(session).mapper(VehicleHistory.class);
		vehicleHistory.setDefaultSaveOptions(Option.saveNullFields(false));
	}
	
	public void writeVehicleHistory(VehicleHistory vh){
		futures.add(session.executeAsync(vehicleHistory.saveQuery(vh)), "vehicle_insert");
	}


	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}	
}
