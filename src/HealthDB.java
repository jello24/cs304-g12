import java.sql.*;
import java.util.*;
import java.text.*;

/**
 * <h2>HealthDB</h2>
 * Handles the back end logic of the Healthcare Database, including communication
 * with the Oracle database
 * <br>
 * CPSC 304 Group 12
 *
 * @author Jenna Bains
 * @author Laura Green
 * @author Michelle Kong
 * @author Jan Louis Evangelista
 *
 *         <br>
 *         Note: The Oracle DB + JDBC example provided by the CS department,
 *         branch.java has been used as a reference for building this
 *         application.
 */
public class HealthDB {
	private String username;
	private String password;

	private Integer userClass;
	private Connection con;

	/**
	 * Primary key constants Increment on creating the corresponding tuple to generate unique primary keys
	 *
	 * prescriptionID
	 * testID
	 * invoiceID
	 */
	static Integer prescriptionIDCounter;
	static Integer testIDCounter;
	static Integer invoiceIDCounter;
	private DateFormat format = new SimpleDateFormat("MMMM dd yyyy");

	/**
	 * HealthDB Constructor
	 */
	public HealthDB() {
		System.out.println("HealthDB App Started");
		try{ // Load the Oracle JDBC driver
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("Oracle driver loaded.");}
		catch (SQLException ex){
				System.out.println("Error loading Oracle driver: " + ex.getMessage());
				System.exit(-1);}
	}

	/**
	 * setOracleCredentials Sets the username and password to be used to log into
	 * the Oracle database
	 *
	 * @param username - the username to log into the Oracle DB
	 * @param password - the password to log into the Oracle DB
	 */
	public void setOracleCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * connectToDB Connects to the Oracle DB using credentials
	 *
	 * @param username - the username to log into the Oracle DB
	 * @param password - the password to log into the Oracle DB
	 *
	 * @return true - if Oracle database is connected to the app, false otherwise
	 */
	public boolean connectToDB(String username, String password) {
		String connectURL = "jdbc:oracle:thin:@dbhost.ugrad.cs.ubc.ca:1522:ug";
		try {
			con = DriverManager.getConnection(connectURL,username,password);
			System.out.println("\nConnected to Oracle!");


			// Set counters
			String maxTest = "select max(testID) as max from labtest";
			String maxInvoice = "select max(invoiceID) as max from invoice";
			String maxPrescription = "select max(prescriptionID) as max from prescription";

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(maxTest);
			if(rs.next()){
				testIDCounter = rs.getInt("max") + 1;
			}

			rs = stmt.executeQuery(maxInvoice);
			if(rs.next()){
				invoiceIDCounter = rs.getInt("max") +1;
			}

			rs = stmt.executeQuery(maxPrescription);
			if(rs.next()){
				prescriptionIDCounter = rs.getInt("max") + 1;
			}
			System.out.println("invoiceIDCounter: " + invoiceIDCounter);
			System.out.println("prescriptionIDCounter: " + prescriptionIDCounter);
			System.out.println("testIDCounter: " + testIDCounter);

			stmt.close();

			return true;}
		catch (SQLException ex){
			System.out.println("Error connecting to Oracle: " + ex.getMessage());
			return false;}
	}

	/**
	 * createX methods: Creates a new X tuple
	 */

	/**
	 * Creates a prescription
	 *
	 * @param medication
	 * @param dosage
	 * @param quantity
	 * @param patientID
	 * @param drHID
	 *
	 * Creates a prescription with current date as prescribedDate
	 */
	public boolean createPrescription(String medication, String dosage, String quantity,
																 String patientID, String drHID) {
		try {
			System.out.println("PrescriptionID Counter pre: " + prescriptionIDCounter);
			String query ="insert into prescription (prescriptionID, medication, dosage, quantity, patientID,"
							+ " drHID, prescribedDate) values (" + prescriptionIDCounter + ",'" + medication + "', " + dosage
							+ ", " + quantity +", " + patientID +", " + drHID + "," + today() + ")";
			prescriptionIDCounter++;
			System.out.println("prescriptionID Counter post: " + prescriptionIDCounter);
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			stmt.close();
			System.out.println("Prescription successfully created");
			return true;
		} catch (SQLException ex){
			System.out.println("Failed to create prescription" + ex.getMessage());
			return false;
		}
	}

