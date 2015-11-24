#!/bin/bash
source teachnet.path

java -jar $tn_path \
--cp . \
--config HirschbergSinclairElection-config-17.txt \
--compile