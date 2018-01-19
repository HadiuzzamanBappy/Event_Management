-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 19, 2018 at 09:32 PM
-- Server version: 10.1.19-MariaDB
-- PHP Version: 7.0.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `eventmanagement`
--

-- --------------------------------------------------------

--
-- Table structure for table `eventcreation`
--

CREATE TABLE `eventcreation` (
  `id` int(11) NOT NULL,
  `title` varchar(30) DEFAULT NULL,
  `EventHoster` int(10) DEFAULT NULL,
  `StartRegistrationDate` varchar(30) DEFAULT NULL,
  `EndRegistrationDate` varchar(30) DEFAULT NULL,
  `EventStartDate` varchar(30) DEFAULT NULL,
  `OrganizedBy` varchar(50) DEFAULT NULL,
  `Venue` varchar(50) DEFAULT NULL,
  `ContactNo` varchar(30) DEFAULT NULL,
  `EventhLength` varchar(30) DEFAULT NULL,
  `Fees` varchar(30) DEFAULT NULL,
  `about` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eventcreation`
--

INSERT INTO `eventcreation` (`id`, `title`, `EventHoster`, `StartRegistrationDate`, `EndRegistrationDate`, `EventStartDate`, `OrganizedBy`, `Venue`, `ContactNo`, `EventhLength`, `Fees`, `about`) VALUES
(1, 'Ku Fest 2017', 7, '2017-11-20', '2017-11-30', '2017-12-01', 'CLUSTER', 'Khulna University,Khulna', '01932089409', '2', '300', 'This is about skill development contest event,anyoe can join in this event.'),
(5, 'See Happening', 1, '2017-12-12', '2017-12-18', '2017-12-22', 'Bappy', 'Khulna', '01932089509', '2', '300', 'this is demo event'),
(6, 'JZS RE-UNION 180th', 1, '2017-12-25', '2017-12-27', '2017-12-28', 'JZS Association', 'Jessore Zilla School', '01932089409', '2', '1000', 'about the reunion of all student and teacher');

-- --------------------------------------------------------

--
-- Table structure for table `eventuseradd`
--

CREATE TABLE `eventuseradd` (
  `id` int(11) NOT NULL,
  `organization` varchar(50) DEFAULT NULL,
  `contactno` varchar(50) DEFAULT NULL,
  `paymentmethod` varchar(50) DEFAULT NULL,
  `eventid` int(5) DEFAULT NULL,
  `userid` int(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `eventuseradd`
--

INSERT INTO `eventuseradd` (`id`, `organization`, `contactno`, `paymentmethod`, `eventid`, `userid`) VALUES
(2, 'ku', '01932089409', 'Direct', 5, 1),
(3, 'ku', '01932089406', 'Direct', 5, 7);

-- --------------------------------------------------------

--
-- Table structure for table `userinformation`
--

CREATE TABLE `userinformation` (
  `id` int(11) NOT NULL,
  `username` varchar(30) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `password` varchar(30) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `recoveryValue` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `userinformation`
--

INSERT INTO `userinformation` (`id`, `username`, `email`, `phone`, `password`, `city`, `recoveryValue`) VALUES
(1, 'Hadiuzzaman Bappy', 'hbappy79@gmail.com', '01932089409', 'bappy', 'Khulna', '123456'),
(7, 'fahim rahman', 'me@gmail.com', '01521365874', 'fahim', 'nilfamari', '323879');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `eventcreation`
--
ALTER TABLE `eventcreation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `EventHoster` (`EventHoster`);

--
-- Indexes for table `eventuseradd`
--
ALTER TABLE `eventuseradd`
  ADD PRIMARY KEY (`id`),
  ADD KEY `eventid` (`eventid`),
  ADD KEY `userid` (`userid`);

--
-- Indexes for table `userinformation`
--
ALTER TABLE `userinformation`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `eventcreation`
--
ALTER TABLE `eventcreation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `eventuseradd`
--
ALTER TABLE `eventuseradd`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `userinformation`
--
ALTER TABLE `userinformation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `eventcreation`
--
ALTER TABLE `eventcreation`
  ADD CONSTRAINT `eventcreation_ibfk_1` FOREIGN KEY (`EventHoster`) REFERENCES `userinformation` (`id`);

--
-- Constraints for table `eventuseradd`
--
ALTER TABLE `eventuseradd`
  ADD CONSTRAINT `eventuseradd_ibfk_1` FOREIGN KEY (`eventid`) REFERENCES `eventcreation` (`id`),
  ADD CONSTRAINT `eventuseradd_ibfk_2` FOREIGN KEY (`userid`) REFERENCES `userinformation` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
