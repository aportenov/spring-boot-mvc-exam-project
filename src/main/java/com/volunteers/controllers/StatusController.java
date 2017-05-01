package com.volunteers.controllers;

import com.volunteers.enumerators.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/status")
public class StatusController {

    @GetMapping()
    public ResponseEntity<List<Status>> getStatusList(){
        List<Status> status = Arrays.asList(Status.values());

        if(status == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        ResponseEntity<List<Status>> responseEntity
                = new ResponseEntity(status, HttpStatus.OK);

        return responseEntity;
    }
}
