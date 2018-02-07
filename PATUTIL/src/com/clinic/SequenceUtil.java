package com.clinic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
/** This class is used to generate the unique no for patients **/
public class SequenceUtil {
	
	private static String getTrailingZeros(Integer seqNo) {
		String strSeqNo = "";
		if(seqNo.toString().length() == 1 ) {
			strSeqNo = "000"+seqNo;
		}else if(seqNo.toString().length() == 2 ) {
			strSeqNo = "00"+seqNo;
		}else if(seqNo.toString().length() == 3 ) {
			strSeqNo = "0"+seqNo;
		}else{
			strSeqNo = seqNo+"";
		}
		return strSeqNo;
	}

	public static void updateSequenceNo(int patientId, int seqNo) throws SQLException {
		int updateRowcount = 0;
		Connection conn = null;
		PreparedStatement pstmnt = null;
		try{
			String patientPrefixNo = getTrailingZeros(seqNo);	
			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("MMYY");
			String dtValue = sdf.format(dt);
			
			patientPrefixNo = "PT" + dtValue + patientPrefixNo; // PT<MM><YY><9999>
			
			//System.out.println(patientPrefixNo);
			String connectionURL = "jdbc:default:connection";
			conn = DriverManager.getConnection(connectionURL);
			String DML = "UPDATE tblpatient SET unique_no = ? WHERE patient_id = ?";
			pstmnt = conn.prepareStatement(DML);
			pstmnt.setString(1, patientPrefixNo);
			pstmnt.setInt(2, patientId);
			updateRowcount = pstmnt.executeUpdate();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally{
			if(pstmnt != null){
				pstmnt.close();
			}
			if(conn != null){
				conn.close();
			}
			System.out.println("Updated Sequence count.."+updateRowcount);
		}
	}

	/*public static void main(String[] arg) throws SQLException{
		SequenceUtil.updateSequenceNo(1,99);
	}*/
}
