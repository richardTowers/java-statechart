#!/usr/bin/env bash

set -e

mvn clean compile 1>&2
mvn -e -q exec:java -Dexec.mainClass=uk.gov.ida.Main
