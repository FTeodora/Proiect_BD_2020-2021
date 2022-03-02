SET SQL_SAFE_UPDATES = 0;

DELIMITER ;;
CREATE FUNCTION days_collide(original_date DATE,chosen_date DATE,freq VARCHAR(20)) RETURNS BOOLEAN
DETERMINISTIC
BEGIN
DECLARE zile INT;
SELECT DATEDIFF(original_date,chosen_date) INTO @no_days;
CASE freq
-- 'zilnic','saptamanal','saptamanal_alternant','lunar'
WHEN 'zilnic' THEN SET zile:=@no_days%1;
WHEN 'saptamanal' THEN SET zile:=@no_days%7;
WHEN 'saptamanal_alternant' THEN SET zile:=@no_days%14;
WHEN 'lunar' THEN SET zile:=@no_days%30;
END CASE;
IF(zile=0) THEN
	RETURN TRUE;
END IF;
RETURN FALSE;
END;;

DELIMITER ..
CREATE FUNCTION times_collide(d1 DATETIME,t1 TIME,d2 DATETIME,t2 TIME) RETURNS BOOLEAN
DETERMINISTIC
BEGIN
	DECLARE final_rez BOOLEAN;
    SET @begin1=DATE_FORMAT(d1,'%H:%i') ;
    SET @end1= DATE_FORMAT(ADDTIME(d1,t1),'%H:%i');
    SET @begin2= DATE_FORMAT(d2,'%H:%i');
    SET @end2= DATE_FORMAT(ADDTIME(d2,t2),'%H:%i') ;
    IF(@begin1>=@end2 OR @begin2>=@end1) THEN
		SET final_rez:=FALSE;
	ELSE
		SET final_rez:=TRUE;
	END IF;
    RETURN final_rez;
END..


DELIMITER //
CREATE FUNCTION nr_participanti_grup(ID int) RETURNS INT
DETERMINISTIC
BEGIN
DECLARE studenti,profesori,nr INT;
SELECT IFNULL(COUNT(*),0) FROM participant_grup 
WHERE  ID_activitate=ID INTO studenti;

SELECT IFNULL(COUNT(*),0) FROM profesori_invitati
WHERE ID_activitate=ID INTO profesori;

SET NR:=studenti+profesori;

RETURN NR;
END //

DELIMITER //
CREATE FUNCTION nr_participanti(ID int) RETURNS INT
DETERMINISTIC
BEGIN
DECLARE nr INT;
SELECT IFNULL(COUNT(*),0) FROM participant_curs p 
WHERE ID_activitate=ID INTO nr;
RETURN NR;
END //

DELIMITER ..
CREATE TRIGGER delete_user BEFORE DELETE ON users FOR EACH ROW
BEGIN
DELETE FROM notificare WHERE receiver_ID=OLD.id;
DELETE FROM mesaj WHERE user_ID=OLD.id ;
END ..

DELIMITER \\
CREATE TRIGGER delete_student BEFORE DELETE ON student FOR EACH ROW
BEGIN
DELETE FROM fisa_participare WHERE fisa_participare.user_ID=old.user_ID;
DELETE FROM membru_grup WHERE membru_grup.user_ID=old.user_ID;
END \\

DELIMITER ..
CREATE TRIGGER delete_profesor BEFORE DELETE ON profesor FOR EACH ROW
BEGIN
DELETE FROM fisa_postului_curs WHERE fisa_postului_curs.user_ID=old.user_ID;
END..

DELIMITER;;
CREATE TRIGGER delete_activity BEFORE DELETE ON activitate FOR EACH ROW
BEGIN
DELETE FROM parcitipant_curs WHERE participant_curs.ID_actvitate=OLD.ID_activitate;
DELETE FROM organizator WHERE organizator.ID_activitate=OLD.ID_activitate;
END ;;

DELIMITER ..
CREATE TRIGGER delete_course BEFORE DELETE ON curs FOR EACH ROW
BEGIN
DELETE FROM activitate WHERE activitate.ID_curs=OLD.ID_curs;
DELETE FROM fisa_postului_curs WHERE fisa_postului_curs.ID_curs=OLD.ID_curs;
DELETE FROM fisa_participare WHERE fisa_participare.ID_curs=OLD.ID_curs;
DELETE FROM grup WHERE grup.ID_curs=OLD.ID_curs;
END..

DELIMITER\\
CREATE TRIGGER remove_teacher BEFORE DELETE ON fisa_postului_curs FOR EACH ROW
BEGIN
DELETE FROM organizator WHERE profesor_principal=OLD.user_ID OR user_ID=OLD.user_ID;
DELETE FROM profesori_invitati WHERE profesori_invitati.user_ID=OLD.user_ID;
END\\

DROP trigger leave_course;
DELIMITER ;;
CREATE TRIGGER leave_course BEFORE DELETE ON fisa_participare FOR EACH ROW
BEGIN
	DELETE FROM participant_curs WHERE participant_curs.id_fisa_participare=old.id;
    DELETE FROM membru_grup WHERE membru_grup.user_ID=OLD.USER_ID AND membru_grup.ID_grup IN(SELECT ID_grup FROM grup WHERE grup.ID_curs=OLD.ID_curs);
END;;
show triggers;
drop trigger leave_group;

DELIMITER ..
CREATE TRIGGER leave_group BEFORE DELETE ON membru_grup FOR EACH ROW
BEGIN
    IF((SELECT COUNT(*) FROM membru_grup WHERE membru_grup.ID_grup=OLD.ID_grup)<0)THEN
		DELETE FROM grup WHERE grup.ID_grup=OLD.ID_grup;
	END IF;
END ..

