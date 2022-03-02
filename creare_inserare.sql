drop database if exists studiu;
create database studiu;
use studiu;
create table users(
				id int auto_increment primary key, 
				CNP char(13) ,
                username VARCHAR(25) unique,
				parola char(32) ,
				nume varchar(60),
                prenume varchar(21),
                adresa varchar(101),
                telefon char(10),
                email varchar(30),
                nr_contract VARCHAR(25),
                IBAN varchar(50), -- AND tip_USER>tip user care cauta
                tip_user enum('SUPER-ADMIN','ADMIN','PROFESOR','STUDENT')
                );
create table student(
						user_id INT primary key,
						an tinyint,
                        nr_ore tinyint,
                        foreign key(user_id) references users(id));
create table profesor(user_id INT primary key,
							min_ore tinyint,
                            max_ore tinyint,
                            departament varchar(30),
						foreign key(user_id) references users(id));
create table curs(ID_curs int primary key auto_increment,
                    materie varchar(45) ,
                    an TINYINT,
                    tip ENUM('OPTIONAL','OBLIGATORIU'),
                    descriere text);
create table fisa_postului_curs(
							id INT AUTO_INCREMENT PRIMARY KEY,
							user_id INT,
							ID_curs INT,
                            foreign key(user_id) references profesor(user_id),
                            foreign key(ID_curs) references curs(ID_curs)
						);
create table fisa_participare(
								id INT AUTO_INCREMENT PRIMARY KEY,
								ID_curs int,
                                user_id int,
                                foreign key(user_id) references student(user_id),
                                foreign key(ID_curs) references curs(ID_curs));

create table activitate(ID_activitate int primary key auto_increment,
                        ID_curs int,
						tip enum ('laborator','seminar','curs'),
                        data_inceperii datetime,
                        data_incheierii datetime,
						frecventa enum('zilnic','saptamanal','saptamanal_alternant','lunar'),
                        durata time,
						nr_maxim_participanti int,
                        pondere TINYINT,
                        foreign key(ID_curs) references curs(ID_curs));
create table participant_curs(
	id INT AUTO_INCREMENT PRIMARY KEY,
	ID_activitate int,
	id_fisa_participare int,
	user_id INT,
	nota decimal(5,2)
	);
create table organizator(
						profesor_principal INT,
						user_id INT,
						ID_activitate int,
                        foreign key(profesor_principal) references fisa_postului_curs(user_id),
                        foreign key(user_id) references fisa_postului_curs(user_id),
                        foreign key(ID_activitate) references activitate(ID_activitate)
                        );
create table grup(ID_grup int primary key auto_increment,
			ID_curs int,
            nume varchar(30),
            descriere text
            );
create table membru_grup(
				id INT AUTO_INCREMENT PRIMARY KEY,
				ID_grup int,
				user_id INT,
				foreign key(user_id) references student(user_id));
create table mesaj(
					ID int auto_increment primary key,
					ID_grup int,
					user_id int,
					continut text,
                    data_postare datetime,
                    foreign key(user_id) references users(id)
                );
create table activitati_grup(ID_activitate int auto_increment primary key,
							ID_grup int,
                            nr_minim_participanti int,
                            timp_anuntare time,
                            data_incepere datetime,
                            descriere text
                         );
create table profesori_invitati(
							user_id int,
                            ID_activitate int,
                            foreign key(user_id) references fisa_postului_curs(user_id),
							foreign key(ID_activitate) references activitati_grup(ID_activitate));	
create table participant_grup(
							user_id int,
							ID_activitate int,
                            foreign key(user_id) references membru_grup(user_id)
							);	
create table notificare(receiver_ID int,
						sender_ID int,
                        sender_name varchar(100),
						descriere text,
                        expirare time,
                        foreign key(receiver_ID) references users(ID)); 
                        
                        
                        
alter table participant_curs
add constraint fk_participant_curs_fisa_participare
foreign key(id_fisa_participare)
references fisa_participare(id)
on update cascade
on delete cascade;

