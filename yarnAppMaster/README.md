
# YARNClient

## build

mvn install

(create fatJar)

## run

`usage: <hdfsUrl> <localFile> <hdfsDir>`

like this

`java -jar ./target/yarnClientJava-0.1.0.jar 192.168.133.214 hdfs://192.168.133.214/ tmp/yarnAppMasterJava-0.14.0.jar tmp/yarnNormalJar-assembly-1.0.jar com.github.uryyyyyyy.hadoop.yarn.appMaster.Main com.example.yarn.app.normal.HelloWorld`

