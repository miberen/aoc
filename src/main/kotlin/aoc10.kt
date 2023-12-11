import java.awt.Point
import java.io.File
import java.util.*

val pipeMap = mapOf(
    '|' to (Point(0, -1) to Point(0, 1)),
    '-' to (Point(-1, 0) to Point(1, 0)),
    'L' to (Point(0, -1) to Point(1, 0)),
    'J' to (Point(0, -1) to Point(-1, 0)),
    '7' to (Point(0, 1) to Point(-1, 0)),
    'F' to (Point(0, 1) to Point(1, 0)),
    //'S' to (Point(0, -1) to Point(-1, 0)), //input
    //'S' to (Point(1, 0) to Point(0, 1)), //demo

)

fun Point.move(input : Point) : Point {
    return Point(this.x + input.x, this.y + input.y)
}

fun Point.isDot(input : Pair<Point, Point>) : Boolean {
    return this == input.first && this == input.second
}


fun getStartMoves(start : Point, input: Map<Point, Pair<Point, Point>>) : Pair<Point, Point> {
    return input.entries.filter {
        start == it.value.first || start == it.value.second
    }.zipWithNext().map { it.first.key to it.second.key }.single()
}

tailrec fun marioTime(start : Point, current : Point, last : Point, pipeRoutes : Map<Point, Pair<Point, Point>>, sum : Int) : Int {
    if(start == current && sum > 0) return sum
    val routes = pipeRoutes[current]!!
    return if(routes.first == last) {
        marioTime(start, routes.second, current, pipeRoutes, sum+1)
    } else {
        marioTime(start, routes.first, current, pipeRoutes, sum+1)
    }
}

fun marioTime2(start : Point, current : Point, last : Point, pipeRoutes : Map<Point, Pair<Point, Point>>, output : MutableMap<Point, Int>)  {
    if(start == current && output[start]!! >= 1) return
    val routes = pipeRoutes[current]!!
    if(routes.first == last) {
        output[current] = 1
        marioTime2(start, routes.second, current, pipeRoutes, output)
    } else {
        output[current] = 1
        marioTime2(start, routes.first, current, pipeRoutes, output)
    }
}

fun main(args: Array<String>) {
    val input = File("input10.txt").readText()
    var start = Point()

    val map = input.split("\n").flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char ->
            val point = Point(x, y)
            when (char) {
                '.' ->  point to Pair(point, point) // Ignore these characters
                'S' -> {
                    start = point
                    point to (Pair(Point(-5,-5), Point(-5, -5)))
                }
                else -> {
                    val movements = pipeMap[char]?.let { (first, second) ->
                        Pair(point.move(first), point.move(second))
                    }
                    point to (movements ?: Pair(point, point))
                }
            }
        }
    }.toMap().toMutableMap()
    map[start] = getStartMoves(start, map)
    //println(getPart101(start, map))
    println(getPart102(start, map))
}
fun getPart101(start : Point, input: Map<Point, Pair<Point, Point>>): Int {
    return marioTime(start, start, start, input, 0) / 2

}

fun getPart102(start : Point, input: Map<Point, Pair<Point, Point>>): Long {
    val output = input.map { it.key to 0 }.toMap().toMutableMap()
    marioTime2(start, start, start, input, output)
    output.map {
        it.value
    }.chunked(140).forEach{ println(it) }
    return 0
}
