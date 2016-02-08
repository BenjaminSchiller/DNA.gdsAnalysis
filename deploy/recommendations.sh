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
					echo "$ds - $name: $dss"
				done
			done
		done
	done
}

function name {
	if [[ $1 == "DArray" ]]; then
		echo "A"
	elif [[ $1 == "DArrayList" ]]; then
		echo "AL"
	elif [[ $1 == "DHashArrayList" ]]; then
		echo "HAL"
	elif [[ $1 == "DHashMap" ]]; then
		echo "HM"
	elif [[ $1 == "DHashSet" ]]; then
		echo "HS"
	elif [[ $1 == "DHashTable" ]]; then
		echo "HT"
	elif [[ $1 == "DLinkedList" ]]; then
		echo "LL"
	else
		echo "unknown"
	fi
}

function rows {
	for ds in $(ls $main); do
		echo $ds
		for at in $(ls $main/$ds); do
			echo $at
			for ft in $(ls $main/$ds/$at); do
				echo $ft
				for r in $(ls $main/$ds/$at/$ft); do
					dss=$(tail -n 1 $main/$ds/$at/$ft/$r)
					name=${r/.recommendation/}
					name=${name/1-20-/}
					entries=(${dss//--/ })
					echo "$ds & $name & $(name ${entries[0]}) & $(name ${entries[1]}) & $(name ${entries[2]}) \\\\"
				done
			done
		done
	done
}


# echo "# # # # # # # # # # # # # # # # #"
# echo "# by AGGREGATION TYPE"
# echo "# # # # # # # # # # # # # # # # #"
# compareByAT
# echo
# echo
# echo "# # # # # # # # # # # # # # # # #"
# echo "# by FIT TYPE"
# echo "# # # # # # # # # # # # # # # # #"
# compareByFT
# echo
# echo

# list

rows


