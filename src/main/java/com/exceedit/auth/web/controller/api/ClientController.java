package com.exceedit.auth.web.controller.api;

import com.exceedit.auth.data.models.entities.Client;
import com.exceedit.auth.data.repository.ClientRepository;
import com.exceedit.auth.utils.Utils;
import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.utils.messages.SuccessMessages;
import com.exceedit.auth.web.controller.api.response.ApiResponse;
import com.exceedit.auth.web.dto.CreateClientParams;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("clients")
public class ClientController {

    private final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("")
    public ResponseEntity<String> getClients() {
        val clients = clientRepository.findAll();

        return new ApiResponse()
                .setMessage(SuccessMessages.SUCCESS)
                .setStatus(200)
                .addField("clients", clients).build();
    }

    @PostMapping("")
    public ResponseEntity<String> createClient(@Valid @RequestBody CreateClientParams params) {
        val client = new Client();
        val mergedClient = Utils.mergeDiff(client, params);
        if (mergedClient == null) return new ApiResponse()
                .setMessage(ErrorMessages.INTERNAL_ERROR)
                .setStatus(500).build();

        clientRepository.save(mergedClient);

        return new ApiResponse()
                .setMessage(SuccessMessages.SUCCESS)
                .setStatus(200).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeClient(@PathVariable Long id) {
        val clientInDb = clientRepository.findByClientId(String.valueOf(id));
        if (clientInDb == null) return new ApiResponse()
                .setStatus(404)
                .setMessage(ErrorMessages.CLIENT_NOT_FOUND)
                .setStatus(404).build();

        clientRepository.deleteById(id);

        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateClient(@PathVariable Long id, @Valid @RequestBody Client client) {

        val clientInDb = clientRepository.findByClientId(String.valueOf(id));
        if (clientInDb == null) return new ApiResponse()
                .setStatus(404)
                .setMessage(ErrorMessages.CLIENT_NOT_FOUND).build();

        val mergedClient = Utils.merge(clientInDb, client);
        if (mergedClient == null) return new ApiResponse()
                .setStatus(500)
                .setMessage(ErrorMessages.INTERNAL_ERROR).build();

        clientRepository.save(mergedClient);

        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS).build();
    }
}
