import java.awt.Point
import java.io.File

val pipeMap = mapOf(
    '|' to (Point(0, -1) to Point(0, 1)),
    '-' to (Point(-1, 0) to Point(1, 0)),
    'L' to (Point(0, -1) to Point(1, 0)),
    'J' to (Point(0, -1) to Point(-1, 0)),
    '7' to (Point(0, 1) to Point(-1, 0)),
    'F' to (Point(0, 1) to Point(1, 0)),
    'S' to (Point(0, -1) to Point(-1, 0)),
)

fun main(args: Array<String>) {
    val input = File("input10demo.txt")
        .readLines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, char ->
                Point(x, y) to char }
        }
        .toMap()

    println(getPart101(input))
    println(getPart102())
}

fun getPart101(input: Map<Point, Char>): Int {
    val start = input.entries.find { it.value == 'S' }!!.key
    val outPut = mutableMapOf<Point, Int>()
    fun marioTime(current: Point) : Point {
        print(outPut)
        if(current == start && outPut[current] != null) return current
        outPut.merge(current, 1, Integer::sum)
        val connecting = pipeMap[input[current]]
        marioTime(connecting!!.first)
        marioTime(connecting.second)
        return current

    }
    val mario = marioTime(start)
    println(input)
    return 0
}



fun getPart102(): Long {
    return 0L
}
