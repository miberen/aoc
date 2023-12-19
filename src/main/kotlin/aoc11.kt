package main.kotlin
import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val input = File("input11.txt").readText().lines()

    println(getPart111(input))
    println(getPart112(input))
}
private fun getPart111(input : List<String>): Long {
    val starList = getStarList(input)
    return getAllPointPairs(starList).sumOf { points ->
        abs(abs(points.first.y-points.second.y) + abs(points.first.x-points.second.x))
    }
}
private fun getPart112(input : List<String>): Long {
    val starList = getStarList(input, true)
    return getAllPointPairs(starList).sumOf { points ->
        abs(abs(points.first.y-points.second.y) + abs(points.first.x-points.second.x))
    }
}
private data class Point(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
}
private fun getStarList(input: List<String>, oldUniverse: Boolean = false): List<Point> {
    return input.flatMapIndexed { index, line ->
        val expansionY = getExpansionY(input, index, oldUniverse)
        line.mapIndexed {cIndex, char ->
            if(char == '#'){
                val expansionX = getExpansionX(input, cIndex, oldUniverse)
                Point(cIndex + expansionX, index+expansionY)
            }
            else null
        }.filterNotNull()
    }
}

private fun getAllPointPairs(points: List<Point>): List<Pair<Point, Point>> {
    return points.flatMapIndexed { index, point1 ->
        points.subList(index + 1, points.size).map { point2 ->
            Pair(point1, point2)
        }
    }
}
private fun getExpansionX(input: List<String>, currentPoint : Int, oldUniverse : Boolean ): Int {
    return getExpansionY(rot90Deg(input), currentPoint, oldUniverse)

}

private fun getExpansionY(input: List<String>, currentPoint : Int, oldUniverse : Boolean): Int {
    val count = input.filterIndexed { index, s ->
        s.all { it == '.' } && index < currentPoint
    }.count()
    return count * if(oldUniverse) 999999 else 1
}


private fun rot90Deg(strings: List<String>): MutableList<String>  {
    val maxLength = strings.maxOfOrNull { it.length } ?: return mutableListOf()
    return (0..<maxLength).map { i ->
        strings.map { it.getOrNull(i) ?: ' ' }.reversed().joinToString("")
    }.toMutableList()
}
