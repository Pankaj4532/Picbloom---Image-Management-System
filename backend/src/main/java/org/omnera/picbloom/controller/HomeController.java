package org.omnera.picbloom.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
/* 
 Meaning:every method in this class returns data directly in the HTTP response body (instead of returning a view name like an HTML page).
 */
@CrossOrigin(origins = "http://localhost:3000", maxAge=3600)
@RequestMapping("/api/v1")
public class HomeController {

    @GetMapping("/")
    public String demo() {
        return "hello pankaj";
    }

    

}