DELIMITER ;;
CREATE TRIGGER delete_group BEFORE DELETE ON grup FOR EACH ROW
BEGIN
	DELETE FROM mesaj WHERE mesaj.ID_grup=OLD.ID_grup;
    DELETE FROM activitati_grup WHERE activitati_grup.ID_grup=OLD.ID_grup;
    DELETE FROM membru_grup WHERE membru_grup.ID_grup=OLD.ID_grup;
END;;

DELIMITER ..
CREATE TRIGGER cancel_activity BEFORE DELETE ON activitati_grup FOR EACH ROW
BEGIN
	DELETE FROM participant_grup WHERE participant_grup.ID_activitate=OLD.ID_activitate;
    DELETE FROM profesori_invitati WHERE profesori_invitati.ID_activitate=OLD.ID_activitate;
END ..

DELIMITER ;;
CREATE TRIGGER notify_cancel BEFORE DELETE ON participant_grup FOR EACH ROW
BEGIN
	DECLARE nume_grup VARCHAR(30);
    DECLARE data_activitate DATETIME;
    
    SELECT nume
    INTO nume_grup
    FROM grup 
    INNER JOIN activitati_grup 
    ON grup.ID_grup=activitati_grup.ID_grup AND activitati_grup.ID_activitate=OLD.ID_activitate ;
    
	SELECT data_incepere INTO data_activitate 
    FROM activitati_grup 
    WHERE activitati_grup.ID_activitate=OLD.ID_activitate ;
	INSERT INTO notificare(receiver_ID,sender_ID,sender_name,descriere,expirare) 
    VALUES(old.user_ID,0,nume_grup,CONCAT("Activitatea de pe data de ",data_activitate," a fost anulata"),TIMEDIFF(data_activitate,now()));
END ;;

DELIMITER ;;
CREATE TRIGGER notify_teacher_cancel BEFORE DELETE ON profesori_invitati FOR EACH ROW
BEGIN
	DECLARE nume_grup VARCHAR(30);
    DECLARE data_activitate DATETIME;
    
    SELECT nume
    INTO nume_grup
    FROM grup 
    INNER JOIN activitati_grup 
    ON grup.ID_grup=activitati_grup.ID_grup AND activitati_grup.ID_activitate=OLD.ID_activitate ;
    
	SELECT data_incepere INTO data_activitate 
    FROM activitati_grup 
    WHERE activitati_grup.ID_activitate=OLD.ID_activitate ;
	INSERT INTO notificare(receiver_ID,sender_ID,sender_name,descriere,expirare) 
    VALUES(old.user_ID,0,nume_grup,CONCAT("Activitatea de pe data de ",data_activitate," a fost anulata"),TIMEDIFF(data_activitate,now()));
END ;;

DROP TRIGGER notify_confirmation;
DELIMITER ;;
CREATE TRIGGER notify_confirmation AFTER INSERT ON participant_grup FOR EACH ROW
BEGIN
	DECLARE participanti_min INT;
    SELECT activitati_grup.nr_minim_participanti INTO participanti_min 
    FROM activitati_grup WHERE ID_activitate=NEW.ID_activitate;
    
	IF(nr_participanti_grup(NEW.ID_activitate)=participanti_min) THEN
    
    SELECT nume
    INTO @nume_grup
    FROM grup 
    INNER JOIN activitati_grup 
    ON grup.ID_grup=activitati_grup.ID_grup AND activitati_grup.ID_activitate=NEW.ID_activitate ;
    
	SELECT data_incepere INTO @data_activitate 
    FROM activitati_grup 
    WHERE activitati_grup.ID_activitate=NEW.ID_activitate ;
	INSERT INTO notificare(receiver_ID,sender_ID,sender_name,descriere,expirare) 
    VALUES(NEW.user_ID,0,@nume_grup,CONCAT("Activitatea de pe data de ",@data_activitate," a adunat suficienti participanti!"),TIMEDIFF(@data_activitate,now()));
	END IF;
END ;;

DELIMITER ;;
CREATE TRIGGER notify_teacher_confirmation AFTER INSERT ON profesori_invitati FOR EACH ROW
BEGIN
	DECLARE participanti_min INT;
    SELECT activitati_grup.nr_minim_participanti INTO participanti_min 
    FROM activitati_grup WHERE ID_activitate=NEW.ID_activitate;
    
	IF(nr_participanti_grup(NEW.ID_activitate)=participanti_min) THEN
    
    SELECT nume
    INTO @nume_grup
    FROM grup 
    INNER JOIN activitati_grup 
    ON grup.ID_grup=activitati_grup.ID_grup AND activitati_grup.ID_activitate=NEW.ID_activitate ;
    
	SELECT data_incepere INTO @data_activitate 
    FROM activitati_grup 
    WHERE activitati_grup.ID_activitate=NEW.ID_activitate ;
	INSERT INTO notificare(receiver_ID,sender_ID,sender_name,descriere,expirare) 
    VALUES(NEW.user_ID,0,@nume_grup,CONCAT("Activitatea de pe data de ",@data_activitate," a adunat suficienti participanti!"),TIMEDIFF(@data_activitate,now()));
	END IF;
END ;;

DELIMITER ..
CREATE EVENT countdown
ON SCHEDULE EVERY 1 MINUTE
ON COMPLETION PRESERVE
DO
BEGIN
	UPDATE activitati_grup 
	SET timp_anuntare=timediff(timp_anuntare, '00:01:00')  
	WHERE timp_anuntare>TIME('00:00:00');
        
    UPDATE notificare 
    SET expirare=timediff(expirare, '00:01:00');
    
	DELETE FROM activitati_grup 
    WHERE timp_anuntare<=TIME('00:00:00') 
    AND nr_participanti_grup(ID_activitate)<nr_minim_participanti;
    
	DELETE FROM notificare 
    WHERE expirare<=TIME('00:00:00');
END ..
