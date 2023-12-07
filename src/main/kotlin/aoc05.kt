import java.io.File

class Mapping(val maps: List<Row>) {

}

class Row(val dest: UInt, private val source: UInt, private val range: UInt) {
    val destRange: UIntRange = UIntRange(dest, dest + range - 1u)
    val sourceRange: UIntRange = UIntRange(source, source + range - 1u)

}

fun main(args: Array<String>) {
    println(getPart51())
    println(getPart522())
}

fun getPart51(): UInt {
    val input = File("input05.txt").readText()
    val re = Regex("\\d[\\d \\r\\n]+")
    val data = re.findAll(input).toList()
    val seeds = data[0].value.split(" ").map { it.trim().toUInt() }.toSet()

    val maps = listOf(data.subList(1, data.count()).map { block -> block.value.trim().lines() }.map { blockLines ->
        Mapping(blockLines.map { line ->
            val (dest, source, range) = line.split(" ").map { it.toUInt() }
            Row(dest, source, range)
        })
    }).first()

    var lowest = UInt.MAX_VALUE

    seeds.forEach { seed ->
        var currentValue = seed
        maps.forEach outer@{ map ->
            map.maps.forEach { line ->
                if (line.sourceRange.contains(currentValue)) {
                    currentValue = line.destRange.first + currentValue - line.sourceRange.first
                    return@outer
                }
            }
        }
        lowest = lowest.coerceAtMost(currentValue)
    }

    return lowest
}

fun getPart52(): UInt {
    val input = File("input05.txt").readText()
    val re = Regex("\\d[\\d \\r\\n]+")
    val data = re.findAll(input).toList()
    val seeds =
        data[0].value.split(" ").map { it.trim().toUInt() }.chunked(2).map { pair -> pair.zipWithNext() }.flatten()

    val maps = listOf(data.subList(1, data.count()).map { block -> block.value.trim().lines() }.map { blockLines ->
        Mapping(blockLines.map { line ->
            val (dest, source, range) = line.split(" ").map { it.toUInt() }
            Row(dest, source, range)
        })
    }).first()

    var lowest = UInt.MAX_VALUE

    seeds.parallelStream().forEach { seedRange ->
        (seedRange.first..<seedRange.first + seedRange.second).forEach { seed ->
            var currentValue = seed
            maps.forEach outer@{ map ->
                map.maps.forEach { line ->
                    if (line.sourceRange.contains(currentValue)) {
                        currentValue = line.destRange.first + currentValue - line.sourceRange.first
                        return@outer
                    }
                }
            }
            lowest = lowest.coerceAtMost(currentValue)
        }
    }

    return lowest
}

fun getPart522(): UInt {
    val input = File("input05demo.txt").readText()
    val re = Regex("\\d[\\d \\r\\n]+")
    val data = re.findAll(input).toList()
    val seeds =
        data[0].value.split(" ").map { it.trim().toUInt() }.chunked(2).map { pair -> pair.zipWithNext() }.flatten()

    val maps = listOf(data.subList(1, data.count()).map { block -> block.value.trim().lines() }.map { blockLines ->
        Mapping(blockLines.map { line ->
            val (dest, source, range) = line.split(" ").map { it.toUInt() }
            Row(dest, source, range)
        })
    }).first()

    var lowest = UInt.MAX_VALUE

    seeds.forEach { seedRange ->
        maps.forEach { map ->
            var previousRanges = mutableListOf(UIntRange(seedRange.first, seedRange.first + seedRange.second-1u))
            var currentRanges = mutableListOf<UIntRange>()
            map.maps.forEach outer@{ line ->
                previousRanges.forEach { range ->
                    //Inputvalue is too high / low, pass it on
                    if(range.first > line.sourceRange.last && range.last > line.sourceRange.first){
                        currentRanges.add(UIntRange(range.first, range.last))
                    }
                    else if(range.first >= line.sourceRange.first && range.last <= line.sourceRange.last){
                        currentRanges.add(mapToDestRange(line, UIntRange(range.first, range.last)))
                    }
                    else {
                        if(range.first < line.sourceRange.first){
                            currentRanges.add(UIntRange(range.first, line.sourceRange.first))
                            currentRanges.add(mapToDestRange(line, UIntRange(line.destRange.first, range.last)))
                        }
                        else {
                            currentRanges.add(UIntRange(line.sourceRange.last, range.last ))
                            currentRanges.add(mapToDestRange(line, UIntRange(range.first, line.destRange.last, )))
                        }
                    }
                }
            }
            previousRanges = currentRanges
            println(currentRanges)
        }
    }

    return lowest
}

fun mapToDestRange(line : Row, range : UIntRange) : UIntRange{
    var newMin = line.destRange.first + range.first - line.sourceRange.first
    var newMax = line.destRange.first + range.last - line.sourceRange.first
    return UIntRange(newMin, newMax)
}