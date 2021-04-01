package evicton_map;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class EvMap<K, V> {

    private long duration;

    private ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();

    public EvMap(long duration) {
        this.duration = duration;
    }

    public void put(K key, V value) {
        map.put(key, value);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                map.remove(key);
                timer.cancel();
            }
        }, duration * 1000);
    }

    public V get(K key) {
        return map.getOrDefault(key, null);
     }

    public static void main(String[] args) throws InterruptedException {
        EvMap<String, Integer> evMap = new EvMap<>(10);
        evMap.put("s", 1);
        evMap.put("s2", 2);
        System.out.println("1) " + evMap.get("s"));
        System.out.println("2) " + evMap.get("s2"));
        Thread.sleep(1000 * 20);
        System.out.println("1) " + evMap.get("s"));
        System.out.println("2) " + evMap.get("s2"));
        System.out.println("m: " + evMap.map);
    }
}
