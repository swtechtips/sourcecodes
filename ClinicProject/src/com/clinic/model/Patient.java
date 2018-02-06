package com.clinic.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Patient {
	 private final IntegerProperty patient_id;
	 private final StringProperty unique_no;
	 private final StringProperty name;
	 private final StringProperty address;
	 private final IntegerProperty age;
	 private final StringProperty sex;
	 private final StringProperty complaints;
	 private final LongProperty primary_contact;
	 private final LongProperty alternate_contact;
	 private final StringProperty medication;
	 private final StringProperty lastVisit;
	 
	 public Patient(){
		 this(0, null, null, null, 0, null, null, 0, 0, null, null);
	 }
	 public Patient(Integer patient_id, String unique_no, String name, String address, Integer age, String sex, String complaints,
				long primary_contact, long alternate_contact, String medication, String lastVisit) {
			this.patient_id = new SimpleIntegerProperty(patient_id);
			this.unique_no = new SimpleStringProperty(unique_no);
	 		this.name = new SimpleStringProperty(name);
			this.address = new SimpleStringProperty(address);
			this.age = new SimpleIntegerProperty(age);
	 		this.sex = new SimpleStringProperty(sex);
			this.complaints = new SimpleStringProperty(complaints);
	 		this.primary_contact = new SimpleLongProperty(primary_contact);
	 		this.alternate_contact = new SimpleLongProperty(alternate_contact);
	 		this.medication = new SimpleStringProperty(medication);
	 		this.lastVisit = new SimpleStringProperty(lastVisit);
	 }
		
	 public Integer getPatient_id() {
		return patient_id.get();
	 }
	 public void setPatient_id(int patient_id){
		this.patient_id.set(patient_id);
	 }
	public IntegerProperty patientIdProperty(){
		return patient_id;
	}
	
	public String getUnique_no() {
		return unique_no.get();
	}
	public void setUnique_no(String unique_no){
		this.unique_no.set(unique_no);
	}
	public StringProperty uniqueNoProperty(){
		return unique_no;
	}
	
	public String getName() {
		return name.get();
	}
	public void setName(String name){
		this.name.set(name);
	}
	public StringProperty nameProperty(){
		return name;
	}
	
	public String getAddress() {
		return address.get();
	}
	public void setAddress(String address){
		this.address.set(address);
	}
	public StringProperty addressProperty(){
		return address;
	}
	
	public int getAge() {
		return age.get();
	}
	public void setAge(Integer age){
		this.age.set(age);
	}
	public IntegerProperty ageProperty(){
		return age;
	}
	
	public String getSex() {
		return sex.get();
	}
	public void setSex(String sex){
		this.sex.set(sex);
	}
	public StringProperty sexProperty(){
		return sex;
	}
	
	public String getComplaints(){
		return complaints.get();
	}
	public void setComplaints(String complaints){
		this.complaints.set(complaints);
	}
	public StringProperty complaintsProperty(){
		return complaints;
	}
	
	public Long getPrimary_contact() {
		return primary_contact.get();
	}
	public void setPrimary_contact(long primary_contact){
		this.primary_contact.set(primary_contact);
	}
	public LongProperty primaryContactProperty(){
		return primary_contact;
	}
	
	public Long getAlternate_contact() {
		return alternate_contact.get();
	}
	public void setAlternate_contact(long alternate_contact){
		this.alternate_contact.set(alternate_contact);
	}
	public LongProperty alternateContactProperty(){
		return alternate_contact;
	}
	
	public String getMedication() {
		return medication.get();
	}
	public void setMedication(String medication){
		this.medication.set(medication);
	}
	public StringProperty medicationProperty(){
		return medication;
	}
	
	public String getLastVisit() {
		return lastVisit.get();
	}
	public void setLastVisit(String lastVisit){
		this.lastVisit.set(lastVisit);
	}
	public StringProperty lastVisitProperty(){
		return lastVisit;
	}
	
}
