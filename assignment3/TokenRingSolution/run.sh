#!/bin/bash
source ../teachnet.path

java -jar $tn_path \
--cp . \
--config configuration.txt \
--compile
