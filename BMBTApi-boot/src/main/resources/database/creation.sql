USE bmbtschema;

CREATE TABLE users (
	id int,
	firstname varchar(50)
);

CREATE TABLE test (
	id int,
    testName varchar(20),
    testTime int
);

CREATE TABLE testQuestion (
	id int,
    testId int,
    xValue int,
    yValue int,
    operatorId int
);

CREATE TABLE operator (
	id int,
    operatorValue varchar(1)
);