package edu.unsj.fcefn.lcc.optimizacion.api.services;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.TransportCompanyDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.entities.TransportCompanyEntity;
import edu.unsj.fcefn.lcc.optimizacion.api.model.mappers.TransportCompanyMapper;
import edu.unsj.fcefn.lcc.optimizacion.api.model.repositories.TransportCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransportCompaniesService {

    @Autowired
    TransportCompanyRepository transportCompanyRepository;

    @Autowired
    TransportCompanyMapper transportCompanyMapper;

    public List<TransportCompanyDTO> findAll(){
        return transportCompanyRepository.findAll()
                .stream() //me permite ejecutar acceciones sobre un conjunto de datos
                .map(transportCompanyEntity -> transportCompanyMapper.entityToDTO(transportCompanyEntity))
                .collect(Collectors.toList()); //all lo que agarra lo compacta en una lista
    }

    public TransportCompanyDTO find(Integer id){
        return transportCompanyRepository
                .findById(id)
                .map(transportCompanyEntity -> transportCompanyMapper.entityToDTO(transportCompanyEntity))
                .orElse(null);
    }

    public TransportCompanyDTO add(TransportCompanyDTO transportCompanyDTO){
        TransportCompanyEntity transportCompanyEntity = transportCompanyMapper.dtoToEntity(transportCompanyDTO);
        transportCompanyEntity = transportCompanyRepository.save(transportCompanyEntity);
        return transportCompanyMapper.entityToDTO(transportCompanyEntity);
    }

    public TransportCompanyDTO edit(TransportCompanyDTO transportCompanyDTO){
        TransportCompanyEntity transportCompanyEntity = transportCompanyMapper.dtoToEntity(transportCompanyDTO);
        transportCompanyEntity = transportCompanyRepository.save(transportCompanyEntity);
        return transportCompanyMapper.entityToDTO(transportCompanyEntity);
    }

    public TransportCompanyDTO delete(Integer id) throws Exception {
        Optional<TransportCompanyEntity> transportCompanyEntityOptional = transportCompanyRepository.findById(id);
        if(transportCompanyEntityOptional.isPresent()){
            transportCompanyRepository.deleteById(id);
            return transportCompanyMapper.entityToDTO(transportCompanyEntityOptional.get());
        } else {
            throw new Exception("TransportCompany not found");
        }
    }
}
