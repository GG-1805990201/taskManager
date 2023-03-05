package com.explore.taskmanager.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class to store Task details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {


    private Integer taskId;
    private String title;
    private String eta;
    private String status;
    private String createdDate;


}

