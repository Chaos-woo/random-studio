package per.chaos.threads;

/**
 * PausableThread管理器
 * <p>
 * 简化PausableThread的使用，保证多种状态切换不引起其他异常，
 * 支持停止任务后再次启动新任务
 */
public class PausableThreadManager {
    // 是否开始
    private boolean isStart;
    // 是否销毁
    private boolean isDispose;
    // 线程，默认为null，即未初始化状态为已销毁状态
    private PausableThread pt;
    // 任务
    private final Runnable runnable;

    private void initNewPausableThread() {
        pt = new PausableThread(
                "pausable",
                () -> {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        throw new RuntimeException("rethrow");
                    }
                }
        );
    }

    private void initAllState() {
        isStart = false;
        isDispose = true;
    }

    public void start() {
        if (!isStart) {
            if (isDispose) {
                initNewPausableThread();
            }

            isStart = true;
            pt.start();
        }
    }

    public void stop() {
        if (isStart) {
            isStart = false;
            isDispose = true;
            pt.interrupt();
            pt = null;
        }
    }

    public void pause() {
        if (isStart) {
            pt.pauseExecute();
        }
    }

    public void resume() {
        if (isStart) {
            pt.resumeExecute();
        }
    }

    public PausableThreadManager(Runnable runnable) {
        this.runnable = runnable;
        initAllState();
    }
}