alter table grup
add constraint fk_fisa_participare_grup
foreign key(ID_curs)
references fisa_participare(ID_curs)
on update cascade
on delete cascade;


alter table membru_grup
add constraint fk_membru_grup_grup
foreign key(ID_grup)
references grup(ID_grup)
on update cascade
on delete cascade;



alter table mesaj
add constraint fk_mesaj_grup
foreign key(ID_grup)
references grup(ID_grup)
on update cascade
on delete cascade;

alter table activitati_grup
add constraint fk_activitati_grup_grup
foreign key(ID_grup)
references grup(ID_grup)
on update cascade
on delete cascade;

alter table participant_grup
add constraint fk_participant_grup_activitate_grup
foreign key(ID_activitate)
references activitati_grup(ID_activitate)
on update cascade
on delete cascade;

alter table participant_curs
add constraint fk_participant_curs_activitate
foreign key(ID_activitate)
references activitate(ID_activitate)
on update cascade
on delete cascade;

alter table curs
add unique (materie);

alter table users
modify CNP VARCHAR(13) not null;


INSERT INTO users (CNP,email,username,parola,prenume,nume,adresa,telefon,nr_contract,IBAN,tip_user) VALUES 
('1900827395576','george@gmail.com','boygeorge','boygeorge','Boy', 'George','3737 Emily Renzelli Boulevard Aptos, CA 95003','7154190769','11','RO51RZBR45911153443419313',1),
('2940101027948','elena@gmail.com','elena','elena','Elena','Wilson','4727 Williams Lane Wichita, KS 67213','9122560889','22','RO63RZBR8423251795571269',2),
('1961218388731','decebal@gmail.com','decebal','decebal','Decebal','Neagu','301 Fritsch Pines East Skye, RI 85612','0752460252','246/4542','RO35454967025462152956046511',2),
('2970830120484','bette@aol.com','bette','bette','Bette', 'Davis','3863 Little Street Akron, OH 44311','6173045495','09/66','RO96RZBR8362857243432419',3),
('1870729280930','arnold@yahoo.com','arnold','arnold','Arnold','Smith','1029 Hardman Road South Burlington, VT 05403','4197228759','01/77','RO42PORL4134932655413153',3),
('1850713147114','samuel@yahoo.com','samuel','samuel','Samuel','Ensley','2029  Sumner Street','8567228759','03/78','RO96RZBR3125426298933254',3),
('5010205103834','terrill.gerlach@hotmail.com','danill','danill','Danika','Farrell','95881 Orion Neck Suite 742 Walterborough, UT 47838','0788575306','07/23','RO77241918639133287703522587',3),
('5000501240906','baumbach.dortha@gmail.com','merleii','merleii','Merle','Russel', '1462 Josie Glens Oswaldofort, NC 75937','0267054403','09/21','RO18242475147363645828908676',3),
('287120322040','gardner38@yahoo.com','mroranii','mroranii','Oran ','Morar','9182 Jaime Island Kiehnberg, MD','0790938514','08/23','RO92351941873375451951062173',3),
('1920514123945','reynolds.shaylee@daniel.biz','florind','florind','Florida','Lind','46073 Roberts Estate Zulaufberg, DC 18220-4924','0340394764','06/21','RO49268364217826621384184017',3),
('2990227176746','mfadel@mertz.net','eloiki','eloiki','Eloisa','Mosciski','55796 Derek Fords Apt. 626 Maggiofurt, CT 64166','0783937161','10/22','RO82251064615249095218396586',3),
('6020222452001','shaniya04@lowe.biz','laneheog','laneheog','Lane','Herzog','757 Little Passage Mertzburgh, MI 16774','0702213410','03/23','RO56815980896954789993318316',3),
('5000416038408','heathcote.arturo@hotmail.com','domingska','domingska','Domingo','Prohaska','54813 Demetrius Ranch Apt. 182 Ankundingtown, SD 29748','0230688046','03/25','RO76174650345940088970481862',3),
('6010918248651','florida.bernier@gmail.com','mia','mia','Mia','Rempel','46140 Hagenes View Apt. 960 East Emily, OR 63902','0360327675','09/54','RO53957751772934652645498031',3),
('2970508453211','marcia.sporer@hotmail.com','ryan','ryan',' Ryann','Osinski', '514 Runte Manor Suite 204 North Winnifredmouth, CO 26557-7058','0339312364','11/51','RO86007693685967763940770980',3),
('5010413358465','josh59@tremblay.com','jule','jule','Justice','Little','40909 Annamae Causeway Kingbury, NV 18999-0068','0721643906','02/43','RO02767887149519334326989120',3),
('1890627121130','rachael53@veum.com','velma','velma','Velma','Ratke','2693 Kassulke Wall Lake Rosie, MS 51439-3029','0798554984','08/19','RO88341385264545715125810957',3),
('1920622148661','wgaylord@hotmail.com','joan','joan','Joan','Schuster','4596 Nitzsche Pine North Cheyennetown, HI 16494','0798554984','08/21','RO88341385264545715125810957',3),
('1950608117914','michael@gmail.com','michael','michael','Michael', 'Bonnie','4479 West Fork Drive Pompano Beach, FL 33064','7737083034','02/33', 'RO34RZBR5425528552556359',4),
('1910120050260','david@yahoo.com','bowiedavid','bowiedavid','David', 'Bowie','1618 Kovar Road Franklin, MA 02038','3125370060','06/44','RO11RZBR5218741712638981',4),
('2880606094562','blue@gmail.com','bluesteele','bluesteele','Blue', 'Steele','4503 Heavner Court Ankeny, IA 50021','7012857470','02/55','RO18PORL1559597241411352',4),
('1950608117914','gm@gmail.com','george','george','George', 'Michael','4479 West Fork Drive Pompano Beach, FL 33064','7737083034','04/33', 'RO34RZBR5425528552556359',4),
('1920619281939','theresa@gmail.com','webb','webb','Theresa', 'Webb','565  Yorkshire Circle 54687','2524888478','09/88', 'RO48PORL3572742368767112',4),
('2970414327852','nina@gmail.com','marion','marion','Nina', 'Marion','2525  Nash Street 654657','8472746261','12/99', 'RO98PORL6581983441412179',4),
('1890929148564','gerardluis@yahoo.com','luis','luis','Gerard', 'Luis','866  Waterview Lane, 87546','5057858093','07/10', 'RO28PORL2545969691429418',4),
('1951028203981','jaedaniels@gmail.com','daniels','daniels','Jae', 'Daniels','4479 West Fork Drive Pompano Beach, FL 33064','5205511768','05/33', 'RO49PORL4167434737819333',4),
('1950713030693','paul@gmail.com','paul','paul','Paul', 'Fugate','3418  Travis Street','9177988419','06/13', 'RO95RZBR5435933732563776',4),
('5011120056967','thomas@yahoo.com','thomas','thomas','Thomas', 'Lake','53  Settlers Lane','6452506605','08/14', 'RO19RZBR7773966959879821',4),
('1961129357845','ksmith@johnston.com','devon','devon','Devon','Bartoletti','8623 Buckridge Stravenue Apt. 746 South Dulcetown, HI 05868-5477','0371833117','11/43','RO34243473291671415703551152',4),
('2970228290177','aubree.durgan@hotmail.com','barry','barry','Barry','Cartwright','4149 Rippin Land West Benjaminside, ME 91068','0371833117','11/48','RO34243473291671415703551152',4),
('1911223159272','miller.maximillia@pagac.info','antonetta','antonetta','Antonetta','Waters','5758 Hill Glens Apt. 211 Lazaroborough, TX 82167','0316530391','02/48','RO92109841821845679037198200',4),
('1860618269895','schaden.lauriane@gmail.com','aliza','aliza','Aliza','Schroeder','541 VonRueden Ridge Apt. 470 Kuphalport, RI 61670','0749146390','17/54','RO86624075007296900682725956',4),
('5020829518734','briana.ullrich@bergnaum.net','lina','lina','Lina','Considine','1742 Kirlin Stream East Eleazarberg, AZ 56905-5196','0234656824','16/26','RO08190371677700803272767290',4),
('2921126273638','vencan@gmail.com','vencan','vencan','Veniamin','Ciocan','982 Bauch Overpass Lake Destiny, MI 46120-5876','0234656824','23/53','RO08190371677700803272767290',4),
('6010910224463','ferry.amani@schaefer.biz','carter','carter','Carter','Haag','491 Crooks Inlet Suite 427 East Carolyneside, NE 84062-3035','0709232316','05/22','RO54750869771566345542323025',4),
('1970719241627','keagan.gleichner@koepp.com','kaleb','kaleb','Kaleb','Wisoky','4580 Kobe Stravenue Apt. 331 Christiansenland, MA 60739','0709232316','13/33','RO09259421931837872062437924',4),
('1930526512101','ansley63@yahoo.com','samir','samir','Samir','Dare','34873 Hodkiewicz Fall Maximoborough, CO 87881-9625','0317275999','45/32','RO90333127704798421820785797',4),
('2940711082325','nayeli38@hotmail.com','clara','clara','Clarissa','Gleason','6199 Ignacio Ports Apt. 121 Jeromebury, TN 82679-4533','0772761199','23/35','RO77503338278090072976830991',4),
('2950726229689','parker.jast@nitzsche.com','roberto','roberto','Roberto','Koepp','761 Dario Hill Apt. 901 East Laila, RI 37042-6999','0745976772','26/62','RO56214647485004144847834609',4),
('1960708251230','dare.alvena@kunze.com','cezar','cezar','Cezar','Keeling','7953 Toy Station Apt. 269 South Ryleightown, ND 69572','0231315967','46/25','RO35161702680480449453839870',4),
('5020512176888','oberbrunner.hector@gmail.com','noble','noble','Noble','Rice','5769 Charles Parkway Lake Genevieve, WV 48306-0488','0773832580','42/89','RO65751559056507960920309367',4),
('2911123254057','zkeeling@mertz.biz','ora','ora','Ora','Jerde','820 Jenkins Brooks Apt. 498 Denesikburgh, NJ 79392','0751768019','63/84','RO14451511034728936380820484',4),
('2961005173022','dean98@bechtelar.com','clau','clau','Claudie','Romaguera','86026 Reynolds Walk New Mohammadport, DE 59244-6583','0731437618','73/74','RO62599546555412244360994354',4),
('1990311277541','okuneva.sylvester@yahoo.com','lue','lue','Lue','Bashirian','1887 Sienna Alley Apt. 434 South Creola, OH 11760-5841','0262040171','73/13','RO37915030752622862928073977',4),
('6030628097789','kessler.jameson@yahoo.com','lulu','lulu','Lulu','Maggio','4503 Beatty Falls North Simeonberg, ME 02560','0317879264','956/32','RO38827567335688025742063303',4),
('1940623193447','rosario06@yahoo.com','arlie','arlie','Arlie','Roob','1043 Magdalen Junctions Lake Niachester, NJ 32612-6709','0763463025','56/792','RO79154228992686677747328752',4),
('6010814214084','luettgen.hulda@gulgowski.org','mary','mary','Mary','Rodriguez','435 Rylee Locks Apt. 606 Bartonville, CA 82821','0371461351','45/789','RO30445232269698921201389581',4),
('2980714517669','rcrona@schinner.com','julien','julien','Julien','Bailey','218 Sadie Burg Lednermouth, OK 22620','0255922880','485/63','RO87011022472492954977127752',4),
('2940515217836','fisher.alf@yahoo.com','octavia','octavia','Octavia','Cummings','74825 Greta Oval Hillsland, FL 17322-4949','0258600641','45/145','RO65725424385399774479047227',4),
('1930407139625','annetta.bins@oreilly.com','alena','alena','Alena','Goldner','65172 Rutherford Valley Apt. 387 North Shyann, NM 06299','0720585792','43/729','RO40086590595442843410060199',4),
('1970410058501','ernestina.fsdotir@lehner.biz','alisha','alisha','Alisha','Rau','969 Rudolph Center Jakubowskiview, OR 05687-700','0341119968','448/23','RO34085062263430051402008705',4),
('2870718088761','rusty.hudson@cole.com','wava','wava','Wava','Prosacco','6465 Tracey Village Suite 891 Lake Anibalfurt, IN 99513-5515','0754361874','75/952','RO40943137793683643418618322',4),
('2950603121011','white.erin@emmerich.biz','myron','myron','Myron','Gibson','63844 Gusikowski Skyway Suite 902 New Austin, ID 81039-6872','0336405501','42/324','RO06808307753149047614765905',4),
('2900719438871','kwintheiser@walter.com','hilario','hilario','Hilario','Morav','776 Torp Unions Stammland, GA 57143-3283','0313556235','46/7523','RO82102257789157961366202316',4),
('2970909093007','pfeffer.rose@langosh.info','fay','fay','Fay','Stokes','20760 Murray Avenue Suite 031 Grahambury, PA 62765','0742184891','125/44','RO10432472326806417547242488',4),
('1861222410475','faltenwerth@ankunding.com','enrique','enrique','Enrique','Von','603 Kristin Vista West Pearlineberg, GA 16627','0797898149','4565/1','RO56120520362874755270339715',4),
('5010304078099','kaylee.pfeffer@oconner.com','jarrod','jarrod','Jarrod','Dickens','29491 Hodkiewicz Prairie South Breanna, IN 19263','0787471756','5412/1','RO08019364232035111592304022',4),
('2921019113495','al.bergstrom@herzog.org','rebeca','rebeca','Rebeca','Kautzer','2406 Anderson Plains Suite 625 East Virgiefurt, PA 23056','0745715518','452/45','RO12488957727380257561234624',4),
('1861121466337','lebsack.gwen@runolfsson.info','christa','christa','Christa','Sipes','9844 Schimmel Path South Caylatown, LA 04032','0267198480','541/6','RO42654404785824594221031437',4),
('1860821319838','littel.branson@zulauf.com','gilberto','gilberto','Gilberto','Boehm','83336 Konopelski Village Apt. 692 Chelsieshire, OR 96974-7220','0259367439','73/421','RO55991412567608241489000559',4),
('1911121182918','weldon54@stamm.info','emerson','emerson','Emerson','Roy','591 Chase Ville Suite 415 Kadinview, VT 46137-2779','0785586092','426/43','RO77499126972957242249852964',4),
('2901111239298','paucek.autumn@goldner.biz','bradly','bradly','Bradly','Crooks','544 Barton Canyon Suite 667 New Tod, AR 35620-2421','0739070028','52/7233','RO13626755362693888176974887',4),
('1931118382853','kasey58@hotmail.com','trent','trent','Trent','Schimmel','761 Kendrick Shores Port Brennonshire, NH 34377','0737097483','543/2354','RO50801121047908661706489129',4),
('1990520063722','susie19@spinka.com','melissa','melissa','Melissa','Hara','124 Keeling Track Apt. 954 New Stephaniafort, GA 55126','0342526604','74/373','RO37856315119949593356693432',4),
('2990106317113','kara38@shields.info','norris','norris','Norris','Balisteri','54144 Wilkinson Ways Apt. 162 Bellamouth, OH 17404','0261229567','545/7','RO69026508489624512106381502',4),
('2870927307538','freddy.hickle@sawayn.info','erin','erin','Erin','Roll','3626 Noel Mission Hattietown, ME 07383-7419','0248801049','764/843','RO23157639018590393540360589',4),
('1880802433340','mireya30@yahoo.com','jamie','jamie','Jamie','Konopelski','8939 Boehm Parks Apt. 590 South Ford, GA 03670','0376482878','74/38','RO73815056661812786515419677',4);








