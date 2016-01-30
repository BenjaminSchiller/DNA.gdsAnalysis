#!/bin/bash

source config.sh

function estimationAll {
	datasetName=$1
	aggregationType=$2
	fitType=$3

	for counts in $(ls $mainRecommendationCountsDir/$datasetName/); do # $aggregationType/$fitType/
		name=${counts/.counts/}
		estimation $datasetName $aggregationType $fitType $name
	done
}

function printEstimation {
	echo "$1	$(java -Xms1g -Xmx8g -jar staticEstimation.jar $mainRecommendationCountsDir/$datasetName/ $name.counts $aggregationType $fitType $defaultFitSizes $1 | tail -n 1)"
}

function estimation {
	datasetName=$1
	aggregationType=$2
	fitType=$3
	name=$4

	recommendation=$(tail -n 1 $mainRecommendationDir/$datasetName/$aggregationType/$fitType/$name.recommendation)
	echo "$datasetName / $aggregationType / $fitType --- $name:"
	dir="$mainEstimationDir/$datasetName/$aggregationType/$fitType"
	dst="$dir/$name.estimation"
	if [[ ! -d $dir ]]; then mkdir -p $dir; fi
	printEstimation $recommendation > $dst
	for ds in ${dataStructures[@]}; do
		printEstimation "$ds--$ds--$ds" >> $dst
	done
}


if [[ $# != 3 ]]; then
	>&2 echo "3 arguments expected, $# given"
	>&2 echo 'staticEstimation.sh $datasetName $aggregationType $fitType'
else
	estimationAll $1 $2 $3
fi