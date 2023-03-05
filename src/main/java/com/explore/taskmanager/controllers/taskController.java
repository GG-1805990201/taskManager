package com.explore.taskmanager.controllers;


import com.explore.taskmanager.exceptions.DeletingException;
import com.explore.taskmanager.exceptions.FetchingException;
import com.explore.taskmanager.exceptions.InsertingException;
import com.explore.taskmanager.exceptions.MapExceptions;
import com.explore.taskmanager.models.AuditTask;
import com.explore.taskmanager.models.Tasks;
import com.explore.taskmanager.services.TaskManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class consisting API endpoints.
 */
@RestController
public class taskController {

    private final TaskManagerService taskManagerService;

    public taskController(TaskManagerService taskManagerService) {
        this.taskManagerService = taskManagerService;
    }

    Logger logger = LoggerFactory.getLogger(taskController.class);

    /**
     * Post Endpoint to create task and saving to database.
     * @param title
     * @param eta
     * @param status
     * @return
     */
    @PostMapping("/createTask")
    public String createtask(@RequestParam String title, @RequestParam String eta, @RequestParam String status) {
        logger.info("Creating Task for the requested paramters");
        int rowAdded = taskManagerService.createTask(title, eta, status);
        if (rowAdded < 0) {
            throw new InsertingException("Error while creating task");
        }
        logger.info("Task Created Successfully");
        return "Task Created successfully";
    }

    /**
     * Get Endpoint to fetch all tasks from DB
     * @return
     */
    @GetMapping(value = "/getAllTasks", produces = "application/json")
    public List<Tasks> getAllTasks() {
        logger.info("Fetching all tasks from DB");
        List<Tasks> res = taskManagerService.getAllTasks();
        logger.info("Fetched total " + res.size() + "records from DB");
        return res;
    }

    /**
     * Get Endpoint to fetch task for the given TaskId
     * @param taskId
     * @return
     */
    @GetMapping(value = "/getTaskById", produces = "application/json")
    public Tasks getTaskById(@RequestParam Integer taskId) {
        logger.info("Fetching task for the given taskid = " + taskId);
        Tasks task = taskManagerService.getTaskByID(taskId);
        if (task == null) {
            throw new FetchingException("Records with taskId : " + taskId + " does not exist in Tasks table");
        }
        return task;
    }

    /**
     * Put endpoint to update the existing task attributes for the given taskId.
     * @param requestJson
     * @return
     * @throws MapExceptions
     */
    @PutMapping(value = "/updateTaskById", consumes = "application/json")
    public String updateTaskById(@RequestBody Map<String, String> requestJson) throws MapExceptions {
        return taskManagerService.updateTaskById(requestJson);
    }

    /**
     * Get Endpoint to fetch AuditDetails for the given taskId.
     * @param taskId
     * @return
     */
    @GetMapping(value = "/getAuditDetails", produces = "application/json")
    public List<AuditTask> getAuditDetails(@RequestParam Integer taskId) {
        List<AuditTask> auditTasks = taskManagerService.getAuditDetailsById(taskId);
        if (auditTasks == null) {
            throw new InsertingException("Records with taskId : " + taskId + " does not exist in AuditTasks table");
        }
        return auditTasks;
    }

    /**
     * Delete endpoint which deletes the particular task with taskId as input.
     * @param taskId
     * @return
     */
    @DeleteMapping("/deleteTaskById")
    public String deleteTaskById(@RequestParam Integer taskId) {
        logger.info("Deleting the task with takdId = " + taskId);
        int rowsDeleted = taskManagerService.deleteTask(taskId);
        if (rowsDeleted <= 0) {
            throw new DeletingException("Task not present for the given taskId = " + taskId);
        }
        return "Task deleted Successfully.";
    }

}
