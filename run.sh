#!/bin/sh

cd `dirname $0`
export CLASSPATH='';for i in `find -name "*jar"`; do export CLASSPATH=$CLASSPATH:$i; done; CLASSPATH=$CLASSPATH:$PWD/runSpin
javac runSpin/Main.java
java runSpin.Main
