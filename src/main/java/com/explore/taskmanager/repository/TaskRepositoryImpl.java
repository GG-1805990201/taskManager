package com.explore.taskmanager.repository;

import com.explore.taskmanager.models.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int saveTask(Tasks tasks) {
        return jdbcTemplate.update("INSERT INTO Tasks (title, eta, status, " +
                "created_date) VALUES (?,?,?,?)", new Object[]{tasks.getTitle(), tasks.getEta(), tasks.getStatus(),
                tasks.getCreatedDate()});
    }

    @Override
    public Tasks getTaskById(Integer taskId) {
        try {
            Tasks task = jdbcTemplate.queryForObject("SELECT * FROM Tasks WHERE taskid=?",
                    BeanPropertyRowMapper.newInstance(Tasks.class), taskId);

            return task;
        } catch (IncorrectResultSizeDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Tasks> getAllTasks() {
        return jdbcTemplate.query("SELECT * FROM Tasks", BeanPropertyRowMapper.newInstance(Tasks.class));
    }

    @Override
    public int updateTask(String query) {
        return jdbcTemplate.update(query);
    }

    @Override
    public int deleteTask(Integer taskId) {
        return jdbcTemplate.update("DELETE FROM Tasks WHERE taskid = ?",new Object[]{taskId});
    }
}