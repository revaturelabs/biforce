package com.revature.spark;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class WordCount implements Serializable {
	private static final long serialVersionUID = -7742147040731572615L;

	public void execute(JavaSparkContext context, String inputPath, String outputPath, Integer minCount) {
		/*
		 * Load all lines RDD
		 */
		JavaRDD<String> lines = context.textFile(inputPath);
		
		/*
		 * Split all words by blank space
		 */
		JavaRDD<String> words = lines.flatMap((line) -> {
			return Arrays.asList(line.split("\\W+")).iterator();
		});
		
		/*
		 * Mark the word ocurrence (1)
		 */
		JavaPairRDD<String, Integer> count = words.mapToPair((word) -> {
			return new Tuple2<String,Integer>(word, 1);
		});
		
		/*
		 * Reduce with the sum of the counts
		 */
		JavaPairRDD<String, Integer> wordCount = count.reduceByKey(Integer::sum);
		
		/*
		 * Filter by minimum count
		 */
		JavaPairRDD<String, Integer> filtered = wordCount.filter((countTuple) -> {
			/*
			 * This is Scala notation, get the second value on the tuple
			 */
			return countTuple._2 >= minCount;
		});
		
		/*
		 * Sort the output descending
		 */
		JavaPairRDD<String, Integer> sorted = filtered.sortByKey(false);
		
		
		/*
		 * Print only the first 100 use .collect() for all words
		 */
		for(Tuple2<String,Integer> wordCountPair: sorted.take(100)) {
			System.out.println(wordCountPair);
		}
		
		/*
		 * Store all output in a file
		 */
		sorted.saveAsTextFile(outputPath);
	}
	
	/*
	 * One line, Welcome to the Functional Programming
	 */
	public void oneLine(JavaSparkContext context, String inputPath, String outputPath, Integer minCount) {
		context.textFile(inputPath)
		.flatMap((line) -> Arrays.asList(line.split("\\W+")).iterator())
		.mapToPair((word) -> new Tuple2<String,Integer>(word,1))
		.reduceByKey(Integer::sum)
		.filter((countTuple) -> countTuple._2 >= minCount)
		.sortByKey(true)
		.saveAsTextFile(outputPath);
	}
}
