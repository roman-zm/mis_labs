package distribution

class SimpleCongruencesUniformDistribution(
    override val from: Double,
    override val to: Double,
    seed: Int = 1250,
    val a: Int = 3883,
    val p: Int = 2 pow Int.SIZE_BITS
): IUniformDistribution {
    override fun getNextNumber() = (a * prevValue % p).apply {
        prevValue = this
    }.toDouble() / p * length + from

    override val length = to - from

    private var prevValue: Long = seed.toLong()
}

private infix fun Number.pow(i: Double) = Math.pow(this.toDouble(), i)
infix fun Number.pow(i: Int) = Math.pow(this.toDouble(), i.toDouble()).toInt()

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
