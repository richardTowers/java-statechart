#!/usr/bin/env bash

set -eu

if [ ! -d "${PLANT_UML_PATH:-}" ]
then
  >&2 echo "Please set PLANT_UML_PATH to the directory containing plantuml.jar"
  exit 1
fi
java -jar "${PLANT_UML_PATH}/plantuml.jar" -pipe < diagram-desired.txt > images/diagram-desired.png
