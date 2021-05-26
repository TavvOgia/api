package edu.unsj.fcefn.lcc.optimizacion.api.model.mappers;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.FrameDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.StopDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.services.FrameService;
import org.moeaframework.core.variable.Permutation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

@Component
public class AlgorithmMapper
{
    @Autowired
    FrameService frameService;

    public List<FrameDTO> permutationToDTO(Permutation permutation, List<StopDTO> stops)
    {
        List<FrameDTO> route = new ArrayList<>();
        FrameDTO frameDTO = new FrameDTO();

        for(int i = 0; i < permutation.size() - 1; i++)
        {
            StopDTO departureStop = stops.get(permutation.get(i));
            StopDTO arrivalStop = stops.get(permutation.get(i + 1));
            Map<Integer, Long> mapTime;

            List<FrameDTO> frames = frameService
                    .findByIdDepartureStopAndIdArrivalStop
                            (departureStop.getId(), arrivalStop.getId());

            if (i==0)
            {
                mapTime = getMapTimePrimero(frames);
            }
            else
            {
                mapTime = getMapTimeCamino(frames, frameDTO.getArrival_datetime());
            }

            Map.Entry<Integer, Long> frameIdDuration = mapTime
                    .entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .orElse(null);

            frameDTO = frames
                    .stream()
                    .filter(frame -> frame.getId().equals(Objects.requireNonNull(frameIdDuration).getKey()))
                    .findFirst()
                    .orElse(null);

            if(Objects.isNull(frameDTO))
            {
                return new ArrayList<>();
            }

            route.add(frameDTO);

        }
        return route;
    }



    private Map<Integer,Long> getMapTimePrimero(List<FrameDTO> frames)
    {
        Map<Integer,Long> timeMap = new HashMap<>();

        for(FrameDTO frame : frames)
        {
            if (frame.getDeparture_datetime().isBefore(frame.getArrival_datetime()))
            {
                timeMap.put(frame.getId(), Duration.between(frame.getDeparture_datetime(),frame.getArrival_datetime()).toMinutes());
            }
            else
            {
                timeMap.put(frame.getId(), 1440 - Duration.between(frame.getArrival_datetime(), frame.getDeparture_datetime()).toMinutes());
            }
        }
        return timeMap;
    }


    private Map<Integer,Long> getMapTimeCamino(List<FrameDTO> frames, LocalTime previousArrivalTime)
    {
        Map<Integer,Long> timeMap = new HashMap<>();
        Long tripDuration;
        Long waitDuration;

        for(FrameDTO frame : frames)
        {
            if (frame.getDeparture_datetime().isBefore(frame.getArrival_datetime()))
            {
                tripDuration = Duration.between(frame.getDeparture_datetime(),frame.getArrival_datetime()).toMinutes();

            }
            else
            {
                tripDuration = 1440 - Duration.between(frame.getArrival_datetime(),frame.getDeparture_datetime()).toMinutes();

            }

            if (previousArrivalTime.isBefore(frame.getDeparture_datetime()))
            {
                waitDuration = Duration.between(previousArrivalTime, frame.getDeparture_datetime()).toMinutes();
            }
            else
            {
                waitDuration = 1440 - Duration.between(frame.getDeparture_datetime(), previousArrivalTime).toMinutes();
            }

            timeMap.put(frame.getId(), tripDuration + waitDuration);

        }
        return timeMap;
    }
}
