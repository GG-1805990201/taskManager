package com.explore.taskmanager.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model class to store the details of field whenever field is updated.
 */
@Data
@AllArgsConstructor
public class AuditField {
    private String name;

    private String oldValue;

    private String newValue;

}
