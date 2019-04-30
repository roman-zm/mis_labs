package distribution

interface INormalDistribution: Distribution {
    val expectedValue: Double
    val standardDeviation: Double
}
