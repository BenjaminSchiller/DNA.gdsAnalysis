# DNA.gdsAnalysis

In this repo, we provide java sources and script for performing an evaluation of different data structures for representing a graph during dynamic graph analysis using DNA (Dynamic Network Analyzer).

## file structure of this repo

	build/
		|- build.xml (ant build file)
		|- (.jar files)
	deploy/
		|- (analysis results)
		|- (analysis scripts)
		|- (deployment scripts)
	java/
		(java source files)

## config file `deploy/config.sh`


## results in `deploy/data/`

int `results/`, the output of all component of gdsAnalysis is stored separated into a folder for each step of the workflow.

	counts/
		|- $dataset/
			|- ${name}.counts

In `counts`, different operation counts are stored for each dataset.
`$dataset` is the name of the dataset and `$name` the name of the operation count part.
	execution/
		|- $dataset/
			|- $metric/
				|- $batches/
					|- ${name}--${dsConfig}/
						|- aggr
						|- 1.dat
						|- 2.dat
						|- ...

In `execution`, the runtimes for executing an analysis instance are stored.
The runtime of each run is stored in a separate file `${i}.run` and the aggregation of all runs written to `aggr`.
`$dataset` is the name of the dataset, `$metric` is the name(s) of the metric(s) that are computed, `$batches` is the number of batches the analysis is executed for, and `$name` is a name for the config (e.g., `1-20` or `default-DArray`).
`$dsConfig` is the configutation of graph data structures that should be used in the form `V--E--adj`, e.g., `DArray--DHashMap--DLinkedList`.
	plots/
		|- counts/
			|- $dataset/
				|- ${name}.png
		|- execution/
			|- $dataset/
				|- $batches/
					|- runtime--${metric}.png
					|- speedup--${metric}.png
				|- byBatches/
					|- absolute--${metric}.png
					|- relative--${metric}.png

In `plots`, all the plots generated from the operations counts and the execution runtimes are stored.
`$dataset`, `$name`, `$batches`, and `$metric` are used as described before.
	recommendationCounts/
		|- $dataset/
			|- ${name}.counts

Here, aggregated subsets of the operation counts stored in `counts` are stored for recommending the best graph data structrues based on the operations.
	recommendations/
		|- $dataset/
			|- $aggregationType/
				|- $fitType/
					|- ${name}.recommendation

In `recommendations`, the recommended dsConfigs for different operation count subsets (`$name`) are stored.
Here, `$aggregationType` is the type how multiple operation counts are aggregated and `$fitType` is the inpute data type for fitting the functions beforehand.


## .jar files (and arguments)

The normal workflow is to execute:

1. staticCounting
2. staticRecommendation
3. staticExecution

where the output of each step is the input of the next.
In addition, staticEstimation can be executed with the input from staticCounting.

Commonly, the result is given on std out unless specified otherwise.

### (1) staticCounting

In the first step, opertion counts are generated for a given dataset and the specified list of metrics.
The analysis is executed for a given number of batches and the counts written to the specified dir.

#### `staticCounting.jar`
	expecting 7 arguments (got 0)
	   0: datasetName - name of the dataset (String)
	
	   1: datasetDir - dir where the dataset is stored (String)
	
	   2: graphFilename - e.g., 0.dnag (String)
	
	   3: batchSuffix - e.g., .dnab (String)
	
	   4: metricTypes - metrics that should be computed (String[]) sep. by ','
	      from: DegreeDistributionR DegreeDistributionU UndirectedClusteringCoefficientR UndirectedClusteringCoefficientU UnweightedAllPairsShortestPathsR UnweightedAllPairsShortestPathsU WeakConnectivityR WeakConnectivityU WeakConnectivityB UndirectedMotifsR UndirectedMotifsU AssortativityR AssortativityU BetweennessCentralityR BetweennessCentralityU RichClubConnectivityByDegreeR RichClubConnectivityByDegreeU
	
	   5: batches - number of batches (Integer)
	
	   6: countsDir - dir where to store counts (String)

