package edu.unsj.fcefn.lcc.optimizacion.api.controllers;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.FrameDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.services.FrameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/frames")
public class FrameController
{
    @Autowired
    FrameService frameService;

    @GetMapping(value = "")
    public List<FrameDTO> findAll()
    {
        return frameService.findAll();
    }

    @GetMapping(value = "{id}")
    public FrameDTO find (@PathVariable("id") Integer id)
    {
        return frameService.find(id);
    }

    @PostMapping(value = "")
    public FrameDTO add (@RequestBody FrameDTO frameDTO)
    {
        return frameService.add(frameDTO);
    }

    @PutMapping(value = "")
    public FrameDTO edit (@RequestBody FrameDTO frameDTO)
    {
        return frameService.edit(frameDTO);
    }

    @DeleteMapping(value = "{id}")
    public FrameDTO delete (@PathVariable("id") Integer id) throws Exception
    {
        return frameService.delete(id);
    }
}
