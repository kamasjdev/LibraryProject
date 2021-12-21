INSERT INTO `authors` (`Id`, `FIRST_NAME`, `LAST_NAME`) VALUES
(1, 'John Ronald Reuel', 'Tolkien'),
(2, 'Henryk', 'Sienkiewicz'),
(3, 'Lewis', 'Carroll');

INSERT INTO `books` (`Id`, `Book_name`, `ISBN`, `cost`, `borrowed`) VALUES
(1, 'Alicja w krainie czarów', '12', '25.0000', 0),
(2, 'Hobbit', '12345', '50.0000', 1),
(3, 'W pustyni i w puszczy', '5578', '20.0000', 0);

INSERT INTO `customers` (`Id`, `FIRST_NAME`, `LAST_NAME`, `can_borrow`, `books_limit`) VALUES
(1, 'Adam', 'Małysz', 1, 0),
(2, 'Andrzej', 'Duda', 1, 0),
(3, 'Jerzy', 'Zięba', 1, 0);

INSERT INTO `bookauthor` (`Id`, `Book_Id`, `Author_Id`) VALUES
(1, 1, 1),
(2, 1, 3),
(3, 2, 1),
(4, 3, 2);

INSERT INTO `bookcustomer` (`Id`, `Customer_Id`, `Book_Id`) VALUES
(1, 1, 1),
(3, 2, 1),
(2, 1, 2),
(4, 3, 3);

INSERT INTO `bills` (`Id`, `cost`, `customer_id`) VALUES
(1, '25.0000', 1),
(2, '25.0000', 2),
(3, '50.0000', 1),
(4, '20.0000', 3);