	/**
	 * Creates a test
	 *
	 * @param patientID
	 * @param drHID
	 *
	 * Creates a lab test with current date as ordered date
	 */
	public boolean createTest(String patientID, String drHID) {
		try {
			System.out.println("TestID Counter pre: " + testIDCounter);
			String query = "insert into labtest (testID, patientID, drHID, orderedDate) values (" + testIDCounter + ", "
							+ patientID + ", " + drHID + ", " + today() + ")";
			testIDCounter++;
			System.out.println("TestID Counter post: " + testIDCounter);
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			stmt.close();
			System.out.println("Test successfully created");
			return true;
		} catch (SQLException ex){
			System.out.println("Failed to create test" + ex.getMessage());
			return false;
		}
	}

	/**
	 * Creates a referral
	 *
	 * @param patientID
	 * @param referrerHID - HID of doctor making the referral
	 * @param referreeHID - HID of doctor being referred to
	 * @return returns true if the referral was successfully created
	 *
	 * Creates a referral with current date as referred date
	 */
	public boolean createReferral(String patientID, String referrerHID, String referreeHID) {
		try {
			String query = "insert into referral (patientID, referrerHID, referreeHID, referredDate) values ("
							+ patientID + ", " + referrerHID + ", " + referreeHID + ", " + today() + ")";
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			stmt.close();
			System.out.println("Referral successfully created");
			return true;
		} catch (SQLException ex){
			System.out.println("Failed to create referral" + ex.getMessage());
			return false;
		}
	}

	/**
	 * Creates an invoice
	 *
	 * Required on creating a new invoice
	 * @param patientID
	 * @param invoiceItem
	 * @param dueDate
	 * @param paymentStatus
	 * @param amountOwing
	 *
	 * Optional on creating a new invoice
	 * @param paymentDate
	 * @param paymentMethod
	 * @param paymentID
	 * @param planID
	 *
	 * Creates an unpaid/fully paid invoice with current date as creation date
	 */
	public boolean createInvoice(String patientID, String invoiceItem, String dueDate, String paymentStatus,
														String paymentDate, String paymentMethod, String amountOwing, String paymentID, String planID) {
		try {
			System.out.println("invoiceID Counter pre: " + invoiceIDCounter);
			// Oracle will insert null if you insert an empty string. Therefore do not need to check if optional values are empty strings
			String query = "insert into invoice (invoiceID, patientID, invoiceItem, creationDate, dueDate, paymentStatus, "
							+ "paymentDate, paymentMethod, amountOwing, paymentID, planID) values (" + invoiceIDCounter + ", "
							+ patientID + ", " + today() + ", " + dueDate + ", " + paymentStatus + ", "
							+ paymentDate + ", " + paymentMethod + ", " + amountOwing + ", " + paymentID + ", " + planID + ")";
			invoiceIDCounter++;
			System.out.println("invoiceID Counter post: " + invoiceIDCounter);
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			stmt.close();
			System.out.println("Invoice successfully created");
			return true;
		} catch (SQLException ex){
			System.out.println("Failed to create invoice" + ex.getMessage());
			return false;
		}
	}

	/**
	 * deleteX method: Deletes an existing X tuple by specified ID
	 */

	/**
	 * Delete specified patient.
	 * @param pid
	 * @return true if the patient was successfully deleted
	 * Deletes patient and cascades delete to
	 * referral, prescription, labtest, provincialhealthplan, extendedbenefitsplan, invoice tables
	 */
	public boolean deletePatient(String pid) {
		try {
			String query = "delete from patient where patientID = " + pid;
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			int result = stmt.executeUpdate(query);
			System.out.println(result);
			stmt.close();
			System.out.println("Patient successfully deleted");
			return true;
		} catch (SQLException ex){
			System.out.println("Failed to delete patient" + ex.getMessage());
			return false;
		}
	}

