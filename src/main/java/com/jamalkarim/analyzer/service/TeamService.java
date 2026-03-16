package com.jamalkarim.analyzer.service;

import com.jamalkarim.analyzer.domain.models.Team;
import com.jamalkarim.analyzer.entities.TeamEntity;
import com.jamalkarim.analyzer.exceptions.TeamNotFoundException;
import com.jamalkarim.analyzer.repository.TeamRepository;
import com.jamalkarim.analyzer.utils.TeamMapper;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository repository;
    private final TeamMapper mapper;

    public TeamService(TeamRepository repository, TeamMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Team getTeamById(long id) {
        return repository.findById(id)
                .map(mapper::entityToDomain)
                .orElseThrow(() -> new TeamNotFoundException(id));
    }

    public Team createTeam(String name) {
        if (repository.findByName(name).isPresent()) {
            return mapper.entityToDomain(repository.findByName(name).get());
        } else {

            Team newTeam = new Team(name);

            TeamEntity savedEntity = repository.save(mapper.domainToEntity(newTeam));
            newTeam.setId(savedEntity.getId());

            return newTeam;
        }
    }
}
