#!/usr/bin/env bash
mvn versions:set -DnewVersion=$@
mvn versions:update-child-modules