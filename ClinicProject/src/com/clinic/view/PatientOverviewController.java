package com.clinic.view;

import java.sql.SQLException;

import com.clinic.GUIApplication;
import com.clinic.core.DBFunctions;
import com.clinic.model.Patient;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PatientOverviewController  {
	
	private static final String RECORD_SAVED_MSG = "Record saved successfully.";
	private static final String RECORD_DELETED_MSG = "Record deleted successfully.";
	
	
	private DBFunctions dbUtil = null;
	
	@FXML
	private AnchorPane mainWindow; 

	@FXML
	private TableView<Patient> patientTable;
	
	
	@FXML
	private TableColumn<Patient, String> unique_noColumn;
	
	@FXML
	private TableColumn<Patient, String> nameColumn;
	
	@FXML
	private TableColumn<Patient, String> addressColumn;
	
	@FXML
	private TableColumn<Patient, Integer> ageColumn;
	
	@FXML
	private TableColumn<Patient, String> sexColumn;
	
	@FXML
	private TableColumn<Patient, String> complaintsColumn;
	
	@FXML
	private TableColumn<Patient, Long> primary_contactColumn;

	@FXML
	private TableColumn<Patient, Long> alternate_contactColumn;

	@FXML
	private TableColumn<Patient, String> medicationColumn;
	
	@FXML
	private TableColumn<Patient, String> lastVisitDateColumn;
	
	@FXML
	private Label unique_noLabel;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label addressLabel;
	
	@FXML
	private Label ageLabel;
	
	@FXML
	private Label sexLabel;
	
	@FXML
	private Label complaintsLabel;
	
	@FXML
	private Label primary_contactLabel;
	
	@FXML
	private Label alternate_contactLabel;

	@FXML
	private Label medicationLabel;

	@FXML		
	private TextField filterField;
	
	@FXML
	private Button btnSave;
	
	@FXML
	private Button btnAddNew;
	
	@FXML
	private Button btnDelete;
	
	@FXML
	private Button btnExit;
	
	@FXML		
	private TextField txtPatientId;
	
	@FXML		
	private TextField txtUniqueNo;
	@FXML		
	private TextField txtName;
	@FXML		
	private TextField txtAddress;
	@FXML		
	private TextField txtAge;
	@FXML		
	private TextField txtSex;
	@FXML		
	private TextField txtComplaints;
	@FXML		
	private TextField txtPrimaryContact;
	@FXML		
	private TextField txtAlternateContact;
	@FXML		
	private TextField txtMedication;
	
	@FXML		
	private TextField txtLastVisitDate;
	
	private GUIApplication mainApp;
	
	private boolean isOldRecordChanged = false;
	
	public PatientOverviewController(){
		dbUtil = new DBFunctions();
	}
	
	private boolean isNewRecord(){
		if(txtUniqueNo.getText().equalsIgnoreCase("")) return true;
		return false;
	}
	
	private boolean isOldRecord(){
		//if()
		return true;
	}
	
	private boolean isUpdatedRecord(){
		if(isOldRecordChanged) return true;
		return false;
	}
	
	@FXML
	private void handleExit(){
		Stage stage = (Stage) btnExit.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void handleOnChange(){
		if(!isOldRecordChanged && !txtUniqueNo.getText().equalsIgnoreCase("")) {
			isOldRecordChanged = true;
			btnSave.setDisable(false);
		}
	}
	
	private void clearControls(){
		txtUniqueNo.setText("");
    	txtName.setText("");
    	txtAddress.setText("");
    	txtAge.setText("");
    	txtSex.setText("");
    	txtComplaints.setText("");
    	txtPrimaryContact.setText("");
    	txtAlternateContact.setText("");
    	txtMedication.setText("");
    	txtPatientId.setText("");
	}
	
	@FXML
	private void handleAddNew(){
		clearControls();

		btnSave.setDisable(false);
    	setControlsEditable(true);
    	txtName.requestFocus();
	}
	
	@FXML
	private void handleDelete(){
		int selectedPatientId = -1;
		boolean bResult = false;
		
		Object patientPKObj = txtPatientId.getText(); 
		selectedPatientId = (patientPKObj != null && !patientPKObj.toString().equalsIgnoreCase(""))? Integer.valueOf(patientPKObj.toString()).intValue(): -1;
		
		if (selectedPatientId != -1 ){
			bResult = dbUtil.deleteRecord(selectedPatientId); 
		}
		if(bResult){
			showAlert(AlertType.INFORMATION, RECORD_DELETED_MSG );
			clearControls();
		}
		try {
			mainApp.populateTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		btnSave.setDisable(true);
		btnDelete.setDisable(true);
		setControlsEditable(false);
	}
	
	private void setControlsEditable(boolean flag){
		txtName.setEditable(flag);
    	txtAddress.setEditable(flag);
    	txtAge.setEditable(flag);
    	txtSex.setEditable(flag);
    	txtComplaints.setEditable(flag);
    	txtPrimaryContact.setEditable(flag);
    	txtAlternateContact.setEditable(flag);
    	txtMedication.setEditable(flag);
	}
	
	private void showAlert(AlertType alertType, String msg){
		Alert alert = new Alert(alertType);
		alert.setTitle("Information");
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	private boolean isValidRecord(){
		boolean validFlag = true;
		if( txtName.getText() == null ){
			validFlag = false;
			showAlert(AlertType.INFORMATION, "Please fill the Name field");
		}else if( txtAddress.getText() == null ){
			validFlag = false;
			showAlert(AlertType.INFORMATION, "Please fill the Address field");
		}else if( txtPrimaryContact.getText() == null ) {
			validFlag = false;
			showAlert(AlertType.INFORMATION, "Please fill the Primary Contact field");
		}
		return validFlag;
	}
	
	@FXML
	private void handleUpdate(){
		boolean bResult = false;
		
		if(!isValidRecord()) return;
		
		if(isNewRecord()){
			Patient patient = new Patient(1, "1", txtName.getText(), txtAddress.getText(), new Integer(txtAge.getText().equalsIgnoreCase("")?"0":txtAge.getText()), txtSex.getText(), txtComplaints.getText(),
					new Long(txtPrimaryContact.getText()), txtAlternateContact.getText().trim().equalsIgnoreCase("")?new Long("0"):new Long(txtAlternateContact.getText()), txtMedication.getText(),null);
			
			bResult = dbUtil.addRecord(patient); 
		} else {
			Patient patient = new Patient(1, txtUniqueNo.getText(), txtName.getText(), txtAddress.getText(), new Integer(txtAge.getText().equalsIgnoreCase("")?"0":txtAge.getText()), txtSex.getText(), txtComplaints.getText(),
											new Long(txtPrimaryContact.getText()), new Long(txtAlternateContact.getText()), txtMedication.getText(),null);
			bResult = dbUtil.updateRecord(patient);
		}
		if(bResult){
			showAlert(AlertType.INFORMATION, RECORD_SAVED_MSG);
			clearControls();
		}
		try {
			mainApp.populateTable();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		isOldRecordChanged = false;
		btnSave.setDisable(true);
		setControlsEditable(false);
	}
	/*private void updateTableView(String updateType, int index, Patient patient) {
		if(updateType.equalsIgnoreCase("Add")){
			patientTable.getSelectionModel().getTableView().getItems().add(patient);
			//patientTable.getSelectionModel().getTableView().;
		}
		else if(updateType.equalsIgnoreCase("Update")){
			patientTable.getSelectionModel().getTableView().getItems().get(index).setName(patient.getName());	
			patientTable.getSelectionModel().getTableView().getItems().get(index).setAddress(patient.getAddress());
			patientTable.getSelectionModel().getTableView().getItems().get(index).setAge(patient.getAge());
			patientTable.getSelectionModel().getTableView().getItems().get(index).setSex(patient.getSex());
			patientTable.getSelectionModel().getTableView().getItems().get(index).setComplaints(patient.getComplaints());
			patientTable.getSelectionModel().getTableView().getItems().get(index).setPrimary_contact(patient.getPrimary_contact());
			patientTable.getSelectionModel().getTableView().getItems().get(index).setAlternate_contact(patient.getAlternate_contact());
			patientTable.getSelectionModel().getTableView().getItems().get(index).setMedication(patient.getMedication());
		}
	}
	*/
	
	@FXML
    private void initialize() {
        // Initialize the patient table with the two columns.
        unique_noColumn.setCellValueFactory(cellData -> cellData.getValue().uniqueNoProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
        sexColumn.setCellValueFactory(cellData -> cellData.getValue().sexProperty());
        complaintsColumn.setCellValueFactory(cellData -> cellData.getValue().complaintsProperty());
        primary_contactColumn.setCellValueFactory(cellData -> cellData.getValue().primaryContactProperty().asObject());
        alternate_contactColumn.setCellValueFactory(cellData -> cellData.getValue().alternateContactProperty().asObject());
        medicationColumn.setCellValueFactory(cellData -> cellData.getValue().medicationProperty());
        lastVisitDateColumn.setCellValueFactory(cellData -> cellData.getValue().lastVisitProperty());
	} 
	
	@FXML
	private void handleSearchPatient() {
		FilteredList<Patient> filteredData = new FilteredList<>(this.mainApp.getPatientData(), p -> true);
		filterField.textProperty().addListener((Observable, oldValue, newValue) -> {
			filteredData.setPredicate(patient -> {
				if(newValue == null || newValue.isEmpty()){
					return true;
				}
				
				String lowercaseFilter = newValue.toLowerCase();
				if(patient.getName().toLowerCase().contains(lowercaseFilter)){
					return true;
				}else if(patient.getAddress().toLowerCase().contains(lowercaseFilter)){
					return true;
				}else if(patient.getPrimary_contact().toString().contains(newValue)){
					return true;
				}
				return false;
			});
		});
		
		SortedList<Patient> sortedPatientData = new SortedList<>(filteredData);
		sortedPatientData.comparatorProperty().bind(patientTable.comparatorProperty());
		patientTable.setItems(sortedPatientData); 
	}

	@FXML 
	private void handleMouseClick(){
		int selectedRow = patientTable.getSelectionModel().getSelectedIndex();
		if(selectedRow == -1) return;
		
		Patient patient = patientTable.getSelectionModel().getSelectedItem();
		txtPatientId.setText(patient.getPatient_id()+"");
		txtUniqueNo.setText(patient.getUnique_no());
    	txtName.setText(patient.getName());
    	txtAddress.setText(patient.getAddress());
    	txtAge.setText(patient.getAge() + "");
    	txtSex.setText(patient.getSex());
    	txtComplaints.setText(patient.getComplaints());
    	txtPrimaryContact.setText(patient.getPrimary_contact() +"");
    	txtAlternateContact.setText(patient.getAlternate_contact() +"");
    	txtMedication.setText(patient.getMedication());
    	txtLastVisitDate.setText(patient.getLastVisit());
    	setControlsEditable(true);
    	btnSave.setDisable(false);
    	btnDelete.setDisable(false);
	}
	   
	public void setMainApp(GUIApplication mainApp){
		this.mainApp = mainApp;
		
		System.out.println("in controller main-1");
		
		patientTable.setItems(mainApp.getPatientData());
		
		//patientTable.setEditable(true);
		 	
		System.out.println("in controller main-2");
	}
} 
