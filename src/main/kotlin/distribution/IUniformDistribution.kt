package distribution

interface IUniformDistribution: Distribution {
    val length: Double
    val from: Double
    val to: Double
}