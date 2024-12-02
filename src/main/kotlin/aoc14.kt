package main.kotlin

import java.awt.Point
import java.io.File

private enum class Direction {
    NORTH, SOUTH, EAST, WEST
}

private enum class TileType(val l : Char) {
    SOLID('#'), ROLLER('O')
}

private data class RockMap(val rocks: Map<Point, TileType>, val dimensions: Point) {

}

fun main(args: Array<String>) {
    val input = File("input14demo.txt").readText().lines()
    val rocks = RockMap(
        input.flatMapIndexed { y, line ->
            line.mapIndexed { x, s ->
                when (s) {
                    '#' -> Point(x, y) to TileType.SOLID
                    'O' -> Point(x, y) to TileType.ROLLER
                    else -> null
                }
            }.filterNotNull()
        }.toMap(),
        Point(input.first().length - 1, input.count() - 1)
    )

    println(getPart141(rocks))
    println(getPart142(rocks))
}

private fun getPart141(input: RockMap): Int {
    val moved = moveRocks(input, Direction.NORTH)
    return 0
}

private fun getPart142(input: RockMap): Long {
    return 0
}

private fun moveRocks(input: RockMap, direction: Direction): Map<Point, TileType> {
    val loopParams = when(direction) {
        Direction.NORTH -> Triple(0..input.dimensions.y, 0..input.dimensions.x, 0)
        Direction.SOUTH -> Triple(input.dimensions.y downTo 0, input.dimensions.x downTo 0, 0)
        Direction.WEST -> Triple(0..input.dimensions.y, input.dimensions.x downTo 0, 1)
        Direction.EAST -> Triple(input.dimensions.y downTo 0, 0..input.dimensions.x, 1)
    }
    for (i in loopParams.first) {
        val line = input.rocks.filterKeys { if (loopParams.third == 0) it.y == i else it.x == i }


        //println(line.map { it.value.l }.joinToString(separator = ""))
//        for (j in loopParams.second) {
//            if(loopParams.third == 0)
//                print(input.rocks[Point(j, i)].let { it?.l ?: '.'})
//            else
//                print(input.rocks[Point(i, j)].let { it?.l ?: '.'})
//        }
        println()
    }
    return mapOf()
}

