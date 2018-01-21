#!/usr/bin/env python
import sys

# Missing temparature to omit:
MISSING = 9999

# Reading line by line:
for line in sys.stdin:
	line=line.strip()

	# Checking validity of a line:
	if(len(line) <94):
	#	print "Empty line"
		continue

	# Extracting year info:
	year = line[15:19]

	# Calculating airTemperature:
	airTemperature = 0
 	if (line[87] == '+') : # parseInt doesn't like leading plus signs
		airTemperature = line[88: 92]
	else :
		airTemperature = line[87: 92]

	# Taking only those data having valid quality in "01459" and airTemperature is the Missing one:
	quality = (line[92: 93])
	if (int(airTemperature) != MISSING and "01459".find(quality) >= 0) :
		print ('%s\t%s' % (year, airTemperature))
