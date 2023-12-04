import java.io.File
import kotlin.math.pow

fun main(args: Array<String>) {
    println(getPart41())
    println(getPart42())
}
fun getPart41(): Int {
    val input = File("input04.txt").readLines()
    val sum = input.sumOf { card ->
        card.substringAfter(":")
            .split("|")
            .map { x ->
                x.trim().replace("  ", " ").split(" ")
            }
            .zipWithNext()
            .map { (winning, scratch) ->
                val count = winning.intersect(scratch.toSet()).count()

                if (count > 0) 2f.pow(count-1).toInt()
                else 0
            }.first()
    }
    return sum
}
fun getPart42(): Int {
    val input = File("input04.txt").readLines()
    val scoreMap =
        (0..input.count())
            .zip(List(input.count()) { 1 })
            .toMap().toMutableMap()

    input.map { card ->
        card.substringAfter(":")
            .split("|")
            .map { x ->
                x.trim().replace("  ", " ").split(" ")
                    .map { y -> y.toInt() }
            }.zipWithNext()
            .sumOf { (winning, scratch) ->
                winning.intersect(scratch.toSet()).count()
            }
    }.forEachIndexed { index, i ->
        for (x in 1..i) {
            scoreMap[index + x] = scoreMap[index + x]!! + scoreMap[index]!!
        }
    }
    return scoreMap.values.sum()
}