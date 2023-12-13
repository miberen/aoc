package main.kotlin
import java.io.File

fun main(args: Array<String>) {
    val input = File("input12.txt").readText().lines().map {
        val (firstPart, secondPart) = it.split(" ", limit = 2)

        firstPart to secondPart.split(",").map(String::toInt)
    }

    println("\n" + getPart131(input))
    println("\n" + getPart132(input))
}
private fun getPart131(input: List<Pair<String, List<Int>>>): Int {


    return 0
}

private fun getPart132(input: List<Pair<String, List<Int>>>): Long {
    return 0
}

