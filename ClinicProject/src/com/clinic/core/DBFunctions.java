package com.clinic.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import com.clinic.model.Patient;

public class DBFunctions {
		
		private String dbPath = "";
		private PropertyUtil propertyUtil = null;
		
		public DBFunctions(){
			dbPath = System.getenv("APP_HOME");
			propertyUtil = new PropertyUtil();
		}
		
		private String DateFormat(Object date){
			if(date == null || date.toString().equalsIgnoreCase("")) return "";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
			return sdf.format(date);
		}
	
		private Connection getConnection(){
			String URL = "jdbc:derby:"+dbPath+"\\"+propertyUtil.getProperty("db.name");
			
			System.out.println(URL);
			Connection con = null;
			try{
				Properties dbProp = new Properties();
				dbProp.put("user", propertyUtil.getProperty("db.username"));
	            dbProp.put("password", propertyUtil.getProperty("db.password"));
	            
				Class.forName(propertyUtil.getProperty("db.driverName"));
				con = DriverManager.getConnection(URL, dbProp);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			finally{
			}
			return con;
		}
		
		private ArrayList getRecords(ResultSet resultSet){
			if (resultSet == null) return null;
			
			ArrayList recordList = new ArrayList();
			try{
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
	
				// Table Columns
				int columnCount = resultSetMetaData.getColumnCount();
				ArrayList columnList = new ArrayList();
				
				HashMap<Integer, Object> columnTypeMap = new HashMap();
				for(int i = 0; i < columnCount; i++){
					columnList.add(resultSetMetaData.getColumnName(i+1));
					columnTypeMap.put(i, resultSetMetaData.getColumnTypeName(i+1));
				}
				recordList.add(columnList);
						
				// Table Records
				ArrayList recordSet = new ArrayList();
				while(resultSet.next()) {
					ArrayList record = new ArrayList();
					for(int i = 0; i < columnCount; i++){
						Object columnType = (Object)columnTypeMap.get(i);
						
						if( columnType.toString().equalsIgnoreCase("VARCHAR") ||
								columnType.toString().equalsIgnoreCase("CHAR")) {
							record.add(resultSet.getString(i+1));
						} else if ( columnType.toString().equalsIgnoreCase("INTEGER") ||
								columnType.toString().equalsIgnoreCase("SMALLINT")) {
							record.add(resultSet.getInt(i+1));
						} else if ( columnType.toString().equalsIgnoreCase("BIGINT")) {
							record.add(resultSet.getBigDecimal(i+1));
						} else if ( columnType.toString().equalsIgnoreCase("TIMESTAMP")) {
							record.add(DateFormat(resultSet.getTimestamp(i+1)));
						}
					}
					recordSet.add(record);
				}
				recordList.addAll(recordSet);
			}
			catch(Exception ex){
				ex.printStackTrace();
			}
			return recordList;
		}

		public boolean addRecord(Patient patient) {
			boolean isRecordAdded = false;
			String INSERT_SQL = "Insert into tblpatient(unique_no, name, address, age, sex, complaints, primary_contact, alternate_contact, medication, created_by, created_date) "+
								" values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			Connection con = getConnection();
			
			PreparedStatement pst = null;
			try {
				con.setAutoCommit(false);
				pst = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				pst.setString(1, "");
				pst.setString(2, patient.getName());
				pst.setString(3, patient.getAddress());
				pst.setInt(4, patient.getAge());
				pst.setString(5, patient.getSex());
				pst.setString(6, patient.getComplaints());
				pst.setLong(7, patient.getPrimary_contact());
				pst.setLong(8, patient.getAlternate_contact());
				pst.setString(9, patient.getMedication());
				pst.setString(10, propertyUtil.getProperty("db.username"));
				pst.setTimestamp(11, getCurrentTimestamp());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				pst.execute();
				if(pst.getUpdateCount() > 0){
					isRecordAdded = true;
					con.commit();
				}
				fetchRecords("","");
			} catch (SQLException e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			return isRecordAdded;
		}
	
		public boolean updateRecord(Patient patient) {
			boolean isRecordUpdated = false;
			String UPDATE_SQL = "Update tblpatient SET name = ?, address = ?, age = ?, sex = ?, complaints = ?, primary_contact = ?," +		
									"alternate_contact = ?, medication = ? WHERE unique_no = ?";
			Connection con = getConnection();
			
			PreparedStatement pst = null;
			try {
				con.setAutoCommit(false);
				pst = con.prepareStatement(UPDATE_SQL);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				pst.setString(1, patient.getName());
				pst.setString(2, patient.getAddress());
				pst.setInt(3, patient.getAge());
				pst.setString(4, patient.getSex());
				pst.setString(5, patient.getComplaints());
				pst.setLong(6, patient.getPrimary_contact());
				pst.setLong(7, patient.getAlternate_contact());
				pst.setString(8, patient.getMedication());
				pst.setString(9, patient.getUnique_no());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				pst.execute();
				if(pst.getUpdateCount() > 0){
					isRecordUpdated = true;
					con.commit();
				}
			} catch (SQLException e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			return isRecordUpdated;
		}
		
		public boolean deleteRecord(int patientId) {
			boolean isRecordRemoved = false;
			String DELETE_SQL = "delete from tblpatient where patient_id =  ?";
			Connection con = getConnection();
			
			PreparedStatement pst = null;
			try {
				con.setAutoCommit(false);
				pst = con.prepareStatement(DELETE_SQL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				pst.setInt(1, patientId);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				pst.execute();
				if(pst.getUpdateCount() > 0){
					isRecordRemoved = true;
					con.commit();
				}
				fetchRecords("","");
			} catch (SQLException e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			return isRecordRemoved;
		}
	
	
		public ArrayList fetchRecords(String paramName, String paramValue) throws SQLException {
			String SELECT_SQL = " Select patient_id, unique_no, name, address, age, sex, complaints, primary_contact, alternate_contact,"
							  + "  medication, created_by, created_date, (select max(last_visit_date) from tblpatient_history where patient_id = p.patient_id) last_visit_date"
							  + "  from tblpatient p ORDER BY name, unique_no";
			
			String paramType = "";
			if(paramName.startsWith("Unique")){
				SELECT_SQL = SELECT_SQL + " where unique_no LIKE ? ";
				paramType = "STRING";
			} else if(paramName.startsWith("Contact")){
				SELECT_SQL = SELECT_SQL + " where primary_contact = ? ";
				paramType = "BIGINT";
			}
			
			System.out.println(SELECT_SQL);
			Connection con = getConnection();
			
			if(paramName.equalsIgnoreCase("")) {
				try {
					Statement st = con.createStatement();
					ResultSet resultSet = st.executeQuery(SELECT_SQL);
					ArrayList recordSet = getRecords(resultSet);
					System.out.println("Results:" + recordSet);
					return recordSet;
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			else{
				try {
					PreparedStatement pst = con.prepareStatement(SELECT_SQL);
					pst.setString(1, paramValue);
					
					ResultSet resultSet = pst.executeQuery();
					ArrayList recordSet = getRecords(resultSet);
					System.out.println("Results:" + recordSet);
					return recordSet;
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			return null;
		}
	
	
	public static void main(String[] arg) throws SQLException{
		/*DBFunctions dbFunctions = new DBFunctions();
		
		dbFunctions.fetchRecords("Unique", "PT091600%");*/
		//try {
			
			//System.out.println(System.getenv("APP_HOME"));
			
			//String db_path = System.getenv("APP_HOME");
			
			/*dbFunctions.getRecords(db_path, 3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
*/	
		DBFunctions dbFunctions =  new DBFunctions();
		dbFunctions.getCurrentTimestamp();
		}
	
	public static String isLongNull(Object objValue) {
		if (objValue == null) return "0";
		if (objValue == "") return "0";
		if (objValue != null && objValue.toString().length() > 0 ) return objValue.toString();
		return "0";
	}
	
	private Timestamp getCurrentTimestamp(){
		Date dt = new Date();
		Timestamp ts = new Timestamp(dt.getTime());
		return ts;
	}
	
	private String getCurrentDate(){
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		String dtValue = sdf.format(dt);
		
		return dtValue;
	}
	
}
