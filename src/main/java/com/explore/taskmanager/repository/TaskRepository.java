package com.explore.taskmanager.repository;


import com.explore.taskmanager.models.Tasks;

import java.util.List;

/**
 * Repository class to perform DB operations on Tasks table
 */
public interface TaskRepository {


    public int saveTask(Tasks tasks);

    public Tasks getTaskById(Integer taskId);

    public List<Tasks> getAllTasks();

    public int updateTask(String query);

    public int deleteTask(Integer taskId);

}
