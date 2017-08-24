USE bmbtschema;

CREATE TABLE users (
	id varchar(100),
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

CREATE TABLE testResult (
	userId int,
    testId int,
    correctAnswers int,
    incorrectAnswers int,
    attemptedAnswers int,
    timeElapsed float,
    testDate datetime
)