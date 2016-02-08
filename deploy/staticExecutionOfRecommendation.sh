#!/bin/bash

source config.sh

function executionOfRecommendation {
	datasetName=$1
	datasetDir=$2
	graphFilename=$defaultGraphFilename
	batchSuffix=$defaultBatchSuffix
	metricTypes=$3
	batches=$4
	run=$5
	aggregationType=$6
	fitType=$7
	recommendationType=$8
	name=$9

	gdsString=$(tail -n 1 "$mainRecommendationDir/$datasetName/$aggregationType/$fitType/$recommendationType.recommendation")
	dst="$mainExecutionDir/$datasetName/$metricTypes/$batches/$name--$gdsString"
	if [[ ! -d "$dst/" ]]; then mkdir -p "$dst/"; fi
	if [[ ! -f "$dst/$run.dat" ]]; then
		java -Xms1g -Xmx8g -jar staticExecution.jar $datasetName $datasetDir $graphFilename $batchSuffix $metricTypes $batches $gdsString > "$dst/$run.dat"
	else
		>&2 echo "$dst/$run.dat exists"
	fi
}

if [[ $# != 9 ]]; then
	>&2 echo "9 arguments expected, $# given"
	>&2 echo 'staticExecutionOfRecommendation.sh $datasetName $datasetDir $metricTypes $batches $run $aggregationType $fitType $recommendationType $name'
else
	executionOfRecommendation $1 $2 $3 $4 $5 $6 $7 $8 $9
fi
