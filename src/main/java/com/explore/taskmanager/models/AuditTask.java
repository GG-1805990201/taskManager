package com.explore.taskmanager.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class to store AuditDetails whenever the task is updated.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditTask {

    private Integer auditLogId;

    private Integer taskId;

    private String taskTitle;

    private String updateTimestamp;

    private String updatedFields;

    private String oldValues;

    private String newValues;

}
