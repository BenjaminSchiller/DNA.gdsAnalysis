#!/bin/bash

source jobs.cfg

if [[ ! -d "data" ]]; then mkdir data; fi

rsync -auvzl --prune-empty-dirs --include '*/' --include 'aggr' --include '*.counts' --include '*.recommendation' --exclude '*' "${server_name}:${server_dir}/data/*" ./data/