INSERT INTO profesor(user_id, min_ore, max_ore, departament)
VALUES
(4,6,12,'Calculatoare'),
(5,5,10,'Calculatoare'),
(6,6,14,'Calculatoare'),
(7,4,14,'Calculatoare'),
(8,10,14,'Calculatoare'),
(9,8,13,'Matematica'),
(10,10,18,'Matematica'),
(11,10,20,'Automatica'),
(12,10,15,'Matematica'),
(13,8,12,'Matematica'),
(14,2,6,'Limbi straine'),
(15,8,16,'Limbi straine'),
(16,6,14,'Limbi straine'),
(17,8,12,'Automatica'),
(18,8,12,'Automatica');



INSERT INTO student(user_id, an,nr_ore)
VALUES
(19,1,28),
(20,1,30),
(21,1,28),
(22,1,30),
(23,1,32),
(24,1,28),
(25,1,30),
(26,1,30),
(27,1,28),
(28,1,28),
(29,1,28),
(30,1,28),
(31,1,28),
(32,1,28),
(33,1,28),
(34,1,28),
(35,1,28),
(36,1,28),
(37,1,28),
(38,1,28),
(39,1,28),
(40,1,28),
(41,1,28),
(42,1,28),
(43,1,28),
(44,1,28),
(45,1,28),
(46,1,28),
(47,2,28),
(48,2,28),
(49,2,28),
(50,2,28),
(51,2,28),
(52,2,28),
(53,2,28),
(54,2,28),
(55,2,28),
(56,2,28),
(57,2,28),
(58,2,28),
(59,2,28),
(60,2,28),
(61,3,28),
(62,3,28),
(63,3,28),
(64,3,28),
(65,3,28),
(66,3,28),
(67,3,28);


