package edu.unsj.fcefn.lcc.optimizacion.api.model.repositories;

import edu.unsj.fcefn.lcc.optimizacion.api.model.entities.FrameEntity;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrameRepository extends CrudRepository<FrameEntity, Integer> {
    List<FrameEntity> findAll();
}
