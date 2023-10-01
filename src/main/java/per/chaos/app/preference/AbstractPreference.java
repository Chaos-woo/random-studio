package per.chaos.app.preference;

import java.util.Objects;

public abstract class AbstractPreference<T> implements IPreference<T>{
    protected T runtimeData;

    /**
     * 默认实现
     */
    public T getRuntimeData() {
        if (validRuntimeData()) {
            return runtimeData;
        }

        runtimeData = get();
        return runtimeData;
    }

    /**
     * 是否是有效的运行时状态，默认实现
     */
    protected boolean validRuntimeData() {
        return Objects.nonNull(this.runtimeData);
    }

    protected void updateRuntimeData(T runtime) {
        this.runtimeData = runtime;
    }
}