INSERT INTO curs(materie,an,tip)
VALUES('Programarea Calculatoarelor',1,2),
('Structuri de date si algoritmi',1,2),
('Proiectare logica',1,2),
('Proiectarea sistemelor numerice',1,2),
('Matematici speciale I',1,2),
('Matematici speciale II',1,2),
('Analiza matematica I',1,2),
('Analiza matematica II',1,2),
('Algebra',1,2),
('Engleza I',1,1),
('Engleza II',2,1),
('Engleza III',3,1),
('Franceza I',1,1),
('Franceza II',2,1),
('Franceza III',3,1),
('Germana I',1,1),
('Germana II',2,1),
('Germana III',3,1),
('Spaniola I',1,1),
('Spaniola II',2,1),
('Spaniola III',3,1),
('Algoritmi fundamentali',2,2),
('Programare orientata pe obiecte',2,2),
('Arhitectura calculatoarelor',2,2),
('Circuite analogice si numerice',2,2),
('Matematici speciale in inginerie I',2,2),
('Matematici speciale in inginerie II',2,2);


INSERT INTO activitate(ID_curs,tip,data_inceperii,data_incheierii,frecventa,durata,nr_maxim_participanti,pondere)
VALUES
(1,1,'2020-10-05 08:00:00','2021-01-11 08:00:00',2,'02:00:00',15,'30'), -- PC
(1,2,'2020-10-07 14:00:00','2021-01-13 14:00:00',3,'02:00:00',15,'20'),
(1,3,'2020-10-09 10:00:00','2021-01-15 10:00:00',2,'02:00:00',15,'50'), 
(1,3,'2020-10-07 10:00:00','2021-01-13 10:00:00',2,'02:00:00',15,'50'),-- PC 
(1,2,'2020-10-08 08:00:00','2021-01-14 08:00:00',3,'02:00:00',15,'20'), 
(1,1,'2020-10-06 10:00:00','2021-01-12 10:00:00',2,'02:00:00',15,'30'), 