	/** getX methods: Returns all X tuples for specified name/ID
	 */

	/** Finds all patients with a name containing the string provided.
	* @param name: the name of the patient to be searched for
	* @return tuples of all patients whose first or last name contains the string provided.
	*/
	public ArrayList<ArrayList<String>> getPatients(String name){
		ArrayList<ArrayList<String>> tuples = new ArrayList<ArrayList<String>>();
		try{
			String query = "select p.firstName, p.lastName, p.patientID, p.street, "
							+ "pc.city, pc.province, pc.postalcode, pc.country, "
							+ "p.homePhone, p.mobilePhone from patient p left join postalcode pc "
							+ "on p.postalcode = pc.postalcode " + "where (p.firstName like '%"
							+ name + "%'" + " or p.lastName like '%" + name + "%')" ;
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()){
				ArrayList<String> tuple = new ArrayList<String>();
				tuple.add(rs.getString("firstName"));
				tuple.add(rs.getString("lastName"));
				tuple.add(rs.getString("patientID"));
				tuple.add(rs.getString("street"));
				tuple.add(rs.getString("city"));
				tuple.add(rs.getString("province"));
				tuple.add(rs.getString("postalcode"));
				tuple.add(rs.getString("country"));
				tuple.add(rs.getString("homePhone"));
				tuple.add(rs.getString("mobilePhone"));
				tuples.add(tuple);
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
		} catch (SQLException ex){
			System.out.println("Failed to get patients. " + ex.getMessage());
		}
		return tuples;
	}

	/**
	 * Returns the prescriptions of the specified patient
	 *
	 * tuple = {0 presID, 1 presDate, 2 medication, 3 dosage, 4 doseMeasure, 5 qty, 6 filledDate}
	 *
	 * @param pid- the PID of the selected Patient, cannot be null
	 * @return prescription data
	 */
	public ArrayList<ArrayList<String>> getPrescriptions(String pid) {
		ArrayList<ArrayList<String>> tuples = new ArrayList<ArrayList<String>>();
		try{
			String query = "select pr.prescriptionID, pr.prescribedDate, m.medication,"+
										 " pr.dosage, m.dosageMeasure, pr.quantity, pr.filledDate"+
										 " from prescription pr, medication m where pr.medication ="+
										 " m.medication and pr.patientID = "+ pid + " order by pr.prescribedDate desc";
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()){
				ArrayList<String> tuple = new ArrayList<String>();
				tuple.add(rs.getString("prescriptionID"));
				if (rs.getDate("prescribedDate")!=null){
					tuple.add(format.format(rs.getDate("prescribedDate")));
			  } else{
					tuple.add("");
			  }
				tuple.add(rs.getString("medication"));
				tuple.add(rs.getString("dosage"));
				tuple.add(rs.getString("dosageMeasure"));
				tuple.add(rs.getString("quantity"));
				if (rs.getDate("filledDate")!=null){
					tuple.add(format.format(rs.getDate("filledDate")));
				} else{
					tuple.add("");
				}

				if(rs.getString("filledDate") == null) {
                    tuple.add("No");
                }
				else {
				    tuple.add("Yes");
                }

				tuples.add(tuple);
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
		} catch (SQLException ex){
			System.out.println("Failed to get prescriptions. " + ex.getMessage());
		}
		return tuples;
	}

