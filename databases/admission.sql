DROP TABLE IF EXISTS Sib_Admission;

CREATE TABLE Sib_Admission (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(1024) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO Sib_Admission(name) VALUES('Video Resources');
INSERT INTO Sib_Admission(name) VALUES('Articles');

DROP TABLE IF EXISTS Sib_Sub_Admission;

CREATE TABLE Sib_Sub_Admission (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(1024) NOT NULL,
  idAdmission int(11) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO Sib_Sub_Admission(name, idAdmission) VALUES('Before Applying', 1);
INSERT INTO Sib_Sub_Admission(name, idAdmission) VALUES('The Application', 1);
INSERT INTO Sib_Sub_Admission(name, idAdmission) VALUES('After Applying', 1);

DROP TABLE IF EXISTS Sib_Topic_Sub_Admission;

CREATE TABLE Sib_Topic_Sub_Admission (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(1024) NOT NULL,
  idSubAdmission int(11) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO Sib_Topic_Sub_Admission(name, idSubAdmission) VALUES('Applying to College Info & Timeline', 1);
INSERT INTO Sib_Topic_Sub_Admission(name, idSubAdmission) VALUES('Testing', 1);
INSERT INTO Sib_Topic_Sub_Admission(name, idSubAdmission) VALUES('College Essays', 2);
INSERT INTO Sib_Topic_Sub_Admission(name, idSubAdmission) VALUES('Letter of Recommendations', 2);
INSERT INTO Sib_Topic_Sub_Admission(name, idSubAdmission) VALUES('Extracurriculars', 2);
INSERT INTO Sib_Topic_Sub_Admission(name, idSubAdmission) VALUES('Interviews', 3);
INSERT INTO Sib_Topic_Sub_Admission(name, idSubAdmission) VALUES('Financial Aid', 3);

DROP TABLE IF EXISTS Sib_Video_Admission;

CREATE TABLE Sib_Video_Admission (
  vId int(11) NOT NULL AUTO_INCREMENT,
  authorId int(11) NOT NULL,
  title text NOT NULL,
  description text,
  youtubeUrl text,
  image varchar(130),
  creationDate timestamp DEFAULT CURRENT_TIMESTAMP,
  idTopicSubAdmission int(11) NOT NULL,
  PRIMARY KEY (vid)
);

DROP TABLE IF EXISTS Sib_Article;

CREATE TABLE Sib_Article (
  arId int(11) NOT NULL AUTO_INCREMENT,
  authorId int(11) NOT NULL,
  title text NOT NULL,
  description text,
  image varchar(130),
  creationDate timestamp DEFAULT CURRENT_TIMESTAMP,
  idAdmission int(11) NOT NULL,
  PRIMARY KEY (arid)
);

DROP TABLE IF EXISTS Sib_Article_Answer;

CREATE TABLE Sib_Article_Answer (
  aId int(11) NOT NULL AUTO_INCREMENT,
  authorId int(11) NOT NULL,
  content text,
  creationDate timestamp DEFAULT CURRENT_TIMESTAMP,
  arId int(11) NOT NULL,
  PRIMARY KEY (aid)
);