#### `staticCounting.sh`
	4 arguments expected, 0 given
	staticCounting.sh $datasetName $datasetDir $metricTypes $batches

### (2) staticRecommendation

In the second step, graph data structures are recommended for a subset of operation counts.
This subset is specified by a list of prefixes and suffixes of the counts files that should be aggregated.

In addition, an aggregationType must be specified to determine how the operation counts are aggregated.
Also, the fitType describing from which data the function where fitted for the stimation function must be provided.

Finally, a directory and file must be specified where the aggregated operation counts should be stored.

#### `staticRecommendation.jar`
	expecting 8 arguments (got 0)
	   0: dir - dir where the operation counts are stored (String)
	
	   1: prefixes - prefixes of the files that should be read (e.g., 1-, 2-ba, 2-bg, 2-DegreeDistributionR) - for no prefix check (String[]) sep. by ','
	
	   2: suffixes - suffixes of the files that should be read (e.g., gg - bg - ba - R) without the .counts at the end - for no suffix check (String[]) sep. by ','
	
	   3: aggregationType - aggr type for the operation counts (String)
	      values:  MIN MAX AVG FIRST LAST
	
	   4: fitType - data type how function are fitted (String)
	      values:  Avg Med AvgSD MedSD
	
	   5: fitSizes - sizes for fitted functions (Integer[]) sep. by ','
	
	   6: countsDir - dir where to write the aggregated counts (String)
	
	   7: countsFilename - filename where to write the aggregated counts (String)

#### `staticRecommendation.sh`
	3 arguments expected, 0 given
	recommendation.sh $datasetName $aggregationType $fitType

### (3) staticExecution

In the third and final step, the analysis of a dataset if performed with a given list of metrics for a specified graph data structure configuration.

#### `staticExecution.jar`
	expecting 7 arguments (got 0)
	   0: datasetName - name of the dataset (String)
	
	   1: datasetDir - dir where the dataset is stored (String)
	
	   2: graphFilename - e.g., 0.dnag (String)
	
	   3: batchSuffix - e.g., .dnab (String)
	
	   4: metricTypes - metrics that should be computed (String[]) sep. by ','
	      from: DegreeDistributionR DegreeDistributionU UndirectedClusteringCoefficientR UndirectedClusteringCoefficientU UnweightedAllPairsShortestPathsR UnweightedAllPairsShortestPathsU WeakConnectivityR WeakConnectivityU WeakConnectivityB UndirectedMotifsR UndirectedMotifsU AssortativityR AssortativityU BetweennessCentralityR BetweennessCentralityU RichClubConnectivityByDegreeR RichClubConnectivityByDegreeU
	
	   5: batches - number of batches (Integer)
	
	   6: gdsString - List of the datastructures to use (V,E,adj) (String[]) sep. by '--'

#### `staticExecution.sh`
	7 arguments expected, 0 given
	staticExecution.sh $datasetName $datasetDir $metricTypes $batches $run $gdsString $name

#### `staticExecutionOfRecommendation.sh`
	8 arguments expected, 0 given
	staticExecutionOfRecommendation.sh $datasetName $datasetDir $metricTypes $batches $run $aggregationType $fitType $name

### (2') staticEstimation

This part uses as input the counts generated in the first step and allows us to generate estimations for arbitrary configurations and operation counts specified by the parameters.

#### `staticEstimation.jar`
	expecting 6 arguments (got 0)
	   0: countsDir - dir where the operation counts are stored (String)
	
	   1: countsFilename - filename where the operation counts are stored (String)
	
	   2: aggregationType - aggr type for the operation counts (String)
	      values:  MIN MAX AVG FIRST LAST
	
	   3: fitType - data type how function are fitted (String)
	      values:  Avg Med AvgSD MedSD
	
	   4: fitSizes - sizes for fitted functions (Integer[]) sep. by ','
	
	   5: gdsType - gds config (V, E, adj) (String[]) sep. by '--'

#### `staticEstimation.sh`
	3 arguments expected, 0 given
	staticEstimation.sh $datasetName $aggregationType $fitType