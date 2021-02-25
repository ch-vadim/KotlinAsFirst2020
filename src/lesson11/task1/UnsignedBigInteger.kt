package lesson11.task1

import lesson3.task1.digitNumber
import java.lang.StringBuilder

/**
 * Класс "беззнаковое большое целое число".
 *
 * Общая сложность задания -- очень сложная, общая ценность в баллах -- 32.
 * Объект класса содержит целое число без знака произвольного размера
 * и поддерживает основные операции над такими числами, а именно:
 * сложение, вычитание (при вычитании большего числа из меньшего бросается исключение),
 * умножение, деление, остаток от деления,
 * преобразование в строку/из строки, преобразование в целое/из целого,
 * сравнение на равенство и неравенство
 */
class UnsignedBigInteger : Comparable<UnsignedBigInteger> {
    val list = mutableListOf<Int>()

    /**
     * Конструктор из строки
     */
    constructor(s: String) {
        if (!s.contains(Regex("""\d+"""))) throw IllegalStateException("Invalid format") //сложно разораться с регексом
        if (s.matches(Regex("""0*"""))) {
            list.add(0)
            return
        }
        var f = true
        for (element in s) {
            if (!(f && (element == '0'))) {
                list.add(element.toString().toInt())
                f = false
            }
        }
    }

    /**
     * Конструктор из целого
     */
    constructor(i: Int) {
        if (i < 0) throw IllegalStateException("Invalid format")
        if (i == 0) {
            list.add(0)
            return
        }
        var num = i
        while (num != 0) {
            list.add(0, (num % 10))
            num /= 10
        }
    }

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger {
        val result = StringBuilder()
        var rem = 0
        val ls = list.size
        val ols = other.list.size
        val m = maxOf(ls, ols) + 1
        for (i in 1..m) {
            val e1 = if (ls - i < 0) 0 else list[ls - i]
            val e2 = if (ols - i < 0) 0 else other.list[ols - i]
            val r = e1 + e2 + rem
            result.append((r % 10).toString())
            rem = r / 10
        }
        return UnsignedBigInteger(result.toString().reversed())
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other > this) throw ArithmeticException("negative number")
        val result = StringBuilder()
        val l = list
        val dif = list.size - other.list.size
        for (i in other.list.size - 1 downTo 0) {
            if (l[i + dif] < other.list[i]) {
                var j = i - 1
                while (l[j + dif] == 0) {
                    l[j + dif] = 9
                    j--
                }
                l[j + dif]--
                result.append((l[i + dif] + 10 - other.list[i]).toString())
            } else
                result.append((l[i + dif] - other.list[i]).toString())
        }
        for (i in dif - 1 downTo 0) {
            result.append(l[i].toString())
        }
        return UnsignedBigInteger(result.toString().reversed())
    }

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        var rem = 0
        var result = UnsignedBigInteger(0)
        for (i in other.list.size - 1 downTo 0) {
            var s: String
            for (j in list.size - 1 downTo 0) {
                s = ((other.list[i] * list[j] + rem) % 10).toString() +
                        "0".repeat(list.size - 1 - j + other.list.size - 1 - i)
                result += UnsignedBigInteger(s)
                rem = (other.list[i] * list[j] + rem) / 10
            }
            s = rem.toString() + "0".repeat(list.size + other.list.size - 1 - i)
            result += UnsignedBigInteger(s)
            rem = 0
        }
        return result
    }

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) throw ArithmeticException("/0")
        if (other > this) return UnsignedBigInteger(0)
        var rem = UnsignedBigInteger(0)
        val t = UnsignedBigInteger(10)
        var num = 0
        var i = 0
        val result = StringBuilder()
        while (rem <= other) {
            rem = rem * t + UnsignedBigInteger(list[i])
            i++
        }
        while (i <= (list.size - 1)) {
            while (rem >= other * UnsignedBigInteger(num)) {
                num++
            }
            num--
            result.append(num.toString())
            rem = (rem - UnsignedBigInteger(num) * other) * t + UnsignedBigInteger(list[i])
            num = 0
            i++
        }
        while (rem >= other * UnsignedBigInteger(num)) {
            num++
        }
        num--
        result.append(num.toString())
        return UnsignedBigInteger(result.toString())
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger): UnsignedBigInteger {
        if (other == UnsignedBigInteger(0)) throw ArithmeticException("%0")
        return this - (this / other) * other
    }

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?): Boolean {
        if (other !is UnsignedBigInteger || list.size != other.list.size) return false
        for (i in 0 until list.size)
            if (list[i] != other.list[i])
                return false
        return true
    }

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int {
        if (list.size > other.list.size) return 1
        if (list.size < other.list.size) return -1
        for (i in 0 until list.size) {
            if (list[i] > other.list[i]) return 1
            if (list[i] < other.list[i]) return -1
        }
        return 0
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String {
        val result = StringBuilder()
        for (i in 0 until list.size)
            result.append(list[i].toString())
        return result.toString()
    }

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int {
        if (list.size > digitNumber(Int.MAX_VALUE) ||
            list.size == digitNumber(Int.MAX_VALUE) && this > UnsignedBigInteger(Int.MAX_VALUE)
        ) throw ArithmeticException("number > MaxValue")
        var result = 0
        for (i in list) {
            result = result * 10 + i
        }
        return result
    }

    override fun hashCode(): Int = list.hashCode()
}