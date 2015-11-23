#!/bin/bash
source teachnet.path

java -jar $tn_path \
--cp . \
--config HirschbergSinclairElection-config-16.txt \
--compile