package com.explore.taskmanager.exceptions;

import com.mysql.cj.x.protobuf.MysqlxCursor;

public class FetchingException extends RuntimeException{

    public FetchingException(String message){
        super(message);
    }
}
