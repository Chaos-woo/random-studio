package per.chaos.app.preference;

public interface IPreference<T> {
    T get();

    void update(T value);

    default void remove() {}
}
