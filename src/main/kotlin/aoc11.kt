import java.awt.Point
import java.io.File
import kotlin.math.abs

fun main(args: Array<String>) {
    val input = File("input11.txt").readText().lines()

    println(getPart111(duplicateDots(input)))
    println(getPart112(duplicateDots(input, true)))
}
fun getPart111(input : List<String>): Long {
    val starList = input.flatMapIndexed { index, line ->
        line.mapIndexed {cIndex, char ->
            if(char == '#') Point(cIndex, index)
            else null
        }.filterNotNull()
    }
    return getAllPointPairs(starList).sumOf { points ->
        abs(abs(points.first.y-points.second.y) + abs(points.first.x-points.second.x))
    }.toLong()
}
fun getPart112(input : List<String>): Int {

    return 0
}
fun getAllPointPairs(points: List<Point>): List<Pair<Point, Point>> {
    return points.flatMapIndexed { index, point1 ->
        points.subList(index + 1, points.size).map { point2 ->
            Pair(point1, point2)
        }
    }
}
fun duplicateDots(input: List<String>, oldUniverse : Boolean = false): List<String> {
    var output = mutableListOf<String>()
    //rows
    input.forEach {line ->
        if(line.all { it == '.' }) {
            if(oldUniverse) output.add(line)
            else output.addAll(List(2) {line})
        }
        else output.add(line)
    }
    //Columns
    output = rot90Deg(output)
    rot90Deg(input).forEachIndexed { index, line ->
        if(line.all { it == '.' }) {
            val offset = index + (output.count() - input.count())
            if(oldUniverse) output.addAll(offset, List(1000000) { output[offset] })
            else output.add(offset, output[offset])
        }
    }
    return rot90Deg(rot90Deg(rot90Deg(output)))
}


fun rot90Deg(strings: List<String>): MutableList<String>  {
    val maxLength = strings.maxOfOrNull { it.length } ?: return mutableListOf()
    return (0 until maxLength).map { i ->
        strings.map { it.getOrNull(i) ?: ' ' }.reversed().joinToString("")
    }.toMutableList()
}
