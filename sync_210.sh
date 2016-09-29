#!/usr/bin/env bash

mvn clean package -Dmaven.test.skip=true

case $1 in 
    "210")
        scp ./target/dev-tools.zip root@182.92.175.210:/export/dm/redisBomb/
        ;;
    "219")
        scp ./target/dev-tools.zip root@182.92.183.219:/export/dm/redisBomb/
        ;;
    "*")
        scp ./target/dev-tools.zip root@182.92.175.210:/export/dm/redisBomb/
        scp ./target/dev-tools.zip root@182.92.183.219:/export/dm/redisBomb/
        ;;
esac




