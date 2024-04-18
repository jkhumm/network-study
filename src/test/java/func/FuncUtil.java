package func;

import func.funci.BranchHandler;
import func.funci.PresentOrElseHandler;
import func.funci.ThrowExFunction;

/**
 * @author humingming
 * @date 2024/4/18 23:16
 * @description
 */
public class FuncUtil {

    public static ThrowExFunction getThrowExFunction(boolean b) {
        return (exMsg) -> {
            if (!b) {
                throw new RuntimeException(exMsg);
            }
        };
    }


    public static BranchHandler getBranchHandler(boolean b) {
        return (trueRunnable, falseRunnable) -> {
            if (b) {
                trueRunnable.run();
            } else {
                falseRunnable.run();
            }
        };
    }

    public static PresentOrElseHandler getPresentOrElseHandler(String result) {
        return (consumer, runnable) -> {
            if (result != null) {
                consumer.accept(result);
            } else {
                runnable.run();
            }
        };

    }


}
