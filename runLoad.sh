#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
ant
ant run-all

sort -u rawUsers.dat > users.dat
sort -u rawItems.dat > items.dat
sort -u rawCategories.dat > categories.dat
sort -u rawBids.dat > bids.dat

# Run the load.sql batch file to load the data
mysql CS144 < load.sql
# mysql CS144 < queries.sql
rm *.dat