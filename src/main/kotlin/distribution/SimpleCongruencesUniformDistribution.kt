package distribution

class SimpleCongruencesUniformDistribution(
    private val from: Double,
    private val to: Double,
    seed: Int = 1250
): Distribution {

    private var prevValue = seed

    private val a = 3883
    private val p = (2 pow Int.SIZE_BITS / 2 - 2).toInt()

    override fun getNextNumber() = ((a * prevValue % p).apply {
        prevValue = this
    } / p.toDouble()).let { d -> d * length + from }

    override val length = to - from
}

private infix fun Number.pow(i: Double) = Math.pow(this.toDouble(), i)
infix fun Number.pow(i: Int) = Math.pow(this.toDouble(), i.toDouble())

fun main() {
    val d = SimpleCongruencesUniformDistribution(0.0, 1.0)

    println(d.getNextNumber())
    println(d.getNextNumber())
    println(d.getNextNumber())
    println(d.getNextNumber())
    println(d.getNextNumber())
    println(d.getNextNumber())
    println(d.getNextNumber())
    println(d.getNextNumber())
    println(d.getNextNumber())
    println(d.getNextNumber())
}
