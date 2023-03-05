package com.explore.taskmanager.models;

import lombok.Data;

import java.util.List;

/**
 * Class containing constants which are used for storing status of task.
 */
@Data
public class Status {
    public static String pending = "PENDING";

    public static String inReview = "IN_REVIEW";

    public static String inProgress = "IN_PROGRESS";

    public static String complete = "COMPLETE";

    public static List<String> states = List.of(new String[]{"PENDING", "IN_REVIEW", "IN_PROGRESS", "COMPLETE"});


}
