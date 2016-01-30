#!/bin/bash

source jobs.cfg

rsync -auvzl aggregation.* config.sh static*.sh ../build/*.jar ${server_name}:${server_dir}/
