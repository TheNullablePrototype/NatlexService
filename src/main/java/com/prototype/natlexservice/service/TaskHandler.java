package com.prototype.natlexservice.service;

import com.prototype.natlexservice.enums.ProgressType;
import com.prototype.natlexservice.service.impl.task.ProgressedTask;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public interface TaskHandler {

    int addTask(ProgressedTask<?> task);

    void removeTaskById(int id);

    Optional<ProgressType> getTaskProgressById(int id);

    Optional<ProgressedTask<?>> getTaskById(int id);

    ExecutorService getExecutorService();

    ScheduledExecutorService getScheduledExecutorService();

}
