# sparkStreaming

## build

mvn clean package

## run

<sparkDir>/bin/spark-submit --class "com.github.uryyyyyyy.hadoop.spark.streaming.Main" --master local[4] ./sparkStreaming/target/sparkStreaming-0.1.0.jar hdfs://XXX.XXX.XXX.XXX/mo.txt