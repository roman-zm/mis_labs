package lab6

import distribution.UniformDistribution

val productList = listOf(
    Product(R = 70, l = 30),
    Product(R = 50, l = 20),
    Product(R = 30, l = 8)
)

val consumers = 10
val consumerList = Array(consumers) {
    Consumer(UniformDistribution(0.0, 10.0, seed = it))
}.toList()

/**
 * Период моделирования
 */
val G = 20

fun main() {
    productList.forEachIndexed { index, product ->
        with(product) { println("Товар $index: b=$b, a=$a, C=$C D=$D, F=$F") }
    }
    println()

    repeat(G) { day ->
        println("День ${day + 1}:")
        productList.forEachIndexed { index, product ->
            product.a = consumerList.map { it.a }.sum()
            with(product) { println("Товар $index: b=$b, a=$a, C=$C D=$D, F=$F") }
        }
        println("H=${Product.H}, A=${Product.A}")
        println()
    }

    productList.forEachIndexed { index, product ->
        with(product) { println("Product $index: R=$R, l=$l, K=$K") }
    }

    println("L=${Product.L}")
}