package edu.unsj.fcefn.lcc.optimizacion.api.controllers;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.TransportCompanyDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.services.TransportCompaniesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transportCompanies")
public class TransportCompaniesController {

    //Establecemos una dependencia, el Controller no define las funciones, se las manda al service
    @Autowired
    TransportCompaniesService transportCompaniesService;


    @GetMapping(value = "")
    public List<TransportCompanyDTO> findAll(){
        return transportCompaniesService.findAll();
    }

    @GetMapping(value = "{id}")
    public TransportCompanyDTO find(@PathVariable("id") Integer id){
        return transportCompaniesService.find(id);
    }

    @PostMapping(value = "")
    public TransportCompanyDTO add(@RequestBody TransportCompanyDTO transportCompanyDTO){
        return transportCompaniesService.add(transportCompanyDTO);
    }

    @PutMapping(value = "{id}")
    public TransportCompanyDTO edit(@RequestBody TransportCompanyDTO transportCompanyDTO){
        return transportCompaniesService.edit(transportCompanyDTO);
    }

    @DeleteMapping(value = "{id}")
    public TransportCompanyDTO delete(@PathVariable("id") Integer id) throws Exception {
        return transportCompaniesService.delete(id);
    }
}
