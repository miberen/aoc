import java.io.File


fun main(args: Array<String>) {
    println(getPart61())
    println(getPart62())
}

fun getPart61(): Int {
    val input = File("input06.txt").readLines()
    val re = Regex("\\d+")
    val data = input.map { line -> re.findAll(line).toList().map { it.value.toInt() }}
    val zipped = data[0].zip(data[1])

    val sum = mutableListOf<Int>()

    zipped.forEach { (time, distance) ->
        var count = 0
        (1..time).forEach { holdDownDuration ->
            val moved = holdDownDuration*(time-holdDownDuration)
            if(moved > distance)
                count++
        }
        sum.add(count)
    }

    return sum.reduce{acc, i -> acc * i}
}

fun getPart62(): Int {
    val input = File("input06.txt").readText().replace(" ", "")
    val re = Regex("\\d+")
    val (time, distance) = re.findAll(input).map { it.value.toLong() }.toList()

    var result = 0

    (1..time).forEach { holdDownDuration ->
        val moved = holdDownDuration*(time-holdDownDuration)
        if(moved > distance)
            result++
    }

    return result
}
