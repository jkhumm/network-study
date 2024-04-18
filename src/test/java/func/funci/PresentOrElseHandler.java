package func.funci;

import java.util.function.Consumer;

/**
 * @author humingming
 * @date 2024/4/18 23:33
 * @description
 */
@FunctionalInterface
public interface PresentOrElseHandler {

    void presentOrElse(Consumer<Object> consumer, Runnable runnable);

}
