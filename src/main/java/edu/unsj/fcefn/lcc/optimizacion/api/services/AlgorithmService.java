package edu.unsj.fcefn.lcc.optimizacion.api.services;

import edu.unsj.fcefn.lcc.optimizacion.api.algorithm.RoutingProblem;
import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.FrameDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.domain.StopDTO;
import edu.unsj.fcefn.lcc.optimizacion.api.model.mappers.AlgorithmMapper;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.variable.Permutation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class AlgorithmService {

    @Autowired
    StopService stopService;

    @Autowired
     FrameService frameService;

    @Autowired
     AlgorithmMapper algorithmMapper;

    private List<StopDTO> stops;

    private List<FrameDTO> frames;

    @PostConstruct
    private void init()
    {
        stops = stopService.find20();
        frames = frameService.findAll();
    }

    public List<FrameDTO> execute()
    {
        NondominatedPopulation population = new Executor()
                .withAlgorithm("NSGAII") //para que siempre devuelva una poblacion no dominada, es un esquema de algoritmo
                .withProblemClass(RoutingProblem.class, stops, frames) //se realiza este problema
                .withMaxEvaluations(100000) //serán 100.000 evaluaciones
                .run();

        return StreamSupport
                .stream(population.spliterator(), false)
                .map(solution -> (Permutation)solution.getVariable(0))
                .map(permutation -> algorithmMapper.permutationToDTO(permutation, stops))
                .findFirst()
                .orElse(new ArrayList<>());
    }
}
