# sparkBatch

## build

mvn clean package

## run local

<sparkDir>/bin/spark-submit --class "com.github.uryyyyyyy.hadoop.spark.batch.Main" --master local[4] ./sparkBatch/target/sparkBatch-0.1.0.jar hdfs://192.111.111.111/my.txt

## run YARN(client mode)

```
# set hadoop conf
export HADOOP_CONF_DIR=/hoge/hadoop-2.5.2/etc/hadoop/

# upload spark-assembly jar

# set spark-assembly jar
export SPARK_JAR=hdfs://192.168.133.214:8020/tmp/spark-assembly-1.4.1-hadoop2.4.0.jar

# run spark
<sparkDir>/bin/spark-submit --class "com.github.uryyyyyyy.hadoop.spark.batch.Main" --master yarn-client ./sparkBatch/target/sparkBatch-0.1.0.jar hdfs://192.111.111.111/my.txt
```



## run YARN(cluster mode)

```
# set hadoop conf
export HADOOP_CONF_DIR=/hoge/hadoop-2.5.2/etc/hadoop/

# upload spark-assembly jar


# set spark-assembly jar
export SPARK_JAR=hdfs://192.168.133.214:8020/tmp/spark-assembly-1.4.1-hadoop2.4.0.jar

# upload sparkBatch

# run spark
<sparkDir>/bin/spark-submit --class "com.github.uryyyyyyy.hadoop.spark.batch.Main" --master yarn-cluster ./sparkBatch/target/sparkBatch-0.1.0.jar hdfs://192.111.111.111/my.txt
```