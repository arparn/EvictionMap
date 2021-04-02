package evicton_map;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class EvMap<K, V> {

    private long duration;

    private ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();

    /**
     * Eviction map constructor.
     * @param duration - duration in milliseconds
     */
    public EvMap(long duration) {
        if (duration < 0) throw new IllegalArgumentException("Illegal Argument Exception, duration must be >= 0");
        this.duration = duration;
    }

    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Illegal Argument Exception, key cannot be null");
        map.put(key, value);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                map.remove(key);
                timer.cancel();
            }
        }, duration);
    }

    public V get(K key) {
        return map.getOrDefault(key, null);
     }

    public static void main(String[] args) throws InterruptedException {
        EvMap<Object, Object> evMap = new EvMap<>(1000);
        evMap.put(null, 1);
        evMap.put("s2", 2);
        System.out.println("1) " + evMap.get("s"));
        System.out.println("2) " + evMap.get("s2"));
        Thread.sleep(1000 * 2);
        System.out.println("1) " + evMap.get("s"));
        System.out.println("2) " + evMap.get("s2"));
        System.out.println("m: " + evMap.map);
    }
}
