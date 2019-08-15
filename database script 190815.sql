-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 15, 2019 at 07:21 PM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.4

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
  `naziv` varchar(45) NOT NULL,
  `dobavljac_id` int(11) NOT NULL,
  `tip_id` int(11) NOT NULL,
  `kolicina` int(11) NOT NULL,
  `cena` decimal(10,2) NOT NULL,
  `slika` varchar(45) DEFAULT NULL,
  `aktuelna` binary(1) NOT NULL,
  `dobavljac_dobavljac_id` int(11) NOT NULL,
  `tip_tip_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

-- --------------------------------------------------------

--
-- Table structure for table `tip`
--

CREATE TABLE `tip` (
  `tip_id` int(11) NOT NULL,
  `naziv` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `detaljifakture`
--
ALTER TABLE `detaljifakture`
  ADD KEY `fk_faktura_id` (`faktura_id`),
  ADD KEY `fk_komponenta_id` (`komponenta_id`);

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
  ADD KEY `fk_komponenta_dobavljac1_idx` (`dobavljac_dobavljac_id`),
  ADD KEY `fk_komponenta_tip1_idx` (`tip_tip_id`);

--
-- Indexes for table `kupac`
--
ALTER TABLE `kupac`
  ADD PRIMARY KEY (`kupac_id`);

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
  MODIFY `dobavljac_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `faktura`
--
ALTER TABLE `faktura`
  MODIFY `faktura_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `komponenta`
--
ALTER TABLE `komponenta`
  MODIFY `komponenta_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kupac`
--
ALTER TABLE `kupac`
  MODIFY `kupac_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tip`
--
ALTER TABLE `tip`
  MODIFY `tip_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detaljifakture`
--
ALTER TABLE `detaljifakture`
  ADD CONSTRAINT `fk_faktura_id` FOREIGN KEY (`faktura_id`) REFERENCES `faktura` (`faktura_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_komponenta_id` FOREIGN KEY (`komponenta_id`) REFERENCES `komponenta` (`komponenta_id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `faktura`
--
ALTER TABLE `faktura`
  ADD CONSTRAINT `fk_kupac_id` FOREIGN KEY (`kupac_id`) REFERENCES `kupac` (`kupac_id`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `komponenta`
--
ALTER TABLE `komponenta`
  ADD CONSTRAINT `fk_komponenta_dobavljac1` FOREIGN KEY (`dobavljac_dobavljac_id`) REFERENCES `dobavljac` (`dobavljac_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_komponenta_tip` FOREIGN KEY (`tip_tip_id`) REFERENCES `tip` (`tip_id`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
