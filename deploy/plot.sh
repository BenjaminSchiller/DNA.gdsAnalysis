#!/bin/bash

source config.sh

# 1 => 2: avg
# 2 => 3: min
# 3 => 4: max
# 4 => 5: med
# 5 => 6: std

function plotExecutionTime {
	main=$1
	dataset=$2
	metrics=$3
	batches=$4

	dir="$main/$dataset/$metrics/$batches"
	plotDir="$mainPlotDir/execution/$dataset/$batches/"
	if [[ ! -d "$plotDir" ]]; then mkdir -p $plotDir; fi
	output="$plotDir/runtime--$metrics.png"
	echo "set terminal png size 1024,800"
	echo "set output '$output'"
	echo "set key outside"
	echo "set style fill solid border -1"
	echo "set title '$dataset: $(metricName $metrics) ($batches batches)'"
	echo "set boxwidth 0.8"
	echo "set ylabel 'runtime (sec)'"
	echo "f(x) = 0"
	echo "plot f(x) notitle, \\"
	i=1
	for setup in $(ls $dir | grep -v '1-20--'); do
		name=$setup
		if [[ $name = default* ]]; then array=(${name//--/ }); name="${array[2]}"; fi
		echo "'-' using 1:(\$5/1000/1000/1000) with boxes lt $i title '$name', \\"
		echo "'-' using 1:(\$2/1000/1000/1000):(\$6/1000/1000/1000) with errorbars lt rgb '#000000' notitle, \\"
		i=$((i+1))
	done
	echo "f(x) notitle"
	i=1
	for setup in $(ls $dir | grep -v '1-20--'); do
		aggr=$(tail -n 1 $dir/$setup/aggr)
		echo "$i	$aggr"
		echo "EOF"
		echo "$i	$aggr"
		echo "EOF"
		i=$((i+1))
	done
}

function plotExecutionSpeedup {
	main=$1
	dataset=$2
	metrics=$3
	batches=$4

	dir="$main/$dataset/$metrics/$batches"
	plotDir="$mainPlotDir/execution/$dataset/$batches/"
	if [[ ! -d "$plotDir" ]]; then mkdir -p $plotDir; fi
	output="$plotDir/speedup--$metrics.png"
	echo "set terminal png size 1024,800"
	output="$plotDir/speedup--$metrics.pdf"
	echo "set terminal pdf font ',18'"
	echo "set output '$output'"
	echo "set key top left"
	echo "set style fill solid border -1"
	# echo "set title '$dataset: $(metricName $metrics) ($batches batches)'"
	echo "set xtics ('cfg*' 1, 'A' 2, 'AL' 3, 'HAL' 4, 'HM' 5, 'HS' 6, 'HT' 7, 'LL' 8)"
	echo "set xtics rotate by -45 offset 0,0"
	echo "set boxwidth 0.8"
	echo "set ylabel 'speedup (relative to cfg*)'"
	echo "set yrange [0:6]"
	echo "f(x) = 0"
	echo "set arrow from 0,1 to 9,1 lw 4 nohead front"
	echo "plot f(x) notitle, \\"
	i=1
	for setup in $(ls $dir | grep -v '1-20--'); do
		name=$setup
		if [[ $name = default* ]]; then array=(${name//--/ }); name="${array[2]}"; fi
		echo "'-' using 1:(\$5/\$10) with boxes lt $i notitle, \\"
		# echo "'-' using 1:(\$5/\$10) with boxes lt $i title '$name', \\"
		i=$((i+1))
	done
	echo "f(x) notitle"
	for setup in $(ls $dir | grep '1-20' | grep -v '1-20--'); do
		recommendation=$(tail -n 1 $dir/$setup/aggr)
		break
	done
	for setup in $(ls $dir | grep 'MedSD_py'); do
		recommendation=$(tail -n 1 $dir/$setup/aggr)
		break
	done
	i=1
	for setup in $(ls $dir | grep -v '1-20--'); do
		aggr=$(tail -n 1 $dir/$setup/aggr)
		echo "$i	$aggr	$recommendation"
		echo "EOF"
		i=$((i+1))
	done
}

function plotExecutionEstimation {
	main=$1
	dataset=$2
	metrics=$3
	batches=$4

	dir="$main/$dataset/$metrics/$batches"
	plotDir="$mainPlotDir/execution/$dataset/$batches/"
	if [[ ! -d "$plotDir" ]]; then mkdir -p $plotDir; fi
	output="$plotDir/estimation--$metrics.png"
	echo "set terminal png size 1024,800"
	echo "set output '$output'"
	echo "set key outside"
	echo "set style fill solid border -1"
	echo "set title '$dataset: $(metricName $metrics) ($batches batches)'"
	echo "set boxwidth 0.8"
	echo "set ylabel 'speedup (relative to our recommendation)'"
	echo "f(x) = 0"
	echo "set arrow from 0,1 to 10,1 lw 2 nohead front"
	echo "plot f(x) notitle, \\"
	i=1
	for setup in $(ls $dir); do
		name=$setup
		if [[ $name = default* ]]; then
			array=(${name//--/ })
			echo "'-' using (\$1*2+0):(\$5/\$10) with boxes lt $i title '${array[2]}', \\"
			echo "'-' using (\$1*2+1):(\$3/\$5) with boxes lt $i notitle, \\"
		else
			echo "'-' using (\$1*2+0):(\$5/\$10) with boxes lt $i title '$name', \\"
			echo "'-' using (\$1*2+1):(\$3/\$5) with boxes lt $i notitle, \\"
		fi
		i=$((i+1))
	done
	echo "f(x) notitle"
	for setup in $(ls $dir | grep '1-20' | grep -v '1-20--'); do
		recommendation=$(tail -n 1 $dir/$setup/aggr)
		break
	done
	for setup in $(ls $dir | grep 'MedSD_py'); do
		recommendation=$(tail -n 1 $dir/$setup/aggr)
		break
	done
	i=1
	for setup in $(ls $dir | grep -v '1-20--'); do
		aggr=$(tail -n 1 $dir/$setup/aggr)
		echo "$i	$aggr	$recommendation"
		echo "EOF"
		# aggr=$(tail -n 1 $dir/$setup/aggr)
		# echo "$i	$aggr	$recommendation"
		# echo "EOF"
		estimationOfRecommendation=$(cat $mainEstimationDir/$dataset/AVG/MedSD/1-20.estimation | head -n 1)
		s=$setup
		s=${s/default-DArray--/}
		s=${s/default-DArrayList--/}
		s=${s/default-DHashArrayList--/}
		s=${s/default-DHashMap--/}
		s=${s/default-DHashSet--/}
		s=${s/default-DHashTable--/}
		s=${s/default-DLinkedList--/}
		estimation=$(cat $mainEstimationDir/$dataset/AVG/MedSD/1-20.estimation | grep $s)
		echo "$i	$estimation	$estimationOfRecommendation"
		echo "EOF"
		i=$((i+1))
	done
}

function plotForBatchesAbsolute {
	main=$1
	dataset=$2
	metrics=$3

	dir="$main/$dataset/$metrics"
	plotDir="$mainPlotDir/execution/$dataset/byBatches/"
	if [[ ! -d "$plotDir" ]]; then mkdir -p $plotDir; fi
	output="$plotDir/absolute--$metrics.png"
	echo "set terminal png size 1024,800"
	echo "set output '$output'"
	echo "set key outside"
	echo "set style fill solid border -1"
	echo "set title '$dataset: $(metricName $metrics) ($batches batches)'"
	echo "set boxwidth 0.8"
	echo "set ylabel 'runtime (sec)'"
	echo "set xlabel '# of batches'"
	echo "f(x) = 0"
	echo "set arrow from 0,1 to 10,1 lw 2 nohead front"
	echo "plot f(x) notitle, \\"
	for batches in $(ls $dir | sort -n); do firstBatches=$batches; break; done
	for setup in $(ls $dir/$firstBatches | grep '1-20' | grep -v '1-20--'); do recommendation=$setup; break; done
	i=1
	for setup in $(ls $dir/$firstBatches); do
		name=$setup
		if [[ $name = default* ]]; then array=(${name//--/ }); name="${array[2]}"; fi
		echo "'-' using 1:(\$5/1000/1000/1000) with linespoint lt $i lw 4 title '$name', \\"
		i=$((i+1))
	done
	echo "f(x) notitle"
	for setup in $(ls $dir/$firstBatches); do
		for batches in $(ls $dir | sort -n); do
			aggr1=$(tail -n 1 $dir/$batches/$setup/aggr)
			aggr2=$(tail -n 1 $dir/$batches/$recommendation/aggr)
			echo "$batches	$aggr1	$aggr2"
		done
		echo "EOF"
	done
}

function plotForBatchesRelative {
	main=$1
	dataset=$2
	metrics=$3

	dir="$main/$dataset/$metrics"
	plotDir="$mainPlotDir/execution/$dataset/byBatches/"
	if [[ ! -d "$plotDir" ]]; then mkdir -p $plotDir; fi
	output="$plotDir/relative--$metrics.png"
	echo "set terminal png size 1024,800"
	echo "set output '$output'"
	echo "set key outside"
	echo "set style fill solid border -1"
	echo "set title '$dataset: $(metricName $metrics) ($batches batches)'"
	echo "set boxwidth 0.8"
	echo "set ylabel 'speedup (relative to our recommendation)'"
	echo "set xlabel '# of batches'"
	echo "f(x) = 0"
	echo "set arrow from 0,1 to 10,1 lw 2 nohead front"
	echo "plot f(x) notitle, \\"
	for batches in $(ls $dir | sort -n); do firstBatches=$batches; break; done
	for setup in $(ls $dir/$firstBatches | grep '1-20' | grep -v '1-20--'); do recommendation=$setup; break; done
	i=1
	for setup in $(ls $dir/$firstBatches); do
		name=$setup
		if [[ $name = default* ]]; then array=(${name//--/ }); name="${array[2]}"; fi
		echo "'-' using 1:(\$5/\$10) with linespoint lt $i lw 4 title '$name', \\"
		i=$((i+1))
	done
	echo "f(x) notitle"
	for setup in $(ls $dir/$firstBatches); do
		for batches in $(ls $dir | sort -n); do
			aggr1=$(tail -n 1 $dir/$batches/$setup/aggr)
			aggr2=$(tail -n 1 $dir/$batches/$recommendation/aggr)
			echo "$batches	$aggr1	$aggr2"
		done
		echo "EOF"
	done
}

function printCountDataBatch {
	printCountDataLine $1 $2 "$2_INIT" "init"
	printCountDataLine $1 $2 "$2_ADD_SUCCESS" "add_s"
	# printCountDataLine $1 $2 "$2_ADD_FAILURE" "add_f"
	# printCountDataLine $1 $2 "$2_RANDOM_ELEMENT" "rand"
	printCountDataLine $1 $2 "$2_SIZE" "size"
	# printCountDataLine $1 $2 "$2_ITERATE" "iter"
	# printCountDataLine $1 $2 "$2_CONTAINS_SUCCESS" "cont_s"
	printCountDataLine $1 $2 "$2_CONTAINS_FAILURE" "cont_f"
	printCountDataLine $1 $2 "$2_GET_SUCCESS" "get_s"
	# printCountDataLine $1 $2 "$2_GET_FAILURE" "get_f"
	printCountDataLine $1 $2 "$2_REMOVE_SUCCESS" "rem_s"
	# printCountDataLine $1 $2 "$2_REMOVE_FAILURE" "rem_f"
}

function printCountDataMetric {
	# printCountDataLine $1 $2 "$2_INIT" "init"
	# printCountDataLine $1 $2 "$2_ADD_SUCCESS" "add_s"
	# printCountDataLine $1 $2 "$2_ADD_FAILURE" "add_f"
	# printCountDataLine $1 $2 "$2_RANDOM_ELEMENT" "rand"
	printCountDataLine $1 $2 "$2_SIZE" "size"
	printCountDataLine $1 $2 "$2_ITERATE" "iter"
	printCountDataLine $1 $2 "$2_CONTAINS_SUCCESS" "cont_s"
	printCountDataLine $1 $2 "$2_CONTAINS_FAILURE" "cont_f"
	# printCountDataLine $1 $2 "$2_GET_SUCCESS" "get_s"
	# printCountDataLine $1 $2 "$2_GET_FAILURE" "get_f"
	# printCountDataLine $1 $2 "$2_REMOVE_SUCCESS" "rem_s"
	# printCountDataLine $1 $2 "$2_REMOVE_FAILURE" "rem_f"
}

function printCountDataLine {
	cat $1 | grep "^$2_" | tr = "\t" | tail -n 12 | grep "$3" | sed "s/$3/$4/g"
}

function plotCounts {
	srcDir=$1
	srcFilename=$2
	dstDir=$3
	dstFilename=$4

	if [[ ! -d $dstDir ]]; then mkdir -p $dstDir; fi

	echo "set style fill solid border -1"
	echo "set key top left"
	echo "set boxwidth 1.0"
	echo "f(x) = 0"
	echo "set xtics rotate by -45 offset 0,0"
	echo "set logscale y"

	echo "set terminal pdf font ',14'"
	echo "set xtics font ',18'"
	echo "set ylabel 'operation counts c_l(o)'"

	### metrics
	echo "set output '$dstDir/metric-$dstFilename.pdf'"
	echo "set yrange [0.1:1000000]"
	echo "set xrange [3.5:18.5]"

	echo "plot	'-' using (\$1*4+0):(\$3/100) with boxes lt 1 title 'V', \\"
	echo "		'-' using (\$1*4+1):(\$3/100):xtic(2) with boxes lt 2 title 'E', \\"
	echo "		'-' using (\$1*4+2):(\$3/100) with boxes lt 3 title 'adj'"
	printCountDataMetric $srcDir/$srcFilename "V" | awk '{printf "%d\t%s\n", NR, $0}'
	echo "EOF"
	printCountDataMetric $srcDir/$srcFilename "E" | awk '{printf "%d\t%s\n", NR, $0}'
	echo "EOF"
	printCountDataMetric $srcDir/$srcFilename "adj" | awk '{printf "%d\t%s\n", NR, $0}'
	echo "EOF"

	### batches
	echo "set output '$dstDir/batch-$dstFilename.pdf'"
	echo "set yrange [0.1:100000]"
	echo "set xrange [3.5:26.5]"

	echo "plot	'-' using (\$1*4+0):3 with boxes lt 1 title 'V', \\"
	echo "		'-' using (\$1*4+1):3:xtic(2) with boxes lt 2 title 'E', \\"
	echo "		'-' using (\$1*4+2):3 with boxes lt 3 title 'adj'"
	printCountDataBatch $srcDir/$srcFilename "V" | awk '{printf "%d\t%s\n", NR, $0}'
	echo "EOF"
	printCountDataBatch $srcDir/$srcFilename "E" | awk '{printf "%d\t%s\n", NR, $0}'
	echo "EOF"
	printCountDataBatch $srcDir/$srcFilename "adj" | awk '{printf "%d\t%s\n", NR, $0}'
	echo "EOF"
}

# main=$mainRecommendationCountsDir
# for dataset in $(ls $main | grep "pnb7\|FB-1000-100"); do
# 	echo "$dataset"
# 	for count in $(ls $main/$dataset | grep "R-\|b."); do
# 		echo "  $count"
# 		plotCounts $main/$dataset $count $mainPlotDir/counts/$dataset ${count/'.counts'/''} | gnuplot
# 	done
# done
# exit


main=$mainExecutionDir
for dataset in $(ls $main | grep "pnb7\|FB-1000-100"); do
	echo "$dataset"
	for metrics in $(ls $main/$dataset); do
		echo "  $metrics"
		for batches in $(ls $main/$dataset/$metrics | grep "19999\|200" | grep -v "20001"); do
			echo "    $batches"
			# plotExecutionTime $main $dataset $metrics $batches | gnuplot
			plotExecutionSpeedup $main $dataset $metrics $batches | gnuplot
			# plotExecutionEstimation $main $dataset $metrics $batches
			# plotExecutionEstimation $main $dataset $metrics $batches | gnuplot
		done
		# echo "    forBatches"
		# plotForBatchesAbsolute $main $dataset $metrics | gnuplot
		# plotForBatchesRelative $main $dataset $metrics | gnuplot
	done
done