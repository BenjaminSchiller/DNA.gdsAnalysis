import sys, numpy

if len(sys.argv) != 3:
	print "expecting 2 arguments: $fileName $prefix"
	sys.exit()

fileName = sys.argv[1]
prefix = sys.argv[2]

with open(fileName, "r") as ins:
	values = []
	for line in ins:
		if line.startswith(prefix):
			# print line.replace(prefix,'').replace('\n','')
			values.append(float(line.replace(prefix,'').replace('\n','')))

if len(values) == 0:
	print "0	0	0	0	0"
	sys.exit()

# 1: avg
# 2: min
# 3: max
# 4: med
# 5: std

avgV = numpy.mean(values)
minV = numpy.min(values)
maxV = numpy.max(values)
medV = numpy.median(values)
stdV = numpy.std(values)

# print "avg: %f" % avgV
# print "min: %i" % minV
# print "max: %i" % maxV
# print "med: %i" % medV
# print "std: %f" % stdV

print "%f	%i	%i	%i	%f" % (avgV, minV, maxV, medV, stdV)
