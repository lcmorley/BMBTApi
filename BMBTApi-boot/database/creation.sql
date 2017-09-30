USE bmbtschema;

DROP TABLE testResult;
DROP TABLE testQuestion;
DROP TABLE operator;
DROP TABLE device;
DROP TABLE users;
DROP TABLE test; 

CREATE TABLE device (
	deviceId varchar(100) NOT NULL,
	registerDate datetime,
	PRIMARY KEY (deviceId)
);

CREATE TABLE users (
	id varchar(100) NOT NULL,
	deviceId varchar(100),
	firstname varchar(50),
	PRIMARY KEY (id),
	FOREIGN KEY(deviceId) REFERENCES device(deviceId) ON DELETE CASCADE
);

CREATE TABLE test (
	id int NOT NULL,
    testName varchar(20),
    testTime int,
    PRIMARY KEY(id)
);

CREATE TABLE operator (
	id int NOT NULL,
    operatorValue varchar(1),
    PRIMARY KEY(id)
);

CREATE TABLE testQuestion (
	id int NOT NULL,
    testId int,
    xValue int,
    yValue int,
    operatorId int,
    PRIMARY KEY(id),
    FOREIGN KEY(testId) REFERENCES test(id),
    FOREIGN KEY(operatorId) REFERENCES operator(id)
);

CREATE TABLE testResult (
	id varchar(100) NOT NULL,
	userId varchar(100),
    testId int,
    correctAnswers int,
    incorrectAnswers int,
    timeElapsed float,
    testDate datetime,
    testType varchar(100),
    PRIMARY KEY(id),
    FOREIGN KEY(userId) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(testId) REFERENCES test(id)
);

CREATE TABLE deviceSecurity (
	deviceId varchar(100) NOT NULL,
	token varchar(500) NOT NULL,
	expiryDate datetime,
	PRIMARY KEY(deviceId),
	FOREIGN KEY(deviceId) REFERENCES device(deviceId) ON DELETE CASCADE
);
	