import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO
 * Create a dialog box to select from multiple patients
 * Format dates as MM-DD-YYYY (SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");)
 * Create a dropdown menu for Invoice panel for payment status and methods
 */

public class HealthDBUI extends JFrame {

    static HealthDB hdb;
    private String username;
    private String password;

    private final String drHID = "52731";   // Dr. Melissa Clark
    private String patientID;
    private String planID;

    private String userClass[] = {"Patient Summary", "Plan Summary", "Prescriptions", "Tests"};
    private String paymentStatus[] = {"Unpaid", "Paid"};
    private String paymentMethod[] = {"", "Cash", "Credit\\Debit", "Cheque"};

    private static JFrame frame;
    private static int width = 1000;
    private static int height = 800;

    GridBagConstraints gbc;

    private JPanel panelRoot = new JPanel();
    private JPanel panelOracleLogin;
    private JPanel panelUserClass;
    private JPanel panelUserClassInfo;
    private JPanel panelPatientSummary;
    private JPanel panelPrescription;
    private JPanel panelTest;
    private JPanel panelPlanSummary;
    private JPanel panelInvoice;
    private JPanel panelEmpty;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JPanel panelUserClassSelect;
    private JComboBox cboxUserClass = new JComboBox(userClass);
    private JButton btnConfirmUserClass;

    private JPanel panelPatientSummaryFinder;
    private JPanel panelPatientSummaryInfo;
    private JScrollPane panePatientSummaryPrescriptions;
    private JScrollPane panePatientSummaryTests;
    private JScrollPane panePatientSummaryReferrals;

    private JPanel panelPrescriptionFinder;
    private JPanel panelPrescriptionInfo;
    private JScrollPane panePrescriptionPrescriptions;

    private Boolean singlePrescBool;
    private String[] singlePrescData;

    private JPanel panelTestFinder;
    private JPanel panelTestInfo;
    private JScrollPane paneTestTests;
    private JPanel panelTestActions;

    private JPanel panelInvoiceFinder;
    private JPanel panelInvoiceInfo;
    private JPanel panelInvoiceSubmit;

    private JPanel panelPlanSummaryFinder;
    private JPanel panelPlanSummaryInfo;
    private JPanel panelProvincialPlan;
    private JPanel panelExtendedBenefits;
    private JPanel panelInvoiceHistory;
    private JPanel panelInvoiceHistoryGrid;
    private JPanel panelPlanSummaryActions;
    private JPanel panelMonthlyInvoiceSummary;

    private JTextField txtPlanSumName;
    private JTextField txtPlanSumPID;
    private JTextField txtPlanSumAddress;
    private JTextField txtPlanSumMobilePhone;
    private JTextField txtPlanSumHomePhone;

    private JTextField txtTotalUnpaid;
    private JTextField txtTotalOverDue;

    private JTextField txtPlanID;
    private JTextField txtStartDate;
    private JTextField txtPolicyType;
    private JTextField txtEndDate;

    private JTextField txtInvoiceID;
    private JTextField txtInvoicePlanID;
    private JTextField txtInvoicePatientName;

    private JTextField txtDocName;
    private JTextField txtDocPID;
    private JTextField txtDocAddr;
    private JTextField txtDocMobileNum;
    private JTextField txtDocHomeNum;

    private ArrayList<String> patientArray;
    private ArrayList<String> planArray;
    private ArrayList<String> planNumArray;
    private ArrayList<String> planInvoiceArray;
    private ArrayList<ArrayList<String>> prescriptions;
    private ArrayList<ArrayList<String>> tests;
    private ArrayList<ArrayList<String>> referrals;
    private ArrayList<ArrayList<String>> extendedBenefitsArray;
    private ArrayList<ArrayList<String>> invoiceHistoryGridArray;
    private ArrayList<ArrayList<String>> monthlyInvoiceSummaryArray;


    private JTextField txtPharmName;
    private JTextField txtPharmPID;
    private JTextField txtPharmAddr;
    private JTextField txtPharmHomeNum;
    private JTextField txtPharmMobileNum;

    private JTextField txtLabName;
    private JTextField txtLabPID;
    private JTextField txtLabAddr;
    private JTextField txtLabHomeNum;
    private JTextField txtLabMobileNum;

    private JTable prescPSTable;
    private DefaultTableModel prescPSTableModel;
    private JTable testPSTable;
    private DefaultTableModel testPSTableModel;
    private JTable refPSTable;
    private DefaultTableModel refPSTableModel;
    private JTable prescPPTable;
    private DefaultTableModel prescPPTableModel;

    // view tables
    private JTable testTPTable;
    private DefaultTableModel testTPTableModel;
    private JTable invoiceTPTable;
    private DefaultTableModel invoiceTPTableModel;

    private JTable extendedBenefitsTable;
    private DefaultTableModel extendedBenefitsTableModel;
    private JTable invoiceHistoryGridTable;
    private DefaultTableModel invoiceHistoryGridTableModel;
    private JTable monthlyInvoiceSummaryTable;
    private DefaultTableModel monthlyInvoiceSummaryTableModel;

    public static void main(String args[]) {
        hdb = new HealthDB();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HealthDBUI ui = new HealthDBUI();
                ui.frame.setVisible(true);
            }
        });
    }

    public HealthDBUI() {
        initialize();
        setPanelOracleLogin();
        setPanelUserClass();
        setPanelUserClassSelect();
        setPanelUserClassInfo();

        setPanelPatientSummary();
        setPanelPatientSummaryFinder();
        setPanelPatientSummaryInfo();

        setPanelPrescription();
        setPanelPrescriptionFinder();
        setPanelPrescriptionInfo();

        setPanelTest();
        setPanelTestFinder();
        setPanelTestInfo();
        setPanelTestActions();

        setPanelInvoice();
        setPanelInvoiceFinder();
        setPanelInvoiceInfo();
        setPanelInvoiceSubmit();

        setPanelPlanSummary();
        setPanelPlanSummaryFinder();
        setPanelPlanSummaryInfo();
        setPanelProvincialPlan();
        setPanelExtendedBenefits();
        setPanelInvoiceHistory();
        setPanelInvoiceHistoryGrid();
        setPanelPlanSummaryActions();
        setPanelMonthlyInvoiceSummary();
    }

    /**
     * initialize()
     * Initializes the window as well as the combo box selections
     */
    private void initialize() {
        frame = new JFrame("Integrated Healthcare Database");
        frame.setBounds(50, 0, width, height);
        frame.setMinimumSize(new Dimension(width, height));
        panelRoot.setLayout(new CardLayout(0, 0));
        frame.setContentPane(panelRoot);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        patientArray = new ArrayList<String>();
        planArray = new ArrayList<String>();
        planNumArray = new ArrayList<String>();
        planInvoiceArray = new ArrayList<String>();
    }

    private void clearPanelData() {
        patientArray.clear();
        planArray.clear();
        planNumArray.clear();
        planInvoiceArray.clear();
        singlePrescData = new String[0];
        singlePrescBool = Boolean.FALSE;

        prescPPTableModel.setRowCount(0);
        prescPSTableModel.setRowCount(0);
        testPSTableModel.setRowCount(0);
        testTPTableModel.setRowCount(0);
        refPSTableModel.setRowCount(0);
        // invoiceTPTableModel.setRowCount(0);

        txtDocName.setText("");
        txtDocPID.setText("");
        txtDocAddr.setText("");
        txtDocHomeNum.setText("");
        txtDocMobileNum.setText("");

        txtPharmName.setText("");
        txtPharmPID.setText("");
        txtPharmAddr.setText("");
        txtPharmHomeNum.setText("");
        txtPharmMobileNum.setText("");

        txtLabName.setText("");
        txtLabPID.setText("");
        txtLabAddr.setText("");
        txtLabHomeNum.setText("");
        txtLabMobileNum.setText("");

        txtPlanSumName.setText("");
        txtPlanSumPID.setText("");
        txtPlanSumAddress.setText("");
        txtPlanSumMobilePhone.setText("");
        txtPlanSumHomePhone.setText("");

        txtPlanID.setText("");
        txtPolicyType.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");

        txtTotalUnpaid.setText("");
        txtTotalOverDue.setText("");

        txtPlanID.setText("");
        txtStartDate.setText("");
        txtPolicyType.setText("");
        txtEndDate.setText("");

        extendedBenefitsTableModel.setRowCount(0);
        invoiceHistoryGridTableModel.setRowCount(0);
        monthlyInvoiceSummaryTableModel.setRowCount(0);
    }

    private String getPIDfromName(ArrayList<ArrayList<String>> names, String nameTxt) {
        // Do a name search
        Object[] nameArr = new Object[names.size()];

        int row = 0;
        int index = 0;

        for(ArrayList<String> tuple : names) {
            nameArr[row] = tuple.get(0) + " " + tuple.get(1) + " - " + tuple.get(2);
            row++;
        }

        // Generate dialog
        String s = (String)JOptionPane.showInputDialog(frame,
                "Choose a patient with " + nameTxt + " in their name:",
                "Choose a Patient",
                JOptionPane.PLAIN_MESSAGE,
                null,
                nameArr,
                null);

        // Find the index
        row = 0;
        if (s != null) {
            for(Object obj : nameArr) {
                if(s.equals(nameArr[row])) {
                    index = row;
                }
                row++;
            }
            return names.get(index).get(2);
        }
        return "";
    }

    private void viewTestData(JTable table, DefaultTableModel tableModel) {
        JTextField dChol = new JTextField();
        JTextField dHDL = new JTextField();
        JTextField dLDL = new JTextField();
        JTextField dTrig = new JTextField();
        JTextField dWBcc = new JTextField();
        JTextField dRBcc = new JTextField();
        JTextField dHema = new JTextField();
        JTextField dPlate = new JTextField();
        JTextField dNRPer = new JTextField();
        JTextField dNRAbs = new JTextField();
        JTextField dSod = new JTextField();
        JTextField dGlu = new JTextField();
        JTextField dPhos = new JTextField();
        JTextField dLabHID = new JTextField();

        if(patientArray.size() > 0) {
            String name = patientArray.get(0) + " " + patientArray.get(1);

            int row = table.getSelectedRow();

            if(row >= 0) {
                String testID = tableModel.getValueAt(row, 0).toString();
                ArrayList<String> testData = hdb.findTestValues(testID);

                dChol.setEditable(false);
                dHDL.setEditable(false);
                dLDL.setEditable(false);
                dTrig.setEditable(false);
                dWBcc.setEditable(false);
                dRBcc.setEditable(false);
                dHema.setEditable(false);
                dPlate.setEditable(false);
                dNRPer.setEditable(false);
                dNRAbs.setEditable(false);
                dSod.setEditable(false);
                dGlu.setEditable(false);
                dPhos.setEditable(false);
                dLabHID.setEditable(false);

                dChol.setText(testData.get(0));
                dHDL.setText(testData.get(1));
                dLDL.setText(testData.get(2));
                dTrig.setText(testData.get(3));
                dWBcc.setText(testData.get(4));
                dRBcc.setText(testData.get(5));
                dHema.setText(testData.get(6));
                dPlate.setText(testData.get(7));
                dNRPer.setText(testData.get(8));
                dNRAbs.setText(testData.get(9));
                dSod.setText(testData.get(10));
                dGlu.setText(testData.get(11));
                dPhos.setText(testData.get(12));
                dLabHID.setText(testData.get(13));

                Object[] data = {"Patient: " + name, "Test ID: " + testID, " ",
                        "Cholesterol", dChol, "HDL Cholesterol", dHDL, "LDL Choleterol", dLDL,
                        "Triglycerides", dTrig, "White Blood Cell Count", dWBcc, "Red Blood Cell Count", dRBcc,
                        "Hematocrit", dHema, "Platelet Count", dPlate, "NRBC Percent", dNRPer,
                        "NRBC Absolute", dNRAbs, "Sodium", dSod, "Glucose", dGlu,
                        "Phosphorus", dPhos, "Lab Tech ID", dLabHID};

                String s = testData.get(0);
                JOptionPane.showMessageDialog(frame, data, "Test # " + testID + " for Patient " + name, JOptionPane.PLAIN_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(frame, "Please select a test from the Test table.", "Error",  JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, "No patient selected!", "Error",  JOptionPane.ERROR_MESSAGE);
        }
    }
    private int findSinglePrescription(String prescNum) {
        ArrayList<String> tuple = hdb.findPrescription(prescNum);

        if(tuple.size() > 0) {
            singlePrescData = new String[tuple.size()];

            patientArray = hdb.findPatient(hdb.findPIDfromPrescription(prescNum));

            // Clear the data tables
            prescPPTableModel.setRowCount(0);

            patientID = patientArray.get(2);
            String name = patientArray.get(0) + " " + patientArray.get(1);
            String addr = patientArray.get(3) + " " + patientArray.get(4) + " " + patientArray.get(6) + " " + patientArray.get(5);

            System.out.println(patientArray.get(2) + ", " + name);

            txtPharmName.setText(name);
            txtPharmPID.setText(patientArray.get(2));
            txtPharmAddr.setText(addr);
            txtPharmHomeNum.setText(patientArray.get(7));
            txtPharmMobileNum.setText(patientArray.get(8));

            int col = 0;
            if (tuple.size() > 0) {
                for (String s : tuple) {
                    singlePrescData[col] = s;
                    col++;
                }

                prescPPTableModel.addRow(singlePrescData);
            }
            return 1;
        }
        return 0;
    }

    private int findSingleTest(String testNum) {
        ArrayList<String> tuple = hdb.findTest(testNum);

        String[] data = new String[tuple.size()];

        if(tuple.size() > 0) {

            patientArray = hdb.findPatient(hdb.findPIDfromTest(testNum));

            // Clear the data tables
            testTPTableModel.setRowCount(0);

            patientID = patientArray.get(2);
            String name = patientArray.get(0) + " " + patientArray.get(1);
            String addr = patientArray.get(3) + " " + patientArray.get(4) + " " + patientArray.get(6) + " " + patientArray.get(5);

            System.out.println(patientArray.get(2) + ", " + name);

            txtLabName.setText(name);
            txtLabPID.setText(patientArray.get(2));
            txtLabAddr.setText(addr);
            txtLabHomeNum.setText(patientArray.get(8));
            txtLabMobileNum.setText(patientArray.get(9));

            int col = 0;
            if (tuple.size() > 0) {
                for (String s : tuple) {
                    data[col] = s;
                    col++;
                }

                testTPTableModel.addRow(data);
            }
            return 1;
        }
        return 0;
    }
    /**
     * Initializes the Oracle Login panel
     */
    private void setPanelOracleLogin() {
        panelOracleLogin = new JPanel();
        panelOracleLogin.setLayout(new GridBagLayout());
        panelOracleLogin.setEnabled(true);
        panelRoot.add(panelOracleLogin, "Card1");

        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(0);
        lbl.setHorizontalTextPosition(0);
        lbl.setText("Oracle Database Login");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelOracleLogin.add(lbl, gbc);

        lbl = new JLabel();
        lbl.setHorizontalAlignment(4);
        lbl.setText("Username  ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panelOracleLogin.add(lbl, gbc);

        usernameField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelOracleLogin.add(usernameField, gbc);

        lbl = new JLabel();
        lbl.setHorizontalAlignment(4);
        lbl.setText("Password  ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panelOracleLogin.add(lbl, gbc);

        passwordField = new JPasswordField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelOracleLogin.add(passwordField, gbc);

        JButton btnLogin = new JButton();
        btnLogin.setHorizontalTextPosition(0);
        btnLogin.setPreferredSize(new Dimension(50, 30));
        btnLogin.setText("Login");
        /* Login button action listener */
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                password = String.valueOf(passwordField.getPassword()); /* Not secure at all! */

                /* Error check for empty fields */
                if (username.equals("") || password.equals("")) {
                    System.out.println("Login credentials invalid");

                    JOptionPane.showMessageDialog(frame, "Login Failed: Username or Password missing!", "Login Failed Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (username.equals("1") || password.equals("1")) {

                    /* Bypass Oracle DB connection for local testing */
                    switchToUserSelectPanel();
                }
                else {
                    //System.out.println("Login credentials: " + username + " " + password);

                    hdb.setOracleCredentials(username, password);

                    /* Switch to next view only if database connection is made */
                    if (hdb.connectToDB(username, password)) {

                        /* Switch to User Class panel when login achieved */
                        switchToUserSelectPanel();
                    }
                    else {
                        System.out.println("Login Failed!");
                        JOptionPane.showMessageDialog(frame, "Login Failed: Database could not connect!", "Login Failed Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(5,0,0,0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelOracleLogin.add(btnLogin, gbc);


        JButton btnLauraLogin = new JButton();
        btnLauraLogin.setHorizontalTextPosition(0);
        btnLauraLogin.setPreferredSize(new Dimension(80, 30));
        btnLauraLogin.setText("Laura Login");
        /* Login button action listener */
        btnLauraLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = "ora_k1j8";
                password = "a30442115"; /* Fill in if you want */

                hdb.setOracleCredentials(username, password);

                /* Switch to next view only if database connection is made */
                if (hdb.connectToDB(username, password)) {

                    /* Switch to User Class panel when login achieved */
                    switchToUserSelectPanel();
                }
                else {
                    System.out.println("Login Failed!");
                    JOptionPane.showMessageDialog(frame, "Login Failed: Database could not connect!", "Login Failed Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.insets = new Insets(10,0,0,0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelOracleLogin.add(btnLauraLogin, gbc);

        JButton btnMichelleLogin = new JButton();
        btnMichelleLogin.setHorizontalTextPosition(0);
        btnMichelleLogin.setPreferredSize(new Dimension(80, 30));
        btnMichelleLogin.setText("Michelle Login");
        /* Login button action listener */
        btnMichelleLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = "ora_u4d9";
                password = "a32746133"; /* Fill in if you want */

                hdb.setOracleCredentials(username, password);

                /* Switch to next view only if database connection is made */
                if (hdb.connectToDB(username, password)) {

                    /* Switch to User Class panel when login achieved */
                    switchToUserSelectPanel();
                }
                else {
                    System.out.println("Login Failed!");
                    JOptionPane.showMessageDialog(frame, "Login Failed: Database could not connect!", "Login Failed Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.insets = new Insets(5,0,0,0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelOracleLogin.add(btnMichelleLogin, gbc);

        JButton btnJennaLogin = new JButton();
        btnJennaLogin.setHorizontalTextPosition(0);
        btnJennaLogin.setPreferredSize(new Dimension(80, 30));
        btnJennaLogin.setText("Jenna Login");
        /* Login button action listener */
        btnJennaLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = "ora_b3z9a";
                password = "a31823115"; /* Fill in if you want */

                hdb.setOracleCredentials(username, password);

                /* Switch to next view only if database connection is made */
                if (hdb.connectToDB(username, password)) {

                    /* Switch to User Class panel when login achieved */
                    switchToUserSelectPanel();
                }
                else {
                    System.out.println("Login Failed!");
                    JOptionPane.showMessageDialog(frame, "Login Failed: Database could not connect!", "Login Failed Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.insets = new Insets(5,0,0,0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelOracleLogin.add(btnJennaLogin, gbc);

        JButton btnJanLogin = new JButton();
        btnJanLogin.setHorizontalTextPosition(0);
        btnJanLogin.setPreferredSize(new Dimension(80, 30));
        btnJanLogin.setText("Jan Login");
        /* Login button action listener */
        btnJanLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = "ora_b3a0b";
                password = "a28912146";

                hdb.setOracleCredentials(username, password);

                /* Switch to next view only if database connection is made */
                if (hdb.connectToDB(username, password)) {

                    /* Switch to User Class panel when login achieved */
                    switchToUserSelectPanel();
                }
                else {
                    System.out.println("Login Failed!");
                    JOptionPane.showMessageDialog(frame, "Login Failed: Database could not connect!", "Login Failed Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.insets = new Insets(5,0,0,0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelOracleLogin.add(btnJanLogin, gbc);
    }

    /**
     * Initializes the main container for both User Class Selection and User Class Info panels
     */
    private void setPanelUserClass() {
        panelUserClass = new JPanel();
        panelUserClass.setLayout(new BorderLayout(0, 0));
        panelRoot.add(panelUserClass, "Card2");
    }

    /**
     * Initializes the User Class Selection panel
     */
    private void setPanelUserClassSelect() {
        panelUserClassSelect = new JPanel();
        panelUserClassSelect.setLayout(new FlowLayout());
        panelUserClass.add(panelUserClassSelect, BorderLayout.NORTH);

        JLabel lbl = new JLabel();
        lbl.setText("Select Page to View: ");

        btnConfirmUserClass = new JButton();
        /* Confirm User Class button action listener */
        btnConfirmUserClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userClass = cboxUserClass.getSelectedItem().toString();

                System.out.println();

                switch (userClass) {
                    case "Patient Summary":
                        System.out.println("Patient Summary");

                        clearPanelData();

                        /* Switch to Patient Summary panel */
                        panelPatientSummary.setVisible(true);
                        panelPrescription.setVisible(false);
                        panelTest.setVisible(false);
                        panelPlanSummary.setVisible(false);
                        panelInvoice.setVisible(false);
                        break;
                    case "Prescriptions":
                        System.out.println("Prescriptions");
                        clearPanelData();

                        /* Switch to Pharmacist Class panel */
                        panelPatientSummary.setVisible(false);
                        panelPrescription.setVisible(true);
                        panelTest.setVisible(false);
                        panelPlanSummary.setVisible(false);
                        panelInvoice.setVisible(false);
                        break;
                    case "Tests":
                        System.out.println("Tests");
                        clearPanelData();

                        /* Switch to Tests panel */
                        panelPatientSummary.setVisible(false);
                        panelPrescription.setVisible(false);
                        panelTest.setVisible(true);
                        panelPlanSummary.setVisible(false);
                        panelInvoice.setVisible(false);
                        break;
                    case "Plan Summary":
                        System.out.println("Plan Summary");
                        clearPanelData();

                        /* Switch to Plan Summary panel */
                        panelPatientSummary.setVisible(false);
                        panelPrescription.setVisible(false);
                        panelTest.setVisible(false);
                        panelPlanSummary.setVisible(true);
                        panelInvoice.setVisible(false);
                        break;
                    default:
                        panelPatientSummary.setVisible(false);
                        panelPrescription.setVisible(false);
                        panelTest.setVisible(false);
                        panelPlanSummary.setVisible(false);
                        panelInvoice.setVisible(false);
                        panelEmpty.setVisible(true);
                        break;
                }
            }
        });
        btnConfirmUserClass.setText("Confirm");

        JButton btnClear = new JButton();
        /* Clear Selections button action listener */
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPanelData();

                panelEmpty.setVisible(true);
                panelPatientSummary.setVisible(false);
                panelPrescription.setVisible(false);
                panelTest.setVisible(false);
                panelPlanSummary.setVisible(false);
                panelInvoice.setVisible(false);

                /* TODO see if anything else needs to be done when clear is pressed */
            }
        });
        btnClear.setText("Clear Selection");

        panelUserClassSelect.add(lbl);
        panelUserClassSelect.add(cboxUserClass);
        panelUserClassSelect.add(btnConfirmUserClass);
        panelUserClassSelect.add(btnClear);

    }

    /**
     * Initializes the User Class Info panel
     */
    private void setPanelUserClassInfo(){
        panelUserClassInfo = new JPanel();
        panelUserClassInfo.setLayout(new CardLayout(0, 0));
        panelUserClass.add(panelUserClassInfo, BorderLayout.CENTER);

        panelPatientSummary = new JPanel();
        panelPatientSummary.setLayout(new GridBagLayout());
        panelUserClassInfo.add(panelPatientSummary, "Card2");

        panelPrescription = new JPanel();
        panelPrescription.setLayout(new GridBagLayout());
        panelUserClassInfo.add(panelPrescription, "Card3");

        panelTest = new JPanel();
        panelTest.setLayout(new GridBagLayout());
        panelUserClassInfo.add(panelTest, "Card4");

        panelPlanSummary = new JPanel();
        panelPlanSummary.setLayout(new GridBagLayout());
        panelUserClassInfo.add(panelPlanSummary, "Card5");

        panelInvoice = new JPanel();
        panelInvoice.setLayout(new GridBagLayout());
        panelUserClassInfo.add(panelInvoice, "Card6");

        panelEmpty = new JPanel();
        panelUserClassInfo.add(panelEmpty, "Card7");
    }

    private void printTuples(ArrayList<ArrayList<String>> tuples){
        StringBuilder sb = new StringBuilder();
        for (ArrayList<String> list : tuples){
          for (String s : list){
            sb.append(" '");
            sb.append(s);
            sb.append("', ");
          }
          sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private void printTuple(ArrayList<String> tuple){
        StringBuilder sb = new StringBuilder();
          for (String s : tuple){
            sb.append(" '");
            sb.append(s);
            sb.append("', ");
          }
        System.out.println(sb.toString());
    }

    private String[][] createData(ArrayList<ArrayList<String>> tuples) {
        String[][] data = new String[tuples.size()][tuples.get(0).size()];

        int row = 0;
        int col = 0;

        /* Using same code as printTuples */
        for(ArrayList<String> tuple : tuples) {
            for (String s : tuple) {
                data[row][col] = s;
                col++;
            }
		    col = 0;
            row++;
        }

       	System.out.println(Arrays.deepToString(data));

        return data;
    }

    private void updateTable(ArrayList<ArrayList<String>> arr, DefaultTableModel tableModel) {
        //printTuples(arr);
        if(arr.size() > 0)
        {
            String[][] data = createData(arr);
            for(int row = 0; row < data.length; row++)
            {
                tableModel.addRow(data[row]);
            }
        }
    }

    private void setPanelPatientSummary() {
        JLabel lbl = new JLabel("Patient Summary");
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        lbl.setHorizontalAlignment(SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelPatientSummary.add(lbl, gbc);

        /* Row 1 */
        panelPatientSummaryFinder = new JPanel();
        panelPatientSummaryFinder.setLayout(new FlowLayout());
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelPatientSummary.add(panelPatientSummaryFinder, gbc);

        /* Row 2 */
        lbl = new JLabel("Personal Information", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 2;
        panelPatientSummary.add(lbl, gbc);

        /* Row 3 */
        panelPatientSummaryInfo = new JPanel();
        panelPatientSummaryInfo.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 3;
        panelPatientSummary.add(panelPatientSummaryInfo, gbc);

        JButton btnDeletePatient = new JButton();
        btnDeletePatient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(patientArray.size() > 0) {
                    String name = patientArray.get(0) + " " + patientArray.get(1);

                    if (patientID.equals("")) {
                        JOptionPane.showMessageDialog(frame,
                                "Patient ID does not exist",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    int resp = JOptionPane.showConfirmDialog(
                            frame,
                            "Are you sure you would like to delete " + name + "?",
                            "Delete Patient " + name,
                            JOptionPane.YES_NO_OPTION);

                    if(resp == JOptionPane.YES_OPTION)
                    {
                        hdb.deletePatient(patientID);
                        clearPanelData();
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame,
                            "No patient selected!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnDeletePatient.setText("Delete Patient");

        JPanel panelPatientSummaryPatBtn = new JPanel();
        panelPatientSummaryPatBtn.setLayout(new FlowLayout());
        panelPatientSummaryPatBtn.add(btnDeletePatient);
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 4;
        panelPatientSummary.add(panelPatientSummaryPatBtn, gbc);

        /* Row 4 */
        lbl = new JLabel("Prescriptions", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 5;
        panelPatientSummary.add(lbl, gbc);


        /* Row 5 */
        String colsP[] = {"ID", "Date", "Medication", "Dosage", "Dosage Unit", "Quantity", "Filled Date", "Filled?"};
        String data[][] = {};
        prescPSTableModel = new DefaultTableModel(data, colsP);
        prescPSTable = new JTable(prescPSTableModel);

        panePatientSummaryPrescriptions = new JScrollPane(prescPSTable);
        panePatientSummaryPrescriptions.setLayout(new ScrollPaneLayout());

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 6;
        panelPatientSummary.add(panePatientSummaryPrescriptions, gbc);

        /* Row 6 */
        JButton btnCreatePrescription = new JButton();
        btnCreatePrescription.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField dMedication = new JTextField();
                JTextField dDosage = new JTextField();
                JTextField dQty = new JTextField();

                if(patientArray.size() > 0) {
                    String name = patientArray.get(0) + " " + patientArray.get(1);

                    Object[] fields = {"Patient: " + name, "Medication", dMedication, "Dosage", dDosage, "Quantity", dQty};

                    int resp = JOptionPane.showConfirmDialog(frame, fields, "Create prescription for " + name, JOptionPane.OK_CANCEL_OPTION);

                    if(resp == JOptionPane.OK_OPTION) {
                        String medication = dMedication.getText();
                        String dosage = dDosage.getText();
                        String qty = dQty.getText();
                        hdb.createPrescription(medication, dosage, qty, patientID, drHID);

                        // Clear and update the table with new data
                        prescPSTableModel.setRowCount(0);
                        prescriptions = hdb.getPrescriptions(patientID);
                        updateTable(prescriptions, prescPSTableModel);
                    }
                    else {
                        System.out.println("No values entered");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "No patient selected!", "Error",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCreatePrescription.setText("Create New Prescription");

        JPanel panelPatientSummaryPresBtn = new JPanel();
        panelPatientSummaryPresBtn.setLayout(new FlowLayout());
        panelPatientSummaryPresBtn.add(btnCreatePrescription);
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 7;
        panelPatientSummary.add(panelPatientSummaryPresBtn, gbc);

        /* Row 6 */
        lbl = new JLabel("Tests", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 8;
        panelPatientSummary.add(lbl, gbc);

        /* Row 7 */
        String colsT[] = {"Test ID", "Ordered Date", "Performed Date", "Completed?"};
        testPSTableModel = new DefaultTableModel(data, colsT);
        testPSTable = new JTable(testPSTableModel);

        panePatientSummaryTests = new JScrollPane(testPSTable);
        panePatientSummaryTests.setLayout(new ScrollPaneLayout());

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 9;
        panelPatientSummary.add(panePatientSummaryTests, gbc);

        JButton btnCreateTest= new JButton();
        btnCreateTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(patientArray.size() > 0) {
                    String name = patientArray.get(0) + " " + patientArray.get(1);

                    if(hdb.createTest(patientID, drHID)) {

                        // Clear and update the table with new data
                        testPSTableModel.setRowCount(0);
                        tests = hdb.getTests(patientID);
                        updateTable(tests, testPSTableModel);
                        JOptionPane.showMessageDialog(frame, "Test created for " + name, "Create test for " + name,  JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "Failed to create test for " + name, "Create test for " + name,  JOptionPane.ERROR_MESSAGE);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "No patient selected!", "Error",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCreateTest.setText("Create New Test");

        JButton btnViewTest= new JButton();
        btnViewTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTestData(testPSTable, testPSTableModel);
            }
        });
        btnViewTest.setText("View Selected Test");

        JPanel panelPatientSummaryTestsBtn = new JPanel();
        panelPatientSummaryTestsBtn.setLayout(new FlowLayout());
        panelPatientSummaryTestsBtn.add(btnCreateTest);
        panelPatientSummaryTestsBtn.add(btnViewTest);
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 10;
        panelPatientSummary.add(panelPatientSummaryTestsBtn, gbc);

        /* Row 8 */
        lbl = new JLabel("Referrals", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 11;
        panelPatientSummary.add(lbl, gbc);

        /* Row 9 */
        String colsR[] = {"First Name", "Last Name", "Specialization", "Date"};
        refPSTableModel = new DefaultTableModel(data, colsR);
        refPSTable = new JTable(refPSTableModel);

        panePatientSummaryReferrals = new JScrollPane(refPSTable);
        panePatientSummaryReferrals.setLayout(new ScrollPaneLayout());

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 12;
        panelPatientSummary.add(panePatientSummaryReferrals, gbc);

        JButton btnCreateReferral = new JButton();
        btnCreateReferral.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField dDrHID = new JTextField();

                if(patientArray.size() > 0) {
                    String name = patientArray.get(0) + " " + patientArray.get(1);

                    Object[] fields = {"Patient: " + name, "Doctor ID for Referral", dDrHID};

                    int resp = JOptionPane.showConfirmDialog(frame, fields, "Create referral for " + name, JOptionPane.OK_CANCEL_OPTION);

                    if(resp == JOptionPane.OK_OPTION) {
                        String drHIDInput = dDrHID.getText();
                        hdb.createReferral(patientID, drHID, drHIDInput);
                        System.out.println(drHIDInput);

                        // Clear and update the table with new data
                        refPSTableModel.setRowCount(0);
                        referrals = hdb.getReferrals(patientID);
                        updateTable(referrals, refPSTableModel);
                    }
                    else {
                        System.out.println("No values entered");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "No patient selected!", "Error",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCreateReferral.setText("Create New Referral");

        /* Row 10 */
        JPanel panelPatientSummaryRefBtn = new JPanel();
        panelPatientSummaryRefBtn.setLayout(new FlowLayout());
        panelPatientSummaryRefBtn.add(btnCreateReferral);
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 13;
        panelPatientSummary.add(panelPatientSummaryRefBtn, gbc);
    }

    private void setPanelPatientSummaryFinder() {
        JLabel lblPID = new JLabel("PID:", SwingConstants.LEADING);
        panelPatientSummaryFinder.add(lblPID, gbc);

        JTextField txtPID = new JTextField(10);
        panelPatientSummaryFinder.add(txtPID);

        JLabel lblName = new JLabel("Name:", SwingConstants.LEADING);
        panelPatientSummaryFinder.add(lblName);

        JTextField txtName = new JTextField(12);
        panelPatientSummaryFinder.add(txtName);

        JButton btnfindPatient = new JButton();
        btnfindPatient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String pidTxt = txtPID.getText();
                String nameTxt = txtName.getText();

                System.out.println(pidTxt + " " + nameTxt);

                if(pidTxt.equals("") && !nameTxt.isEmpty())
                {
                    ArrayList<ArrayList<String>> names = hdb.getPatients(nameTxt);
                    pidTxt = getPIDfromName(names, nameTxt);

                    patientArray = hdb.findPatient(pidTxt);
                }
                else if(!pidTxt.isEmpty()) {
                    patientArray = hdb.findPatient(pidTxt);
                }

                if(patientArray.size() > 0) {

                    // Clear the data tables
                    prescPSTableModel.setRowCount(0);
                    testPSTableModel.setRowCount(0);
                    refPSTableModel.setRowCount(0);

                    patientID = patientArray.get(2);
                    String name = patientArray.get(0) + " " + patientArray.get(1);
                    String addr = patientArray.get(3) + " " + patientArray.get(4) + " " + patientArray.get(6) + " " + patientArray.get(5);

                    System.out.println(patientArray.get(2) + ", " + name);

                    txtDocName.setText(name);
                    txtDocPID.setText(patientArray.get(2));
                    txtDocAddr.setText(addr);
                    txtDocHomeNum.setText(patientArray.get(8));
                    txtDocMobileNum.setText(patientArray.get(9));

                    prescriptions = hdb.getPrescriptions(patientArray.get(2));
                    printTuples(prescriptions);
                    updateTable(prescriptions, prescPSTableModel);

                    tests = hdb.getTests(patientArray.get(2));
                    printTuples(tests);
                    updateTable(tests, testPSTableModel);

                    referrals = hdb.getReferrals(patientArray.get(2));
                    printTuples(referrals);
                    updateTable(referrals, refPSTableModel);

                    txtPID.setText("");
                    txtName.setText("");
                }
                else {
                    txtPID.setText("");
                    txtName.setText("");

                    // Clear the data tables
                    clearPanelData();

                    JOptionPane.showMessageDialog(frame, "Patient not found!", "Invalid Patient Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnfindPatient.setText("Find Patient");
        panelPatientSummaryFinder.add(btnfindPatient);
    }
    private void setPanelPatientSummaryInfo() {
        JLabel lbl = new JLabel("Name");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 3;
        panelPatientSummaryInfo.add(lbl, gbc);

        txtDocName = new JTextField(12);
        txtDocName.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 3;
        panelPatientSummaryInfo.add(txtDocName, gbc);

        lbl = new JLabel("PID");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 3;
        panelPatientSummaryInfo.add(lbl, gbc);

        txtDocPID = new JTextField(10);
        txtDocPID.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 3;
        panelPatientSummaryInfo.add(txtDocPID, gbc);

        lbl = new JLabel("Address");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelPatientSummaryInfo.add(lbl, gbc);

        txtDocAddr = new JTextField(26);
        txtDocAddr.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 4;
        panelPatientSummaryInfo.add(txtDocAddr, gbc);

        lbl = new JLabel("Mobile Num");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 6;
        panelPatientSummaryInfo.add(lbl, gbc);

        txtDocMobileNum = new JTextField(10);
        txtDocMobileNum.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 1; gbc.gridy = 6;
        panelPatientSummaryInfo.add(txtDocMobileNum, gbc);

        lbl = new JLabel("Home Num");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 6;
        panelPatientSummaryInfo.add(lbl, gbc);

        txtDocHomeNum = new JTextField(10);
        txtDocHomeNum.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 6;
        panelPatientSummaryInfo.add(txtDocHomeNum, gbc);
    }

    private void setPanelPrescription() {
        JLabel lbl = new JLabel("Prescription Information");
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        lbl.setHorizontalAlignment(SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelPrescription.add(lbl, gbc);

        /* Row 1 */
        panelPrescriptionFinder = new JPanel();
        panelPrescriptionFinder.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 1;
        panelPrescription.add(panelPrescriptionFinder, gbc);

        /* Row 2 */
        lbl = new JLabel("Personal Information", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 2;
        panelPrescription.add(lbl, gbc);

        /* Row 3 */
        panelPrescriptionInfo = new JPanel();
        panelPrescriptionInfo.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 3;
        panelPrescription.add(panelPrescriptionInfo, gbc);

        /* Row 4 */
        lbl = new JLabel("Prescriptions", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 4;
        panelPrescription.add(lbl, gbc);


        /* Row 5 */
        String cols[] = {"ID", "Date", "Medication", "Dosage", "Dosage Unit", "Quantity", "Filled Date", "Filled?"};
        String data[][] = {};
        prescPPTableModel = new DefaultTableModel(data, cols);
        prescPPTable = new JTable(prescPPTableModel);

        panePrescriptionPrescriptions = new JScrollPane(prescPPTable);
        panePrescriptionPrescriptions.setLayout(new ScrollPaneLayout());

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 5;
        panelPrescription.add(panePrescriptionPrescriptions, gbc);

        JButton btnMarkFilled= new JButton();
        btnMarkFilled.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField pharmHIDField = new JTextField();
                if(patientArray.size() > 0) {
                    int row = prescPPTable.getSelectedRow();

                    String prescID = prescPPTableModel.getValueAt(row, 0).toString();

                    Object[] fields = {"Please enter your Pharmacist ID:", pharmHIDField};

                    int resp = JOptionPane.showConfirmDialog(frame, fields, "Mark " + prescID + " as Filled", JOptionPane.OK_CANCEL_OPTION);

                    if(resp == JOptionPane.OK_OPTION)
                    {
                        if(pharmHIDField.getText().equals("")) {
                            JOptionPane.showMessageDialog(frame, "No Pharmacist ID entered!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            hdb.updatePrescription(pharmHIDField.getText(), prescID);
                            prescPPTableModel.setRowCount(0);

                            if(singlePrescBool)
                            {
                                findSinglePrescription(prescID);
                            } else {
                                updateTable(hdb.getPrescriptions(patientID), prescPPTableModel);
                            }
                        }
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "No patient selected!", "Error",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnMarkFilled.setText("Mark as Filled");


        JPanel panelPrescriptionBtns = new JPanel();
        panelPrescriptionBtns.setLayout(new FlowLayout());
        panelPrescriptionBtns.add(btnMarkFilled);
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 10;
        panelPrescription.add(panelPrescriptionBtns, gbc);
    }



    private void setPanelPrescriptionFinder() {
        JTextField txtPID = new JTextField(10);
        JTextField txtName = new JTextField(12);

        JLabel lbl = new JLabel("Prescription #:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0,0,0,0);
        gbc.gridx = 0; gbc.gridy = 0;
        panelPrescriptionFinder.add(lbl, gbc);

        JTextField txtPrescNum = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0,5,0,0);
        gbc.gridx = 1; gbc.gridy = 0;
        panelPrescriptionFinder.add(txtPrescNum, gbc);

        JButton btnFindPrescNum = new JButton();
        btnFindPrescNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String prescNum = txtPrescNum.getText();

                if(findSinglePrescription(prescNum) == 1) {
                    singlePrescBool = Boolean.TRUE;
                }
                else {
                    txtPrescNum.setText("");
                    txtPID.setText("");
                    txtName.setText("");
                    singlePrescBool = Boolean.FALSE;

                    // Clear the data tables
                    clearPanelData();

                    JOptionPane.showMessageDialog(frame, "Patient not found!", "Invalid Patient Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFindPrescNum.setText("Find by Prescription Number");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0,5,0,0);
        gbc.gridx = 4; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrescriptionFinder.add(btnFindPrescNum, gbc);

        lbl = new JLabel("PID:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,0,0,0);
        gbc.gridx = 0; gbc.gridy = 1;
        panelPrescriptionFinder.add(lbl, gbc);

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,0,0);
        gbc.gridx = 1; gbc.gridy = 1;
        panelPrescriptionFinder.add(txtPID, gbc);

        lbl = new JLabel("Name:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,0,0);
        gbc.gridx = 2; gbc.gridy = 1;
        panelPrescriptionFinder.add(lbl, gbc);

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,0,0);
        gbc.gridx = 3; gbc.gridy = 1;
        panelPrescriptionFinder.add(txtName, gbc);

        JButton btnFindPrescPatient = new JButton();
        btnFindPrescPatient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pidTxt = txtPID.getText();
                String nameTxt = txtName.getText();

                System.out.println(pidTxt + " " + nameTxt);

                if(pidTxt.equals("") && !nameTxt.isEmpty())
                {
                    ArrayList<ArrayList<String>> names = hdb.getPatients(nameTxt);
                    pidTxt = getPIDfromName(names, nameTxt);

                    patientArray = hdb.findPatient(pidTxt);
                }
                else if(!pidTxt.isEmpty()) {
                    patientArray = hdb.findPatient(pidTxt);
                }

                if(patientArray.size() > 0) {

                    // Clear the data tables
                    prescPPTableModel.setRowCount(0);

                    patientID = patientArray.get(2);
                    String name = patientArray.get(0) + " " + patientArray.get(1);
                    String addr = patientArray.get(3) + " " + patientArray.get(4) + " " + patientArray.get(6) + " " + patientArray.get(5);

                    System.out.println(patientArray.get(2) + ", " + name);

                    txtPharmName.setText(name);
                    txtPharmPID.setText(patientArray.get(2));
                    txtPharmAddr.setText(addr);
                    txtPharmHomeNum.setText(patientArray.get(8));
                    txtPharmMobileNum.setText(patientArray.get(9));

                    prescriptions = hdb.getPrescriptions(patientArray.get(2));
                    printTuples(prescriptions);
                    if(prescriptions.size() > 0)
                    {
                        String[][] data = createData(prescriptions);
                        for(int row = 0; row < data.length; row++)
                        {
                            prescPPTableModel.addRow(data[row]);
                        }
                    }

                    txtPrescNum.setText("");
                    txtPID.setText("");
                    txtName.setText("");
                    singlePrescBool = Boolean.FALSE;
                }
                else {
                    txtPrescNum.setText("");
                    txtPID.setText("");
                    txtName.setText("");
                    singlePrescBool = Boolean.FALSE;

                    // Clear the data tables
                    clearPanelData();

                    JOptionPane.showMessageDialog(frame, "Patient not found!", "Invalid Patient Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFindPrescPatient.setText("Find Patient");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,0,0);
        gbc.gridx = 4; gbc.gridy = 1;
        panelPrescriptionFinder.add(btnFindPrescPatient, gbc);
    }
    private void setPanelPrescriptionInfo() {
        JLabel lbl = new JLabel("Name");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 3;
        panelPrescriptionInfo.add(lbl, gbc);

        txtPharmName = new JTextField(12);
        txtPharmName.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 3;
        panelPrescriptionInfo.add(txtPharmName, gbc);

        lbl = new JLabel("PID");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 3;
        panelPrescriptionInfo.add(lbl, gbc);

        txtPharmPID = new JTextField(10);
        txtPharmPID.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 3;
        panelPrescriptionInfo.add(txtPharmPID, gbc);

        lbl = new JLabel("Address");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 4;
        panelPrescriptionInfo.add(lbl, gbc);

        txtPharmAddr = new JTextField(26);
        txtPharmAddr.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 4;
        panelPrescriptionInfo.add(txtPharmAddr, gbc);

        lbl = new JLabel("Mobile Num");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 6;
        panelPrescriptionInfo.add(lbl, gbc);

        txtPharmMobileNum = new JTextField(10);
        txtPharmMobileNum.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 1; gbc.gridy = 6;
        panelPrescriptionInfo.add(txtPharmMobileNum, gbc);

        lbl = new JLabel("Home Num");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 6;
        panelPrescriptionInfo.add(lbl, gbc);

        txtPharmHomeNum = new JTextField(10);
        txtPharmHomeNum.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 6;
        panelPrescriptionInfo.add(txtPharmHomeNum, gbc);
    }

    private void setPanelTest() {
        JLabel lbl = new JLabel("Test Information");
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        lbl.setHorizontalAlignment(SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelTest.add(lbl, gbc);

        /* Row 1 */
        panelTestFinder = new JPanel();
        panelTestFinder.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelTest.add(panelTestFinder, gbc);

        /* Row 2 */
        lbl = new JLabel("Personal Information", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 2;
        panelTest.add(lbl, gbc);

        /* Row 3 */
        panelTestInfo = new JPanel();
        panelTestInfo.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelTest.add(panelTestInfo, gbc);

        /* Row 4 */
        lbl = new JLabel("Tests", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 4;
        panelTest.add(lbl, gbc);

        /* Row 5 */
        String cols[] = {"Test ID", "Ordered Date", "Performed Date", "Completed?"};
        String data[][] = {};
        testTPTableModel = new DefaultTableModel(data, cols);
        testTPTable = new JTable(testTPTableModel);

        paneTestTests = new JScrollPane(testTPTable);
        paneTestTests.setLayout(new ScrollPaneLayout());

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 5;
        panelTest.add(paneTestTests, gbc);

        /* Row 6 */
        panelTestActions = new JPanel();
        panelTestActions.setLayout(new FlowLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 6;
        panelTest.add(panelTestActions, gbc);
    }

    private void setPanelTestFinder() {
        JTextField txtPID = new JTextField(10);
        JTextField txtName = new JTextField(12);

        JLabel lbl = new JLabel("Test #:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0,0,0,0);
        gbc.gridx = 0; gbc.gridy = 0;
        panelTestFinder.add(lbl, gbc);

        JTextField txtTest = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0,5,0,0);
        gbc.gridx = 1; gbc.gridy = 0;
        panelTestFinder.add(txtTest, gbc);

        JButton btnFindTestNum = new JButton();
        btnFindTestNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String testNum = txtTest.getText();

                if(findSingleTest(testNum) == 1) {
                    txtTest.setText("");
                    txtPID.setText("");
                    txtName.setText("");
                }
                else {
                    txtTest.setText("");
                    txtPID.setText("");
                    txtName.setText("");

                    // Clear the data tables
                    clearPanelData();

                    JOptionPane.showMessageDialog(frame, "Patient not found!", "Invalid Patient Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFindTestNum.setText("Find by Test Number");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0,5,0,0);
        gbc.gridx = 4; gbc.gridy = 0;
        panelTestFinder.add(btnFindTestNum, gbc);

        lbl = new JLabel("PID:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,0,0,0);
        gbc.gridx = 0; gbc.gridy = 1;
        panelTestFinder.add(lbl, gbc);


        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,0,0);
        gbc.gridx = 1; gbc.gridy = 1;
        panelTestFinder.add(txtPID, gbc);

        lbl = new JLabel("Name:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,0,0);
        gbc.gridx = 2; gbc.gridy = 1;
        panelTestFinder.add(lbl, gbc);

        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,0,0);
        gbc.gridx = 3; gbc.gridy = 1;
        panelTestFinder.add(txtName, gbc);

        JButton btnFindTestPID = new JButton();
        btnFindTestPID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pidTxt = txtPID.getText();
                String nameTxt = txtName.getText();

                System.out.println(pidTxt + " " + nameTxt);

                if(pidTxt.equals("") && !nameTxt.isEmpty())
                {
                    ArrayList<ArrayList<String>> names = hdb.getPatients(nameTxt);
                    pidTxt = getPIDfromName(names, nameTxt);
                    // str != null && !str.isEmpty()
                    if (!pidTxt.equals("")) {
                        patientArray = hdb.findPatient(pidTxt);
                    }
                }
                else if(!pidTxt.equals("")) {
                    patientArray = hdb.findPatient(pidTxt);
                }

                if(patientArray.size() > 0) {

                    // Clear the data tables
                    testTPTableModel.setRowCount(0);

                    patientID = patientArray.get(2);
                    String name = patientArray.get(0) + " " + patientArray.get(1);
                    String addr = patientArray.get(3) + " " + patientArray.get(4) + " " + patientArray.get(6) + " " + patientArray.get(5);

                    System.out.println(patientArray.get(2) + ", " + name);

                    txtLabName.setText(name);
                    txtLabPID.setText(patientArray.get(2));
                    txtLabAddr.setText(addr);
                    txtLabHomeNum.setText(patientArray.get(7));
                    txtLabMobileNum.setText(patientArray.get(8));

                    tests = hdb.getTests(patientArray.get(2));
                    printTuples(tests);
                    if(tests.size() > 0)
                    {
                        String[][] data = createData(tests);
                        for(int row = 0; row < data.length; row++)
                        {
                            testTPTableModel.addRow(data[row]);
                        }
                    }
                    txtTest.setText("");
                    txtPID.setText("");
                    txtName.setText("");
                }
                else {
                    txtTest.setText("");
                    txtPID.setText("");
                    txtName.setText("");

                    // Clear the data tables
                    clearPanelData();

                    JOptionPane.showMessageDialog(frame, "Patient not found!", "Invalid Patient Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFindTestPID.setText("Find Patient");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5,5,0,0);
        gbc.gridx = 4; gbc.gridy = 1;
        panelTestFinder.add(btnFindTestPID, gbc);
    }
    private void setPanelTestInfo() {
        JLabel lbl = new JLabel("Name");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 3;
        panelTestInfo.add(lbl, gbc);

        txtLabName = new JTextField(12);
        txtLabName.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 3;
        panelTestInfo.add(txtLabName, gbc);

        lbl = new JLabel("PID");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 3;
        panelTestInfo.add(lbl, gbc);

        txtLabPID = new JTextField(10);
        txtLabPID.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 3;
        panelTestInfo.add(txtLabPID, gbc);

        lbl = new JLabel("Address");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 4;
        panelTestInfo.add(lbl, gbc);

        txtLabAddr = new JTextField(26);
        txtLabAddr.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 4;
        panelTestInfo.add(txtLabAddr, gbc);

        lbl = new JLabel("Mobile Num");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 6;
        panelTestInfo.add(lbl, gbc);

        txtLabMobileNum = new JTextField(10);
        txtLabMobileNum.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 1; gbc.gridy = 6;
        panelTestInfo.add(txtLabMobileNum, gbc);

        lbl = new JLabel("Home Num");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 6;
        panelTestInfo.add(lbl, gbc);

        txtLabHomeNum = new JTextField(10);
        txtLabHomeNum.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 6;
        panelTestInfo.add(txtLabHomeNum, gbc);

    }

    private void setPanelTestActions() {
        JButton btnFillTest= new JButton();
        btnFillTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField dChol = new JTextField();
                JTextField dHDL = new JTextField();
                JTextField dLDL = new JTextField();
                JTextField dTrig = new JTextField();
                JTextField dWBcc = new JTextField();
                JTextField dRBcc = new JTextField();
                JTextField dHema = new JTextField();
                JTextField dPlate = new JTextField();
                JTextField dNRPer = new JTextField();
                JTextField dNRAbs = new JTextField();
                JTextField dSod = new JTextField();
                JTextField dGlu = new JTextField();
                JTextField dPhos = new JTextField();
                JTextField dLabHID = new JTextField();

                if(patientArray.size() > 0)
                {
                    int row = testTPTable.getSelectedRow();
                    String testID = testTPTableModel.getValueAt(row, 0).toString();

                    String name = patientArray.get(0) + " " + patientArray.get(1);

                    Object[] fields = {"Patient: " + name, "Test ID: " + testID, "Cholesterol", dChol, "HDL Cholesterol", dHDL, "LDL Choleterol", dLDL,
                            "Triglycerides", dTrig, "White Blood Cell Count", dWBcc, "Red Blood Cell Count", dRBcc,
                            "Hematocrit", dHema, "Platelet Count", dPlate, "NRBC Percent", dNRPer,
                            "NRBC Absolute", dNRAbs, "Sodium", dSod, "Glucose", dGlu,
                            "Phosphorus", dPhos, "Lab Tech ID", dLabHID};

                    int resp = JOptionPane.showConfirmDialog(frame, fields, "Edit Test for " + name, JOptionPane.OK_CANCEL_OPTION);

                    if(resp == JOptionPane.OK_OPTION) {
                        System.out.println(testID+ " " +dChol.getText()+ " " +dHDL.getText()+ " " +dLDL.getText()+ " " +dTrig.getText()
                                        + " " +dWBcc.getText()+ " " + dRBcc.getText()+ " " +dHema.getText()+ " " +dPlate.getText()
                                        + " " +dNRPer.getText() + " " + dNRAbs.getText()+ " " +dSod.getText()+ " " +dGlu.getText()
                                + " " +dPhos.getText()+ " " + dLabHID.getText());
                        if(hdb.updateTest(testID,
                                dChol.getText(), dHDL.getText(),dLDL.getText(), dTrig.getText(),
                                dWBcc.getText(), dRBcc.getText(), dHema.getText(), dPlate.getText(),
                                dNRPer.getText(), dNRAbs.getText(), dSod.getText(), dGlu.getText(),
                                dPhos.getText(), dLabHID.getText())) {
                            JOptionPane.showMessageDialog(frame, "Test edited for " + name + "\nTest ID: " + testID,
                                    "Edit Test for " + name,  JOptionPane.INFORMATION_MESSAGE);

                            findSingleTest(testID);
                        }
                        else {
                            JOptionPane.showMessageDialog(frame, "Failed to edit test for " + name + "\nTest ID: " + testID,
                                    "Edit Test for " + name,  JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else {
                        System.out.println("No value entered");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "No patient selected!", "Error",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFillTest.setText("Fill Out Test");
        panelTestActions.add(btnFillTest);

        JButton btnViewTest= new JButton();
        btnViewTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewTestData(testTPTable, testTPTableModel);
            }
        });
        btnViewTest.setText("View Selected Test");
        panelTestActions.add(btnViewTest);
    }

    private void setPanelInvoice() {
        JLabel lbl = new JLabel("Invoice");
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        lbl.setHorizontalAlignment(SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelInvoice.add(lbl, gbc);

        /* Row 1 */
        panelInvoiceFinder = new JPanel();
        panelInvoiceFinder.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 1;
        panelInvoice.add(panelInvoiceFinder, gbc);

        /* Row 2 */
        lbl = new JLabel("Invoice Information", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 2;
        panelInvoice.add(lbl, gbc);

        /* Row 3 */
        panelInvoiceInfo = new JPanel();
        panelInvoiceInfo.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 3;
        panelInvoice.add(panelInvoiceInfo, gbc);

        /* Row 4 */
        lbl = new JLabel("Submit Information", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 4;
        panelInvoice.add(lbl, gbc);

        /* Row 5 */
        panelInvoiceSubmit = new JPanel();
        panelInvoiceSubmit.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 5;
        panelInvoice.add(panelInvoiceSubmit, gbc);

        /* Bottom Row, fill the space */
        JPanel panel = new JPanel();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 6;
        panelInvoice.add(panel, gbc);

    }
    private void setPanelInvoiceFinder() {
        JLabel lbl = new JLabel("HID:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelInvoiceFinder.add(lbl, gbc);

        JTextField txtHID = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 0;
        panelInvoiceFinder.add(txtHID, gbc);

        lbl = new JLabel("Staff Name:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 2; gbc.gridy = 0;
        panelInvoiceFinder.add(lbl, gbc);

        JTextField txtStaffName = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 3; gbc.gridy = 0;
        panelInvoiceFinder.add(txtStaffName, gbc);

        lbl = new JLabel("Invoice #:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 4; gbc.gridy = 0;
        panelInvoiceFinder.add(lbl, gbc);

        JTextField txtInvoiceNum = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 5; gbc.gridy = 0;
        panelInvoiceFinder.add(txtInvoiceNum, gbc);

        lbl = new JLabel("PID:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 1;
        panelInvoiceFinder.add(lbl, gbc);

        JTextField txtPID = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 1;
        panelInvoiceFinder.add(txtPID, gbc);

        lbl = new JLabel("Patient Name:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 2; gbc.gridy = 1;
        panelInvoiceFinder.add(lbl, gbc);

        JTextField txtPatientName = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 3; gbc.gridy = 1;
        panelInvoiceFinder.add(txtPatientName, gbc);

        lbl = new JLabel("Plan #:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 4; gbc.gridy = 1;
        panelInvoiceFinder.add(lbl, gbc);

        JTextField txtPlanNum = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 5; gbc.gridy = 1;
        panelInvoiceFinder.add(txtPlanNum, gbc);

        JButton btnFindInvoice = new JButton();
        btnFindInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = hdb.findInvoice(txtInvoiceNum.getText());

                /* TODO get tests */
            }
        });
        btnFindInvoice.setText("Find Invoices");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 2; gbc.gridy = 2;
        panelInvoiceFinder.add(btnFindInvoice, gbc);
    }
    private void setPanelInvoiceInfo() {
        JLabel lbl = new JLabel("Invoice ID:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 3;
        panelInvoiceInfo.add(lbl, gbc);

        txtInvoiceID = new JTextField(12);
        txtInvoiceID.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 3;
        panelInvoiceInfo.add(txtInvoiceID, gbc);

        lbl = new JLabel("Plan ID:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 3;
        panelInvoiceInfo.add(lbl, gbc);

        txtInvoicePlanID = new JTextField(12);
        txtInvoicePlanID.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 3;
        panelInvoiceInfo.add(txtInvoicePlanID, gbc);

        lbl = new JLabel("Patient ID:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 4;
        panelInvoiceInfo.add(lbl, gbc);

        txtInvoicePlanID = new JTextField(12);
        txtInvoicePlanID.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 4;
        panelInvoiceInfo.add(txtInvoicePlanID, gbc);

        lbl = new JLabel("Patient Name:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 4;
        panelInvoiceInfo.add(lbl, gbc);

        txtInvoicePatientName = new JTextField(12);
        txtInvoicePatientName.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 4;
        panelInvoiceInfo.add(txtInvoicePatientName, gbc);
    }
    private void setPanelInvoiceSubmit() {
        JLabel lbl = new JLabel("Creation Date:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelInvoiceSubmit.add(lbl, gbc);

        final JTextField txtCreationDate = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 0;
        panelInvoiceSubmit.add(txtCreationDate, gbc);

        lbl = new JLabel("Due Date:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 1;
        panelInvoiceSubmit.add(lbl, gbc);

        final JTextField txtDueDate = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 1;
        panelInvoiceSubmit.add(txtDueDate, gbc);

        lbl = new JLabel("Invoice item:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 2;
        panelInvoiceSubmit.add(lbl, gbc);

        final JTextField txtInvoiceItem = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 2;
        panelInvoiceSubmit.add(txtInvoiceItem, gbc);

        lbl = new JLabel("Payment Status:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 2; gbc.gridy = 0;
        panelInvoiceSubmit.add(lbl, gbc);

        JComboBox<String> cboxPaymentStatus = new JComboBox<>(paymentStatus);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 3; gbc.gridy = 0;
        panelInvoiceSubmit.add(cboxPaymentStatus, gbc);

        lbl = new JLabel("Payment Date:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 2; gbc.gridy = 1;
        panelInvoiceSubmit.add(lbl, gbc);

        JTextField txtPaymentDate = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 3; gbc.gridy = 1;
        panelInvoiceSubmit.add(txtPaymentDate, gbc);

        lbl = new JLabel("Payment Method:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 2; gbc.gridy = 2;
        panelInvoiceSubmit.add(lbl, gbc);

        JComboBox<String> cboxPaymentMethod = new JComboBox<String>(paymentMethod);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 3; gbc.gridy = 2;
        panelInvoiceSubmit.add(cboxPaymentMethod, gbc);

        lbl = new JLabel("Amount Owing:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 2; gbc.gridy = 3;
        panelInvoiceSubmit.add(lbl, gbc);

        final JTextField txtAmountOwing = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 3; gbc.gridy = 3;
        panelInvoiceSubmit.add(txtAmountOwing, gbc);

        JButton btnSubmitInvoice = new JButton();
        btnSubmitInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object obj = hdb.updateInvoice(txtCreationDate.getText(), txtDueDate.getText(), txtInvoiceItem.getText(),
                        cboxPaymentStatus.getSelectedItem().toString(), txtPaymentDate.getText(), cboxPaymentMethod.getSelectedItem().toString(),
                        txtAmountOwing.getText());

                // updateInvoice(String creationDate, String dueDate, String invoiceItem,
                //                              String paymentStatus, String paymentDate, String paymentMethod, String amountOwing)

                /* TODO get tests */
            }
        });
        btnSubmitInvoice.setText("Submit Invoices");
        gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 1; gbc.gridy = 4;
        panelInvoiceSubmit.add(btnSubmitInvoice, gbc);
    }

    private void setPanelPlanSummary() {

    	// --------------------------------------------------

        /* Row 0 */
        JLabel lbl = new JLabel("Plan Summary");
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        lbl.setHorizontalAlignment(SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelPlanSummary.add(lbl, gbc);

        /* Row 1 */
        panelPlanSummaryFinder = new JPanel();
        panelPlanSummaryFinder.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 1;
        panelPlanSummary.add(panelPlanSummaryFinder, gbc);

        // --------------------------------------------------

        /* Row 2 */
        lbl = new JLabel("Personal Information", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 2;
        panelPlanSummary.add(lbl, gbc);

        /* Row 3 */
        panelPlanSummaryInfo = new JPanel();
        panelPlanSummaryInfo.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 3;
        panelPlanSummary.add(panelPlanSummaryInfo, gbc);

        // --------------------------------------------------

        /* Row 4 */
        lbl = new JLabel("Provincial Plan", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 4;
        panelPlanSummary.add(lbl, gbc);

        /* Row 5 */
        panelProvincialPlan = new JPanel();
        panelProvincialPlan.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 5;
        panelPlanSummary.add(panelProvincialPlan, gbc);

        // --------------------------------------------------

        /* Row 6 */
        lbl = new JLabel("Extended Benefits Plan", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 6;
        panelPlanSummary.add(lbl, gbc);

        /* Row 7 */
        panelExtendedBenefits = new JPanel();
        panelExtendedBenefits.setLayout(new BorderLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 7;
        panelPlanSummary.add(panelExtendedBenefits, gbc);

        // --------------------------------------------------

        /* Row 8 */
        lbl = new JLabel("Invoice History", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 8;
        panelPlanSummary.add(lbl, gbc);

        /* Row 9 */
        panelInvoiceHistory = new JPanel();
        panelInvoiceHistory.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.gridx = 0; gbc.gridy = 9;
        panelPlanSummary.add(panelInvoiceHistory, gbc);

        // --------------------------------------------------

        /* Row 10 */
        panelInvoiceHistoryGrid = new JPanel();
        panelInvoiceHistoryGrid.setLayout(new BorderLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 10;
        panelPlanSummary.add(panelInvoiceHistoryGrid, gbc);

        // -------------------------------------

        /* Row 11 */
        panelPlanSummaryActions= new JPanel();
        panelPlanSummaryActions.setLayout(new FlowLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 11;
        panelPlanSummary.add(panelPlanSummaryActions, gbc);

     // -------------------------------------

        /* Row 12 */
        lbl = new JLabel("Monthly Invoice Summary for Unpaid Amounts", SwingConstants.LEADING);
        lbl.setFont(new Font("Arial", Font.BOLD, 20));
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(10,0,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridwidth = 2;
        gbc.gridx = 0; gbc.gridy = 12;
        panelPlanSummary.add(lbl, gbc);

        /* Row 13 */
        panelMonthlyInvoiceSummary = new JPanel();
        panelMonthlyInvoiceSummary.setLayout(new BorderLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.gridx = 0; gbc.gridy = 13;
        panelPlanSummary.add(panelMonthlyInvoiceSummary, gbc);

     // -------------------------------------
        /* Row 14 */
        JButton btnGetSummary = new JButton();
        btnGetSummary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            	// Clear and update the table with new data
                monthlyInvoiceSummaryTableModel.setRowCount(0);

                if(patientArray.size() > 0) {
                    monthlyInvoiceSummaryArray = hdb.getOwingInvoicesMonthlySummary(patientID);
                    printTuples(monthlyInvoiceSummaryArray);
                    String[][] data = createData(monthlyInvoiceSummaryArray);
                    for(int row = 0; row < data.length; row++) {
                        monthlyInvoiceSummaryTableModel.addRow(data[row]);
                    }
                } else {
                    monthlyInvoiceSummaryTableModel.setRowCount(0);
                }
            }
        });
        btnGetSummary.setText("Get Summary");

        JPanel panelMonthlyInvoiceSummaryPresBtn = new JPanel();
        panelMonthlyInvoiceSummaryPresBtn.setLayout(new FlowLayout());
        panelMonthlyInvoiceSummaryPresBtn.add(btnGetSummary);
        gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 14;
        panelPlanSummary.add(panelMonthlyInvoiceSummaryPresBtn, gbc);

        // ----------------------------
    }
    private void setPanelProvincialPlan() {
    	JLabel lbl = new JLabel("Plan ID:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 3;
        panelProvincialPlan.add(lbl, gbc);

        txtPlanID = new JTextField(12);
        txtPlanID.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 3;
        panelProvincialPlan.add(txtPlanID, gbc);

        lbl = new JLabel("Start Date:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 3;
        panelProvincialPlan.add(lbl, gbc);

        txtPolicyType = new JTextField(12);
        txtPolicyType.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 3;
        panelProvincialPlan.add(txtPolicyType, gbc);

        lbl = new JLabel("Policy Type:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 4;
        panelProvincialPlan.add(lbl, gbc);

        txtStartDate = new JTextField(20);
        txtStartDate.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 4;
        panelProvincialPlan.add(txtStartDate, gbc);

        lbl = new JLabel("End Date:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 4;
        panelProvincialPlan.add(lbl, gbc);

        txtEndDate = new JTextField(12);
        txtEndDate.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 3; gbc.gridy = 4;
        panelProvincialPlan.add(txtEndDate, gbc);

    }
    private void setPanelExtendedBenefits() {
    	String cols[] = {"Chiropractic", "Chiropractic Annual Limit", "Chiropractic YTD","Physiotherapy", "Physiotherapy Annual Limit", "Physiotherapy YTD",
    			"Non-Surgical Podiatry", "Non-Surgical Podiatry Annual Limit", "Non-Surgical Podiatry YTD", "Acupuncture", "Acupuncture Annual Limit", "Acupuncture YTD",
    			"Medication", "Medication Annual Limit", "Medication YTD"};

        String data[][] = {};
        extendedBenefitsTableModel = new DefaultTableModel (data, cols);
        extendedBenefitsTable = new JTable(extendedBenefitsTableModel);
        panelExtendedBenefits.add(extendedBenefitsTable.getTableHeader(), BorderLayout.PAGE_START);
        panelExtendedBenefits.add(extendedBenefitsTable, BorderLayout.CENTER);

    }

    private void setPanelMonthlyInvoiceSummary() {
        String cols[] = {"Invoice Item", "Month, Year", "Average Unpaid Balance"};

        String data[][] = {};
        monthlyInvoiceSummaryTableModel = new DefaultTableModel (data, cols);
        monthlyInvoiceSummaryTable = new JTable(monthlyInvoiceSummaryTableModel);
        panelMonthlyInvoiceSummary.add(monthlyInvoiceSummaryTable.getTableHeader(), BorderLayout.PAGE_START);
        panelMonthlyInvoiceSummary.add(monthlyInvoiceSummaryTable, BorderLayout.CENTER);
    }

    private void setPanelInvoiceHistoryGrid() {
    	String cols[] = {"Invoice ID", "Invoice Item", "Creation Date", "Due Date", "Status", "Balance"};
        String data[][] = {};
        invoiceHistoryGridTableModel = new DefaultTableModel (data, cols);
        invoiceHistoryGridTable = new JTable(invoiceHistoryGridTableModel);
        panelInvoiceHistoryGrid.add(invoiceHistoryGridTable.getTableHeader(), BorderLayout.PAGE_START);
        panelInvoiceHistoryGrid.add(invoiceHistoryGridTable, BorderLayout.CENTER);
    }
    private void setPanelInvoiceHistory() {
    	JLabel lbl = new JLabel("Total Unpaid:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelInvoiceHistory.add(lbl, gbc);

        txtTotalUnpaid = new JTextField(10);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 0;
        panelInvoiceHistory.add(txtTotalUnpaid, gbc);

        lbl = new JLabel("Total Overdue:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 2; gbc.gridy = 0;
        panelInvoiceHistory.add(lbl, gbc);

        txtTotalOverDue = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 3; gbc.gridy = 0;
        panelInvoiceHistory.add(txtTotalOverDue, gbc);

        JButton btnUpdateInvoiceTotals = new JButton();
        btnUpdateInvoiceTotals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clear data first

                txtTotalUnpaid.setText("");
                txtTotalOverDue.setText("");

                if(patientArray.size() > 0) {
                    txtTotalUnpaid.setText(Double.toString((hdb.getAmountOwing(patientID))));
                    txtTotalOverDue.setText(Double.toString(hdb.getOverdueAmountOwing(patientID)));
                } else {
                    txtTotalUnpaid.setText("");
                    txtTotalOverDue.setText("");
                }
            }
        });
        btnUpdateInvoiceTotals.setText("Update Totals");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 4; gbc.gridy = 0;
        panelInvoiceHistory.add(btnUpdateInvoiceTotals, gbc);

    }
    private void setPanelPlanSummaryFinder() {
        JLabel lbl = new JLabel("PID:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 0;
        panelPlanSummaryFinder.add(lbl, gbc);

        JTextField txtPID = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 0;
        panelPlanSummaryFinder.add(txtPID, gbc);

        lbl = new JLabel("Name:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 2; gbc.gridy = 0;
        panelPlanSummaryFinder.add(lbl, gbc);

        JTextField txtName = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 3; gbc.gridy = 0;
        panelPlanSummaryFinder.add(txtName, gbc);

        lbl = new JLabel("Plan #:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 0; gbc.gridy = 1;
        panelPlanSummaryFinder.add(lbl, gbc);

        JTextField txtNum = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 1;
        panelPlanSummaryFinder.add(txtNum, gbc);

        lbl = new JLabel("Invoice #:", SwingConstants.LEADING);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 0; gbc.gridy = 2;
        panelPlanSummaryFinder.add(lbl, gbc);

        JTextField txtInvoiceNumber = new JTextField(12);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 0);
        gbc.gridx = 1; gbc.gridy = 2;
        panelPlanSummaryFinder.add(txtInvoiceNumber, gbc);

        JButton btnFindPlanPatientID = new JButton();
        btnFindPlanPatientID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pidTxt = txtPID.getText();
                String nameTxt = txtName.getText();

                System.out.println(pidTxt + " " + nameTxt);

                if (pidTxt.equals("") && !nameTxt.isEmpty()) {
                    ArrayList<ArrayList<String>> names = hdb.getPatients(nameTxt);
                    pidTxt = getPIDfromName(names, nameTxt);

                    if (!pidTxt.equals("")) {
                        patientArray = hdb.findPatient(pidTxt);
                    }
                } else if (!pidTxt.equals("")) {
                    patientArray = hdb.findPatient(pidTxt);
                }

                planArray = hdb.getPlan(pidTxt);

                if (patientArray.size() > 0) {

                    // Clear the data tables
                    extendedBenefitsTableModel.setRowCount(0);
                    invoiceHistoryGridTableModel.setRowCount(0);
                    monthlyInvoiceSummaryTableModel.setRowCount(0);

                    patientID = patientArray.get(2);
                    String name = patientArray.get(0) + " " + patientArray.get(1);
                    String addr = patientArray.get(3) + " " + patientArray.get(4) + " " + patientArray.get(6) + " " + patientArray.get(5);
                    String mobile = patientArray.get(9);
                    String home = patientArray.get(8);

                    System.out.println(patientArray.get(2) + ", " + name);

                    txtPlanSumName.setText(name);
                    txtPlanSumPID.setText(patientID);
                    txtPlanSumAddress.setText(addr);
                    txtPlanSumMobilePhone.setText(mobile);
                    txtPlanSumHomePhone.setText(home);

                    txtPlanID.setText(planArray.get(0));
                    txtStartDate.setText(planArray.get(1));
                    txtPolicyType.setText(planArray.get(2));
                    txtEndDate.setText(planArray.get(3));

                    extendedBenefitsArray = hdb.getExtendedBenefits(patientArray.get(2));
                    printTuples(extendedBenefitsArray);
                    if (extendedBenefitsArray.size() > 0) {
                        String[][] data = createData(extendedBenefitsArray);
                        for (int row = 0; row < data.length; row++) {
                            extendedBenefitsTableModel.addRow(data[row]);
                        }
                    }


                    invoiceHistoryGridArray = hdb.getInvoices(patientArray.get(2));
                    printTuples(invoiceHistoryGridArray);
                    if (invoiceHistoryGridArray.size() > 0) {
                        String[][] data = createData(invoiceHistoryGridArray);
                        for (int row = 0; row < data.length; row++) {
                            invoiceHistoryGridTableModel.addRow(data[row]);
                        }
                    }


                    txtName.setText("");
                    txtPID.setText("");
                    txtTotalUnpaid.setText(Double.toString((hdb.getAmountOwing(patientID))));
                    txtTotalOverDue.setText(Double.toString(hdb.getOverdueAmountOwing(patientID)));

                } else {
                    txtPID.setText("");
                    txtName.setText("");
                    txtTotalUnpaid.setText("");
                    txtTotalOverDue.setText("");

                    clearPanelData();

                    // Clear the data tables
                    extendedBenefitsTableModel.setRowCount(0);
                    invoiceHistoryGridTableModel.setRowCount(0);
                    monthlyInvoiceSummaryTableModel.setRowCount(0);


                    JOptionPane.showMessageDialog(frame, "Plan Summary not found!", "Invalid Patient ID Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFindPlanPatientID.setText("Find Patient");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 4; gbc.gridy = 0;
        panelPlanSummaryFinder.add(btnFindPlanPatientID, gbc);

        JButton btnFindPlanSumPlanNum = new JButton();
        btnFindPlanSumPlanNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            	planNumArray = hdb.findPlan(txtNum.getText());

            	if(planNumArray.size() > 0) {

                    // Clear the data tables
                	extendedBenefitsTableModel.setRowCount(0);
                	invoiceHistoryGridTableModel.setRowCount(0);
                	monthlyInvoiceSummaryTableModel.setRowCount(0);

                	patientID = planNumArray.get(3);

                    if (!patientID.equals("")) {
                        patientArray = hdb.findPatient(patientID);
                    }

                    String name = patientArray.get(0) + " " + patientArray.get(1);
                    String addr = patientArray.get(3) + " " + patientArray.get(4) + " " + patientArray.get(6) + " " + patientArray.get(5);
                    String mobile = patientArray.get(9);
                    String home = patientArray.get(8);

                    txtPlanSumName.setText(name);
                    txtPlanSumPID.setText(patientID);
                    txtPlanSumAddress.setText(addr);
                    txtPlanSumMobilePhone.setText(mobile);
                    txtPlanSumHomePhone.setText(home);

                    planArray = hdb.getPlan(patientID);

                    txtPlanID.setText(planArray.get(0));
                    txtStartDate.setText(planArray.get(1));
                    txtPolicyType.setText(planArray.get(2));
                    txtEndDate.setText(planArray.get(3));

                    extendedBenefitsArray = hdb.getExtendedBenefits(patientArray.get(2));
                    printTuples(extendedBenefitsArray);
                    if (extendedBenefitsArray.size() > 0) {
                        String[][] data = createData(extendedBenefitsArray);
                        for (int row = 0; row < data.length; row++) {
                            extendedBenefitsTableModel.addRow(data[row]);
                        }
                    }


                    invoiceHistoryGridArray = hdb.getInvoices(patientArray.get(2));
                    printTuples(invoiceHistoryGridArray);
                    if (invoiceHistoryGridArray.size() > 0) {
                        String[][] data = createData(invoiceHistoryGridArray);
                        for (int row = 0; row < data.length; row++) {
                            invoiceHistoryGridTableModel.addRow(data[row]);
                        }
                    }

                    txtNum.setText("");
                    txtTotalUnpaid.setText(Double.toString((hdb.getAmountOwing(patientID))));
                    txtTotalOverDue.setText(Double.toString(hdb.getOverdueAmountOwing(patientID)));

                } else {
                    txtNum.setText("");

                    clearPanelData();

                    // Clear the data tables
                    extendedBenefitsTableModel.setRowCount(0);
                	invoiceHistoryGridTableModel.setRowCount(0);
                	monthlyInvoiceSummaryTableModel.setRowCount(0);

                    JOptionPane.showMessageDialog(frame, "Plan Summary not found!", "Invalid Plan Number Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFindPlanSumPlanNum.setText("Find by Plan Number");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 4; gbc.gridy = 1;
        panelPlanSummaryFinder.add(btnFindPlanSumPlanNum, gbc);

        JButton btnFindPlanInvoiceNum = new JButton();
        btnFindPlanInvoiceNum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patientID = hdb.findPIDfromInvoice(txtInvoiceNumber.getText());
                patientArray = hdb.findPatient(patientID);

                if(patientArray.size() > 0) {

                    // Clear the data tables

                	extendedBenefitsTableModel.setRowCount(0);
                	invoiceHistoryGridTableModel.setRowCount(0);
                	monthlyInvoiceSummaryTableModel.setRowCount(0);

                    String name = patientArray.get(0) + " " + patientArray.get(1);
                    String addr = patientArray.get(3) + " " + patientArray.get(4) + " " + patientArray.get(6) + " " + patientArray.get(5);
                    String mobile = patientArray.get(9);
                    String home = patientArray.get(8);

                    txtPlanSumName.setText(name);
                    txtPlanSumPID.setText(patientID);
                    txtPlanSumAddress.setText(addr);
                    txtPlanSumMobilePhone.setText(mobile);
                    txtPlanSumHomePhone.setText(home);

                    planArray = hdb.getPlan(patientID);

                    txtPlanID.setText(planArray.get(0));
                    txtStartDate.setText(planArray.get(1));
                    txtPolicyType.setText(planArray.get(2));
                    txtEndDate.setText(planArray.get(3));

                    extendedBenefitsArray = hdb.getExtendedBenefits(patientArray.get(2));
                    printTuples(extendedBenefitsArray);

                    if (extendedBenefitsArray.size() > 0) {
                    	String[][] data = createData(extendedBenefitsArray);
                    	for(int row = 0; row < data.length; row++) { 
                    		extendedBenefitsTableModel.addRow(data[row]);
                    	}
                    }

                    invoiceHistoryGridArray = hdb.getInvoices(patientArray.get(2));
                    printTuples(invoiceHistoryGridArray);
                    if (invoiceHistoryGridArray.size() > 0) {
                    	String[][] data = createData(invoiceHistoryGridArray);
                    	for(int row = 0; row < data.length; row++) {
                    		invoiceHistoryGridTableModel.addRow(data[row]);
                    	}
                    }
                    
                    txtInvoiceNumber.setText("");
                    txtTotalUnpaid.setText(Double.toString((hdb.getAmountOwing(patientID))));
                    txtTotalOverDue.setText(Double.toString(hdb.getOverdueAmountOwing(patientID)));

                } else {
                	txtInvoiceNumber.setText("");

                	clearPanelData();
                    // Clear the data tables
                    extendedBenefitsTableModel.setRowCount(0);
                	invoiceHistoryGridTableModel.setRowCount(0);
                	monthlyInvoiceSummaryTableModel.setRowCount(0);

                    JOptionPane.showMessageDialog(frame, "Invoice not found!", "Invalid Invoice Number Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnFindPlanInvoiceNum.setText("Find by Invoice Number");
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 0);
        gbc.gridwidth = 2;
        gbc.gridx = 4; gbc.gridy = 2;
        panelPlanSummaryFinder.add(btnFindPlanInvoiceNum, gbc);
    }
    private void setPanelPlanSummaryInfo() {
        JLabel lbl = new JLabel("Name:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 3;
        panelPlanSummaryInfo.add(lbl, gbc);

        txtPlanSumName = new JTextField(12);
        txtPlanSumName.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 3;
        panelPlanSummaryInfo.add(txtPlanSumName, gbc);

        lbl = new JLabel("PID:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 3;
        panelPlanSummaryInfo.add(lbl, gbc);

        txtPlanSumPID = new JTextField(12);
        txtPlanSumPID.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 3;
        panelPlanSummaryInfo.add(txtPlanSumPID, gbc);

        // address
        lbl = new JLabel("Address:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 4;
        panelPlanSummaryInfo.add(lbl, gbc);

        txtPlanSumAddress = new JTextField(30);
        txtPlanSumAddress.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 4;
        panelPlanSummaryInfo.add(txtPlanSumAddress, gbc);

        // mobile
        lbl = new JLabel("Mobile Phone:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 0; gbc.gridy = 5;
        panelPlanSummaryInfo.add(lbl, gbc);

        txtPlanSumMobilePhone = new JTextField(12);
        txtPlanSumMobilePhone.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(0,5,0,0);
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1; gbc.gridy = 5;
        panelPlanSummaryInfo.add(txtPlanSumMobilePhone, gbc);

        // home
        lbl = new JLabel("Home Phone:");
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 2; gbc.gridy = 5;
        panelPlanSummaryInfo.add(lbl, gbc);

        txtPlanSumHomePhone = new JTextField(12);
        txtPlanSumHomePhone.setEditable(false);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets= new Insets(0,5,0,0);
        gbc.gridx = 3; gbc.gridy = 5;
        panelPlanSummaryInfo.add(txtPlanSumHomePhone, gbc);
    }

    private void setPanelPlanSummaryActions() {
        JButton btnCreateInvoice = new JButton();
        btnCreateInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField aInvoiceItem = new JTextField();
                JTextField aDueDate = new JTextField();
                JTextField aPaymentDate = new JTextField();
                JTextField aAmountOwing = new JTextField();
                JComboBox<String> aPaymentStatus = new JComboBox<>(paymentStatus);
                JComboBox<String> aPaymentMethod = new JComboBox<>(paymentMethod);


                if(patientArray.size() > 0) {
                    String name = patientArray.get(0) + " " + patientArray.get(1);


                    Object[] fields = {"Patient: " + name, "Invoice Item: ", aInvoiceItem, "Due Date (YYYY-MM-DD): ", aDueDate, "Payment Status: ", aPaymentStatus, "Payment Date (YYYY-MM-DD): ",
                    		aPaymentDate, "Payment Method: ", aPaymentMethod, "Amount Owing: ", aAmountOwing};


                    int resp = JOptionPane.showConfirmDialog(frame, fields, "Create invoice for " + name, JOptionPane.OK_CANCEL_OPTION);

                    if(resp == JOptionPane.OK_OPTION) {

                    	String invoiceItem = aInvoiceItem.getText();
                        String dueDate = aDueDate.getText();
                        String paymentStatus = aPaymentStatus.getSelectedItem().toString();
                        String paymentDate = aPaymentDate.getText();
                        String paymentMethod = aPaymentMethod.getSelectedItem().toString();
                        String amountOwing = aAmountOwing.getText();
                        planID = hdb.getPlan(patientID).get(0);

                        System.out.println(patientID + " " + invoiceItem + " " + dueDate + " " + paymentStatus + " " + paymentDate + " " + paymentMethod + " " + amountOwing + " " + planID);

                        hdb.createInvoice(patientID, invoiceItem, dueDate, paymentStatus,
                        		paymentDate, paymentMethod, amountOwing, planID);

                     // Clear and update the table with new data
                        invoiceHistoryGridTableModel.setRowCount(0);
                        invoiceHistoryGridArray = hdb.getInvoices(patientID);
                        updateTable(invoiceHistoryGridArray, invoiceHistoryGridTableModel);
                    }
                    else {
                        System.out.println("No values entered");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "No Invoice selected!", "Error",  JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCreateInvoice.setText("Create Invoice");
        panelPlanSummaryActions.add(btnCreateInvoice);

        JButton btnViewInvoice= new JButton();
        btnViewInvoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                viewInvoiceData(invoiceHistoryGridTable, invoiceHistoryGridTableModel);
            	// viewInvoiceData(invoiceTPTable, invoiceTPTableModel);
            }
        });
        btnViewInvoice.setText("View Selected Invoice");
        panelPlanSummaryActions.add(btnViewInvoice);

    }

    private void viewInvoiceData(JTable table, DefaultTableModel tableModel) {

    	JTextField aPatientID = new JTextField();
        // JTextField aInvoiceID = new JTextField();
        JTextField aInvoiceItem = new JTextField();
        JTextField aDueDate = new JTextField();
        JTextField aPaymentStatus = new JTextField();
        JTextField aPaymentDate = new JTextField();
        JTextField aPaymentMethod = new JTextField();
        JTextField aAmountOwing = new JTextField();
        JTextField aPaymentID = new JTextField();
        JTextField aPlanID = new JTextField();
        JTextField aCreationDate = new JTextField();

        if(patientArray.size() > 0) {
            String name = patientArray.get(0) + " " + patientArray.get(1);

            int row = table.getSelectedRow();

            if(row >= 0) {
                String invoiceID = tableModel.getValueAt(row, 0).toString();
                ArrayList<String> testData = hdb.findInvoice(invoiceID);

                aPatientID.setEditable(false);
                // aInvoiceID.setEditable(false);
                aInvoiceItem.setEditable(false);
                aDueDate.setEditable(false);
                aPaymentStatus.setEditable(false);
                aPaymentDate.setEditable(false);
                aPaymentMethod.setEditable(false);
                aAmountOwing.setEditable(false);
                aPaymentID.setEditable(false);
                aPlanID.setEditable(false);
                aCreationDate.setEditable(false);

                aPatientID.setText(testData.get(0));
                // aInvoiceID.setText(testData.get(1));
                aInvoiceItem.setText(testData.get(1));
                aDueDate.setText(testData.get(2));
                aPaymentStatus.setText(testData.get(3));
                aPaymentDate.setText(testData.get(4));
                aPaymentMethod.setText(testData.get(5));
                aAmountOwing.setText(testData.get(6));
                aPaymentID.setText(testData.get(7));
                aPlanID.setText(testData.get(8));
                aCreationDate.setText(testData.get(9));

                Object[] data = {"Patient: " + name, "Invoice ID: " + invoiceID, " ",
                		"Patiend ID: ", aPatientID, "Invoice Item: ", aInvoiceItem, "Due Date: ", aDueDate,
                		"Payment Status: ", aPaymentStatus, "Payment Date: ", aPaymentDate, "Payment Method: ", aPaymentMethod,
                		"Amount Owing: ", aAmountOwing, "Payment ID: ", aPaymentID, "Plan ID: ", aPlanID, "Creation Date: ", aCreationDate};

                JOptionPane.showMessageDialog(frame, data, "Invoice ID # " + invoiceID + " for Patient " + name, JOptionPane.PLAIN_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(frame, "Please select an invoice from the Invoice table.", "Error",  JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, "No invoice selected!", "Error",  JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchToUserSelectPanel() {
        /* Switch to User Class panel when login achieved */
        panelOracleLogin.setVisible(false);
        panelUserClass.setVisible(true);
        panelEmpty.setVisible(true);
        panelPatientSummary.setVisible(false);
        panelPrescription.setVisible(false);
        panelTest.setVisible(false);
        panelPlanSummary.setVisible(false);
        panelInvoice.setVisible(false);
    }

    private void switchToInvoicePanel() {
        /* Switch to User Class panel when login achieved */
        panelOracleLogin.setVisible(false);
        panelUserClass.setVisible(false);
        panelEmpty.setVisible(false);
        panelPatientSummary.setVisible(false);
        panelPrescription.setVisible(false);
        panelTest.setVisible(false);
        panelPlanSummary.setVisible(false);
        panelInvoice.setVisible(true);
    }

}
