#!/usr/bin/env bash

set -e

mvn clean compile
mvn -e -q exec:java -Dexec.mainClass=uk.gov.ida.Main
