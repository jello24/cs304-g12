CREATE TABLE PostalCode (
    postalCode  varchar(6),
    city        varchar(20),
    province    varchar(20),
    country     varchar(20),
    PRIMARY KEY (postalCode)
);

CREATE TABLE HealthcareProfessional (
    HID 		    integer,
    firstName 		varchar2(20),
    lastName 		varchar2(20),
    officeNumber 	varchar2(10),
    street 			varchar2(60),
    postalCode 		varchar2(6),
    homePhone 		varchar2(20),
    mobilePhone 	varchar2(20),
    PRIMARY KEY (HID),
    FOREIGN KEY (postalCode) REFERENCES PostalCode
);

CREATE TABLE LabTechnician (
    HID 				integer,
    certificationNumber integer,
    PRIMARY KEY(HID),
    FOREIGN KEY (HID) REFERENCES HealthCareProfessional
);

CREATE TABLE Pharmacist (
    HID 				integer,
    certificationNumber integer,
    PRIMARY KEY(HID),
    FOREIGN KEY (HID) REFERENCES HealthCareProfessional
);

CREATE TABLE Doctor (
    HID				    integer,
    certificationNumber integer,
    specialization  varchar(30) CHECK (specialization IN ('Anesthesiologist', 'Cardiologist', 'Dermatologist', 'Endocrinologist', 'Gastroenterologist', 'Geriatric Medicine Specialist', 'Gynecologist', 'Hematologist', 'Heptologist', 'Neonatologist', 'Nephrologist', 'Neurologist', 'Obstetrician', 'Oncologist', 'Oral Surgeon', 'Ophthalmologist', 'Orthopedic Surgeon', 'Otolaryngologist', 'Pediatrician', 'Psychiatrist', 'Pulmonologist', 'Radiologist', 'Rheumatologist', 'Sleep Disorder Specialist', 'Surgeon', 'Urologist', 'Family Doctor')),
    PRIMARY KEY(HID),
    FOREIGN KEY (HID) REFERENCES HealthCareProfessional
);

CREATE TABLE Patient (
    patientID 		integer,
    firstName 		varchar2(20),
    lastName 		varchar2(20),
    street 			varchar2(60),
    postalCode 		varchar2(6),
    homePhone 		varchar2(20),
    mobilePhone 	varchar2(20),
    PRIMARY KEY (patientID),
    FOREIGN KEY (postalCode) REFERENCES PostalCode
);

CREATE TABLE Referral (
    patientID			integer,
    referrerHID			integer,
    referreeHID			integer,
    referredDate		date not null,
    PRIMARY KEY (patientID, referrerHID, referreeHID),
    FOREIGN KEY (patientID) REFERENCES Patient ON DELETE CASCADE,
    FOREIGN KEY (referrerHID) REFERENCES Doctor,
    FOREIGN KEY (referreeHID) REFERENCES Doctor
);

CREATE TABLE Medication (
    medication			varchar2(40),
    dosageMeasure		varchar2(10) not null,
    PRIMARY KEY (medication)
);

CREATE TABLE Prescription (
    prescriptionID 	integer,
    medication 		varchar2(40),
    dosage 			decimal not null,
    quantity 		integer not null,
    patientID		integer not null,
    drHID			integer not null,
    pharmHID		integer not null,
    prescribedDate	date not null,
    filledDate		date,
    PRIMARY KEY (prescriptionID),
    FOREIGN KEY (medication) REFERENCES Medication,
    FOREIGN KEY (patientID) REFERENCES Patient ON DELETE CASCADE,
    FOREIGN KEY (drHID) REFERENCES Doctor,
    FOREIGN KEY (pharmHID) REFERENCES Pharmacist
);

CREATE TABLE LabTest (
    testID 			integer,
    cholesterol 	decimal,
    HDLcholesterol 	decimal,
    LDLcholesterol 	decimal,
    triglycerides 	decimal,
    whiteBloodCellCount 	decimal,
    redBloodCellCount 		decimal,
    hematocrit 		decimal,
    plateletCount 	decimal,
    NRBCpercent 	decimal,
    NRBCabsolute 	decimal,
    sodium 			decimal,
    phosphorus 		decimal,
    glucose 		decimal,
    patientID		integer not null,
    drHID			integer not null,
    labTechHID		integer,
    orderedDate		date not null,
    performedDate	date,
    PRIMARY KEY(testID),
    FOREIGN KEY (patientID) REFERENCES Patient ON DELETE CASCADE,
    FOREIGN KEY (drHID) REFERENCES Doctor,
    FOREIGN KEY (labTechHID) REFERENCES LabTechnician
);

CREATE TABLE ProvincialHealthPlan (
    planID 		integer,
    policyType  VARCHAR(40) CHECK (policyType IN ('BC Resident - MSP', 'BC Resident - Premium MSP', 'BC Resident - Income Assistance', 'BC Resident - Convention Refugees', 'Out-of-Province')),
    startDate	date not null,
    endDate		date not null,
    patientID	integer not null,
    PRIMARY KEY(planID),
    FOREIGN KEY (patientID) REFERENCES Patient ON DELETE CASCADE
);

CREATE TABLE ExtendedBenefitsPlan (
    EBPID 		    integer,
    planID		    integer not null,
    startDate 	    date not null,
    endDate 	    date not null,
    chiropractic 	varchar(1) CHECK (chiropractic IN ('Y', 'N')),
    physiotherapy 	varchar(1) CHECK (physiotherapy IN ('Y', 'N')),
    nonSurgicalPodiatry 	varchar(1) CHECK (nonSurgicalPodiatry IN ('Y', 'N')),
    massageTherapy			varchar(1) CHECK (massageTherapy IN ('Y', 'N')),
    acupuncture 	varchar(1) CHECK (acupuncture IN ('Y', 'N')),
    medication 		varchar(1) CHECK (medication IN ('Y', 'N')),
    physiotherapyAnnualLimit 	decimal,
    physiotherapyYTD	 	decimal,
    nonSurgicalPodiatryAnnualLimit 	decimal,
    nonSurgicalPodiatryYTD	 	decimal,
    chiropracticAnnualLimit		decimal,
    chiropracticYTD			decimal,
    massageTherapyAnnualLimit	decimal,
    massageTherapyYTD		decimal,
    acupunctureAnnualLimit 	decimal,
    acupunctureYTD			decimal,
    medicationAnnualLimit 	decimal,
    medicationYTD			decimal,
    PRIMARY KEY(EBPID),
    FOREIGN KEY (planID) REFERENCES ProvincialHealthPlan ON DELETE CASCADE
);

CREATE TABLE Invoice (
    invoiceID 			integer,
    patientID 		integer,
    invoiceItem 		varchar2(60),
    creationDate 		date not null,
    dueDate 			date not null,
    paymentStatus   VARCHAR(10) CHECK (paymentStatus IN ('Paid', 'Unpaid')) not null,
    paymentDate 		date,
    paymentMethod       VARCHAR(20) CHECK (paymentMethod IN ('Credit\Debit', 'Cash', 'Cheque')),
    amountOwing 		decimal not null,
    paymentID			integer,
    planID				integer not null,
    PRIMARY KEY(invoiceID),
    FOREIGN KEY (patientID) REFERENCES Patient ON DELETE CASCADE,
    FOREIGN KEY (planID) REFERENCES ProvincialHealthPlan
);