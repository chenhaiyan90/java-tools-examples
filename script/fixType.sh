#!/usr/bin/env bash

java -cp ".:`ls ./lib/*.jar|awk '{a = a":"$0}END{print a}'`" com.dova.dev.dataFix.FixDishType