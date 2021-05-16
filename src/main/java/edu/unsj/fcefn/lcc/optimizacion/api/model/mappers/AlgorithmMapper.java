package edu.unsj.fcefn.lcc.optimizacion.api.model.mappers;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.FrameDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.StopDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.services.FrameService;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.moeaframework.core.variable.Permutation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlgorithmMapper
{
    @Autowired
    FrameService frameService;

    public List<FrameDTO> permutationToDTO(Permutation permutation, List<StopDTO> stops)
    {
        List<FrameDTO> route = new ArrayList<>();
        for(int i = 0; i < permutation.size() - 1; i++)
        {
            Integer departureStopId = stops.get(i).getId();
            Integer arrivalStopId = stops.get(i + 1).getId();
            FrameDTO frame = frameService
                    .findByIdDepartureStopAndIdArrivalStop(departureStopId, arrivalStopId)
                    .stream()
                    .findFirst()
                    .get();

            route.add(frame);
        }
        return route;
    }
}
