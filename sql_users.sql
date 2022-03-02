CREATE ROLE 'ADMIN';
CREATE ROLE 'PROFESOR';
CREATE ROLE 'STUDENT';

GRANT ALL ON studiu.activitate TO 'ADMIN';
GRANT SELECT,DELETE ON studiu.activitati_grup TO 'ADMIN';
GRANT ALL ON studiu.curs TO 'ADMIN';
GRANT SELECT ON studiu.fisa_participare TO 'ADMIN';
GRANT INSERT,SELECT ON studiu.fisa_postului_curs TO 'ADMIN';
GRANT DELETE,SELECT,UPDATE ON studiu.grup TO 'ADMIN';
GRANT SELECT,DELETE ON studiu.membru_grup TO 'ADMIN';
GRANT ALL ON studiu.mesaj TO 'ADMIN';
GRANT ALL ON studiu.notificare TO 'ADMIN';
GRANT INSERT,SELECT ON studiu.organizator TO 'ADMIN';
GRANT SELECT,DELETE ON studiu.participant_curs TO 'ADMIN';
GRANT SELECT,DELETE ON studiu.participant_grup TO 'ADMIN';
GRANT SELECT ON studiu.profesori_invitati TO 'ADMIN';
GRANT ALL ON studiu.users TO 'ADMIN';
GRANT ALL ON studiu.student TO 'ADMIN';
GRANT ALL ON studiu.profesor TO 'ADMIN';

GRANT SELECT,UPDATE,INSERT ON studiu.activitate TO 'PROFESOR';
GRANT SELECT ON studiu.activitati_grup TO 'PROFESOR';
GRANT SELECT ON studiu.curs TO 'PROFESOR';
GRANT SELECT ON studiu.fisa_participare TO 'PROFESOR';
GRANT SELECT ON studiu.fisa_postului_curs TO 'PROFESOR';
GRANT ALL ON studiu.notificare TO 'PROFESOR';
GRANT ALL ON studiu.organizator TO 'PROFESOR';
GRANT SELECT,INSERT ON studiu.profesori_invitati TO 'PROFESOR';
GRANT SELECT ON studiu.users TO 'PROFESOR';
GRANT SELECT ON studiu.profesor TO 'PROFESOR';
GRANT SELECT ON studiu.student TO 'PROFESOR';
GRANT SELECT,UPDATE ON studiu.participant_curs TO 'PROFESOR';

GRANT SELECT ON studiu.activitate TO 'STUDENT';
GRANT SELECT,INSERT ON studiu.activitati_grup TO 'STUDENT';
GRANT SELECT ON studiu.curs TO 'STUDENT';
GRANT ALL ON studiu.fisa_participare TO 'STUDENT';
GRANT SELECT ON studiu.fisa_postului_curs TO 'STUDENT';
GRANT ALL ON studiu.grup TO 'STUDENT';
GRANT SELECT,INSERT ON studiu.membru_grup TO 'STUDENT';
GRANT ALL ON studiu.mesaj TO 'STUDENT';
GRANT ALL ON studiu.notificare TO 'STUDENT';
GRANT SELECT ON studiu.organizator TO 'STUDENT';
GRANT INSERT ON studiu.participant_curs TO 'STUDENT';
GRANT INSERT,SELECT ON studiu.participant_grup TO 'STUDENT';
GRANT SELECT ON studiu.profesori_invitati TO 'STUDENT';
GRANT SELECT ON studiu.student TO 'STUDENT';
GRANT SELECT ON studiu.users TO 'STUDENT';


CREATE USER 'demostudent'@'localhost' IDENTIFIED BY 'demostudent';
GRANT 'STUDENT' TO 'demostudent'@'localhost';
CREATE USER 'demoprofesor'@'localhost' IDENTIFIED BY 'demoprofesor';
GRANT 'PROFESOR' TO 'demoprofesor'@'localhost';
CREATE USER 'demoadmin'@'localhost' IDENTIFIED BY 'demoadmin';
GRANT 'ADMIN' TO 'demoadmin'@'localhost';
SHOW GRANTS FOR 'ADMIN';
SHOW GRANTS FOR 'PROFESOR';
SELECT* from mysql.user;

INSERT INTO users (CNP,email,username,parola,prenume,nume,adresa,telefon,nr_contract,IBAN,tip_user) VALUES 
('1900827395576','george@gmail.com','demoadmin','demoadmin','Demo', 'Admin','Cont cu grantare de rol de admin','7154190769','1','RO51RZBR45911153443419313',2),
('1900827395576','george@gmail.com','demoprofesor','demoprofesor','Demo', 'Profesor','Cont cu grantare de rol de profesor','7154190769','2','RO51RZBR45911153443419313',3),
('1900827395576','george@gmail.com','demostudent','demostudent','Demo', 'Student','Cont cu grantare de rol de students','7154190769','3','RO51RZBR45911153443419313',4);