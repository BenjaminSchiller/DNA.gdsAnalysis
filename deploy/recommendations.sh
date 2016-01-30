#!/bin/bash

source config.sh

main="$mainRecommendationDir"

function compareByAT {
	for ds in $(ls $main); do
		echo $ds
		for at in $(ls $main/$ds); do
			for ft in $(ls $main/$ds/$at); do
				echo $ft
				for r in $(ls $main/$ds/$at/$ft); do
					echo $r
					for at in $(ls $main/$ds); do
						dss=$(tail -n 1 $main/$ds/$at/$ft/$r)
						echo "$at: $dss"
					done
				done
			done
			break
		done
	done
}

function compareByFT {
	for ds in $(ls $main); do
		echo $ds
		for at in $(ls $main/$ds); do
			echo $at
			for ft in $(ls $main/$ds/$at); do
				for r in $(ls $main/$ds/$at/$ft); do
					echo $r
					for ft in $(ls $main/$ds/$at); do
						dss=$(tail -n 1 $main/$ds/$at/$ft/$r)
						echo "$ft: 	$dss"
					done
				done
				break
			done
		done
	done
}

function list {
	for ds in $(ls $main); do
		echo $ds
		for at in $(ls $main/$ds); do
			echo $at
			for ft in $(ls $main/$ds/$at); do
				echo $ft
				for r in $(ls $main/$ds/$at/$ft); do
					dss=$(tail -n 1 $main/$ds/$at/$ft/$r)
					name=${r/.recommendation/}
					echo "$name: $dss"
				done
			done
		done
	done
}


echo "# # # # # # # # # # # # # # # # #"
echo "# by AGGREGATION TYPE"
echo "# # # # # # # # # # # # # # # # #"
compareByAT
echo
echo
echo "# # # # # # # # # # # # # # # # #"
echo "# by FIT TYPE"
echo "# # # # # # # # # # # # # # # # #"
compareByFT
echo
echo