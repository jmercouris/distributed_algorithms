#!/bin/bash
source ../teachnet.path

java -jar $tn_path \
--cp . \
--config configuration.txt \
--compile

mkdir -p bin/
mv *.class bin/
