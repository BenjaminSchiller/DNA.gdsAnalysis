#!/bin/bash

source config.sh

function counting {
	name=$1
	dir="datasets/$name/"
	./jobs.sh create "./staticCounting.sh $name $dir $2 $3"
}

function recommendation {
	./jobs.sh create "./staticRecommendation.sh $1 $2 $3"
}

function execution {
	echo "./staticExecution.sh $1 $2 $3 $4 $5 $6 $7"
}

function executionR {
	echo "./staticExecutionOfRecommendation.sh $1 $2 $3 $4 $5 $6 $7 $8 $9"
}

# function executionRMetrics {
# 	dataset=$1
# 	dir=$2
# 	batches=$3
# 	run=$4
# 	aggregationType=$5
# 	fitType=$6
# 	name=$7

# 	# executionR $dataset $dir $metricAPSP $batches $run $aggregationType $fitType $name
# 	# executionR $dataset $dir $metricCC $batches $run $aggregationType $fitType $name
# 	# executionR $dataset $dir $metricDD $batches $run $aggregationType $fitType $name

# 	# executionR $dataset $dir $metricASS $batches $run $aggregationType $fitType $name
# 	# executionR $dataset $dir $metricBC $batches $run $aggregationType $fitType $name
# 	# executionR $dataset $dir $metricRCC $batches $run $aggregationType $fitType $name
# 	# executionR $dataset $dir $metricUM $batches $run $aggregationType $fitType $name
# 	# executionR $dataset $dir $metricWC $batches $run $aggregationType $fitType $name
# }

function executionAll {
	name=$1
	dir="datasets/$name/"
	batches=$2
	from=$3
	to=$4
	for run in $(seq $from $to); do
		### recommendation from single metric (+BG/BA)
		# executionR $name $dir $metricAPSP $batches $run "AVG" "MedSD" "1-20-APSP" "1-20-APSP"
		# executionR $name $dir $metricCC $batches $run "AVG" "MedSD" "1-20-CC" "1-20-CC"
		# executionR $name $dir $metricDD $batches $run "AVG" "MedSD" "1-20-DD" "1-20-DD"
		# executionR $name $dir $metricASS $batches $run "AVG" "MedSD" "1-20-ASS" "1-20-ASS"
		# executionR $name $dir $metricRCC $batches $run "AVG" "MedSD" "1-20-RCC" "1-20-RCC"
		# executionR $name $dir $metricWC $batches $run "AVG" "MedSD" "1-20-WC" "1-20-WC"

		# executionR $name $dir $metricAPSP $batches $run "AVG" "AvgSD" "1-20-APSP" "1-20-APSP-AvgSD"
		# executionR $name $dir $metricAPSP $batches $run "AVG" "MedSD" "1-20-APSP" "1-20-APSP-MedSD"
		# executionR $name $dir $metricAPSP $batches $run "AVG" "AvgSD_py" "1-20-APSP" "1-20-APSP-AvgSD_py"
		# executionR $name $dir $metricAPSP $batches $run "AVG" "MedSD_py" "1-20-APSP" "1-20-APSP-MedSD_py"

		executionR $name $dir $metricCC $batches $run "AVG" "AvgSD" "1-20-CC" "1-20-CC-AvgSD"
		executionR $name $dir $metricDD $batches $run "AVG" "AvgSD" "1-20-DD" "1-20-DD-AvgSD"
		executionR $name $dir $metricASS $batches $run "AVG" "AvgSD" "1-20-ASS" "1-20-ASS-AvgSD"
		executionR $name $dir $metricRCC $batches $run "AVG" "AvgSD" "1-20-RCC" "1-20-RCC-AvgSD"
		executionR $name $dir $metricWC $batches $run "AVG" "AvgSD" "1-20-WC" "1-20-WC-AvgSD"

		executionR $name $dir $metricCC $batches $run "AVG" "MedSD" "1-20-CC" "1-20-CC-MedSD"
		executionR $name $dir $metricDD $batches $run "AVG" "MedSD" "1-20-DD" "1-20-DD-MedSD"
		executionR $name $dir $metricASS $batches $run "AVG" "MedSD" "1-20-ASS" "1-20-ASS-MedSD"
		executionR $name $dir $metricRCC $batches $run "AVG" "MedSD" "1-20-RCC" "1-20-RCC-MedSD"
		executionR $name $dir $metricWC $batches $run "AVG" "MedSD" "1-20-WC" "1-20-WC-MedSD"

		executionR $name $dir $metricCC $batches $run "AVG" "AvgSD_py" "1-20-CC" "1-20-CC-AvgSD_py"
		executionR $name $dir $metricDD $batches $run "AVG" "AvgSD_py" "1-20-DD" "1-20-DD-AvgSD_py"
		executionR $name $dir $metricASS $batches $run "AVG" "AvgSD_py" "1-20-ASS" "1-20-ASS-AvgSD_py"
		executionR $name $dir $metricRCC $batches $run "AVG" "AvgSD_py" "1-20-RCC" "1-20-RCC-AvgSD_py"
		executionR $name $dir $metricWC $batches $run "AVG" "AvgSD_py" "1-20-WC" "1-20-WC-AvgSD_py"

		executionR $name $dir $metricCC $batches $run "AVG" "MedSD_py" "1-20-CC" "1-20-CC-MedSD_py"
		executionR $name $dir $metricDD $batches $run "AVG" "MedSD_py" "1-20-DD" "1-20-DD-MedSD_py"
		executionR $name $dir $metricASS $batches $run "AVG" "MedSD_py" "1-20-ASS" "1-20-ASS-MedSD_py"
		executionR $name $dir $metricRCC $batches $run "AVG" "MedSD_py" "1-20-RCC" "1-20-RCC-MedSD_py"
		executionR $name $dir $metricWC $batches $run "AVG" "MedSD_py" "1-20-WC" "1-20-WC-MedSD_py"

		# ### recommendation from all metrics (+BG/BA)
		# executionR $name $dir $metricAPSP $batches $run "AVG" "MedSD" "1-20" "1-20"
		# executionR $name $dir $metricCC $batches $run "AVG" "MedSD" "1-20" "1-20"
		# executionR $name $dir $metricDD $batches $run "AVG" "MedSD" "1-20" "1-20"
		# executionR $name $dir $metricASS $batches $run "AVG" "MedSD" "1-20" "1-20"
		# executionR $name $dir $metricRCC $batches $run "AVG" "MedSD" "1-20" "1-20"
		# executionR $name $dir $metricWC $batches $run "AVG" "MedSD" "1-20" "1-20"

		for ds in ${dataStructures[@]}; do
			### default configurations
			# execution $name $dir $metricAPSP $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricCC $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricDD $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricASS $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricRCC $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricWC $batches $run "$ds--$ds--$ds" "default-$ds"
		done
	done
}

