package evicton_map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvMapTest {

    long duration;
    long durationLonger;
    long durationNegative;
    long longDuration;
    EvMap<String, String> evMapStrStr;
    EvMap<Integer, Integer> evMapIntInt;
    EvMap<Object, Object> evMapObjObj;
    EvMap<Integer, String> evMapIntStr;
    EvMap<Integer, Integer> evMapIntInt2;

    @BeforeEach
    void setUp() {
        this.duration = 1000;
        this.durationLonger = 5000;
        this.durationNegative = -1000;
        this.longDuration = 10000;
        this.evMapStrStr = new EvMap<>(duration);
        this.evMapIntInt = new EvMap<>(duration);
        this.evMapIntStr = new EvMap<>(durationLonger);
        this.evMapObjObj = new EvMap<>(duration);
        this.evMapIntInt2 = new EvMap<>(longDuration);
        evMapIntStr.put(1, "v1");
        evMapIntStr.put(2, "v2");
        evMapIntStr.put(3, "v3");
    }

    @Test
    void TestGet() {
        assertEquals("v1", evMapIntStr.get(1));
        assertEquals("v2", evMapIntStr.get(2));
        assertEquals("v3", evMapIntStr.get(3));
    }

    @Test
    void TestUpdateValue() throws InterruptedException {
        Thread.sleep(4000);
        assertEquals("v2", evMapIntStr.get(2));
        assertEquals("v3", evMapIntStr.get(3));
        evMapIntStr.put(2, "v4");
        evMapIntStr.put(3, "v5");
        Thread.sleep(2000);
        assertEquals("v4", evMapIntStr.get(2));
        assertEquals("v5", evMapIntStr.get(3));
    }

    @Test
    void TestPut1000Times() {
        for (int i = 0; i < 1000; i++) {
            evMapIntInt.put(i, i + 1);
        }
        assertEquals(java.util.Optional.of(1).get(), evMapIntInt.get(0));
        assertEquals(java.util.Optional.of(500).get(), evMapIntInt.get(499));
        assertEquals(java.util.Optional.of(1000).get(), evMapIntInt.get(999));
    }

    @Test
    void TestPutAndGet1000Times() {
        for (int i = 0; i < 1000; i++) {
            evMapIntInt.put(i, i + 1);
            assertEquals(java.util.Optional.of(i + 1).get(), evMapIntInt.get(i));
        }
    }

    @Test
    void TestEviction() throws InterruptedException {
        evMapStrStr.put("k1", "1");
        evMapStrStr.put("k2", "2");
        evMapStrStr.put("k3", "3");
        assertEquals("1", evMapStrStr.get("k1"));
        assertEquals("2", evMapStrStr.get("k2"));
        assertEquals("3", evMapStrStr.get("k3"));
        Thread.sleep(duration * 2);
        assertNull(evMapStrStr.get("k1"));
        assertNull(evMapStrStr.get("k2"));
        assertNull(evMapStrStr.get("k3"));
    }

    @Test
    void TestNegativeDurationShouldGiveException() {
        boolean exception = false;
        try {
            EvMap<Integer, Integer> evMap = new EvMap<>(durationNegative);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    void TestZeroDurationShouldGiveException() {
        boolean exception = false;
        try {
            EvMap<Integer, Integer> evMap = new EvMap<>(0);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    void TestNullKeyShouldGiveException() {
        boolean exception = false;
        try {
            evMapObjObj.put(null, 1);
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    void TestSpeed() {
        for (int i = 0; i < 1000000; i++) {
            evMapIntInt2.put(i, i + 1);
        }
        for (int i = 0; i < 1000000; i++) {
            assertEquals(java.util.Optional.of(i + 1).get(), evMapIntInt2.get(i));
        }
    }
}