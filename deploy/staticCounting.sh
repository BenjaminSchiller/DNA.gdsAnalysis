#!/bin/bash

source config.sh

function counting {
	datasetName=$1
	datasetDir=$2
	graphFilename=$defaultGraphFilename
	batchSuffix=$defaultBatchSuffix
	metricTypes=$3
	batches=$4
	countsDir="$mainCountsDir/$datasetName/"
	if [[ ! -d "$countsDir" ]]; then mkdir -p "$countsDir"; fi
	if [[ ! -f "${countsDir}_log" ]]; then
		java -Xms1g -Xmx8g -jar staticCounting.jar $datasetName $datasetDir $graphFilename $batchSuffix $metricTypes $batches $countsDir > "${countsDir}_log"
	else
		>&2 echo "${countsDir}_log exists..."
	fi
}

if [[ $# != 4 ]]; then
	>&2 echo "4 arguments expected, $# given"
	>&2 echo 'staticCounting.sh $datasetName $datasetDir $metricTypes $batches'
else
	counting $1 $2 $3 $4
fi