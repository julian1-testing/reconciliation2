#!/bin/bash

DB='jdbc:postgresql://dbprod.emii.org.au/harvest?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory'

. ./credentials.sh

# acorn_hourly_avg_nonqc
if true; then
java -jar ./target/s3-example-1.0-SNAPSHOT-shaded.jar \
    -db "$DB" -u "$USER" -p "$PASS" \
    -bucket     imos-data \
    -prefix     IMOS/ACORN/gridded_1h-avg-current-map_non-QC \
    -s3Regex    '.*\.nc$' \
    -schema     acorn_hourly_avg_nonqc
fi