(2,3,'2020-10-05 12:00:00','2021-01-11 12:00:00',2,'02:00:00',15,'60'),-- SDA
(2,1,'2020-10-07 16:00:00','2021-01-12 16:00:00',2,'02:00:00',15,'40'), -- SDA
(2,3,'2020-10-08 16:00:00','2021-01-13 16:00:00',2,'02:00:00',15,'60'), -- SDA
(2,1,'2020-10-09 12:00:00','2021-01-15 12:00:00',2,'02:00:00',15,'40'), -- SDA



(3,3,'2020-10-07 12:00:00','2021-01-13 12:00:00',2,'02:00:00',15,'50'), -- PL
(3,1,'2020-10-09 10:00:00','2021-01-15 10:00:00',2,'02:00:00',15,'50'),
(3,1,'2020-10-06 12:00:00','2021-01-12 12:00:00',2,'02:00:00',15,'50'),
(3,3,'2020-10-05 18:00:00','2021-01-11 18:00:00',2,'02:00:00',15,'50'),

(4,3,'2020-10-05 14:00:00','2021-01-11 14:00:00',2,'02:00:00',30,'70'), -- PSN
(4,1,'2020-10-06 08:00:00','2021-01-12 08:00:00',2,'02:00:00',30,'30'), -- PSN

