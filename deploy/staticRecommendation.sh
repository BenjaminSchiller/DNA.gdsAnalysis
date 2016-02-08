#!/bin/bash

source config.sh

function recommendation {
	datasetName=$1
	dir="$mainCountsDir/$datasetName/"
	prefixes=$2
	suffixes=$3
	aggregationType=$5
	fitType=$6
	fitSizes=$defaultFitSizes
	countsDir="$mainRecommendationCountsDir/$datasetName/"
	name=$4
	countsFilename="$name"
	dst="$mainRecommendationDir/$datasetName/$aggregationType/$fitType"
	if [[ ! -d "$dst/" ]]; then mkdir -p $dst/; fi
	if [[ ! -f "$dst/$name.recommendation" ]]; then
		java -Xms1g -Xmx8g -jar staticRecommendation.jar $dir $prefixes $suffixes $aggregationType $fitType $fitSizes $countsDir $countsFilename > "$dst/$name.recommendation"
	else
		>&2 echo "$dst/$name.recommendation exists"
	fi
}

function recommendationAll {
	# recommendation $1 "-" "-gg" "gg" $2 $3
	# recommendation $1 "-" "-bg" "bg" $2 $3
	# recommendation $1 "-" "-ba" "ba" $2 $3
	# recommendation $1 "-" "-ba,-bg" "b" $2 $3

	# for m in ${recommendationMetrics[@]}; do
	# 	recommendation $1 "1-$m,2-$m,3-$m,4-$m,5-$m,1-ba,2-ba,3-ba,4-ba,5-ba,1-bg,2-bg,3-bg,4-bg,5-bg" "-" "1-5-$(metricName $m)" $2 $3
	# 	recommendation $1 "1-$m,2-$m,3-$m,4-$m,5-$m,6-$m,7-$m,8-$m,9-$m,10-$m,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg" "-" "1-10-$(metricName $m)" $2 $3
	# 	recommendation $1 "1-$m,2-$m,3-$m,4-$m,5-$m,6-$m,7-$m,8-$m,9-$m,10-$m,11-$m,12-$m,13-$m,14-$m,15-$m,16-$m,17-$m,18-$m,19-$m,20-$m,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,11-ba,12-ba,13-ba,14-ba,15-ba,16-ba,17-ba,18-ba,19-ba,20-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg,11-bg,12-bg,13-bg,14-bg,15-bg,16-bg,17-bg,18-bg,19-bg,20-bg" "-" "1-20-$(metricName $m)" $2 $3
	# done

	# recommendation $1 "1-$metricAll,2-$metricAll,3-$metricAll,4-$metricAll,5-$metricAll,1-ba,2-ba,3-ba,4-ba,5-ba,1-bg,2-bg,3-bg,4-bg,5-bg" "-" "1-5-all" $2 $3
	# recommendation $1 "1-$metricAPSP,2-$metricAPSP,3-$metricAPSP,4-$metricAPSP,5-$metricAPSP,1-ba,2-ba,3-ba,4-ba,5-ba,1-bg,2-bg,3-bg,4-bg,5-bg" "-" "1-5-APSP" $2 $3
	# recommendation $1 "1-$metricCC,2-$metricCC,3-$metricCC,4-$metricCC,5-$metricCC,1-ba,2-ba,3-ba,4-ba,5-ba,1-bg,2-bg,3-bg,4-bg,5-bg" "-" "1-5-CC" $2 $3
	# recommendation $1 "1-$metricDD,2-$metricDD,3-$metricDD,4-$metricDD,5-$metricDD,1-ba,2-ba,3-ba,4-ba,5-ba,1-bg,2-bg,3-bg,4-bg,5-bg" "-" "1-5-DD" $2 $3

	# recommendation $1 "1-$metricAll,2-$metricAll,3-$metricAll,4-$metricAll,5-$metricAll,6-$metricAll,7-$metricAll,8-$metricAll,9-$metricAll,10-$metricAll,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg" "-" "1-10-all" $2 $3
	# recommendation $1 "1-$metricAPSP,2-$metricAPSP,3-$metricAPSP,4-$metricAPSP,5-$metricAPSP,6-$metricAPSP,7-$metricAPSP,8-$metricAPSP,9-$metricAPSP,10-$metricAPSP,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg" "-" "1-10-APSP" $2 $3
	# recommendation $1 "1-$metricCC,2-$metricCC,3-$metricCC,4-$metricCC,5-$metricCC,6-$metricCC,7-$metricCC,8-$metricCC,9-$metricCC,10-$metricCC,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg" "-" "1-10-CC" $2 $3
	# recommendation $1 "1-$metricDD,2-$metricDD,3-$metricDD,4-$metricDD,5-$metricDD,6-$metricDD,7-$metricDD,8-$metricDD,9-$metricDD,10-$metricDD,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg" "-" "1-10-DD" $2 $3

	# recommendation $1 "1-$metricAll,2-$metricAll,3-$metricAll,4-$metricAll,5-$metricAll,6-$metricAll,7-$metricAll,8-$metricAll,9-$metricAll,10-$metricAll,11-$metricAll,12-$metricAll,13-$metricAll,14-$metricAll,15-$metricAll,16-$metricAll,17-$metricAll,18-$metricAll,19-$metricAll,20-$metricAll,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,11-ba,12-ba,13-ba,14-ba,15-ba,16-ba,17-ba,18-ba,19-ba,20-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg,11-bg,12-bg,13-bg,14-bg,15-bg,16-bg,17-bg,18-bg,19-bg,20-bg" "-" "1-20-all" $2 $3
	# recommendation $1 "1-$metricAPSP,2-$metricAPSP,3-$metricAPSP,4-$metricAPSP,5-$metricAPSP,6-$metricAPSP,7-$metricAPSP,8-$metricAPSP,9-$metricAPSP,10-$metricAPSP,11-$metricAPSP,12-$metricAPSP,13-$metricAPSP,14-$metricAPSP,15-$metricAPSP,16-$metricAPSP,17-$metricAPSP,18-$metricAPSP,19-$metricAPSP,20-$metricAPSP,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,11-ba,12-ba,13-ba,14-ba,15-ba,16-ba,17-ba,18-ba,19-ba,20-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg,11-bg,12-bg,13-bg,14-bg,15-bg,16-bg,17-bg,18-bg,19-bg,20-bg" "-" "1-20-APSP" $2 $3
	# recommendation $1 "1-$metricCC,2-$metricCC,3-$metricCC,4-$metricCC,5-$metricCC,6-$metricCC,7-$metricCC,8-$metricCC,9-$metricCC,10-$metricCC,11-$metricCC,12-$metricCC,13-$metricCC,14-$metricCC,15-$metricCC,16-$metricCC,17-$metricCC,18-$metricCC,19-$metricCC,20-$metricCC,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,11-ba,12-ba,13-ba,14-ba,15-ba,16-ba,17-ba,18-ba,19-ba,20-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg,11-bg,12-bg,13-bg,14-bg,15-bg,16-bg,17-bg,18-bg,19-bg,20-bg" "-" "1-20-CC" $2 $3
	# recommendation $1 "1-$metricDD,2-$metricDD,3-$metricDD,4-$metricDD,5-$metricDD,6-$metricDD,7-$metricDD,8-$metricDD,9-$metricDD,10-$metricDD,11-$metricDD,12-$metricDD,13-$metricDD,14-$metricDD,15-$metricDD,16-$metricDD,17-$metricDD,18-$metricDD,19-$metricDD,20-$metricDD,1-ba,2-ba,3-ba,4-ba,5-ba,6-ba,7-ba,8-ba,9-ba,10-ba,11-ba,12-ba,13-ba,14-ba,15-ba,16-ba,17-ba,18-ba,19-ba,20-ba,1-bg,2-bg,3-bg,4-bg,5-bg,6-bg,7-bg,8-bg,9-bg,10-bg,11-bg,12-bg,13-bg,14-bg,15-bg,16-bg,17-bg,18-bg,19-bg,20-bg" "-" "1-20-DD" $2 $3

	# recommendation $1 "-" $metricAPSP "R-APSP" $2 $3
	# recommendation $1 "-" $metricCC "R-CC" $2 $3
	# recommendation $1 "-" $metricDD "R-DD" $2 $3

	recommendation $1 "-" $metricDD "R-DD" $2 $3
	recommendation $1 "-" $metricCC "R-CC" $2 $3
	recommendation $1 "-" $metricWC "R-WC" $2 $3

	recommendation $1 "-" $metricASS "R-ASS" $2 $3
	recommendation $1 "-" $metricBC "R-BC" $2 $3
	recommendation $1 "-" $metricAPSP "R-APSP" $2 $3
	recommendation $1 "-" $metricRCC "R-RCC" $2 $3

	recommendation $1 "-" $metricAll "R-all" $2 $3

	# recommendation $1 "1-,2-,3-,4-,5-" "-" "1-5" $2 $3
	# recommendation $1 "1-,2-,3-,4-,5-,6-,7-,8-,9-,10-" "-" "1-10" $2 $3
	# recommendation $1 "1-,2-,3-,4-,5-,6-,7-,8-,9-,10-,11-,12-,13-,14-,15-,16-,17-,18-,19-,20-" "-" "1-20" $2 $3

	# recommendation $1 "0-,1-,2-,3-,4-,5-" "-" "0-5" $2 $3
	# recommendation $1 "0-,1-,2-,3-,4-,5-,6-,7-,8-,9-,10-" "-" "0-10" $2 $3
	# recommendation $1 "0-,1-,2-,3-,4-,5-,6-,7-,8-,9-,10-,11-,12-,13-,14-,15-,16-,17-,18-,19-,20-" "-" "0-20" $2 $3
}

if [[ $# != 3 ]]; then
	>&2 echo "3 arguments expected, $# given"
	>&2 echo 'recommendation.sh $datasetName $aggregationType $fitType'
else
	recommendationAll $1 $2 $3
fi
