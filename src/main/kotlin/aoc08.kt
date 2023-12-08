import java.io.File
import java.util.*

fun main(args: Array<String>) {
    println(getPart81())
    //println(getPart82())
}
fun getPart81(): Int {

    val input = File("input08.txt").readText()
    val instructions = input.substringBefore("\r").toList()
    val networks = Regex("\\w+(?=.*\\))").findAll(input).map { it.value }.chunked(3).associate { Pair(it[0], Pair(it[1], it[2])) }
    var currentLocation = "AAA"

    var sum = 0
    val instCount = instructions.count()
    while(currentLocation != "ZZZ"){
        val network = networks[currentLocation]!!
        currentLocation = if(instructions[sum % instCount] == 'L') network.first else network.second
        sum++
    }
    return sum
}

fun getPart82(): Int {
    return 0
}