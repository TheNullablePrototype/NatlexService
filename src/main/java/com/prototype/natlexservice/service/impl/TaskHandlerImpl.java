package com.prototype.natlexservice.service.impl;

import com.prototype.natlexservice.config.TaskHandlerProps;
import com.prototype.natlexservice.enums.ProgressType;
import com.prototype.natlexservice.helper.FileHelper;
import com.prototype.natlexservice.service.TaskHandler;
import com.prototype.natlexservice.service.impl.task.FileExportTask;
import com.prototype.natlexservice.service.impl.task.ProgressedTask;
import com.prototype.natlexservice.util.ThreadFactoryBuilder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TaskHandlerImpl implements TaskHandler {

    private final AtomicInteger counter = new AtomicInteger();
    private final Map<Integer, ProgressedTask<?>> tasks = new ConcurrentHashMap<>();

    @Getter
    private ExecutorService executorService;
    @Getter
    private ScheduledExecutorService scheduledExecutorService;

    private final TaskHandlerProps props;

    @Autowired
    public TaskHandlerImpl(TaskHandlerProps props) {
        this.props = props;
    }

    @Override
    public int addTask(ProgressedTask<?> task) {
        var id = this.counter.incrementAndGet();
        this.executorService.submit(task);
        this.tasks.put(id, task);
        this.scheduledExecutorService.schedule(() -> removeTaskById(id), this.props.exportAliveTime(), TimeUnit.MINUTES);
        return id;
    }

    @Override
    public void removeTaskById(int id) {
        ProgressedTask<?> task = this.tasks.remove(id);
        if (task != null) {
            if (!task.isDone()) {
                task.cancel(false);
            }
            if (task instanceof FileExportTask exportTask) {
                FileHelper.deleteIfExists(exportTask.getPath());
            }
        }
    }

    @Override
    public Optional<ProgressType> getTaskProgressById(int id) {
        return getTaskById(id).map(ProgressedTask::getProgressType);
    }

    @Override
    public Optional<ProgressedTask<?>> getTaskById(int id) {
        return Optional.ofNullable(this.tasks.get(id));
    }

    @PostConstruct
    public void init() {

        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("task-handler-thread-%d")
                .setDaemon(true)
                .setPriority(Thread.NORM_PRIORITY)
                .build();

        var boundedQueue = new ArrayBlockingQueue<Runnable>(this.props.queueMaxCapacity());
        this.executorService = new ThreadPoolExecutor(
                this.props.threadsSize(),
                this.props.threadsSizeMax(),
                this.props.keepAliveTime(), TimeUnit.SECONDS,
                boundedQueue,
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );

        this.scheduledExecutorService = Executors.newScheduledThreadPool(this.props.scheduledThreadsSize());
    }

    @PreDestroy
    public void destroy() {
        shutdown(this.executorService);
        shutdown(this.scheduledExecutorService);
    }

    public void shutdown(ExecutorService executor) {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(this.props.shutdownTimeout(), TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

}
