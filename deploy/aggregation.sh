#!/bin/bash

if [[ $1 = "server" ]]; then
	source config.sh
	main=$mainExecutionDir

	for dataset in $(ls $main); do
		echo "$dataset"
		for metrics in $(ls $main/$dataset); do
			echo "  $metrics"
			for batches in $(ls $main/$dataset/$metrics); do
				echo "    $batches"
				for setup in $(ls $main/$dataset/$metrics/$batches); do
					echo "      $setup"
					dir="$main/$dataset/$metrics/$batches/$setup"

					### TOTAL
					cat $dir/*.dat | grep 'TOTAL: ' > "$dir/aggr"

					### all except init
					# if [[ -f $dir/aggr ]]; then rm $dir/aggr; fi
					# for d in $(ls $dir); do
					# 	echo "TOTAL: $(cat $dir/$d | grep -v "^0-\|TOTAL\|using" | awk '{ sum += $2; } END { print sum; }')" >> "$dir/aggr"
					# done

					aggr=$(python aggregation.py "$dir/aggr" "TOTAL: ")
					echo $aggr >> "$dir/aggr"
				done
			done
		done
	done
else
	source jobs.cfg
	ssh ${server_name} "cd ${server_dir}; ./aggregation.sh server"
fi