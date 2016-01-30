SELECT COUNT(*)
FROM Users;

SELECT COUNT(*)
FROM Items
WHERE UserID IN (SELECT UserID
	FROM Users
	WHERE Address = 'New York');

SELECT COUNT(*)
FROM (SELECT ItemID
	  FROM ItemCategory
	  GROUP BY ItemID
	  HAVING COUNT(Category) = 4) X;
	 
SELECT ItemID
FROM Items
WHERE CurrentHighestBid = (SELECT MAX(CurrentHighestBid)
				   FROM Items
				   WHERE EndTime > '2001-12-20 00:00:01' AND NumberOfBids > 0) AND NumberOfBids > 0;

SELECT COUNT(*)
FROM Users
WHERE UserID in (SELECT UserID
                 From Items) AND SellerRating > 1000;

SELECT Count(*)
FROM Users
WHERE UserID IN (SELECT UserID
                 FROM Items) AND UserID IN (SELECT UserID FROM Bids);

SELECT Count(DISTINCT Category)
FROM ItemCategory
WHERE ItemID IN (SELECT ItemID
	FROM Bids
	WHERE Amount > 100);

