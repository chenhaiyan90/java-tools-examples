#!/usr/bin/env bash

classpath=".:lib/*:conf/bomb_redis.conf:conf"

java -cp $classpath com.dova.dev.port_detector.ActorRun