(5,3,'2020-10-06 08:00:00','2021-01-12 08:00:00',2,'02:00:00',30,'90'), -- MSI
(5,2,'2020-10-05 14:00:00','2021-01-11 14:00:00',2,'02:00:00',30,'10'), -- MSI

(6,2,'2020-10-08 18:00:00','2021-01-14 18:00:00',2,'02:00:00',30,'0'), -- MSII
(6,3,'2020-10-08 12:00:00','2021-01-14 12:00:00',2,'02:00:00',30,'100'), -- MSII


(7,3,'2020-10-06 14:00:00','2021-01-12 14:00:00',2,'02:00:00',30,'75'), -- Analiza I
(7,2,'2020-10-05 10:00:00','2021-01-11 10:00:00',2,'02:00:00',30,'25'), -- Analiza I


(8,3,'2020-10-07 08:00:00','2021-01-13 08:00:00',2,'02:00:00',30,'85'), -- Analiza II
(8,2,'2020-10-09 16:00:00','2021-01-15 16:00:00',2,'02:00:00',30,'15'), -- Analiza II

(9,3,'2020-10-06 16:00:00','2021-01-12 16:00:00',2,'02:00:00',30,'85'), -- ALGA
(9,2,'2020-10-09 08:00:00','2021-01-15 08:00:00',2,'02:00:00',30,'15'), -- ALGA

