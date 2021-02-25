package lesson11.task1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import java.lang.ArithmeticException

internal class UnsignedBigIntegerTest {

    @Test
    @Tag("8")
    fun plus() {
        assertEquals(UnsignedBigInteger(4), UnsignedBigInteger(2) + UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654330"), UnsignedBigInteger("9087654329") + UnsignedBigInteger(1))
        assertEquals(UnsignedBigInteger("0"), UnsignedBigInteger("0") + UnsignedBigInteger(0))
        assertEquals(UnsignedBigInteger("13254"), UnsignedBigInteger(12345) + UnsignedBigInteger("909"))
        assertEquals(UnsignedBigInteger("2"), UnsignedBigInteger("1") + UnsignedBigInteger("01"))
        assertEquals(UnsignedBigInteger("1245540515840"),UnsignedBigInteger("386547056640") + UnsignedBigInteger("858993459200"))
        assertEquals(UnsignedBigInteger("31310311587840"),UnsignedBigInteger("1245540515840") + UnsignedBigInteger("30064771072000"))
        assertEquals(UnsignedBigInteger("18446744047939747840"),UnsignedBigInteger("1266874863939747840") + UnsignedBigInteger("17179869184000000000"))

    }

    @Test
    @Tag("8")
    fun minus() {
        assertEquals(UnsignedBigInteger(2), UnsignedBigInteger(4) - UnsignedBigInteger(2))
        assertEquals(UnsignedBigInteger("9087654329"), UnsignedBigInteger("9087654330") - UnsignedBigInteger(1))
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(2) - UnsignedBigInteger(4)
        }
        assertEquals(UnsignedBigInteger("9087654329"), UnsignedBigInteger("9087654329") - UnsignedBigInteger(0))
    }

    @Test
    @Tag("12")
    fun times() {
        assertEquals(
            UnsignedBigInteger("12340"),
            UnsignedBigInteger("1234") * UnsignedBigInteger("10")
        )
        assertEquals(
            UnsignedBigInteger("18446744073709551616"),
            UnsignedBigInteger("4294967296") * UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger(0),
            UnsignedBigInteger("1234") * UnsignedBigInteger("0")
        )
    }

    @Test
    @Tag("16")
    fun div() {
        assertEquals(
            UnsignedBigInteger("4294967296"),
            UnsignedBigInteger("18446744073709551616") / UnsignedBigInteger("4294967296")
        )
        assertThrows(ArithmeticException::class.java) {
            UnsignedBigInteger(2) / UnsignedBigInteger(0)
        }
        assertEquals(
            UnsignedBigInteger("4294967296"),
            UnsignedBigInteger("18446744073709551619") / UnsignedBigInteger("4294967296")
        )
    }

    @Test
    @Tag("16")
    fun rem() {
        assertEquals(UnsignedBigInteger(5), UnsignedBigInteger(19) % UnsignedBigInteger(7))
        assertEquals(
            UnsignedBigInteger(0),
            UnsignedBigInteger("18446744073709551616") % UnsignedBigInteger("4294967296")
        )
        assertEquals(
            UnsignedBigInteger(3),
            UnsignedBigInteger("18446744073709551619") % UnsignedBigInteger("4294967296")
        )
    }

    @Test
    @Tag("8")
    fun equals() {
        assertEquals(UnsignedBigInteger(123456789), UnsignedBigInteger("123456789"))
        assertFalse(UnsignedBigInteger(123) != UnsignedBigInteger("123"))
        assertFalse(UnsignedBigInteger(1) == UnsignedBigInteger("123"))
    }

    @Test
    @Tag("8")
    fun compareTo() {
        assertTrue(UnsignedBigInteger(123456789) < UnsignedBigInteger("9876543210"))
        assertTrue(UnsignedBigInteger("9876543210") > UnsignedBigInteger(123456789))
        assertTrue(UnsignedBigInteger("0") == UnsignedBigInteger(0))
        assertFalse(UnsignedBigInteger("1") < UnsignedBigInteger(0))
    }

    @Test
    @Tag("8")
    fun toInt() {
        assertEquals(123456789, UnsignedBigInteger("123456789").toInt())
        assertEquals(0, UnsignedBigInteger("0").toInt())
    }
}