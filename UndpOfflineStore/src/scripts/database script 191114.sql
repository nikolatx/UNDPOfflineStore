-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 14, 2019 at 11:26 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `undpofflinestore`
--

-- --------------------------------------------------------

--
-- Table structure for table `detaljifakture`
--

CREATE TABLE `detaljifakture` (
  `faktura_id` int(11) NOT NULL,
  `komponenta_id` int(11) NOT NULL,
  `cena` decimal(10,0) NOT NULL,
  `kolicina` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `detaljiprijemnice`
--

CREATE TABLE `detaljiprijemnice` (
  `prijemnica_id` int(11) NOT NULL,
  `komponenta_id` int(11) NOT NULL,
  `kolicina` int(11) NOT NULL,
  `cena` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `dobavljac`
--

CREATE TABLE `dobavljac` (
  `dobavljac_id` int(11) NOT NULL,
  `naziv` varchar(45) NOT NULL,
  `ulica` varchar(45) NOT NULL,
  `broj` varchar(10) NOT NULL,
  `grad` varchar(45) NOT NULL,
  `postanski_broj` varchar(6) NOT NULL,
  `drzava` varchar(45) NOT NULL,
  `telefon` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `dobavljac`
--

INSERT INTO `dobavljac` (`dobavljac_id`, `naziv`, `ulica`, `broj`, `grad`, `postanski_broj`, `drzava`, `telefon`) VALUES
(1, 'Alti d.o.o.', 'Dunavska', 'bb', 'Beograd', '11000', 'Srbija', '0116555705'),
(2, 'DS Computers', 'Bulevar Revolucije', '71', 'Beograd', '11274', 'Srbija', '0113085542'),
(3, 'Pit Computers', 'Kralja Petra I', '122', 'Kragujevac', '34000', 'Srbija', '034343544');

-- --------------------------------------------------------

--
-- Table structure for table `faktura`
--

CREATE TABLE `faktura` (
  `faktura_id` int(11) NOT NULL,
  `kupac_id` int(11) NOT NULL,
  `datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `komponenta`
--

CREATE TABLE `komponenta` (
  `komponenta_id` int(11) NOT NULL,
  `naziv` varchar(55) NOT NULL,
  `proizvodjac_id` int(11) NOT NULL,
  `tip_id` int(11) NOT NULL,
  `kolicina` int(11) NOT NULL,
  `cena` decimal(10,2) NOT NULL,
  `slika` varchar(59) DEFAULT NULL,
  `aktuelna` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `komponenta`
--

INSERT INTO `komponenta` (`komponenta_id`, `naziv`, `proizvodjac_id`, `tip_id`, `kolicina`, `cena`, `slika`, `aktuelna`) VALUES
(1, 'AM4 APU 220GE, 3.4GHz BOX', 2, 1, 28, '6890.00', 'AM4_APU_220GE_34Box.jpg', 1),
(2, 'Intel LGA1151 i3-9100F 3.6GHZ', 1, 1, 21, '11890.00', 'intel_i3_9100F_36B.jpg', 1),
(3, 'NVidia GeForce GTX1050Ti', 3, 4, 21, '21990.00', 'NVidia GTX1050 Ti Asus.jpg', 1),
(4, 'NVidia GeForce GTX1050Ti', 4, 4, 16, '21990.00', 'NVidia GTX1050 Ti MSI.jpg', 1),
(5, 'AM3500+ CPU', 2, 1, 0, '3550.00', 'amd_athlon_64_3_1_1.jpg', 0),
(11, 'AM4 PRIME X570-P', 3, 3, 5, '28990.00', 'AM4 PRIME X570-P.jpg', 1),
(12, 'AM4 TUF B450-Plus Gaming', 3, 3, 0, '14990.00', 'AM4 TUF B450-Plus Gaming.jpg', 1),
(13, 'LGA1151 Z390 ROG STRIX Z390-E GAMING', 3, 3, 0, '33990.00', 'LGA1151 Z390 ROG STRIX Z390-E GAMING.jpg', 1),
(14, 'LGA1151 Z390 ROG STRIX Z390-F GAMING', 3, 3, 0, '31990.00', 'LGA1151 Z390 ROG STRIX Z390-F GAMING.jpg', 1),
(15, 'LGA1151 Z390 ROG STRIX Z390-H GAMING', 3, 3, 0, '28490.00', 'LGA1151 Z390 ROG STRIX Z390-H GAMING.jpg', 1),
(16, 'Intel LGA1151 i5-9600KF, 3.7 GHz BOX', 1, 1, 0, '29990.00', 'Intel LGA1151 i5-9600KF, 3.7 GHz BOX.jpg', 1),
(17, 'Intel LGA1151 i7-8700K, 3.7GHz BOX', 1, 1, 0, '48990.00', 'Intel LGA1151 i7-8700K, 3.7GHz BOX.jpg', 1),
(18, 'Intel LGA1151 i9-9900K, 3.6GHz BOX', 1, 1, 0, '68990.00', 'Intel LGA1151 i9-9900K, 3.6GHz BOX.jpg', 1),
(19, 'AMD AM4 Ryzen 5 3600, 3.6GHz BOX', 2, 1, 0, '26490.00', 'AMD AM4 Ryzen 5 3600, 3.6GHz BOX.jpg', 1),
(20, 'AMD AM4 Ryzen 9 3900X, 4.6GHz BOX', 2, 1, 0, '69990.00', 'AMD AM4 Ryzen 9 3900X, 4.6GHz BOX.jpg', 1),
(21, 'AMD Radeon RX 570 XFX Hard Swap 4GB', 2, 4, 0, '20990.00', 'AMD Radeon RX 570 XFX Hard Swap 4GB.jpg', 1),
(22, 'AMD Radeon RX 5700 XT XFX 8GB', 2, 4, 0, '53990.00', 'AMD Radeon RX 5700 XT XFX 8GB.jpg', 1),
(23, 'Nvidia Quadro PNY K120 4GB', 9, 4, 0, '45990.00', 'Nvidia Quadro PNY K120 4GB.jpg', 1),
(24, 'HDD 2.5\" SATA3 5400 1TB ST1000LM048, 128MB', 10, 5, 0, '5990.00', '1TB Seagate ST1000LM048, 128MB.jpg', 1),
(25, 'HDD 3.5\" 5400 1TB WD Purple WD10PURZ, 64MB', 11, 5, 0, '5990.00', 'HDD 3.5 1TB WD Purple WD10PURZ, 64MB.jpg', 1),
(26, 'HDD 3.5\" SATA3 5400 2TB WD Red WD20EFRX, 64MB', 11, 5, 0, '9990.00', 'HDD 3.5 SATA3 5400 2TB WD Red WD20EFRX, 64MB.jpg', 1),
(27, 'SSD M.2 1TB Samsung 970 PRO V-NAND NVMe MZ-V7P1T0BW', 12, 6, 0, '39990.00', 'SSD M.2 1TB Samsung 970 PRO V-NAND NVMe MZ-V7P1T0BW.jpg', 1),
(28, 'SSD M.2 500GB Kingston SKC2000M8 NVME SKC2000M8/500G', 7, 6, 0, '13490.00', 'SSD M.2 500GB Kingston SKC2000M8 NVME SKC2000M8 500G.jpg', 1),
(29, 'DDR4 4x8GB 3200 HyperX Predator CL16, HX432C16PB3K4 32', 7, 2, 0, '24990.00', 'DDR4 4x8GB 3200 HyperX Predator CL16, HX432C16PB3K4 32.jpg', 1),
(30, 'DDR4 2x16GB 3200 HyperX Fury CL16 HX432C16FB3K2 32', 7, 2, 0, '24990.00', 'DDR4 2x16GB 3200 HyperX Fury CL16 HX432C16FB3K2 32.jpg', 1),
(31, 'DDR4 2x8GB 3200 HyperX Fury CL16 HX432C16FB3K2 16', 7, 2, 0, '11990.00', 'DDR4 2x8GB 3200 HyperX Fury CL16 HX432C16FB3K2 16.jpg', 1),
(32, 'DVD+ -RW USB Transcend TS8XDVDS-K, Ultra slim', 13, 7, 0, '3390.00', 'DVD+ -RW USB Transcend TS8XDVDS-K, Ultra slim.jpg', 1),
(33, 'DVD+ -RW ASUS DRW-24D5MT E-Green', 3, 7, 0, '1890.00', 'DVD+ -RW ASUS DRW-24D5MT E-Green.jpg', 1),
(34, 'Gaming 991B - Lighthouse, Black', 14, 8, 0, '12990.00', 'Gaming 991B - Lighthouse, Black.jpg', 1),
(35, 'Asus GT501 TUF GAMING', 3, 8, 0, '16490.00', 'Asus GT501 TUF GAMING.jpg', 1),
(36, 'Commander C32 TG ARGB, CA-1N3-00M1WN-00', 15, 8, 0, '12490.00', 'Commander C32 TG ARGB, CA-1N3-00M1WN-00.jpg', 1),
(37, 'Carbide 88R Black Window, CC-9011086-WW', 16, 8, 0, '6590.00', 'Carbide 88R Black Window, CC-9011086-WW.jpg', 1),
(38, '1200W Continuum C1200PL, 80Plus Platinum, Modular', 17, 9, 0, '28990.00', '1200W Continuum C1200PL, 80Plus Platinum, Modular.jpg', 1),
(39, '850W HCG850 Extreme, Modular 80PLUS Gold', 18, 9, 0, '21990.00', '850W HCG850 Extreme, Modular 80PLUS Gold.jpg', 1),
(40, '1600W UM-1600, 80 PLUS Gold', 19, 9, 0, '20090.00', '1600W UM-1600, 80 PLUS Gold.jpg', 1),
(41, 'CPU Hladnjak 775 1151 Hyper TX3i, RR-TX3E-22PK-B1', 20, 10, 0, '3190.00', 'CPU Hladnjak 775 1151 Hyper TX3i, RR-TX3E-22PK-B1.jpg', 1),
(42, 'CPU Hladnjak 11501151FM2+AM4 Hyper, RR-H412-20PK-R2', 20, 10, 0, '3190.00', 'CPU Hladnjak 11501151FM2+AM4 Hyper, RR-H412-20PK-R2.jpg', 1);

-- --------------------------------------------------------

--
-- Table structure for table `kupac`
--

CREATE TABLE `kupac` (
  `kupac_id` int(11) NOT NULL,
  `naziv` varchar(45) NOT NULL,
  `ulica` varchar(45) NOT NULL,
  `broj` varchar(10) NOT NULL,
  `grad` varchar(45) NOT NULL,
  `postanski_broj` varchar(6) NOT NULL,
  `drzava` varchar(45) NOT NULL,
  `telefon` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kupac`
--

INSERT INTO `kupac` (`kupac_id`, `naziv`, `ulica`, `broj`, `grad`, `postanski_broj`, `drzava`, `telefon`) VALUES
(1, 'Nike shop', 'Kralja Petra I', '28', 'Kragujevac', '34000', 'Srbija', '034308749'),
(2, 'Djak sport', 'Kralja Petra I', '26', 'Kragujevac', '34000', 'Srbija', '034308222'),
(3, 'Lilly', 'Atinska', '54', 'Kragujevac', '34000', 'Srbija', '034343233'),
(4, 'aas', 'sss', 'sss', 'sss', 'sss', 'sss', '123');

-- --------------------------------------------------------

--
-- Table structure for table `prijemnica`
--

CREATE TABLE `prijemnica` (
  `prijemnica_id` int(11) NOT NULL,
  `dobavljac_id` int(11) NOT NULL,
  `datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `proizvodjac`
--

CREATE TABLE `proizvodjac` (
  `proizvodjac_id` int(11) NOT NULL,
  `naziv` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `proizvodjac`
--

INSERT INTO `proizvodjac` (`proizvodjac_id`, `naziv`) VALUES
(1, 'Intel'),
(2, 'AMD'),
(3, 'Asus'),
(4, 'MSI'),
(5, 'HP'),
(6, 'Dell'),
(7, 'Kingston'),
(8, 'Philips'),
(9, 'NVidia'),
(10, 'Seagate'),
(11, 'WD'),
(12, 'Samsung'),
(13, 'Transcend'),
(14, 'LC Power'),
(15, 'Thermaltake'),
(16, 'Corsair'),
(17, 'Kolink'),
(18, 'Antec'),
(19, 'Prittec'),
(20, 'Cooler Master');

-- --------------------------------------------------------

--
-- Table structure for table `tip`
--

CREATE TABLE `tip` (
  `tip_id` int(11) NOT NULL,
  `naziv` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tip`
--

INSERT INTO `tip` (`tip_id`, `naziv`) VALUES
(1, 'procesori'),
(2, 'ram memorije'),
(3, 'maticne ploce'),
(4, 'graficke kartice'),
(5, 'hard diskovi'),
(6, 'ssd'),
(7, 'opticki uredjaji'),
(8, 'kucista'),
(9, 'napajanja'),
(10, 'hladnjaci, ventilatori'),
(11, 'adapteri'),
(12, 'kablovi'),
(13, 'konektori');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `detaljifakture`
--
ALTER TABLE `detaljifakture`
  ADD KEY `fk_faktura` (`faktura_id`),
  ADD KEY `fk_komponenta2` (`komponenta_id`);

--
-- Indexes for table `detaljiprijemnice`
--
ALTER TABLE `detaljiprijemnice`
  ADD PRIMARY KEY (`prijemnica_id`,`komponenta_id`),
  ADD KEY `fk_komponenta` (`komponenta_id`);

--
-- Indexes for table `dobavljac`
--
ALTER TABLE `dobavljac`
  ADD PRIMARY KEY (`dobavljac_id`);

--
-- Indexes for table `faktura`
--
ALTER TABLE `faktura`
  ADD PRIMARY KEY (`faktura_id`),
  ADD KEY `fk_kupac_id` (`kupac_id`);

--
-- Indexes for table `komponenta`
--
ALTER TABLE `komponenta`
  ADD PRIMARY KEY (`komponenta_id`),
  ADD KEY `fk_proizvodjac` (`proizvodjac_id`),
  ADD KEY `fk_tip` (`tip_id`);

--
-- Indexes for table `kupac`
--
ALTER TABLE `kupac`
  ADD PRIMARY KEY (`kupac_id`);

--
-- Indexes for table `prijemnica`
--
ALTER TABLE `prijemnica`
  ADD PRIMARY KEY (`prijemnica_id`),
  ADD KEY `fk_dobavljac` (`dobavljac_id`);

--
-- Indexes for table `proizvodjac`
--
ALTER TABLE `proizvodjac`
  ADD PRIMARY KEY (`proizvodjac_id`);

--
-- Indexes for table `tip`
--
ALTER TABLE `tip`
  ADD PRIMARY KEY (`tip_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `dobavljac`
--
ALTER TABLE `dobavljac`
  MODIFY `dobavljac_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `faktura`
--
ALTER TABLE `faktura`
  MODIFY `faktura_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `komponenta`
--
ALTER TABLE `komponenta`
  MODIFY `komponenta_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `kupac`
--
ALTER TABLE `kupac`
  MODIFY `kupac_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `prijemnica`
--
ALTER TABLE `prijemnica`
  MODIFY `prijemnica_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `proizvodjac`
--
ALTER TABLE `proizvodjac`
  MODIFY `proizvodjac_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `tip`
--
ALTER TABLE `tip`
  MODIFY `tip_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detaljifakture`
--
ALTER TABLE `detaljifakture`
  ADD CONSTRAINT `fk_faktura` FOREIGN KEY (`faktura_id`) REFERENCES `faktura` (`faktura_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_komponenta2` FOREIGN KEY (`komponenta_id`) REFERENCES `komponenta` (`komponenta_id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `detaljiprijemnice`
--
ALTER TABLE `detaljiprijemnice`
  ADD CONSTRAINT `fk_komponenta` FOREIGN KEY (`komponenta_id`) REFERENCES `komponenta` (`komponenta_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_prijemnica` FOREIGN KEY (`prijemnica_id`) REFERENCES `prijemnica` (`prijemnica_id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `faktura`
--
ALTER TABLE `faktura`
  ADD CONSTRAINT `fk_kupac_id` FOREIGN KEY (`kupac_id`) REFERENCES `kupac` (`kupac_id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `komponenta`
--
ALTER TABLE `komponenta`
  ADD CONSTRAINT `fk_proizvodjac` FOREIGN KEY (`proizvodjac_id`) REFERENCES `proizvodjac` (`proizvodjac_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_tip` FOREIGN KEY (`tip_id`) REFERENCES `tip` (`tip_id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `prijemnica`
--
ALTER TABLE `prijemnica`
  ADD CONSTRAINT `fk_dobavljac` FOREIGN KEY (`dobavljac_id`) REFERENCES `dobavljac` (`dobavljac_id`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
