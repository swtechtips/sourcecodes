package com.clinic;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;

import com.clinic.core.DBFunctions;
import com.clinic.model.Patient;
import com.clinic.view.PatientOverviewController;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class GUIApplication extends Application {
	
	private Stage primaryStage;
	private ObservableList<Patient> patientData = FXCollections.observableArrayList();
	private DBFunctions dbFunctions = null;
	
	public GUIApplication()  {
		try{
			dbFunctions = new DBFunctions();
			populateTable();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void populateTable() throws SQLException{
		
		if(patientData != null && patientData.size() > 0 ) patientData.clear();
		
		ArrayList resultSet = dbFunctions.fetchRecords("", "");
		if(resultSet.size() == 1) return;
		for(int i=1; i < resultSet.size(); i++){
			ArrayList record = (ArrayList) resultSet.get(i);
			Patient patient = new Patient(new Integer(record.get(0).toString()), record.get(1).toString(), record.get(2).toString(),
										  (record.get(3) == null)?"":record.get(3).toString(), new Integer((record.get(4)==null)?"0":record.get(4).toString()), 
										  (record.get(5) == null)?"":record.get(5).toString(),
										  (record.get(6) == null)?"":record.get(6).toString(), new Long(dbFunctions.isLongNull(record.get(7))), new Long(dbFunctions.isLongNull(record.get(8))), 
										  (record.get(9) == null)?"":record.get(9).toString(),(record.get(12) == null)?"":record.get(12).toString());
			patientData.add(patient);
			
		}
	}
	
	public ObservableList<Patient> getPatientData(){
		return patientData;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Cakra Clinic");
		
        showPersonOverview();
	}

    /**
     * Shows the Patient overview inside the Anchorpane layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIApplication.class.getResource("/com/clinic/view/PatientOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            Scene scene = new Scene(personOverview);
            primaryStage.setScene(scene);
            primaryStage.show();

            // Give the controller access to the main app.
            PatientOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		System.out.println("Main Method!!");
        launch(args);
    }

}
	
