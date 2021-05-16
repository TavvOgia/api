package edu.unsj.fcefn.lcc.optimizacion.api.model.mappers;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.FrameDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.entities.FrameEntity;
import org.springframework.stereotype.Component;

@Component
public class FrameMapper {
    public FrameDTO entityToDTO (FrameEntity frameEntity) {
        FrameDTO frameDTO = new FrameDTO();

        frameDTO.setId(frameEntity.getId());
        frameDTO.setId_transport_company(frameEntity.getId_transport_company());
        frameDTO.setId_stop_departure(frameEntity.getId_stop_departure());
        frameDTO.setId_stop_arrival(frameEntity.getId_stop_arrival());
        frameDTO.setPrice(frameEntity.getPrice());
        frameDTO.setCategory(frameEntity.getCategory());
        frameDTO.setDeparture_datetime(frameEntity.getDeparture_datetime());
        frameDTO.setArrival_datetime(frameEntity.getArrival_datetime());

        return frameDTO;
    }

    public FrameEntity dtoToEntity (FrameDTO frameDTO) {
        FrameEntity frameEntity = new FrameEntity();

        frameEntity.setId(frameDTO.getId());
        frameEntity.setId_transport_company(frameDTO.getId_transport_company());
        frameEntity.setId_stop_departure(frameDTO.getId_stop_departure());
        frameEntity.setId_stop_arrival(frameDTO.getId_stop_arrival());
        frameEntity.setPrice(frameDTO.getPrice());
        frameEntity.setCategory(frameDTO.getCategory());
        frameEntity.setDeparture_datetime(frameDTO.getDeparture_datetime());
        frameEntity.setArrival_datetime(frameDTO.getArrival_datetime());

        return frameEntity;
    }
}
