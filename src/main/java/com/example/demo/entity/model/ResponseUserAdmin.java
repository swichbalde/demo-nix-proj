package com.example.demo.entity.model;

import com.example.demo.entity.user.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseUserAdmin {

    private Long id;
    private String login;
    private Date created;
    private Status status;

    public ResponseUserAdmin(Long id, String login, Date created, Status status) {
        this.id = id;
        this.login = login;
        this.created = created;
        this.status = status;
    }
}
