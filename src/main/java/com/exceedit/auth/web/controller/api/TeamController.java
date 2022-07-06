package com.exceedit.auth.web.controller.api;

import com.exceedit.auth.data.models.entities.Team;
import com.exceedit.auth.data.repository.TeamRepository;
import com.exceedit.auth.utils.Utils;
import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.utils.messages.SuccessMessages;
import com.exceedit.auth.web.controller.api.response.ApiResponse;
import com.exceedit.auth.web.dto.CreateTeamParams;
import lombok.val;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/teams")
public class TeamController {

    private final Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamRepository teamRepository;

    @RequestMapping(path = "", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    public ResponseEntity<String> getTeams() {
        return new ApiResponse()
                .setStatus(200)
                .addField("data", new JSONArray()
                        .put(new JSONObject()
                                .put("users", new JSONArray())
                                .put("_id", "5fd8977aec290cace76dc08d")
                                .put("name", "Pavel Petrash Mkp")
                                .put("city", "Майкоп")
                                .put("users", new JSONArray())
                                .put("users", new JSONArray())
                        )

                ).build();
    }

    @PostMapping("")
    public ResponseEntity<String> createTeam(@Valid @RequestBody CreateTeamParams createTeamParams) {
        var team = new Team();
        team = Utils.mergeDiff(team, createTeamParams);

        if (team == null) return new ApiResponse()
                .setStatus(500)
                .setMessage(ErrorMessages.INTERNAL_ERROR).build();

        team.set_id(new ObjectId().toString());
        teamRepository.save(team);

        return new ApiResponse()
                .setMessage(SuccessMessages.SUCCESS)
                .setStatus(200)
                .addField("createdItem", team).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeTeam(@PathVariable Long teamId) {
        val team = teamRepository.findById(teamId);
        if (team.isEmpty()) return new ApiResponse()
                .setStatus(404)
                .setMessage(ErrorMessages.TEAM_NOT_FOUND).build();

        teamRepository.delete(team.get());

        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTeam(@PathVariable Long id, @Valid @RequestBody Team team) {

        val teamInDb = teamRepository.findById(id);
        if (teamInDb.isEmpty()) return new ApiResponse()
                .setStatus(404)
                .setMessage(ErrorMessages.TEAM_NOT_FOUND).build();

        val mergedTeam = Utils.merge(teamInDb, team);
        if (mergedTeam == null) return new ApiResponse()
                .setMessage(ErrorMessages.INTERNAL_ERROR)
                .setStatus(500).build();

        teamRepository.save((Team) mergedTeam);

        return new ApiResponse()
                .setMessage(SuccessMessages.SUCCESS)
                .setStatus(200).build();
    }
}
