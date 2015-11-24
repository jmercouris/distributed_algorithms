#!/bin/bash
source teachnet.path

java -jar $tn_path \
--cp . \
--config ThreePhaseElectionRing-config.txt \
--compile
