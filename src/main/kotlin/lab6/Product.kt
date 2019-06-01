package lab6

data class Product(
    override val R: Int,
    override val l: Int
): IProduct {
    override var F: Int = 0

    override var a: Int = 0
    set(value) {
        field = value
        F += value
        Product.A += value
        C = b - value
    }

    override var C: Int = 0
    set(value) {
        field = value
        when {
            field < 0 -> {
                b = R
                D += field
                Product.H += field
            }
            field < l -> b = field + R
            else -> b = field
        }
    }

    override var D: Int = 0

    override var b: Int = R

    override val K: Double
        get() = D.toDouble() / F

    companion object {
        /**
         * суммарная заявка потребителей на все виды товара с первого дня моделирования по текущий день
         */
        var A: Int = 0

        /**
         * общее кол-во неудовлетворенных заявок потребителей за весь период моделирования
         */
        var H: Int = 0

        /**
         * доля всех не выполненных заявок потребителей к общему числу заявок на весь период моделирования
         */
        val L: Double
            get() = H.toDouble() / A
    }
}

data class HistoryProduct(
    override val R: Int,
    override val l: Int,
    override var F: Int,
    override var a: Int,
    override var C: Int,
    override var D: Int,
    override var b: Int,
    override val K: Double
): IProduct

fun IProduct.toHistory() = HistoryProduct(R, l, F, a, C, D, b, K)

interface IProduct {
    /**
     * Заказ оптового всех потребителей на товар на текущий день
     */
    val R: Int

    /**
     * Минимальное число мест на складе под товар, при котором еще не требуется дополнительных закупок на предприятии
     */
    val l: Int

    /**
     * общее число заявок потребителей на товар с первого дня моделирования по текущий день
     */
    var F: Int

    /**
     * суммарные заявки производство товаров предприятия
     */
    var a: Int

    /**
     * остаток товара после удовлетворения всех заявок покупателей на текущий день
     */
    var C: Int

    /**
     * кол-во товара, которого не хватило для удовлетворения заявок потребителей на товар, начиная с первого дня моделирования на текущий день
     */
    var D: Int

    /**
     * кол-во товара на начало дня после произведения закупок всеми потребителями в предыдущий день и выполнения заказов на склада предприятиям на следующий день
     */
    var b: Int

    /**
     * доля товара, невыданного потребителям к общему числу заявок за весь период моделирования
     */
    val K: Double
}