	/**
	 * Returns the tests of the specified doctor
	 *
	 * tuple = {0 testID, 1 orderedDate, 2 performedDate}
	 *
	 * @param pid - the PID of the selected Patient
	 * @return test data
	 */
	public ArrayList<ArrayList<String>> getTests(String pid) {
		ArrayList<ArrayList<String>> tuples = new ArrayList<ArrayList<String>>();
		try{
			String query = "select testID, orderedDate, performedDate from LabTest where patientID = " + pid
										 + " order by orderedDate desc";
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()){
				ArrayList<String> tuple = new ArrayList<String>();
				tuple.add(rs.getString("testID"));
				if (rs.getDate("orderedDate")!=null){
					tuple.add(format.format(rs.getDate("orderedDate")));
				} else{
					tuple.add("");
				}
				if (rs.getDate("performedDate")!=null){
					tuple.add(format.format(rs.getDate("performedDate")));
			  } else{
					tuple.add("");
			  }

                if(rs.getString("performedDate") == null) {
                    tuple.add("No");
                }
                else {
                    tuple.add("Yes");
                }
				tuples.add(tuple);
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
		} catch (SQLException ex){
			System.out.println("Failed to get test summary. " + ex.getMessage());
		}
		return tuples;
	}

	/**
	 * Returns the referrals of the specified patient
	 *
	 * tuple = {0 firstName, 1 lastName, 2 specialization, 3 referredDate}
	 *
	 * @param pid - the PID of the selected Patient
	 * @return referral data
	 */
	public ArrayList<ArrayList<String>> getReferrals(String pid) {
		ArrayList<ArrayList<String>> tuples = new ArrayList<ArrayList<String>>();
		try{
			String query = "select h.firstName, h.lastName, d.specialization,"+
										 " r.referredDate from Referral r, HealthcareProfessional h,"+
										 " Doctor d where r.referreeHID = h.HID and d.HID = h.hid"+
										 " and r.patientID = " + pid + " order by referredDate desc";
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()){
				ArrayList<String> tuple = new ArrayList<String>();
				tuple.add(rs.getString("firstName"));
				tuple.add(rs.getString("lastName"));
				tuple.add(rs.getString("specialization"));
				if (rs.getDate("referredDate")!=null){
					tuple.add(format.format(rs.getDate("referredDate")));
				} else{
					tuple.add("");
				}
				tuples.add(tuple);
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
		} catch (SQLException ex){
			System.out.println("Failed to get referrals. " + ex.getMessage());
		}
		return tuples;
	}

	/**
	 * Returns provincial plan information for specified patient
	 *
	 * tuple = {0 planID, 1 planType, 2 startDate, 3 endDate}
	 *
	 * @param pid - the PID of the selected Patient
	 * @return provincial plan information
	 */
	public ArrayList<String> getPlan(String pid) {
		ArrayList<String> tuple = new ArrayList<String>();
		try{
			String query = "select planID, policyType, startDate, endDate from "+
										 "ProvincialHealthPlan where patientID = " + pid;
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()){
				tuple.add(rs.getString("planID"));
				tuple.add(rs.getString("policyType"));
				if (rs.getDate("startDate")!=null){
					tuple.add(format.format(rs.getDate("startDate")));
				} else{
					tuple.add("");
				}
				if (rs.getDate("endDate")!=null){
					tuple.add(format.format(rs.getDate("endDate")));
			  } else {
					tuple.add("");
			  }
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
		} catch (SQLException ex){
			System.out.println("Failed to get provincial plan information " + ex.getMessage());
		}
		return tuple;
	}

