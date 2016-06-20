# Yuzu
## A Scala + Akka Streams massive fake data generator for Elasticsearch

Generates fake JSON data for elasticsearch research and development. Built for massive dataset generation. 
Uses Akka streams run output in parallel, segmenting JSON generation and combine into final JSON output. 
JSON is transferred into Elasticsearch using the official Elasticsearch Java library, connecting through TCP. 
Batches of 10,000-100,000 fake person documents are then passed in bulk asynchronously to the cluster.

## Performance
Yuzu has been tested with a 10-node Elasticsearch v2.3.3 cluster running at 31GB of heap per node. [recommended maximum] (https://www.elastic.co/guide/en/elasticsearch/guide/current/heap-sizing.html) Each node was running on a 20-core, 64 GB memory Digital Ocean Docker VM. 

Creation and transfer of 100 million fake person documents into the cluster took 58 minutes.

## Elasticsearch Settings
* Increase the default bulk queue size from the default 50:
```
curl -XPUT localhost:9200/_cluster/settings -d '{
    "persistent" : {
        "threadpool.bulk.queue_size" : 3000
    }
}'
```

It may be helpful to increase the index queue size. Yuzu was tested with both bulk and index updated, although only bulk should be necessary.
```
curl -XPUT localhost:9200/_cluster/settings -d '{
    "persistent" : {
        "threadpool.index.queue_size" : 3000
    }
}'
```
For a bulk of 100M use

* Increase the heap memory allocation from the default 1GB by following the directions here: 
https://www.elastic.co/guide/en/elasticsearch/guide/current/heap-sizing.html

If you do not increase the heap size, due to the rate of ingestion Elasticsearch will likely crash during indexing.


## Source Data
Name data is from U.S. Census data for most popular names. The source data set includes 1,219 Male first names,
4,275 Female first names, and 88,799 last names. Street names are from San Francisco street names data retrieved through
data.gov, and consist of 2,556 street names.

## JVM settings
Increase or reduce memory as needed, but make sure both match.
-Xms31g
-Xmx31g
-XX:-UseParallelOldGC

## Use
For ease of use on a remote cluster, the app can be compiled into a jar by running:
```
sbt assembly
```
A yuzu.conf file containing the cluster settings and bulk size are required to run the application.
```javascript
"cluster" : {
  "name": "democluster"
  "location": "localhost:9300"
}
batch-size=10000
```
Note:
* Batches can be up to 100,000 while still maintaining performance. Any greater value may require that you modify the number of replicas and bulk queue size.
* Port 9300 is required for Yuzu, as it transfers data over TCP. You may see port 9200 in many other Elasticsearch documents, but that port is only for HTTP connections.
