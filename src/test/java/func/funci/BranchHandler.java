package func.funci;

/**
 * @author humingming
 * @date 2024/4/18 23:24
 */
@FunctionalInterface
public interface BranchHandler {

    void trueOrFalse(Runnable trueRunnable, Runnable falseRunnable);

}
