#!/bin/bash

source config.sh

function execution {
	datasetName=$1
	datasetDir=$2
	graphFilename=$defaultGraphFilename
	batchSuffix=$defaultBatchSuffix
	metricTypes=$3
	batches=$4
	run=$5
	gdsString=$6
	name=$7

	dst="$mainExecutionDir/$datasetName/$metricTypes/$batches/$name--$gdsString"
	if [[ ! -d "$dst/" ]]; then mkdir -p "$dst/"; fi
	if [[ ! -f "$dst/$run.dat" ]]; then
		java -Xms1g -Xmx8g -jar staticExecution.jar $datasetName $datasetDir $graphFilename $batchSuffix $metricTypes $batches $gdsString > "$dst/$run.dat"
	else
		>&2 echo "$dst/$run.dat exists"
	fi
}

if [[ $# != 7 ]]; then
	>&2 echo "7 arguments expected, $# given"
	>&2 echo 'staticExecution.sh $datasetName $datasetDir $metricTypes $batches $run $gdsString $name'
else
	execution $1 $2 $3 $4 $5 $6 $7
fi
