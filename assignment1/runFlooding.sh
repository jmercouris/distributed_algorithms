#!/bin/bash
source teachnet.path

java -jar $tn_path \
--cp . \
--config Flooding-config.txt \
--compile
