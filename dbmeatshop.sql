-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 13, 2019 at 03:34 PM
-- Server version: 10.1.40-MariaDB
-- PHP Version: 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbmeatshop`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customerID` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `orderline`
--

CREATE TABLE `orderline` (
  `id` int(11) NOT NULL,
  `orderID` bigint(20) NOT NULL,
  `productID` int(11) NOT NULL,
  `orderQty` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Stand-in structure for view `orderlineview`
-- (See below for the actual view)
--
CREATE TABLE `orderlineview` (
`orderID` bigint(20)
,`productName` varchar(255)
,`orderQty` int(11)
);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `orderID` bigint(20) NOT NULL,
  `customerID` bigint(20) NOT NULL,
  `orderDate` datetime NOT NULL,
  `amountPaid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `productID` int(10) NOT NULL,
  `productName` varchar(255) DEFAULT NULL,
  `price` int(10) NOT NULL,
  `productQty` int(10) NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`productID`, `productName`, `price`, `productQty`, `status`) VALUES
(1, 'Pork Chop', 210, 93, 1),
(2, 'Pork Adobo Cut', 230, 98, 1),
(3, 'Pork Bones', 180, 100, 1),
(4, 'Pork Feet', 200, 100, 1),
(5, 'Pork Belly', 250, 98, 1),
(6, 'Pork Ribs', 210, 100, 1),
(7, 'Pork Steak', 300, 99, 1),
(8, 'Bacon Cut', 280, 98, 1),
(9, 'Ground Pork', 200, 90, 1),
(10, 'Head Skin', 240, 84, 1),
(11, 'Chicken Breast', 160, 100, 1),
(12, 'Chicken Drumstick', 160, 99, 1),
(13, 'Chicken Wings', 140, 98, 1),
(14, 'Chicken Neck', 145, 94, 1),
(15, 'Chicken Thigh', 150, 85, 1),
(16, 'Chicken Leg', 160, 98, 1),
(17, 'Chicken Liver', 150, 100, 1),
(18, 'Chicken Gizzard', 140, 100, 1),
(19, 'Chicken Feet', 150, 98, 1);

-- --------------------------------------------------------

--
-- Table structure for table `user_account`
--

CREATE TABLE `user_account` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `inventoryAllowed` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_account`
--

INSERT INTO `user_account` (`id`, `username`, `password`, `status`, `inventoryAllowed`) VALUES
(1, 'admin', 'admin', 1, 1),
(2, 'kyle', '234', 1, 0),
(3, 'kket', '123', 1, 1),
(4, 'a', 'a', 1, 0),
(5, 'f', 'f', 1, 1),
(6, 'micay', 'micay', 1, 0);

-- --------------------------------------------------------

--
-- Structure for view `orderlineview`
--
DROP TABLE IF EXISTS `orderlineview`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `orderlineview`  AS  select `orderline`.`orderID` AS `orderID`,`product`.`productName` AS `productName`,`orderline`.`orderQty` AS `orderQty` from (`orderline` join `product` on((`orderline`.`productID` = `product`.`productID`))) ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customerID`);

--
-- Indexes for table `orderline`
--
ALTER TABLE `orderline`
  ADD PRIMARY KEY (`id`),
  ADD KEY `orderID` (`orderID`),
  ADD KEY `productID` (`productID`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`orderID`),
  ADD KEY `customerID` (`customerID`) USING BTREE;

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`productID`);

--
-- Indexes for table `user_account`
--
ALTER TABLE `user_account`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `orderline`
--
ALTER TABLE `orderline`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `productID` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `user_account`
--
ALTER TABLE `user_account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orderline`
--
ALTER TABLE `orderline`
  ADD CONSTRAINT `orderline_ibfk_1` FOREIGN KEY (`orderID`) REFERENCES `orders` (`orderID`),
  ADD CONSTRAINT `orderline_ibfk_2` FOREIGN KEY (`productID`) REFERENCES `product` (`productID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
