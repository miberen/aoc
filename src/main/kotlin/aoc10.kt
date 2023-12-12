package main.kotlin

import java.awt.Point
import java.io.File

val pipeMap = mapOf(
    '|' to (Point(0, -1) to Point(0, 1)),
    '-' to (Point(-1, 0) to Point(1, 0)),
    'L' to (Point(0, -1) to Point(1, 0)),
    'J' to (Point(0, -1) to Point(-1, 0)),
    '7' to (Point(0, 1) to Point(-1, 0)),
    'F' to (Point(0, 1) to Point(1, 0)),
)

private fun Point.move(input : Point) : Point {
    return Point(this.x + input.x, this.y + input.y)
}

private fun getStartMoves(start : Point, input: Map<Point, Pair<Point, Point>>) : Pair<Point, Point> {
    return input.entries.filter {
        start == it.value.first || start == it.value.second
    }.zipWithNext().map { it.first.key to it.second.key }.single()
}

tailrec fun marioTime(start : Point, current : Point, last : Point, pipeRoutes : Map<Point, Pair<Point, Point>>, sum : Int) : Int {
    if(start == current && sum > 0) return sum
    val route = if(pipeRoutes[current]!!.first == last) pipeRoutes[current]!!.second else pipeRoutes[current]!!.first
    return marioTime(start, route, current, pipeRoutes, sum+1)
}

tailrec fun marioTime2(start : Point, current : Point, last : Point, pipeRoutes : Map<Point, Pair<Point, Point>>, output : MutableMap<Point, Int>)  {
    if(start == current && output[start]!! >= 1) return
    val route = if(pipeRoutes[current]!!.first == last) pipeRoutes[current]!!.second else pipeRoutes[current]!!.first
    output[current] = 1
    marioTime2(start, route, current, pipeRoutes, output)
}

fun main(args: Array<String>) {
    val input = File("input10demo2.txt").readText()
    var start = Point()

    val map = input.split("\r\n").flatMapIndexed { y, line ->
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
    println(getPart101(start, map))
    println(getPart102(start, map))
}
private fun getPart101(start : Point, input: Map<Point, Pair<Point, Point>>): Int {
    return marioTime(start, start, start, input, 0) / 2

}

private fun getPart102(start : Point, input: Map<Point, Pair<Point, Point>>): Long {
    val output = input.map { it.key to 0 }.toMap().toMutableMap()
    marioTime2(start, start, start, input, output)
    output.map {
        it.value
    }

    return 0
}
