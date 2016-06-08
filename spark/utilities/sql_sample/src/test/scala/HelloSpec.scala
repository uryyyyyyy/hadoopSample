import com.github.uryyyyyyy.hadoop.spark.sql.Person
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest._

class HelloSpec extends FunSpec
  with MustMatchers
  with BeforeAndAfterEach {

  var conf:SparkConf = null
  var sc:SparkContext = null
  var sqlContext: SQLContext = null

  override def beforeEach(): Unit ={
    conf = new SparkConf().setAppName("unitTestApp").setMaster("local[1]")
    sc = new SparkContext(conf)
    sqlContext = new SQLContext(sc)
  }

  override def afterEach(): Unit ={
    sc.stop()
  }

  describe("Hello"){
    it("spark sql"){

      val rdd = sc.parallelize(Seq(Person("a", 16), Person("b", 17), Person("c", 18)))
      val dataFrame = sqlContext.createDataFrame(rdd)

      dataFrame.registerTempTable("person")
      println(dataFrame.printSchema())
      println(dataFrame.show())
      val dataFrame2 = dataFrame.sqlContext.sql("select name from person where id = 17")
      println(dataFrame2.show())
    }
  }
}
