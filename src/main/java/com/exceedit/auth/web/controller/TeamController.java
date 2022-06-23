package com.exceedit.auth.web.controller;

import com.exceedit.auth.exception.ResourceNotFoundException;
import com.exceedit.auth.model.Client;
import com.exceedit.auth.model.Team;
import com.exceedit.auth.repository.TeamRepository;
import com.exceedit.auth.util.MergeUtil;
import com.exceedit.auth.util.Response;
import com.exceedit.auth.web.dto.CreateClientDTO;
import com.exceedit.auth.web.dto.CreateTeamDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;


@RestController
@RequestMapping("teams")
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("")
    public ResponseEntity<Response> getItems(Pageable pageable) {
        List<Team> items = teamRepository.findAll();
        Response resp = new Response(items);
        //TODO should had object owner and director
        return ResponseEntity.ok(resp);
    }

    @PostMapping("")
    public Team createItem(@Valid @RequestBody CreateTeamDTO params) throws IllegalAccessException, InstantiationException {
        Team team = new Team();
        team = MergeUtil.mergeDiff(team, params);
        team.set_id(new ObjectId().toString());
        return teamRepository.save(team);
    }

    @DeleteMapping("/{id}")
    public String removeItem(@PathVariable Long id) {
        teamRepository.deleteById(id);
        return "REMOVED";
    }

    @PutMapping("/{id}")
    public Team updateItem(@PathVariable Long id, @Valid @RequestBody Team team) {
        return teamRepository.findById(id).map(item -> {
                    try {
                        Team data = MergeUtil.merge(item, team);
                        return teamRepository.save(data);
                    } catch (IllegalAccessException | InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
    }
}
