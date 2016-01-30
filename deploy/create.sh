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
	echo "./staticExecutionOfRecommendation.sh $1 $2 $3 $4 $5 $6 $7 $8"
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
		executionR $name $dir $metricAPSP $batches $run "AVG" "MedSD" "1-20-APSP"
		executionR $name $dir $metricCC $batches $run "AVG" "MedSD" "1-20-CC"
		executionR $name $dir $metricDD $batches $run "AVG" "MedSD" "1-20-DD"
		executionR $name $dir $metricASS $batches $run "AVG" "MedSD" "1-20-ASS"
		executionR $name $dir $metricBC $batches $run "AVG" "MedSD" "1-20-BC"
		executionR $name $dir $metricRCC $batches $run "AVG" "MedSD" "1-20-RCC"
		executionR $name $dir $metricUM $batches $run "AVG" "MedSD" "1-20-UM"
		executionR $name $dir $metricWC $batches $run "AVG" "MedSD" "1-20-WC"

		### recommendation from all metrics (+BG/BA)
		executionR $name $dir $metricAPSP $batches $run "AVG" "MedSD" "1-20"
		executionR $name $dir $metricCC $batches $run "AVG" "MedSD" "1-20"
		executionR $name $dir $metricDD $batches $run "AVG" "MedSD" "1-20"
		executionR $name $dir $metricASS $batches $run "AVG" "MedSD" "1-20"
		executionR $name $dir $metricBC $batches $run "AVG" "MedSD" "1-20"
		executionR $name $dir $metricRCC $batches $run "AVG" "MedSD" "1-20"
		executionR $name $dir $metricUM $batches $run "AVG" "MedSD" "1-20"
		executionR $name $dir $metricWC $batches $run "AVG" "MedSD" "1-20"
		for ds in ${dataStructures[@]}; do
			### default configurations
			execution $name $dir $metricAPSP $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricCC $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricDD $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricASS $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricBC $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricRCC $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricUM $batches $run "$ds--$ds--$ds" "default-$ds"
			execution $name $dir $metricWC $batches $run "$ds--$ds--$ds" "default-$ds"
		done
	done
}

# counting "pnb7" $metricAll 100
# counting "pnb8" $metricAll 100
# counting "FB-100-100" $metricAll 50
# counting "FB-1000-100" $metricAll 50


# recommendation "pnb7" "AVG" "MedSD"
# recommendation "pnb8" "AVG" "MedSD"
# recommendation "FB-100-100" "AVG" "MedSD"
# recommendation "FB-1000-100" "AVG" "MedSD"

batchFile="jobs.batch"
if [[ -f $batchFile ]]; then rm $batchFile; fi
touch $batchFile
# executionAll "FB-1000-100" 10 11 50 >> $batchFile
# executionAll "FB-1000-100" 20 11 50 >> $batchFile
# executionAll "FB-1000-100" 50 11 50 >> $batchFile
executionAll "FB-1000-100" 100 11 20 >> $batchFile
executionAll "FB-1000-100" 150 11 20 >> $batchFile
executionAll "FB-1000-100" 200 11 20 >> $batchFile
./jobs.sh bulkCreate $batchFile

# batchFile="jobs.batch"
# if [[ -f $batchFile ]]; then rm $batchFile; fi
# touch $batchFile
# executionAll "pnb7" 1000 21 50 >> $batchFile
# executionAll "pnb8" 1000 21 50 >> $batchFile
# executionAll "pnb7" 5000 21 50 >> $batchFile
# executionAll "pnb8" 5000 21 50 >> $batchFile
# executionAll "pnb7" 10000 21 50 >> $batchFile
# executionAll "pnb8" 10000 21 50 >> $batchFile
# executionAll "pnb7" 15000 1 20 >> $batchFile
# executionAll "pnb8" 15000 1 20 >> $batchFile
# executionAll "pnb7" 19999 11 50 >> $batchFile
# executionAll "pnb8" 19999 11 50 >> $batchFile
# ./jobs.sh bulkCreate $batchFile

# while [[ true ]]; do echo "start"; ./jobs.sh start; ./jobs.sh st; sleep 5; done
