#!/bin/Bash

source config.sh

main="$mainExecutionDir"

function list {
	for ds in $(ls $main); do
		echo "$ds"
		for metrics in $(ls $main/$ds); do
			echo "  $metrics"
			for batches in $(ls $main/$ds/$metrics | sort -n); do
				runs=()
				for setup in $(ls $main/$ds/$metrics/$batches); do
					runs+="$(($(cat $main/$ds/$metrics/$batches/$setup/aggr | wc -l)-1))_"
				done
				echo "    $batches: ${runs[@]}"
			done
		done
	done
}

list