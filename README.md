# Yuzu
## A Scala + Akka Streams massive fake data generator for Elasticsearch

Generates fake JSON data for elasticsearch research and development. Built for massive dataset generation. 
Uses Akka streams run output in parallel, segmenting JSON generation and combine into final JSON output. 
JSON is transferred into Elasticsearch using the official Elasticsearch Java library, connecting through TCP. 
Results are passed in bulk uploads of 10,000 documents.

## Elasticsearch Settings
Make sure to increase the heap memory allocation from the default 1GB by following the directions here: 
https://www.elastic.co/guide/en/elasticsearch/guide/current/heap-sizing.html
If you do not increase the heap size, due to the rate of ingestion Elasticsearch will likely crash during indexing.
For a load of 30 million documents, about 8GB of memory was consumed, and 10GB allocation was successful.   

## Source Data
Name data is from U.S. Census data for most popular names. The source data set includes 1,219 Male first names,
4,275 Female first names, and 88,799 last names. Street names are from San Francisco street names data retrieved through
data.gov, and consist of 2,556 street names.

## JVM settings
Increase or reduce memory as needed, but make sure both match.
-Xms6g
-Xmx6g
-XX:-UseParallelGC
