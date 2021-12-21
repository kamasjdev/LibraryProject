# LibraryProject

Schemat bazy:
```sql
-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 21 Gru 2021, 18:52
-- Wersja serwera: 10.4.21-MariaDB
-- Wersja PHP: 7.4.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `library`
--
CREATE DATABASE IF NOT EXISTS `library` DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci;
USE `library`;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `authors`
--

DROP TABLE IF EXISTS `authors`;
CREATE TABLE IF NOT EXISTS `authors` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(200) COLLATE utf8_polish_ci NOT NULL,
  `LAST_NAME` varchar(200) COLLATE utf8_polish_ci NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `bills`
--

DROP TABLE IF EXISTS `bills`;
CREATE TABLE IF NOT EXISTS `bills` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `cost` decimal(18,4) NOT NULL,
  `customer_id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `fk_bill_customer` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `bookauthor`
--

DROP TABLE IF EXISTS `bookauthor`;
CREATE TABLE IF NOT EXISTS `bookauthor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Book_Id` int(11) NOT NULL,
  `Author_Id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UI_Book_Author` (`Book_Id`,`Author_Id`),
  KEY `Author_Id` (`Author_Id`),
  KEY `Book_Id` (`Book_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `bookcustomer`
--

DROP TABLE IF EXISTS `bookcustomer`;
CREATE TABLE IF NOT EXISTS `bookcustomer` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Customer_Id` int(11) NOT NULL,
  `Book_Id` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Customer_Id` (`Customer_Id`,`Book_Id`),
  KEY `Book_Id` (`Book_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `books`
--

DROP TABLE IF EXISTS `books`;
CREATE TABLE IF NOT EXISTS `books` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Book_name` varchar(100) COLLATE utf8_polish_ci NOT NULL,
  `ISBN` varchar(25) COLLATE utf8_polish_ci NOT NULL,
  `cost` decimal(18,4) NOT NULL,
  `borrowed` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `UI_book_isbn` (`ISBN`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `customers`
--

DROP TABLE IF EXISTS `customers`;
CREATE TABLE IF NOT EXISTS `customers` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(100) COLLATE utf8_polish_ci NOT NULL,
  `LAST_NAME` varchar(100) COLLATE utf8_polish_ci NOT NULL,
  `can_borrow` tinyint(1) NOT NULL DEFAULT 0,
  `books_limit` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `bills`
--
ALTER TABLE `bills`
  ADD CONSTRAINT `fk_bill_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`Id`);

--
-- Ograniczenia dla tabeli `bookauthor`
--
ALTER TABLE `bookauthor`
  ADD CONSTRAINT `bookauthor_ibfk_1` FOREIGN KEY (`Book_Id`) REFERENCES `books` (`Id`),
  ADD CONSTRAINT `bookauthor_ibfk_2` FOREIGN KEY (`Author_Id`) REFERENCES `authors` (`Id`);

--
-- Ograniczenia dla tabeli `bookcustomer`
--
ALTER TABLE `bookcustomer`
  ADD CONSTRAINT `bookcustomer_ibfk_1` FOREIGN KEY (`Customer_Id`) REFERENCES `customers` (`Id`),
  ADD CONSTRAINT `bookcustomer_ibfk_2` FOREIGN KEY (`Book_Id`) REFERENCES `books` (`Id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
