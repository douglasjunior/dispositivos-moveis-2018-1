package br.grupointegrado.tads.listadeespera

import android.provider.BaseColumns
import org.junit.Test

import org.junit.Assert.*
import java.lang.reflect.Modifier

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ContractClassUnitTest {

    @Test
    @Throws(Exception::class)
    fun inner_class_exists() {
        val innerClasses = ListaEsperaContrato::class.java!!.getDeclaredClasses()
        assertEquals("There should be 1 Inner class inside the contract class", 1, innerClasses.size.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun inner_class_type_correct() {
        val innerClasses = ListaEsperaContrato::class.java!!.getDeclaredClasses()
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.size.toLong())
        val entryClass = innerClasses[0]
        assertTrue("Inner class should implement the BaseColumns interface", BaseColumns::class.java.isAssignableFrom(entryClass))
        assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()))
        assertTrue("Inner class should be static", Modifier.isStatic(entryClass.getModifiers()))
    }

    @Test
    @Throws(Exception::class)
    fun inner_class_members_correct() {
        val innerClasses = ListaEsperaContrato::class.java!!.getDeclaredClasses()
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.size.toLong())
        val entryClass = innerClasses[0]
        val allFields = entryClass.getDeclaredFields()
        assertEquals("There should be exactly 4 String members in the inner class", 4, allFields.size.toLong())
        for (field in allFields) {
            assertTrue("All members in the contract class should be Strings", field.getType() == String::class.java)
            assertTrue("All members in the contract class should be final", Modifier.isFinal(field.getModifiers()))
            assertTrue("All members in the contract class should be static", Modifier.isStatic(field.getModifiers()))
        }
    }

}
