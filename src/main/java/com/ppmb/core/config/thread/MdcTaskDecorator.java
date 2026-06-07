package com.ppmb.core.config.thread;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
 * TaskDecorator to propagate MDC context from the parent thread to the child (virtual) thread.
 * Ensures the MDC is cleared after the task execution to prevent context leakage.
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // Capture the MDC context map from the calling thread
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // Restore the MDC context in the new thread
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                // Always clear MDC context from the virtual thread
                MDC.clear();
            }
        };
    }
}