(19,2,'2020-10-05 16:00:00','2021-01-11 16:00:00',2,'02:00:00',30,'90'), -- Spaniola I
(10,2,'2020-10-08 10:00:00','2021-01-14 10:00:00',2,'02:00:00',30,'100'), -- Engleza I
(16,2,'2020-10-08 14:00:00','2021-01-14 14:00:00',2,'02:00:00',30,'100'), -- Germana I
(13,2,'2020-10-09 14:00:00','2021-01-15 14:00:00',2,'02:00:00',30,'100'); -- Franceza I

INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (4,1),(5,1),(6,1); -- PC
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (4,2),(5,2),(6,2); -- SDA
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (7,3),(8,3); -- PL
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (7,4); -- PSN
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (12,5),(10,5); -- MSI I
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (13,6); -- MSII I
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (11,7),(9,7); -- Analiza I
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (17,8),(9,8); -- Analiza II
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (18,9); -- Alga
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (15,10); -- Engleza I
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (15,19); -- Spaniola I
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (14,16); -- Germana I
INSERT INTO fisa_postului_curs(user_id,ID_curs) VALUES (16,13); -- Franceza I



INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (4,4,3),(5,5,4),(4,4,2),(5,5,5),(4,6,1),(5,5,6); -- PC
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (4,4,7),(5,5,9),(4,6,8),(5,6,10); -- SDA
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (7,7,11),(7,7,14),(7,8,12),(7,8,13); -- PL
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (7,7,15),(7,7,16); -- PSN
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (12,12,17),(12,10,18); -- MSI I
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (13,13,20),(13,13,19); -- MSI II
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (11,11,21),(11,9,22); -- Analiza I
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (17,17,23),(17,9,24); -- Analiza II
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (18,18,25),(18,18,26); -- ALGA
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (15,15,27); -- Spaniola I
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (15,15,28); -- Engleza I
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (14,14,29); -- Germana I
INSERT INTO organizator(profesor_principal,user_id,ID_activitate) VALUES (16,16,30); -- Franceza I






