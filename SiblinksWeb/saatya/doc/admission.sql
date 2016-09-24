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
  idadmission int(11) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO Sib_Sub_Admission(name, idadmission) VALUES('Before Applying', 1);
INSERT INTO Sib_Sub_Admission(name, idadmission) VALUES('The Application', 1);
INSERT INTO Sib_Sub_Admission(name, idadmission) VALUES('After Applying', 1);

DROP TABLE IF EXISTS Sib_Topic_Sub_Admission;

CREATE TABLE Sib_Topic_Sub_Admission (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(1024) NOT NULL,
  idsubadmission int(11) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO Sib_Topic_Sub_Admission(name, idsubadmission) VALUES('Applying to College Info & Timeline', 1);
INSERT INTO Sib_Topic_Sub_Admission(name, idsubadmission) VALUES('Testing', 1);
INSERT INTO Sib_Topic_Sub_Admission(name, idsubadmission) VALUES('College Essays', 2);
INSERT INTO Sib_Topic_Sub_Admission(name, idsubadmission) VALUES('Letter of Recommendations', 2);
INSERT INTO Sib_Topic_Sub_Admission(name, idsubadmission) VALUES('Extracurriculars', 2);
INSERT INTO Sib_Topic_Sub_Admission(name, idsubadmission) VALUES('Interviews', 3);
INSERT INTO Sib_Topic_Sub_Admission(name, idsubadmission) VALUES('Financial Aid', 3);

DROP TABLE IF EXISTS Sib_Video_Admission;

CREATE TABLE Sib_Video_Admission (
  vid int(11) NOT NULL AUTO_INCREMENT,
  authorId int(11) NOT NULL,
  title text NOT NULL,
  description text,
  youtubeUrl text,
  image varchar(130),
  createDay timestamp DEFAULT CURRENT_TIMESTAMP,
  idtopicsubadmission int(11) NOT NULL,
  PRIMARY KEY (vid)
);

DROP TABLE IF EXISTS Sib_Articles;

CREATE TABLE Sib_Articles (
  arid int(11) NOT NULL AUTO_INCREMENT,
  authorId int(11) NOT NULL,
  title text NOT NULL,
  description text,
  image varchar(130),
  createDay timestamp DEFAULT CURRENT_TIMESTAMP,
  idadmission int(11) NOT NULL,
  PRIMARY KEY (arid)
);

DROP TABLE IF EXISTS Sib_Articles_Answer;

CREATE TABLE Sib_Articles_Answer (
  aid int(11) NOT NULL AUTO_INCREMENT,
  authorId int(11) NOT NULL,
  content text,
  createDay timestamp DEFAULT CURRENT_TIMESTAMP,
  arid int(11) NOT NULL,
  PRIMARY KEY (aid)
);
