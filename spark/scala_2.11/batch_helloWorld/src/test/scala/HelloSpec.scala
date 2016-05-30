import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest._

class HelloSpec extends FunSpec with MustMatchers {

  describe("SparkRDD") {
    it("test1") {
      val conf = new SparkConf().setAppName("Simple Application").setMaster("local[3]")
      val sc = new SparkContext(conf)

      val rdd = sc.parallelize(1 to 10)
      val value = rdd.map(_ * 2).reduce(_ + _)
      value mustBe 110

      sc.stop()
    }
  }
}
