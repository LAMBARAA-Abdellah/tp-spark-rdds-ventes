package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class TotalSalesByCity {
    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("Sale").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> rddLines = sc.textFile("text.txt");  // Assure-toi que ce fichier est bien présent

        // Transformation : (ville, prix)
        JavaPairRDD<String, Double> ventes = rddLines.mapToPair(line -> {
            String[] parts = line.split(",");
            String city = parts[1];
            double price = Double.parseDouble(parts[3]);
            return new Tuple2<>(city, price);
        });

        // Agrégation
        JavaPairRDD<String, Double> totalParVille = ventes.reduceByKey(Double::sum);

        // Affichage
        totalParVille.collect().forEach(tuple ->
                System.out.println("City : " + tuple._1 + " | Total des ventes : " + tuple._2 + " MAD")
        );

        sc.close();
    }
}
