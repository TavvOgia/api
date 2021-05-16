package edu.unsj.fcefn.lcc.optimizacion.api.controllers;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.StopDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.services.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stops")
public class StopController {

    @Autowired
    StopService stopService;

    @GetMapping(value = "")
    public List<StopDTO> findAll(){
        return stopService.findAll();
    }

    @GetMapping(value = "{id}")
    public StopDTO find(@PathVariable("id") Integer id){
        return stopService.find(id);
    }

    @PostMapping(value = "")
    public StopDTO add(@RequestBody StopDTO stopDTO){
        return stopService.add(stopDTO);
    }

    @PutMapping(value = "{id}")
    public StopDTO edit(@RequestBody StopDTO stopDTO){
        return stopService.edit(stopDTO);
    }

    @DeleteMapping(value = "{id}")
    public StopDTO delete(@PathVariable("id") Integer id) throws Exception {
        return stopService.delete(id);
    }
}
