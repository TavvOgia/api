package edu.unsj.fcefn.lcc.optimizacion.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController()
@RequestMapping("/moga")
public class MogaController {

    @GetMapping(value = "")
    public String index(){
        return "index";
    }

    @PostMapping(value = "")
    public String post(){
        return "index";
    }

    @GetMapping(value = "algo")
    public String algo(){
        return "algo";
    }
}
