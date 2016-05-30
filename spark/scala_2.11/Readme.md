
## Spark with Scala2.11

（とりあえず日本語で書きます。）

SparkをScala2.11で動かす際の色々をまとめています。
なお、動作確認は以下の環境で行っています。

- EMR: 4.6.0
- Spark: 1.6.x
- Scala: 2.11.x
- Java: 8

## 動かし方

ここでは一番簡単な例として、`batch_helloWorld`プロジェクトを動かしてみます。

### 1. 事前準備

#### 1-1. Spark-package/assemblyの準備

こちらをご確認ください。

http://qiita.com/uryyyyyyy/items/e9ec40a8c748d82d4bc4

#### 1-2. batch_helloWorld jarの準備

```
./activator batch_helloWorld/assembly
```

これで実行可能jarが生成されます。

### 2. localで動かす

```
spark-submit --class com.github.uryyyyyyy.hadoop.spark.batch.helloWorld.Hello --master local spark2.11_batch_helloWorld-assembly-1.0.jar
```

### 3. yarn-clusterで動かす

#### 4. spark-submitする場合

EMRのマスターノードにsshログイン

```
spark-submit --class com.github.uryyyyyyy.hadoop.spark.batch.helloWorld.Hello --master yarn-cluster --conf spark.hadoop.mapred.output.committer.class=org.apache.hadoop.mapred.DirectOutputCommitter --conf spark.yarn.jar="s3://<bucket>/path/spark-assembly-1.6.1-hadoop2.6.0.jar" s3://<bucket>/path/spark2.11_batch_helloWorld-assembly-1.0.jar
```