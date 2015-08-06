CREATE DATABASE IF NOT EXISTS `Analysisdb`;
USE `Analysisdb`;

CREATE TABLE "Staticanalysis" ("Name" TEXT PRIMARY KEY  NOT NULL ,"time" DATETIME,"status" TEXT);
insert into Staticanalysis values('subash',DateTime('now'), 'Not Yet Started')
