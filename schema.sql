USE taskmanager;

CREATE TABLE IF NOT EXISTS Tasks (
taskid INT PRIMARY KEY auto_increment,
title VARCHAR(200),
eta VARCHAR(20),
status VARCHAR(20),
created_date VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS AuditTasks (
audit_log_id INT PRIMARY KEY auto_increment,
taskid INT,
title VARCHAR(200),
updated_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
updated_fields VARCHAR(200),
old_values VARCHAR(200),
new_values VARCHAR(200)
);