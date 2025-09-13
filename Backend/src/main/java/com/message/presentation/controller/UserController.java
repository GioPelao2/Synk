package com.message.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.message.domain.entities.Message;


@Controller
@RequestMapping(value = "/user")
public class UserController {

    @PostMapping("/getUser")
    public Message getUser() {
      return null;
    }


}
