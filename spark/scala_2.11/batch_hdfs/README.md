
usage

at local

you can use s3n/s3a

```
./bin/spark-submit --class com.sample.Main --master local \
--packages org.apache.hadoop:hadoop-aws:2.7.1 \
yourSparkApp-assembly-1.0.jar \
<bucket>/input/path <bucket>/output/path
```

at EMR

you can use s3/s3a

```
spark-submit --class com.sample.Main --master yarn-cluster \
yourSparkApp-assembly-1.0.jar \
<bucket>/input/path <bucket>/output/path
```