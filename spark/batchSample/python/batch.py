from __future__ import print_function

import sys

from pyspark import SparkContext
from pyspark.streaming import StreamingContext

if __name__ == "__main__":
    sc = SparkContext(appName="PythonStreamingNetworkWordCount")
    rdd = sc.range(1, 1000)

    counts = rdd.map(lambda i: i * 2)
    counts.saveAsTextFile("/tmp/pythooon")