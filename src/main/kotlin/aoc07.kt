import java.io.File


fun main(args: Array<String>) {
    println(getPart71())
    println(getPart72())
}
val de = 0xA

val cardValues = mapOf(
    "T" to "10",
    "J" to "11",
    "Q" to "12",
    "K" to "13",
    "A" to "14",
) //+ (1..9).map { it.toString() }.associateWith { it }


fun String.handToInt() : Int {
    return cardValues.entries.fold(this) { acc, (key, value) -> acc.replace(key, value)}.toInt()
}

fun calcHand(hand : String) : Pair<Int, Int> {
    val cardSetCount = hand.toSet().count()
    if (cardSetCount == 1) //Five of a kind
        return Pair(hand.handToInt(), 0)

    val cardGroup = hand.groupingBy { it }.eachCount()

    if (cardGroup.filter { it.value == 4 }.isNotEmpty()) //Four of a kind
        return Pair(hand.handToInt(), 1)
    if (cardSetCount == 2) //Full house (Must be placed after four of a kind check, or it could give false answers)
        return Pair(hand.handToInt(), 2)
    if (cardGroup.filter { it.value == 3 }.isNotEmpty()) //Three of a kind
        return Pair(hand.handToInt(), 3)
    if (cardSetCount == 3) //Two pair
        return Pair(hand.handToInt(), 4)
    if(cardSetCount == 4) //One pair
        return Pair(hand.handToInt(), 5)
    else //High card
        return Pair(hand.handToInt(), 6)
}


fun getPart71(): Int {
    val input = File("input07demo.txt").readLines().map { it.split(" ").zipWithNext().first() }
    var sum = 0
    val calculatedHands = input.map {
        val (handInt, type) = calcHand(it.first)
        Triple(it.first, type, it.second.toInt())
    }.sortedWith(compareByDescending<Triple<String, Int, Int>> { it.second }.thenByDescending { it.first } )

    return calculatedHands.mapIndexed { index, triple -> (1+index) * triple.third }.sum()
}

fun getPart72(): Int {
    val input = File("input07demo.txt").readText()
    return 0
}


