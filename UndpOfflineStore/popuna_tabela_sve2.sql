INSERT INTO dobavljac (naziv, ulica, broj, grad, postanski_broj,drzava,telefon) VALUES
	('Alti d.o.o.', 'Dunavska', 'bb', 'Beograd', '11000', 'Srbija', '0116555705'),
	('DS Computers', 'Bulevar Revolucije', '71', 'Beograd', '11274', 'Srbija', '0113085542');

INSERT INTO tip (naziv) VALUES
	('procesori'),('ram memorije'), ('maticne ploce'), ('graficke kartice'), 
	('hard diskovi'), ('ssd'), ('opticki uredjaji'),('kucista'), ('napajanja'),('hladnjaci, ventilatori'),
	('adapteri'), ('kablovi'), ('konektori');
    
INSERT INTO kupac (naziv, ulica, broj, grad, postanski_broj, drzava, telefon) VALUES
	('Nike shop','Kralja Petra I','28','Kragujevac','34000','Srbija','034308749'),
	('Djak sport','Kralja Petra I','26','Kragujevac','34000','Srbija','034308222'),
	('Lilly','Atinska','54','Kragujevac','34000','Srbija','034343233');

INSERT INTO proizvodjac (naziv) VALUES
('Intel'), ('AMD'), ('Asus'), ('MSI'), ('HP'), ('Dell'), ('Kingston'), ('Philips');

INSERT INTO komponenta (naziv, proizvodjac_id, dobavljac_id, tip_id, kolicina, cena, slika, aktuelna) VALUES
('AM4 APU 220GE, 3.4GHz BOX', 2, 1, 1, 10, 6890.00, 'AthlonAM4_3_4', 1),
('Intel LGA1151 i3-9100F 3.6GHZ', 1, 2, 1, 5, 11890.00, 'Inteli39100F', 1),
('NVidia GeForce GTX1050Ti', 3, 1, 4, 6, 21990.00, 'GeForceGTX1050TiAsus', 1),
('NVidia GeForce GTX1050Ti', 4, 1, 4, 6, 21990.00, 'GeForceGTX1050TiMSI', 1);