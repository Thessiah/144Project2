CREATE TABLE Users
(
	UserID VARCHAR(20) PRIMARY KEY, 
	Country VARCHAR(20), 
	Address VARCHAR(50), 
	SellerRating INT
);

CREATE TABLE Items
(
	ItemID BIGINT PRIMARY KEY, 
	UserID VARCHAR(20), 
	ItemName VARCHAR(50), 
	FirstBid DECIMAL(8,2), 
	BuyPrice DECIMAL(8,2),
	CurrentHighestBid DECIMAL(8,2),
	StartTime TIMESTAMP,
	EndTime TIMESTAMP,
	NumberOfBids INT, 
	Description VARCHAR(4000)
);

CREATE TABLE ItemCategory
(
	ItemID BIGINT PRIMARY KEY, 
	Category VARCHAR(20)
);

CREATE TABLE Bids
(
	UserID VARCHAR(20) PRIMARY KEY, 
	ItemID BIGINT, 
	BidTime TIMESTAMP, 
	Amount DECIMAL(8,2)
);
