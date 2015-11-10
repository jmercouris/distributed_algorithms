#!/bin/bash
source teachnet.path

java -jar $tn_path \
--cp . \
--config Election-config-c.txt \
--compile
