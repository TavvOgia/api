package edu.unsj.fcefn.lcc.optimizacion.api.algorithm;

import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.FrameDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.StopDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.services.AlgorithmService;
import edu.unsj.fcefn.lcc.optimizacion.api.services.FrameService;
import edu.unsj.fcefn.lcc.optimizacion.api.services.StopService;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.Permutation;
import org.moeaframework.problem.AbstractProblem;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class RoutingProblem extends AbstractProblem
{

    //No se habla con los repositorios directamente por la estructura de capas

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private StopService stopService;

    @Autowired
    private FrameService frameService;

    private List<StopDTO> stops;

    public RoutingProblem()
    {
        super(1,2);
        this.stops = stopService.findAll().stream()
                .sorted(Comparator.comparing(StopDTO::getRanking).reversed())
                .collect(Collectors.toList())
                .subList(0,20); //buscamos las 20 primeras ciudades
                //con los :: se accede de manera estática al metodo
                //el reverse lo devuelve ordenado de mayor a menor


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

            List<FrameDTO> frames = frameService
                    .findByIdDepartureStopAndIdArrivalStop(departureStop.getId(), arrivalStop.getId());

            FrameDTO bestPriceFrame = frames
                    .stream()
                    .min(Comparator.comparing(FrameDTO::getPrice))
                    .orElse(null);

            if(Objects.isNull(bestPriceFrame)) //si es null entonces la ruta no es viable
            {
                return Double.MAX_VALUE; //indica que no funciona
            }
            totalPrice += bestPriceFrame.getPrice();
        }

        return totalPrice;
    }

    private double totalTime(Variable variable)
    {
        Permutation permutation = (Permutation) variable; //cast
        List<StopDTO> stops = algorithmService.getStops();

        double totalTime = 0;

        for(int i = 0; i < permutation.size() - 1; i++)
        {
            StopDTO departureStop = stops.get(permutation.get(i));
            StopDTO arrivalStop = stops.get(permutation.get(i + 1));

            List<FrameDTO> frames = frameService
                    .findByIdDepartureStopAndIdArrivalStop(departureStop.getId(), arrivalStop.getId());

            Map<Integer,Long> mapTime = getTimeMaps(frames);
            Map.Entry<Integer, Long> frameIdTimeToArrival = mapTime
                    .entrySet() //un set es un conjunto, se convierte ya que un Hash no es iterable
                    .stream() //se itera con stream
                    .min(Map.Entry.comparingByValue()) //encuentra el mínimo
                    .orElse(null);

            if(Objects.isNull(frameIdTimeToArrival))
            {
                return Double.MAX_VALUE;
            }

            FrameDTO frameDTO = frames
                    .stream()
                    .filter(frame -> frame.getId().equals(frameIdTimeToArrival.getKey()))  //el menor tiempo de todos los frames
                    .findFirst()
                    .orElse(null);

            totalTime += frameIdTimeToArrival.getValue();
        }

        return totalTime;
    }

    private Map<Integer, Long> getTimeMaps(List<FrameDTO> frames)
    {
        Map<Integer, Long> mapTime = new HashMap<>(); //Integer: Id del frame y Long: el tiempo que demora el frame
        for(FrameDTO frame : frames)
        {
            if(frame.getDeparture_datetime().isBefore(frame.getArrival_datetime()))
            {
                Long TimeToArrival = Duration.between(frame.getDeparture_datetime(), frame.getArrival_datetime()).getSeconds();
                mapTime.put(frame.getId(), TimeToArrival);
            }
            else
            {
                Long TimeToArrivalRange1 = Duration.between(frame.getDeparture_datetime(), LocalTime.MIDNIGHT).getSeconds();
                Long TimeToArrivalRange2 = Duration.between(LocalTime.MIDNIGHT, frame.getArrival_datetime()).getSeconds();
                Long TimeToArrival = TimeToArrivalRange1 + TimeToArrivalRange2;
                mapTime.put(frame.getId(), TimeToArrival);
            }
        }

        return mapTime;
    }

    @Override
    public Solution newSolution()
    {
        Solution solution = new Solution(1, 2); //1 variable y 2 objetivos: tiempo y plata
        solution.setVariable(0, EncodingUtils.newPermutation(20)); //0 porque es 1 sola variable
        return solution;
    }
}
