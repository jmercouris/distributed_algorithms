#!/bin/bash
source teachnet.path

java -jar $tn_path \
--cp . \
--config ThreePhaseElectionTree-config.txt \
--compile
