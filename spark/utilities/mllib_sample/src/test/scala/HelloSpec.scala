import org.apache.spark.mllib.recommendation.{ALS, Rating}
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
    it("spark mllib"){

      val ratingsRaw = Seq(
        Rating(1, 1, 1.0),
        Rating(1, 2, 1.0),
        Rating(1, 3, 1.0),
        Rating(2, 1, 1.0),
        Rating(2, 2, 1.0)
      )

      // Load and parse the data
      val ratings = sc.parallelize(ratingsRaw)

      // Build the recommendation model using ALS
      val rank = 10
      val numIterations = 10
      val model = ALS.train(ratings, rank, numIterations, 0.01)

      // Evaluate the model on rating data
      val usersProducts = ratings.map { case Rating(user, product, rate) =>
        (user, product)
      }
      val predictions =
        model.predict(usersProducts).map { case Rating(user, product, rate) =>
          ((user, product), rate)
        }
      val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }.join(predictions)
      val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
        val err = r1 - r2
        err * err
      }.mean()
      println("Mean Squared Error = " + MSE)

      // Save and load model
      model.save(sc, "model.sample")
    }
  }
}
