package com.explore.taskmanager.repository;

import com.explore.taskmanager.models.AuditTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditRepositoryImpl implements AuditRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int saveAuditTask(AuditTask auditTask) {
        return jdbcTemplate.update("INSERT INTO AuditTasks (taskid,title,updated_fields," +
                "old_values,new_values) VALUES (?,?,?,?,?)", new Object[]{auditTask.getTaskId(),
                auditTask.getTaskTitle(), auditTask.getUpdatedFields(), auditTask.getOldValues(),
                auditTask.getNewValues()});
    }

    @Override
    public List<AuditTask> getauditTaskById(Integer taskId) {
        return jdbcTemplate.query("SELECT * FROM AuditTasks where taskid = ?", new Object[]{taskId}, (rs, rowNum) ->
                new AuditTask(rs.getInt("audit_log_id"),
                        rs.getInt("taskid"),
                        rs.getString("title"),
                        rs.getString("updated_timestamp"),
                        rs.getString("updated_fields"),
                        rs.getString("old_Values"),
                        rs.getString("new_values")
                ));
    }
}
