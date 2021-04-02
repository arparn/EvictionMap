package evicton_map;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is used for creating key-value map with time-based eviction policy defined by user.
 * @param <K> - data type for key.
 * @param <V> - data type for value.
 */
public class EvMap<K, V> {

    private long duration;
    private Timer timer;

    /**
     * map - main map where are stored all not expired key-value pairs.
     * taskMap - map where are stored all tasks and keys so I can cancel old task and start new one
     * if I need to update value for this key.
     */
    private ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
    private ConcurrentHashMap<K, TimerTask> taskMap = new ConcurrentHashMap<>();

    /**
     * Eviction map constructor.
     * Throws IllegalArgumentException if passed duration is fewer or equals zero.
     * @param duration - duration in milliseconds.
     */
    public EvMap(long duration) {
        if (duration <= 0) throw new IllegalArgumentException("Illegal Argument Exception, duration must be > 0");
        this.duration = duration;
    }

    /**
     * This method is used to put new key-value pair to the eviction map or update an already existing value.
     * This method returns nothing.
     * Throws IllegalArgumentException if passed key is null.
     * @param key - key to access the stored value.
     * @param value - value to store.
     */
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Illegal Argument Exception, key cannot be null");
        if (map.isEmpty()) {
            timer = new Timer();
        }
        if (map.containsKey(key)) {
            taskMap.get(key).cancel();
            taskMap.remove(key);
        }
        map.put(key, value);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                taskMap.remove(key);
                map.remove(key);
                cancel();
                if (taskMap.isEmpty()) {
                    timer.cancel();
                }
            }
        };
        taskMap.put(key, task);
        timer.schedule(task, duration);
    }

    /**
     * This method is used to get value from an eviction map based on a passed key that returns the value
     * or null if no such key found.
     * @param key key to get the value from eviction map.
     * @return value (if exists such key) or null (if no such key exists or it was evicted from the eviction map).
     */
    public V get(K key) {
        return map.getOrDefault(key, null);
     }
}
