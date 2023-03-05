package com.explore.taskmanager.repository;

import com.explore.taskmanager.models.AuditTask;

import java.util.List;

/**
 * Repository class to perform DB operations on AuditTasks table
 */
public interface AuditRepository {
    public int saveAuditTask(AuditTask auditTask);

    public List<AuditTask> getauditTaskById(Integer taskId);
}
