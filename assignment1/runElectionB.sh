#!/bin/bash
source teachnet.path

java -jar $tn_path \
--cp . \
--config Election-config-b.txt \
--compile
