Description
===========
The PriceUpdater.java will print the closing (or latest) price for each CUSIP in a text file. It is subscribing/tailing the prices from the CUSIP.txt file.

PriceUpdaterTest.java will write to the text file including price ticks for a set of bonds identified by their CUSIPs. CUSIP is
an 8-character alphanumeric string. Each CUSIP may have any number of prices (e.g., 95.752, 101.255) following it in sequence, one per line.

Usage
=====
Run PriceUpdaterTest.java as junit test to test the program.
It will write the CUSIP and price to file named CUSIP.txt in the classpath and we will be able to to see the latest price updates for each CUSIP print in console.

Logs
====
Set up console log (with logback) to print the output

Output format
=============
Format[Symbol: price] 
