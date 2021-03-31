import java.util.concurrent.ConcurrentHashMap;

public class Eviction_Map {

    private long duration;

    private ConcurrentHashMap<Object, Value> map = new ConcurrentHashMap<>();

    public Eviction_Map(long duration) {
        this.duration = duration;
    }

    private void put(Object key, Object value) {
        Value valueObj = new Value(value);
        map.put(key, valueObj);
    }

    private Object get(Object key) {
        Value valueObj = map.get(key);
        if (System.currentTimeMillis() - valueObj.getTime() > duration) {
            return null;
        } else {
            return valueObj.getValue();
        }
    }
}
