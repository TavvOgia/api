package edu.unsj.fcefn.lcc.optimizacion.api.algorithm;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.FrameDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.StopDTO;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.Permutation;
import org.moeaframework.problem.AbstractProblem;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class RoutingProblem extends AbstractProblem
{
    //No se habla con los repositorios directamente por la estructura de capas

    List<StopDTO> stops;
    List<FrameDTO> frames;

    public RoutingProblem(List<StopDTO> stopList, List<FrameDTO> frameList)
    {
        super(1,2);
        stops = stopList;
        frames = frameList;
    }

    @Override
    public void evaluate(Solution solution)
    {
        solution.setObjective(0, totalPrice(solution.getVariable(0))); //calculo del precio
        solution.setObjective(1, totalTime(solution.getVariable(0))); //calculo del tiempo
    }

    private double totalPrice(Variable variable)
    {
        Permutation permutation = (Permutation) variable; //cast

        double totalPrice = 0;

        for(int i = 0; i < permutation.size() - 1; i++)
        {
            StopDTO departureStop = stops.get(permutation.get(i));
            StopDTO arrivalStop = stops.get(permutation.get(i + 1));

            List<FrameDTO> frameList = findByIdDepartureStopAndIdArrivalStop(departureStop.getId(), arrivalStop.getId());

            FrameDTO bestPriceFrame = frameList
                    .stream()
                    .min(Comparator.comparing(FrameDTO::getPrice))
                    .orElse(null);

            if(Objects.isNull(bestPriceFrame)) //si es null entonces la ruta no es viable
            {
                return Double.MAX_VALUE; //indica que no funciona
            }
            totalPrice += bestPriceFrame.getPrice();
        }

        totalPrice = totalPrice + 1;
        totalPrice = totalPrice - 1;

        return totalPrice;
    }

    private double totalTime(Variable variable)
    {
        Permutation permutation = (Permutation) variable; //cast

        double totalTime = 0;
        FrameDTO frameDTO = null;

        for(int i = 0; i < permutation.size() - 1; i++)
        {
            StopDTO departureStop = stops.get(permutation.get(i));
            StopDTO arrivalStop = stops.get(permutation.get(i + 1));
            Map<Integer, Long> mapTime;

            List<FrameDTO> frameList = findByIdDepartureStopAndIdArrivalStop(departureStop.getId(), arrivalStop.getId());

            if (frameList.isEmpty())
            {
                return Double.MAX_VALUE;
            }

            if (i==0)
            {
                mapTime = getTimeMapsPrimero(frameList);
            }
            else
            {
                mapTime = getTimeMapsCamino(frameList, frameDTO.getArrival_datetime());
            }

            Map.Entry<Integer, Long> frameIdTimeToArrival = mapTime
                    .entrySet() //un set es un conjunto, se convierte ya que un Hash no es iterable
                    .stream() //se itera con stream
                    .min(Map.Entry.comparingByValue()) //encuentra el mínimo
                    .orElse(null);

            frameDTO = frameList
                    .stream()
                    .filter(frame -> frame.getId().equals(Objects.requireNonNull(frameIdTimeToArrival).getKey()))
                    .findFirst()
                    .orElse(null);

            if(Objects.isNull(frameDTO))
            {
                return Double.MAX_VALUE;
            }

            totalTime += frameIdTimeToArrival.getValue();
        }

        totalTime = totalTime + 1;
        totalTime = totalTime - 1;

        return totalTime;
    }

    private Map<Integer, Long> getTimeMapsPrimero(List<FrameDTO> frames)
    {
        Map<Integer, Long> mapTime = new HashMap<>(); //Integer: Id del frame y Long: el tiempo que demora el frame

        for(FrameDTO frame : frames)
        {
            if(frame.getDeparture_datetime().isBefore(frame.getArrival_datetime()))
            {
                mapTime.put(frame.getId(), Duration.between(frame.getDeparture_datetime(),frame.getArrival_datetime()).toMinutes());
            }
            else
            {
                mapTime.put(frame.getId(), 1440 - Duration.between(frame.getArrival_datetime(), frame.getDeparture_datetime()).toMinutes());
            }
        }

        return mapTime;
    }

    private Map<Integer, Long> getTimeMapsCamino(List<FrameDTO> frames, LocalTime previousArrivalTime)
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
            timeMap.put(frame.getId(),tripDuration+waitDuration);
        }
        return timeMap;
    }

    public List<FrameDTO> findByIdDepartureStopAndIdArrivalStop(Integer idDepartureStop, Integer idArrivalStop)
    {
        return this.frames
                .stream()
                .filter(frameDTO -> frameDTO.getId_stop_departure().equals(idDepartureStop))
                .filter(frameDTO -> frameDTO.getId_stop_arrival().equals(idArrivalStop))
                .collect(Collectors.toList());
    }

    @Override
    public Solution newSolution()
    {
        Solution solution = new Solution(1, 2); //1 variable y 2 objetivos: tiempo y plata
        solution.setVariable(0, EncodingUtils.newPermutation(14)); //0 porque es 1 sola variable
        return solution;
    }
}
