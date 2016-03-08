package com.datastax.verizon;

import java.util.HashSet;
import java.util.Set;

public class Account {
	private String account_id;
	private String first_name;
	private String last_name;
	private String name_prefix;
	private String name_suffix;
	private String street_address;
	private String city;
	private String state;
	private String zip;
	private String email_address;
	private Set<String> subscribers;
	
	public Account(){
		subscribers = new HashSet<String>();
	}
	
	public void addSub(String subscriber_id){
		subscribers.add(subscriber_id);
	}
	
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getName_prefix() {
		return name_prefix;
	}
	public void setName_prefix(String name_prefix) {
		this.name_prefix = name_prefix;
	}
	public String getName_suffix() {
		return name_suffix;
	}
	public void setName_suffix(String name_suffix) {
		this.name_suffix = name_suffix;
	}
	public String getStreet_address() {
		return street_address;
	}
	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public Set<String> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(Set<String> subscribers) {
		this.subscribers = subscribers;
	}
	
}
