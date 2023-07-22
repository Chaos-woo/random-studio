package per.chaos.app.prefs;

public interface IPreference<T> {
    T get();

    void update(T value);

    default void remove() {}
}
