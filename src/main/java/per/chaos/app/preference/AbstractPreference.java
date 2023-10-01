package per.chaos.app.preference;

import java.util.Objects;

public abstract class AbstractPreference<T> implements IPreference<T>{
    protected T runtime;

    /**
     * 默认实现
     */
    public T getRuntime() {
        if (validRuntime()) {
            return this.runtime;
        }

        this.runtime = get();
        return this.runtime;
    }

    /**
     * 是否是有效的运行时状态，默认实现
     */
    protected boolean validRuntime() {
        return Objects.nonNull(this.runtime);
    }

    protected void updateRuntime(T runtime) {
        this.runtime = runtime;
    }
}
