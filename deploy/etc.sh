while [[ true ]]; do echo "start"; ./jobs.sh start; ./jobs.sh st; sleep 5; done


while [[ true ]]; do date; ./jobs.sh startServer; ./jobs.sh statusServer; sleep 5; done