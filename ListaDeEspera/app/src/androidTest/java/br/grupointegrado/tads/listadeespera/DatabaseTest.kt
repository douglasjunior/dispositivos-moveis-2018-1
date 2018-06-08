package br.grupointegrado.tads.listadeespera

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    /* Context used to perform operations on the database and create WaitlistDbHelper */
    private val mContext = InstrumentationRegistry.getTargetContext()
    /* Class reference to help load the constructor on runtime */
    private val mDbHelperClass = ListaEsperaBdHelper::class.java


    @Before
    fun setUp() {
        deleteTheDatabase()
    }

    /**
     * This method tests that our database contains all of the tables that we think it should
     * contain.
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    @Throws(Exception::class)
    fun create_database_test() {


        /* Use reflection to try to run the correct constructor whenever implemented */
        val dbHelper = mDbHelperClass!!.getConstructor(Context::class.java).newInstance(mContext) as SQLiteOpenHelper

        /* Use WaitlistDbHelper to get access to a writable database */
        val database = dbHelper.writableDatabase


        /* We think the database is open, let's verify that here */
        val databaseIsNotOpen = "The database should be open and isn't"
        assertEquals(databaseIsNotOpen,
                true,
                database.isOpen)

        /* This Cursor will contain the names of each table in our database */
        val tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        ListaEsperaContrato.Clientes.TABELA + "'",
                null)

        /*
                * If tableNameCursor.moveToFirst returns false from this query, it means the database
                * wasn't created properly. In actuality, it means that your database contains no tables.
                */
        val errorInCreatingDatabase = "Error: This means that the database has not been created correctly"
        assertTrue(errorInCreatingDatabase,
                tableNameCursor.moveToFirst())

        /* If this fails, it means that your database doesn't contain the expected table(s) */
        assertEquals("Error: Your database was created without the expected tables.",
                ListaEsperaContrato.Clientes.TABELA, tableNameCursor.getString(0))

        /* Always close a cursor when you are done with it */
        tableNameCursor.close()
    }

    /**
     * This method tests inserting a single record into an empty table from a brand new database.
     * The purpose is to test that the database is working as expected
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    @Throws(Exception::class)
    fun insert_single_record_test() {

        /* Use reflection to try to run the correct constructor whenever implemented */
        val dbHelper = mDbHelperClass!!.getConstructor(Context::class.java).newInstance(mContext) as SQLiteOpenHelper

        /* Use WaitlistDbHelper to get access to a writable database */
        val database = dbHelper.writableDatabase

        val testValues = ContentValues()
        testValues.put(ListaEsperaContrato.Clientes.COLUNA_NOME, "test name")
        testValues.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, 99)

        /* Insert ContentValues into database and get first row ID back */
        val firstRowId = database.insert(
                ListaEsperaContrato.Clientes.TABELA,
                /* Sort order to return in Cursor */
                null,
                testValues)

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the database", -1, firstRowId)

        /*
                * Query the database and receive a Cursor. A Cursor is the primary way to interact with
                * a database in Android.
                */
        val wCursor = database.query(
                /* Name of table on which to perform the query */
                ListaEsperaContrato.Clientes.TABELA, null, null, null, null, null, null)/* Columns; leaving this null returns every column in the table *//* Optional specification for columns in the "where" clause above *//* Values for "where" clause *//* Columns to group by *//* Columns to filter by row groups */

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        val emptyQueryError = "Error: No Records returned from waitlist query"
        assertTrue(emptyQueryError,
                wCursor.moveToFirst())

        /* Close cursor and database */
        wCursor.close()
        dbHelper.close()
    }


    /**
     * Tests to ensure that inserts into your database results in automatically
     * incrementing row IDs.
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    @Throws(Exception::class)
    fun autoincrement_test() {

        /* First, let's ensure we have some values in our table initially */
        insert_single_record_test()

        /* Use reflection to try to run the correct constructor whenever implemented */
        val dbHelper = mDbHelperClass!!.getConstructor(Context::class.java).newInstance(mContext) as SQLiteOpenHelper

        /* Use WaitlistDbHelper to get access to a writable database */
        val database = dbHelper.writableDatabase

        val testValues = ContentValues()
        testValues.put(ListaEsperaContrato.Clientes.COLUNA_NOME, "test name")
        testValues.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, 99)

        /* Insert ContentValues into database and get first row ID back */
        val firstRowId = database.insert(
                ListaEsperaContrato.Clientes.TABELA,
                null,
                testValues)

        /* Insert ContentValues into database and get another row ID back */
        val secondRowId = database.insert(
                ListaEsperaContrato.Clientes.TABELA, null,
                testValues)

        assertEquals("ID Autoincrement test failed!",
                firstRowId + 1, secondRowId)


    }


    /**
     * Tests that onUpgrade works by inserting 2 rows then calling onUpgrade and verifies that the
     * database has been successfully dropped and recreated by checking that the database is there
     * but empty
     * @throws Exception in case the constructor hasn't been implemented yet
     */
    @Test
    @Throws(Exception::class)
    fun upgrade_database_test() {

        /* Insert 2 rows before we upgrade to check that we dropped the database correctly */

        /* Use reflection to try to run the correct constructor whenever implemented */
        val dbHelper = mDbHelperClass!!.getConstructor(Context::class.java).newInstance(mContext) as SQLiteOpenHelper

        /* Use WaitlistDbHelper to get access to a writable database */
        var database = dbHelper.writableDatabase

        val testValues = ContentValues()
        testValues.put(ListaEsperaContrato.Clientes.COLUNA_NOME, "test name")
        testValues.put(ListaEsperaContrato.Clientes.COLUNA_TAMANHO_GRUPO, 99)

        /* Insert ContentValues into database and get first row ID back */
        val firstRowId = database.insert(
                ListaEsperaContrato.Clientes.TABELA,
                null,
                testValues)

        /* Insert ContentValues into database and get another row ID back */
        val secondRowId = database.insert(
                ListaEsperaContrato.Clientes.TABELA, null,
                testValues)

        dbHelper.onUpgrade(database, 0, 1)
        database = dbHelper.readableDatabase

        /* This Cursor will contain the names of each table in our database */
        val tableNameCursor = database.rawQuery(
                ("SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        ListaEsperaContrato.Clientes.TABELA + "'"), null)

        assertTrue(tableNameCursor.count == 1)

        /*
                * Query the database and receive a Cursor. A Cursor is the primary way to interact with
                * a database in Android.
                */
        val wCursor = database.query(
                /* Name of table on which to perform the query */
                ListaEsperaContrato.Clientes.TABELA, null, null, null, null, null, null)/* Columns; leaving this null returns every column in the table *//* Optional specification for columns in the "where" clause above *//* Values for "where" clause *//* Columns to group by *//* Columns to filter by row groups *//* Sort order to return in Cursor */

        /* Cursor.moveToFirst will return false if there are no records returned from your query */

        assertFalse("Database doesn't seem to have been dropped successfully when upgrading",
                wCursor.moveToFirst())

        tableNameCursor.close()
        database.close()
    }

    /**
     * Deletes the entire database.
     */
    internal fun deleteTheDatabase() {
        try {
            /* Use reflection to get the database name from the db helper class */
            val f = mDbHelperClass!!.getDeclaredField("BD_NOME")
            f.setAccessible(true)
            mContext.deleteDatabase(f.get(null) as String)
        } catch (ex: Exception) {
            fail(ex.message)
        }
    }

}
