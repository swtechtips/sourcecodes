	set autocommit off;
	
	CREATE TABLE tblpatient
	(
	   patient_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY(START WITH 1,INCREMENT BY 1),
	   unique_no VARCHAR(10),
	   name VARCHAR(100) not null,
	   address VARCHAR(200) not null,
	   age SMALLINT,
	   sex char(1),
	   complaints VARCHAR(1500),
	   primary_contact BIGINT not null,
	   alternate_contact BIGINT,
	   medication VARCHAR(2500),
	   created_by VARCHAR(25) not null,
	   created_date TIMESTAMP not null,
	   updated_by VARCHAR(25),
	   updated_date TIMESTAMP
	);

	-- Patient Visit History
	Create Table tblpatient_history(patient_id INT NOT NULL, last_visit_date timestamp not null);

	-- Sequence Number for the Patient badge NO  // // PT<MM><YY><9999>
	CREATE SEQUENCE SEQ_TBLPATIENT AS INT 
	START WITH 1
	INCREMENT BY 1 
	MAXVALUE 9999
	CYCLE;

	-- Invoke the Procedure to update the Badge Number
	CREATE PROCEDURE SP_SEQ_UPDATE(IN patient_id INTEGER, IN seq_no INTEGER) LANGUAGE JAVA EXTERNAL NAME 'com.clinic.SequenceUtil.updateSequenceNo' PARAMETER STYLE JAVA;

	-- Trigger will invoke Procedure to update the unique_no in the TBLPATIENT table
	CREATE TRIGGER TRG_SEQ_UPDATE
	AFTER INSERT ON tblpatient
	REFERENCING NEW ROW AS newRecord
	FOR EACH ROW
	CALL "SP_SEQ_UPDATE"(newRecord.patient_id, NEXT VALUE FOR SEQ_TBLPATIENT);

	-- Trigger to add a patient history record for new patient
	CREATE TRIGGER TRG_ADD_VISIT_DATE
	AFTER INSERT ON tblpatient
	REFERENCING NEW ROW AS newRow
	FOR EACH ROW
	INSERT INTO TBLPATIENT_HISTORY(patient_id, LAST_VISIT_DATE) VALUES (newRow.patient_id, CURRENT_TIMESTAMP);

	-- Trigger to add a patient history record for old patient
	CREATE TRIGGER TRG_APPEND_VISIT_DATE
	AFTER UPDATE ON tblpatient
	REFERENCING OLD ROW AS oldRow
	FOR EACH ROW
	INSERT INTO TBLPATIENT_HISTORY(patient_id, LAST_VISIT_DATE) VALUES (oldRow.patient_id, CURRENT_TIMESTAMP);