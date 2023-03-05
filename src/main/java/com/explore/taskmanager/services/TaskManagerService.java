package com.explore.taskmanager.services;

import com.explore.taskmanager.exceptions.InsertingException;
import com.explore.taskmanager.exceptions.MapExceptions;
import com.explore.taskmanager.repository.AuditRepository;
import com.explore.taskmanager.repository.TaskRepository;
import com.explore.taskmanager.models.AuditField;
import com.explore.taskmanager.models.AuditTask;
import com.explore.taskmanager.models.Status;
import com.explore.taskmanager.models.Tasks;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TaskManagerService {

    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final AuditRepository auditRepository;

    Logger logger = LoggerFactory.getLogger(TaskManagerService.class);

    public TaskManagerService(TaskRepository taskRepository, AuditRepository auditRepository) {
        this.taskRepository = taskRepository;
        this.auditRepository = auditRepository;
    }

    /**
     * Service created to create the task object and store it in DB.
     * @param title
     * @param eta
     * @param status
     * @return
     */
    public int createTask(String title, String eta, String status) {
        logger.info("Inserting the data to task object");
        if (status.length() == 0 || status == null) {
            status = Status.pending;
        } else {
            if (!Status.states.contains(status)) {
                throw new RuntimeException("Invalid Status");
            }
        }
        if (eta.length() == 0 || eta == null) {
            eta = generateETA();
        }
        Tasks tasks = new Tasks(null, title, eta, status, generateCreatedDate());
        logger.info("Inserting data in DB");
        return taskRepository.saveTask(tasks);
    }

    /**
     * Service created to fetch all tasks in DB.
     * @return
     */
    public List<Tasks> getAllTasks() {

        return taskRepository.getAllTasks();

    }

    /**
     * Service created to fetch record for the given taskId.
     * @param taskId
     * @return
     */
    public Tasks getTaskByID(Integer taskId) {

        return taskRepository.getTaskById(taskId);
    }

    /**
     * Service used to update the existing task attributes and save to DB.
     * @param requestJson
     * @return
     * @throws MapExceptions
     */
    public String updateTaskById(Map<String, String> requestJson) throws MapExceptions {
        if (!requestJson.containsKey("taskId")) {
            throw new MapExceptions("TaskId not present");
        }
        Integer taskId = Integer.valueOf(requestJson.get("taskId"));
        logger.info("Fetching task record as per taskid = " + taskId);
        if (taskId == null) {
            throw new NullPointerException();
        }
        Tasks task = taskRepository.getTaskById(taskId);
        try {
            if (task == null) {
                throw new NullPointerException();
            } else {
                updateandAuditTask(requestJson, taskId, task);
            }
        } catch (NullPointerException e) {
            throw new NullPointerException("TaskId not present");
        } catch (InsertingException e) {
            throw new InsertingException(e.getMessage());
        }

        return "Updated Task SuccessFully.";
    }

    /**
     * Method used for updating the task attributes and insert the operation to Audit records.
     * @param requestJSON
     * @param taskId
     * @param task
     * @throws MapExceptions
     */
    public void updateandAuditTask(Map<String, String> requestJSON, Integer taskId, Tasks task) throws MapExceptions {
        ObjectMapper oMapper = new ObjectMapper();
        List<AuditField> auditfieldList = new ArrayList<>();
        for (String key : requestJSON.keySet()) {
            if (key.equals("taskId")) {
                continue;
            } else {
                Map<String, Object> oldObjectMap = oMapper.convertValue(task, Map.class);
                if (!oldObjectMap.containsKey(key)) {
                    throw new RuntimeException("Key not found");
                }
                Object oldvalue = oldObjectMap.get(key);
                Object updatedValue = requestJSON.get(key);
                if (oldvalue.equals(updatedValue)) {
                    System.out.println("The value of " + key + " attribute cant be updated as it is same to oldvalue");
                } else {
                    AuditField auditField = new AuditField(key, (String) oldvalue, (String) updatedValue);
                    auditfieldList.add(auditField);
                }
            }
        }
        if (!auditfieldList.isEmpty()) {
            List<String> updatedfields = new ArrayList<>();
            List<String> oldValues = new ArrayList<>();
            List<String> newValues = new ArrayList<>();
            for (int i = 0; i < auditfieldList.size(); i++) {
                updatedfields.add(auditfieldList.get(i).getName());
                oldValues.add(auditfieldList.get(i).getOldValue());
                newValues.add(auditfieldList.get(i).getNewValue());
            }
            AuditTask auditTask = new AuditTask(null, taskId, task.getTitle(),
                    null, updatedfields.toString(), oldValues.toString(), newValues.toString());
            int auditRowAdded = auditRepository.saveAuditTask(auditTask);
            if (auditRowAdded < 0) {
                throw new InsertingException("Failed to insert data in Audit table");
            }
            int updatedTaskRow = taskRepository.updateTask(generateUpdateQuery(auditfieldList, taskId));
            if (updatedTaskRow < 0) {
                throw new InsertingException("Failed to update tasks table for taskId = " + taskId);
            }
        } else {
            throw new Error("All the attributes are same so can't update!");
        }
    }

    /**
     * Service created to fetch Audit details for particular Task.
     * @param taskId
     * @return
     */
    public List<AuditTask> getAuditDetailsById(Integer taskId) {
        return auditRepository.getauditTaskById(taskId);
    }

    /**
     * Service created to delete record for the given taskId.
     * @param taskId
     * @return
     */
    public int deleteTask(Integer taskId) {
        return taskRepository.deleteTask(taskId);
    }

    /**
     * Method used to generate update query according to attributes changes.
     * @param auditFieldList
     * @param taskId
     * @return
     */
    public String generateUpdateQuery(List<AuditField> auditFieldList, Integer taskId) {
        String query = "UPDATE TASKS SET ";
        for (int i = 0; i < auditFieldList.size(); i++) {
            String key = auditFieldList.get(i).getName();
            String value = auditFieldList.get(i).getNewValue();
            query += key + " = '" + value + "' ";
            if (i != auditFieldList.size() - 1) {
                query += ", ";
            }
        }
        query += "WHERE taskid = " + taskId;
        return query;
    }

    /**
     * Method used to generate ETA when eta field is empty.
     * @return
     */

    public String generateETA() {
        LocalDate currentDate = LocalDate.now();
        LocalDate result = currentDate.plus(1, ChronoUnit.WEEKS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return formatter.format(result).toString();
    }

    /**
     * Method used to get created date of the task.
     * @return
     */
    public String generateCreatedDate() {
        SimpleDateFormat formDate = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formDate.format(new Date()); // option 2
        return strDate;
    }
}
