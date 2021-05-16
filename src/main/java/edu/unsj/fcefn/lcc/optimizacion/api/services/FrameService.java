package edu.unsj.fcefn.lcc.optimizacion.api.services;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.FrameDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.entities.FrameEntity;
import edu.unsj.fcefn.lcc.optimizacion.api.model.mappers.FrameMapper;
import edu.unsj.fcefn.lcc.optimizacion.api.model.repositories.FrameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FrameService {

    @Autowired
    FrameRepository frameRepository;

    @Autowired
    FrameMapper frameMapper;

    List<FrameDTO> frames;

    @PostConstruct
    private void init(){ //esto es para no hacer constantes accesos a la Basse de Datos
        this.frames = this.findAll();
    }

    public List<FrameDTO> getFrames(){
        return this.frames;
    }

    public List<FrameDTO> findByIdDepartureStopAndIdArrivalStop(Integer idDepartureStop, Integer idArrivalStop){
        return this.frames
                .stream()
                .filter(frameDTO -> frameDTO.getId_stop_departure().equals(idDepartureStop))
                .filter(frameDTO -> frameDTO.getId_stop_arrival().equals(idArrivalStop))
                .collect(Collectors.toList());
    }

    public List<FrameDTO> findAll(){
        return frameRepository.findAll()
                .stream()
                .map(frameEntity -> frameMapper.entityToDTO(frameEntity))
                .collect(Collectors.toList());
    }

    public FrameDTO find(Integer id){
        return frameRepository.findById(id)
                .map(frameEntity -> frameMapper.entityToDTO(frameEntity))
                .orElse(null);
    }

    public FrameDTO add(FrameDTO frameDTO){
        FrameEntity frameEntity = frameMapper.dtoToEntity(frameDTO);
        frameEntity = frameRepository.save(frameEntity);
        return frameMapper.entityToDTO(frameEntity);
    }

    public FrameDTO edit(FrameDTO frameDTO){
        FrameEntity frameEntity = frameMapper.dtoToEntity(frameDTO);
        frameEntity = frameRepository.save(frameEntity);
        return frameMapper.entityToDTO(frameEntity);
    }

    public FrameDTO delete(Integer id) throws Exception {
        Optional<FrameEntity> frameEntityOptional = frameRepository.findById(id);
        if(frameEntityOptional.isPresent()){
            frameRepository.deleteById(id);
            return frameMapper.entityToDTO(frameEntityOptional.get());
        } else {
            throw new Exception("Frame not found");
        }
    }
}
