
# YARNClient

## build

mvn install

(create fatJar)

## run (normal jar mode)

```bash
# create hdfsClient(uploader)
cd hdfsClient
mvn clean package
cd ../

# create normalBatch
cd normalBatch/
mvn clean package
cd ../

# upload normalBatch (usage: <hdfs NameNode IP>, <localFile>, <targetHdfsPath>)
java -jar hdfsClient/target/hdfsClient-0.1.0.jar 192.168.133.214 ./normalBatch/target/normalBatch-0.1.0.jar /tmp/normalBatch-0.1.0.jar

# create yarnAppMaster
cd yarnAppMaster/
mvn clean package
cd ../

# upload yarnAppMaster (usage: <hdfs NameNode IP> <localFile> <targetHdfsPath>)
java -jar hdfsClient/target/hdfsClient-0.1.0.jar 192.168.133.214 ./yarnAppMaster/target/yarnAppMaster-0.1.0.jar /tmp/yarnAppMaster-0.1.0.jar

# create yarnClient
cd yarnClient/
## edit mainClass as "com.github.uryyyyyyy.hadoop.yarn.client.LaunchApp"
vim pom.xml
mvn clean package
cd ../

# launchApp (usage: <ResourceManager IP> <hdfs NameNode IP> <appMasterPath in HDFS> <normalBatchPath in HDFS> <normalBatch mainClass> <normalBach args> ...)
java -jar ./yarnClient/target/yarnClient-0.1.0.jar 192.168.133.214 hdfs://192.168.133.214/ /tmp/yarnAppMaster-0.1.5.jar /tmp/normalBatch-0.1.0.jar com.github.uryyyyyyy.hadoop.yarn.appMaster.Main com.github.uryyyyyyy.normal.Main hoge hugo

```



## run (spark mode)

```bash
# create hdfsClient(uploader)
cd hdfsClient
mvn clean package
cd ../

# create sparkBatch
cd sparkBatch/
mvn clean package
cd ../

# upload normalBatch (usage: <hdfs NameNode IP>, <localFile>, <targetHdfsPath>)
java -jar hdfsClient/target/hdfsClient-0.1.0.jar 192.168.133.214 ./sparkBatch/target/sparkBatch-0.1.0.jar /tmp/sparkBatch-0.1.0.jar

# create yarnClient
cd yarnClient/
## edit mainClass as "com.github.uryyyyyyy.hadoop.yarn.client.LaunchSparkApp"
vim pom.xml
mvn clean package
cd ../

# launchApp (usage: <ResourceManager IP> <hdfs NameNode IP> <appMasterPath in HDFS> <normalBatchPath in HDFS> <normalBatch mainClass> <normalBach args> ...)
java -jar ./yarnClient/target/yarnClient-0.1.0.jar 192.168.133.214 hdfs://192.168.133.214/ /tmp/yarnAppMaster-0.1.5.jar /tmp/normalBatch-0.1.0.jar com.github.uryyyyyyy.hadoop.yarn.appMaster.Main com.github.uryyyyyyy.normal.Main hoge hugo

```

## kill app

