package com.exceedit.auth.web.controller;

import com.exceedit.auth.data.models.Team;
import com.exceedit.auth.data.repository.TeamRepository;
import com.exceedit.auth.utils.Utils;
import com.exceedit.auth.web.dto.CreateTeamParams;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("teams")
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;

//    @GetMapping("")
//    public ResponseEntity<Response> getItems(Pageable pageable) {
//        List<Team> items = teamRepository.findAll();
//        Response resp = new Response(items);
//        //TODO should had object owner and director
//        return ResponseEntity.ok(resp);
//    }

    @PostMapping("")
    public Team createItem(@Valid @RequestBody CreateTeamParams params) throws IllegalAccessException, InstantiationException {
        Team team = new Team();
        team = Utils.mergeDiff(team, params);
        team.set_id(new ObjectId().toString());
        return teamRepository.save(team);
    }

    @DeleteMapping("/{id}")
    public String removeItem(@PathVariable Long id) {
        teamRepository.deleteById(id);
        return "REMOVED";
    }

//    @PutMapping("/{id}")
//    public Team updateItem(@PathVariable Long id, @Valid @RequestBody Team team, ServletServerHttpResponse response) {
////        return teamRepository.findById(id).map(item -> {
////                    try {
////                        Team data = MergeUtil.merge(item, team);
////                        return teamRepository.save(data);
////                    } catch (IllegalAccessException | InstantiationException e) {
////                        throw new RuntimeException(e);
////                    }
////                }))
//    }
}
