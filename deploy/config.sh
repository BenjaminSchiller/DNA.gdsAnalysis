mainCountsDir="data/counts"
mainRecommendationCountsDir="data/recommendationCounts"
mainRecommendationDir="data/recommendations"
mainExecutionDir="data/execution"
mainPlotDir="data/plots"
mainEstimationDir="data/estimation/"

defaultGraphFilename="0.dnag"
defaultBatchSuffix=".dnab"
defaultFitSizes="100,1000,10000,50000,100000,1000000"

metricAPSP="UnweightedAllPairsShortestPathsR"
metricCC="UndirectedClusteringCoefficientR"
metricDD="DegreeDistributionR"
metricASS="AssortativityR"
metricBC="BetweennessCentralityR"
metricRCC="RichClubConnectivityByDegreeR"
metricUM="UndirectedMotifsR"
metricWC="WeakConnectivityR"
metricAll="$metricAPSP,$metricCC,$metricDD,$metricASS,$metricBC,$metricRCC,$metricUM,$metricWC"

recommendationMetrics=($metricAPSP $metricCC $metricDD $metricASS $metricBC $metricRCC $metricUM $metricWC $metricAll)

dataStructures=(DArray DArrayList DHashArrayList DHashMap DHashSet DHashTable DLinkedList)

function metricName {
	if [[ $1 = $metricAll ]]; then
		echo "all"
	elif [[ $1 = $metricAPSP ]]; then
		echo "APSP"
	elif [[ $1 = $metricCC ]]; then
		echo "CC"
	elif [[ $1 = $metricDD ]]; then
		echo "DD"
	elif [[ $1 = $metricBC ]]; then
		echo "BC"
	elif [[ $1 = $metricWC ]]; then
		echo "WC"
	elif [[ $1 = $metricASS ]]; then
		echo "ASS"
	elif [[ $1 = $metricUM ]]; then
		echo "UM"
	elif [[ $1 = $metricRCC ]]; then
		echo "RCC"
	else
		echo $1
	fi
}