
usage

at local

```
./bin/spark-submit --class com.sample.Main --master local \
--packages org.apache.hadoop:hadoop-aws:2.7.1 \
yourSparkApp-assembly-1.0.jar \
<bucket>/input/path <bucket>/output/path
```

at EMR

```
spark-submit --class com.sample.Main --master yarn-cluster \
yourSparkApp-assembly-1.0.jar \
<bucket>/input/path <bucket>/output/path
```