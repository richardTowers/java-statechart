#!/usr/bin/env bash

mvn compile
mvn -e -q exec:java -Dexec.mainClass=uk.gov.ida.Main
