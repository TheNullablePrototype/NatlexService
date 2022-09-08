package com.prototype.natlexservice.service.impl.task;

import com.prototype.natlexservice.enums.ProgressType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.Callable;

@Slf4j
@Setter
@Getter
public abstract class ProgressedTask<V> extends ListenableFutureTask<V> implements ListenableFutureCallback<V> {

    private String name;

    private ProgressType progressType = ProgressType.IN_PROGRESS;

    public ProgressedTask(Callable<V> callable) {
        super(callable);
        addCallback(this);
    }

    public ProgressedTask(Runnable runnable, V result) {
        super(runnable, result);
        addCallback(this);
    }

    @Override
    public void onSuccess(V result) {
        setProgressType(ProgressType.DONE);
        this.logger.info("Task with name '{}' done", getName());
    }

    @Override
    public void onFailure(Throwable ex) {
        setProgressType(ProgressType.ERROR);
        this.logger.error(String.format("Task with name '%s' failure", getName()), ex);
    }

}
