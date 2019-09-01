-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 30, 2019 at 12:56 AM
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

--
-- Dumping data for table `detaljifakture`
--

INSERT INTO `detaljifakture` (`faktura_id`, `komponenta_id`, `cena`, `kolicina`) VALUES
(1, 1, '6890', 3),
(1, 3, '21990', 1),
(2, 2, '11890', 3),
(2, 3, '21990', 2),
(2, 1, '6890', 4),
(3, 1, '6890', 2),
(3, 2, '11890', 3),
(3, 3, '21990', 1),
(4, 1, '6890', 3),
(4, 2, '11890', 2),
(4, 3, '21990', 1);

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

--
-- Dumping data for table `detaljiprijemnice`
--

INSERT INTO `detaljiprijemnice` (`prijemnica_id`, `komponenta_id`, `kolicina`, `cena`) VALUES
(1, 1, 1, 6890),
(1, 4, 3, 21990),
(2, 1, 1, 6890),
(3, 1, 1, 6890),
(11, 1, 2, 6890),
(12, 3, 1, 21990),
(13, 4, 1, 21990),
(14, 1, 1, 6890),
(15, 1, 1, 6890),
(16, 2, 1, 11890),
(17, 2, 1, 11890),
(18, 1, 1, 6890),
(19, 1, 1, 6890),
(20, 2, 1, 11890),
(21, 3, 1, 21990),
(22, 2, 1, 11890),
(23, 2, 1, 11890),
(24, 1, 1, 6890),
(25, 2, 1, 11890),
(26, 1, 3, 6890),
(26, 2, 1, 11890),
(26, 4, 1, 21990),
(27, 1, 5, 6890),
(27, 2, 2, 11890),
(27, 3, 3, 21990),
(27, 4, 1, 21990),
(28, 1, 3, 6890),
(28, 2, 1, 11890),
(28, 3, 2, 21990),
(28, 4, 3, 21990),
(29, 1, 3, 6890),
(29, 2, 2, 11890),
(29, 3, 4, 21990),
(29, 4, 2, 21990);

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

--
-- Dumping data for table `faktura`
--

INSERT INTO `faktura` (`faktura_id`, `kupac_id`, `datum`) VALUES
(1, 1, '2019-08-28'),
(2, 2, '2019-08-28'),
(3, 3, '2019-08-28'),
(4, 1, '2019-08-28');

-- --------------------------------------------------------

--
-- Table structure for table `komponenta`
--

CREATE TABLE `komponenta` (
  `komponenta_id` int(11) NOT NULL,
  `naziv` varchar(45) NOT NULL,
  `proizvodjac_id` int(11) NOT NULL,
  `tip_id` int(11) NOT NULL,
  `kolicina` int(11) NOT NULL,
  `cena` decimal(10,2) NOT NULL,
  `slika` varchar(45) DEFAULT NULL,
  `aktuelna` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `komponenta`
--

INSERT INTO `komponenta` (`komponenta_id`, `naziv`, `proizvodjac_id`, `tip_id`, `kolicina`, `cena`, `slika`, `aktuelna`) VALUES
(1, 'AM4 APU 220GE, 3.4GHz BOX', 2, 1, 17, '6890.00', 'AM4_APU_220GE_34Box.jpg', 1),
(2, 'Intel LGA1151 i3-9100F 3.6GHZ', 1, 1, 9, '11890.00', 'intel_i3_9100F_36B.jpg', 1),
(3, 'NVidia GeForce GTX1050Ti', 3, 4, 12, '21990.00', 'NVidia GTX1050 Ti Asus.jpg', 1),
(4, 'NVidia GeForce GTX1050Ti', 4, 4, 10, '21990.00', 'NVidia GTX1050 Ti MSI.jpg', 1),
(5, 'AM3500+ CPU', 2, 1, 0, '3550.00', 'amd_athlon_64_3_1_1.jpg', 0),
(6, 'i7 6600k', 1, 1, 10, '34000.00', '', 1),
(7, 'i7 7000', 1, 1, 12, '54000.00', '', 1);

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
(3, 'Lilly', 'Atinska', '54', 'Kragujevac', '34000', 'Srbija', '034343233');

-- --------------------------------------------------------

--
-- Table structure for table `prijemnica`
--

CREATE TABLE `prijemnica` (
  `prijemnica_id` int(11) NOT NULL,
  `dobavljac_id` int(11) NOT NULL,
  `datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prijemnica`
--

INSERT INTO `prijemnica` (`prijemnica_id`, `dobavljac_id`, `datum`) VALUES
(1, 1, '2019-08-24'),
(2, 1, '2019-08-24'),
(3, 1, '2019-08-27'),
(11, 1, '2019-08-27'),
(12, 1, '2019-08-27'),
(13, 1, '2019-08-27'),
(14, 1, '2019-08-27'),
(15, 1, '2019-08-27'),
(16, 1, '2019-08-27'),
(17, 1, '2019-08-27'),
(18, 1, '2019-08-27'),
(19, 1, '2019-08-27'),
(20, 1, '2019-08-27'),
(21, 1, '2019-08-27'),
(22, 1, '2019-08-27'),
(23, 1, '2019-08-27'),
(24, 1, '2019-08-27'),
(25, 1, '2019-08-28'),
(26, 1, '2019-08-28'),
(27, 1, '2019-08-28'),
(28, 1, '2019-08-28'),
(29, 1, '2019-08-28');

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
(8, 'Philips');

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
  MODIFY `faktura_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `komponenta`
--
ALTER TABLE `komponenta`
  MODIFY `komponenta_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `kupac`
--
ALTER TABLE `kupac`
  MODIFY `kupac_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `prijemnica`
--
ALTER TABLE `prijemnica`
  MODIFY `prijemnica_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `proizvodjac`
--
ALTER TABLE `proizvodjac`
  MODIFY `proizvodjac_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

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