# counting "pnb7" $metricAll 100
# counting "pnb8" $metricAll 100
# counting "FB-100-100" $metricAll 50
# counting "FB-1000-100" $metricAll 50
# counting "FB-1000-50" $metricAll 50


# recommendation "pnb7" "AVG" "Avg"
# recommendation "pnb7" "AVG" "Med"
# recommendation "pnb7" "AVG" "AvgSD"
# recommendation "pnb7" "AVG" "MedSD"
# recommendation "pnb7" "AVG" "Avg_py"
# recommendation "pnb7" "AVG" "Med_py"
# recommendation "pnb7" "AVG" "AvgSD_py"
# recommendation "pnb7" "AVG" "MedSD_py"

# recommendation "FB-1000-50" "AVG" "Avg"
# recommendation "FB-1000-50" "AVG" "Med"
# recommendation "FB-1000-50" "AVG" "AvgSD"
# recommendation "FB-1000-50" "AVG" "MedSD"
# recommendation "FB-1000-50" "AVG" "Avg_py"
# recommendation "FB-1000-50" "AVG" "Med_py"
# recommendation "FB-1000-50" "AVG" "AvgSD_py"
# recommendation "FB-1000-50" "AVG" "MedSD_py"


batchFile="jobs.batch"
if [[ -f $batchFile ]]; then rm $batchFile; fi
touch $batchFile
# executionAll "FB-1000-50" 49 11 100 >> $batchFile
# executionAll "FB-1000-50" 99 11 100 >> $batchFile
# executionAll "FB-1000-50" 149 11 100 >> $batchFile
# executionAll "FB-1000-50" 199 11 100 >> $batchFile
executionAll "FB-1000-50" 249 1 50 >> $batchFile
executionAll "FB-1000-50" 299 1 50 >> $batchFile
executionAll "FB-1000-50" 349 1 50 >> $batchFile
executionAll "FB-1000-50" 399 1 50 >> $batchFile
./jobs.sh bulkCreate $batchFile

# batchFile="jobs.batch"
# if [[ -f $batchFile ]]; then rm $batchFile; fi
# touch $batchFile
# executionAll "pnb7" 4999 11 100 >> $batchFile
# executionAll "pnb7" 9999 11 100 >> $batchFile
# executionAll "pnb7" 14999 11 100 >> $batchFile
# executionAll "pnb7" 19999 11 100 >> $batchFile
# ./jobs.sh bulkCreate $batchFile


# while [[ true ]]; do echo "start"; ./jobs.sh start; ./jobs.sh st; sleep 5; done
