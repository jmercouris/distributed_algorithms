#!/bin/bash
source teachnet.path

java -jar $tn_path \
--cp . \
--config Echo-config.txt \
--compile