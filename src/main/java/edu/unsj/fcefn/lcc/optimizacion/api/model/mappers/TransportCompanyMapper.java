package edu.unsj.fcefn.lcc.optimizacion.api.model.mappers;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.TransportCompanyDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.entities.TransportCompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class TransportCompanyMapper {
    public TransportCompanyDTO entityToDTO (TransportCompanyEntity transportCompanyEntity) {
        TransportCompanyDTO transportCompanyDTO = new TransportCompanyDTO();

        transportCompanyDTO.setId(transportCompanyEntity.getId());
        transportCompanyDTO.setName(transportCompanyEntity.getName());
        transportCompanyDTO.setLogo(transportCompanyEntity.getLogo());

        return transportCompanyDTO;
    }

    public TransportCompanyEntity dtoToEntity (TransportCompanyDTO transportCompanyDTO) {
        TransportCompanyEntity transportCompanyEntity = new TransportCompanyEntity();

        transportCompanyEntity.setId(transportCompanyDTO.getId());
        transportCompanyEntity.setName(transportCompanyDTO.getName());
        transportCompanyEntity.setLogo(transportCompanyDTO.getLogo());

        return transportCompanyEntity;
    }
}
