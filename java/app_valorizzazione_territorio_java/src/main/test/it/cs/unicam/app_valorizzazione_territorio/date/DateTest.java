package it.cs.unicam.app_valorizzazione_territorio.date;

import it.cs.unicam.app_valorizzazione_territorio.model.date.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTest {
    @Test
    void shouldConstructDate() {
        assertDoesNotThrow(() -> new Date("01-01-2021"));
        assertDoesNotThrow(() -> new Date("31-12-2021"));
        assertDoesNotThrow(() -> new Date("29-02-2020"));
        assertDoesNotThrow(() -> new Date("28-02-2021"));
        assertDoesNotThrow(() -> new Date("30-04-2011"));
        assertDoesNotThrow(() -> new Date("31-05-2011"));
        assertDoesNotThrow(() -> new Date("30-06-9999"));
    }

    @Test
    void shouldNotConstructDate(){
        assertThrows(IllegalArgumentException.class, () -> new Date("29-02-2021"));
        assertThrows(IllegalArgumentException.class, () -> new Date("33-12-2021"));
        assertThrows(IllegalArgumentException.class, () -> new Date("32-01-1999"));
    }

    @Test
    void hashCodesShouldBeEqual(){
        Date date1 = new Date("31-12-9999");
        Date date2 = new Date("31-12-9999");
        int hash1 = date1.hashCode();
        int hash2 = date2.hashCode();
        assertEquals(hash1, hash2);
    }

}