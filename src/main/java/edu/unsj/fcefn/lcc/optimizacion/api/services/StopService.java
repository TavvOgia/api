package edu.unsj.fcefn.lcc.optimizacion.api.services;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.StopDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.entities.StopEntity;
import edu.unsj.fcefn.lcc.optimizacion.api.model.mappers.StopMapper;
import edu.unsj.fcefn.lcc.optimizacion.api.model.repositories.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StopService {

    @Autowired
    StopRepository stopRepository;

    @Autowired
    StopMapper stopMapper;

    public List<StopDTO> findAll(){
        return stopRepository.findAll()
                .stream()
                .map(stopEntity -> stopMapper.entityToDTO(stopEntity))
                .collect(Collectors.toList());
    }

    public StopDTO find(Integer id){
        return stopRepository.findById(id)
                .map(stopEntity -> stopMapper.entityToDTO(stopEntity))
                .orElse(null);
    }

    public StopDTO add(StopDTO stopDTO){
        StopEntity stopEntity = stopMapper.dtoToEntity(stopDTO);
        stopEntity = stopRepository.save(stopEntity);
        return stopMapper.entityToDTO(stopEntity);
    }

    public StopDTO edit(StopDTO stopDTO){
        StopEntity stopEntity = stopMapper.dtoToEntity(stopDTO);
        stopEntity = stopRepository.save(stopEntity);
        return stopMapper.entityToDTO(stopEntity);
    }

    public StopDTO delete(Integer id) throws Exception {
        Optional<StopEntity> stopEntityOptional = stopRepository.findById(id);
        if(stopEntityOptional.isPresent()){
            stopRepository.deleteById(id);
            return stopMapper.entityToDTO(stopEntityOptional.get());
        } else {
            throw new Exception("Stop not found");
        }
    }
}
