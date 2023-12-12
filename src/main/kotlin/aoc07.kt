package main.kotlin

import java.io.File

val cardValues = mapOf(
    "T" to "V",
    "J" to "W",
    "Q" to "X",
    "K" to "Y",
    "A" to "Z",
) + ('2'..'9').map{it.toString()}.
zip(('B'..'I').map{it.toString()})

fun main(args: Array<String>) {
    println(getPart71())
    println(getPart72())
}
private fun getPart71(): Int {
    val input = File("input07.txt").readLines().map { it.split(" ").zipWithNext().first() }
    return calcAllHands(input, cardValues, false)
}

private fun getPart72(): Int {
    val input = File("input07.txt").readLines().map { it.split(" ").zipWithNext().first() }
    val cardValuesJokerRules = cardValues.mapValues { it.value.replace("W", "A") }
    return calcAllHands(input, cardValuesJokerRules, true)
}
private fun calcAllHands(hands : List<Pair<String, String>>, cardValues : Map<String, String>, jokerRules : Boolean) : Int {
    val calculatedHands = hands.map {
        Triple(getHandType(it.first, jokerRules), it.first.convertHand(cardValues), it.second.toInt())
    }.sortedWith(compareByDescending<Triple<Int, String, Int>> { it.first }.thenBy{ it.second } )

    return calculatedHands.mapIndexed { index, triple ->
        (1+index) * triple.third }.sum()
}

private fun String.convertHand(map : Map<String, String>) : String {
    return this.map { it.toString() }.joinToString(separator = "") { map[it]!!}
}

private fun getHandType(hand : String, jokerRules: Boolean) : Int {
    val cardGroup = hand.groupingBy { it }.eachCount().map { Pair(it.key, it.value) }.sortedByDescending { it.second }.toMutableList()

    if(jokerRules){
        val jokerCount = cardGroup.find{it.first == 'J'}
        if(jokerCount != null && jokerCount.second != 5){
            cardGroup.remove(jokerCount)
            val (type, count) = cardGroup[0]
            cardGroup[0] = Pair(type, count+jokerCount.second)
        }
    }

    val cardTypeCount = cardGroup.count()

    return when {
        cardTypeCount == 1 -> 0 // Five of a kind
        cardGroup.any { it.second == 4 } -> 1 //Four of a kind
        cardTypeCount == 2 -> 2 //Full house (Must be placed after four of a kind check, or it could give false answers)
        cardGroup.any { it.second == 3 } -> 3 //Three of a kind
        cardTypeCount == 3 -> 4 //Two pair
        cardTypeCount == 4 -> 5 //One pair
        else -> 6 //High card
    }
}