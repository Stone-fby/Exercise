data class Person(val name: String, val age: Int)

fun main() {
    val person = Person("Alice", 30)

    val (name, age) = person
    println("$name is $age years old") // Alice is 30 years old
}