	/**
	 * Returns extended benefits information for specified patient
	 *
	 * tuple = {0 chiropractic}
	 *
	 * @param pid - the PID of the selected Patient
	 * @return extended benefits information
	 */
	public ArrayList<ArrayList<String>> getExtendedBenefits(String pid) {
		ArrayList<ArrayList<String>> tuples = new ArrayList<ArrayList<String>>();
		try{
			String query = "select chiropractic, chiropracticAnnualLimit, chiropracticYTD,"+
										 " physiotherapy, physiotherapyAnnualLimit, physiotherapyYTD,"+
										 " nonSurgicalPodiatry, nonSurgicalPodiatryAnnualLimit, "+
										 "nonSurgicalPodiatryYTD, acupuncture, acupunctureAnnualLimit,"+
										 " acupunctureYTD, medication, medicationAnnualLimit, "+
										 "medicationYTD from ExtendedBenefitsPlan ebp, ProvincialHealthPlan php where ebp.planID = php.planID and php.patientID = "+ pid;
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()){
				ArrayList<String> tuple = new ArrayList<String>();
				tuple.add(rs.getString("chiropractic"));
				tuple.add(rs.getString("chiropracticAnnualLimit"));
				tuple.add(rs.getString("chiropracticYTD"));
				tuple.add(rs.getString("physiotherapy"));
				tuple.add(rs.getString("physiotherapyAnnualLimit"));
				tuple.add(rs.getString("physiotherapyYTD"));
				tuple.add(rs.getString("nonSurgicalPodiatry"));
				tuple.add(rs.getString("nonSurgicalPodiatryAnnualLimit"));
				tuple.add(rs.getString("nonSurgicalPodiatryYTD"));
				tuple.add(rs.getString("acupuncture"));
				tuple.add(rs.getString("acupunctureAnnualLimit"));
				tuple.add(rs.getString("acupunctureYTD"));
				tuple.add(rs.getString("medication"));
				tuple.add(rs.getString("medicationAnnualLimit"));
				tuple.add(rs.getString("medicationYTD"));
				tuples.add(tuple);
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
		} catch (SQLException ex){
			System.out.println("Failed to get extended benefits information " + ex.getMessage());
		}
		return tuples;
	}

	/**
	 * Returns total unpaid amount owing for specified patient
	 *
	 * @param pid - the PID of the selected Patient
	 * @return total unpaid amount owing
	 */
	public double getAmountOwing(String pid) {
		double amountOwing = 0;
		try{
			String query = "select sum(amountOwing) as amountOwing from Invoice where "+
										 "patientID = " + pid + " and paymentStatus = 'Unpaid'";
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			if(rs.next()){
				amountOwing = rs.getDouble("amountOwing");
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
		} catch (SQLException ex){
			System.out.println("Failed to get amount owing " + ex.getMessage());
		}
		return amountOwing;
	}

	/**
	 * Returns total OVERDUE unpaid amount owing for specified patient.
	 *
	 * @param pid - the PID of the selected Patient
	 * @return total OVERDUE unpaid amount owing
	 */
	public double getOverdueAmountOwing(String pid) {
		double amountOverdue = 0;
		try{
			String query = "select sum(amountOwing) as overdueAmountOwing from Invoice "+
										 "where patientID = " + pid + "and paymentStatus = 'Unpaid' and dueDate < " + today();
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			if(rs.next()){
				amountOverdue = rs.getDouble("overdueAmountOwing");
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
			return amountOverdue;
		} catch (SQLException ex){
			System.out.println("Failed to get overdue amount owing " + ex.getMessage());
		}
		return amountOverdue;
	}

	/**
	 * Returns invoices for specified patient
	 *
	 * @param pid - the PID of the selected Patient
	 * @return invoices for specified patient
	 */
	public ArrayList<ArrayList<String>> getInvoices(String pid) {
		ArrayList<ArrayList<String>> tuples = new ArrayList<ArrayList<String>>();
		try{
			String query = "select invoiceID, invoiceItem, creationDate, dueDate, "+
										 "paymentStatus, amountOwing from Invoice where patientID = " + pid
										 + " order by creationDate desc";
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute each query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()){
				ArrayList<String> tuple = new ArrayList<String>();
				tuple.add(rs.getString("invoiceID"));
				tuple.add(rs.getString("invoiceItem"));
				if (rs.getDate("creationDate")!=null){
					tuple.add(format.format(rs.getDate("creationDate")));
				} else{
					tuple.add("");
				}
				if (rs.getDate("dueDate")!=null){
					tuple.add(format.format(rs.getDate("dueDate")));
				} else{
					tuple.add("");
				}
				tuple.add(rs.getString("paymentStatus"));
				tuple.add(rs.getString("amountOwing"));
				tuples.add(tuple);
			}

			// Close the statement, the result set will be closed in the process.
			stmt.close();
		} catch (SQLException ex){
			System.out.println("Failed to get invoice information " + ex.getMessage());
		}
		return tuples;
	}

	/**
	 * findX methods: Finds X tuple by its primary key
	 */

	/**
	 * findPatient
	 * Finds a patient in the database, stores tuple information in a data structure
	 * tuple[] = {0 firstname, 1 lastname, 2 pid, 3 street, 4 city, 5 postalcode, 6 country, 7 homephone, 8 mobilephone}
	 * @param PID
	 * @return the single tuple for the patient with the given PID
	 */
	public ArrayList<String> findPatient(String PID) {
		ArrayList<String> tuple = new ArrayList<String>();
		try{
			String query = "select p.firstName, p.lastName, p.patientID, p.street,"+
										 " pc.city, pc.province, pc.postalcode, pc.country, "+
										 "p.homePhone, p.mobilePhone from patient p left join postalcode"+
										 " pc on p.postalcode = pc.postalcode where p.patientID = " + PID;
			// Create a statement
			Statement stmt = con.createStatement();
			// Execute the query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()){
				tuple.add(rs.getString("firstName"));
				tuple.add(rs.getString("lastName"));
				tuple.add(rs.getString("patientID"));
				tuple.add(rs.getString("street"));
				tuple.add(rs.getString("city"));
				tuple.add(rs.getString("province"));
				tuple.add(rs.getString("postalcode"));
				tuple.add(rs.getString("country"));
				tuple.add(rs.getString("homePhone"));
				tuple.add(rs.getString("mobilePhone"));
			}
			stmt.close();

	} catch (SQLException ex){
		System.out.println("Failed to get patient personal info. " + ex.getMessage());
	}
	return tuple;
	}


