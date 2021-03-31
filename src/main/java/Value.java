public class Value {

    private final long creationTime;

    private final Object value;

    public Value(Object value) {
        this.value = value;
        this.creationTime = System.currentTimeMillis();
    }

    public Object getValue() {
        return this.value;
    }

    public long getTime() {
        return this.creationTime;
    }

}
