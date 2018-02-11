#!/usr/bin/env python
import sys

# A dictionary to store year and air temparature 
year2airtemp= {}

# Creating Partitoner First:
# Reading line by line:
for line in sys.stdin:
	line=line.strip()
	
	# Extracting year and airtemp info:
	year,airtemp = line.split('\t')

	# Gathering airtemp together for each year:
	if year in year2airtemp:
		year2airtemp[year].append(int(airtemp))
	else:
		year2airtemp[year] = []
		year2airtemp[year].append(int(airtemp))
	

#Reducer
# making an average of airtemp for each year:
for year in year2airtemp.keys():
    ave_airtemp = sum(year2airtemp[year])*1.0 / len(year2airtemp[year])
    print '%s\t%s'% (year, ave_airtemp)