	/**
	 * Finds the patient ID associated with a prescription.
	 * @param prescriptionID: ID of the prescription
	 * @return PID of patient associated with prescription. Returns the empty
	 					 string if no prescription is found.
	 */
	public String findPrescription(String prescriptionID) {
		try{
			String query = "select patientID from Prescription where prescriptionID = " + prescriptionID;
			Statement stmt = con.createStatement();
			// Execute each query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			if(rs.next()){
				return rs.getString("patientID");
		  }

		} catch (SQLException ex){
				System.out.println("Error finding prescription. " + ex.getMessage());
		}
		return "";
	}

	/**
	 * Finds the patientID associated with a test and returns it
	 * @param testID: ID of the test to be found
	 * @return the tuple of the test with the ID provided if no tuple is found
	 * 				returns the empty string.
	 */
	public String findTest(String testID) {
		try{
			String query = "select patientID from LabTest where testID = " + testID;
			Statement stmt = con.createStatement();
			// Execute each query.
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			if(rs.next()){
				return rs.getString("patientID");
			}

		} catch (SQLException ex){
				System.out.println("Error finding test. " + ex.getMessage());
		}
		return "";
	}

	/**
     * Finds the patientID associated with an invoice
     * @param invoiceID
     * @return the patientID associated with the invoice. If no tuple is found
		 * 				returns the empty string.
     */
    public String findInvoice(String invoiceID) {
			try{
				String query = "select patientID from Invoice where invoiceID = " + invoiceID;
				Statement stmt = con.createStatement();
				// Execute each query.
				ResultSet rs = stmt.executeQuery(query);
				ResultSetMetaData rsmd = rs.getMetaData();

				if(rs.next()){
					return rs.getString("patientID");
				}

			} catch (SQLException ex){
					System.out.println("Error finding invoice. " + ex.getMessage());
			}
			return "";
    }

    /**
     * updateX methods: Updates an existing X tuple with given data
     */

	/**
	* Allows pharmacists to mark a prescription as filled.
	* @param hid: HID of the pharmacist filling the prescription
	* @param prescriptionID: the ID of the prescription being filled
	*/
	public boolean updatePrescription(String hid, String prescriptionID){
		boolean success = false;
		try{
			String prescription = "update prescription set pharmHID=" + hid +
														", filledDate=" + today() + ", where prescriptionID="
														+ prescriptionID;

			Statement stmt = con.createStatement();
			stmt.executeUpdate(prescription);
			success = true;

		} catch (SQLException ex){
				System.out.println("Error updating prescription. " + ex.getMessage());
		}
		return success;
	}

	/**
	* Updates an existing test.
	* Returns true if update/creation was sucessful.
	* @param testID: ID of the test
	* @param labTechID: HID of the lab tech filling in the test info.
	* @param Rest: Lab test values
	*/
	public boolean updateTest(String testID, String cholesterol, String HDLcholesterol,
													 String LDLcholesterol, String trigycerides, String whiteBloodCellCount,
													 String redBloodCellCount, String hematocrit, String plateletCount,
													 String NRBCPercent, String NRBCAbsolute, String sodium, String glucose,
													 String phosphorus, String labTechHID) {
	 boolean success = false;
	 try{
	 	String test = "update labtest set cholesterol='" + cholesterol + ", HDLcholesterol="
	 								+ HDLcholesterol + ", LDLcholesterol=" + LDLcholesterol + ", trigycerides="
									+ trigycerides + ", whiteBloodCellCount=" + whiteBloodCellCount +
									", redBloodCellCount=" + redBloodCellCount + ", hematocrit=" + hematocrit +
									", plateletCount=" + plateletCount + ", NRBCPercent=" + NRBCPercent +
									", NRBCAbsolute=" + NRBCAbsolute + ", sodium=" + sodium + ", glucose="
									+ glucose + ", phosphorus=" + phosphorus + ", labTechHID=" + labTechHID +
									" where testID=" + testID;

	 	Statement stmt = con.createStatement();
	 	stmt.executeUpdate(test);
	 	success = true;

	 } catch (SQLException ex){
	 		System.out.println("Error updating test. " + ex.getMessage());
	 }
	 return success;
	}

		/**
		* Updates an existing invoice in the database.
		* @param invoiceID: The ID of the invoice to be updated.
		* @param dueDate: Date the invoice is due.
		* @param invoiceItem: Item the invoice is for.
		* @param paymentStatus: Status of the invoice. One of: paid, unpaid
		* @param paymentMethod: Payment method if invoice has been paid. One of: Credit/Debit, Cash, Cheque
		* @param amountOwing: Amount owing on the invoice
		*/
    public boolean updateInvoice(String invoiceID, String dueDate, String invoiceItem,
                                String paymentStatus, String paymentDate, String paymentMethod,
																String amountOwing) {
			boolean success = false;
			try{
				String invoice = "update invoice set dueDate= to_date('" + dueDate +
															"', 'yyyy-MM-dd'), invoiceItem=" + invoiceItem +
															", paymentStatus=" + paymentStatus + ", paymentMethod="
															+ "amountOwing=" + amountOwing + ", paymentDate=to_date('" +
															paymentDate + ", 'yyyy-MM-dd') where invoiceID=" + invoiceID;

				Statement stmt = con.createStatement();
				stmt.executeUpdate(invoice);
				success = true;

			} catch (SQLException ex){
					System.out.println("Error updating invoice. " + ex.getMessage());
			}
			return success;
    }
 
/**
* Helper method that prints a single tuples
*/
	private void printTuple(ArrayList<String> tuple){
			StringBuilder sb = new StringBuilder();
				for (String s : tuple){
					sb.append(" '");
					sb.append(s);
					sb.append("', ");
				}
			System.out.println(sb.toString());
	}

/**
* Helper method that generates todays date in the proper format to submit in a query.
* @return today's date as a string in SQL query format.
*/
	static String today(){
		DateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
		return "TO_DATE('"+ sqlDate.format(today) +"','YYYY-MM-DD')";
	}
}
