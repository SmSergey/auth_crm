package com.exceedit.auth.web.controller;

import com.exceedit.auth.data.models.Client;
import com.exceedit.auth.data.repository.ClientRepository;
import com.exceedit.auth.web.dto.CreateClientParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;


@RestController
@RequestMapping("clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

//    @GetMapping("")
//    public ResponseEntity<Response> getItems(Pageable pageable) {
//        List<Client> items = clientRepository.findAll();
//        Response resp = new Response(items);
//        return ResponseEntity.ok(resp);
//    }

    @PostMapping("")
    public Client createItem(@Valid @RequestBody CreateClientParams params) throws IllegalAccessException, InstantiationException {
        Client client = new Client();

        client = mergeDiff(client, params);
        return clientRepository.save(client);
    }

    @DeleteMapping("/{id}")
    public String removeItem(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return "REMOVED";
    }

//    @PutMapping("/{id}")
//    public Client updateItem(@PathVariable Long id, @Valid @RequestBody Client user) {
//        return clientRepository.findById(id).map(item -> {
//                    try {
//                        Client data = merge(item, user);
//                        return clientRepository.save(data);
//                    } catch (IllegalAccessException | InstantiationException e) {
//                        throw new RuntimeException(e);
//                    }
//                })
//                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
//    }

    public <T> T merge(T local, T remote) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = local.getClass();
        Object merged = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object localValue = field.get(local);
            Object remoteValue = field.get(remote);
            if (localValue != null) {
                switch (localValue.getClass().getSimpleName()) {
                    case "Default":
                    case "Detail":
                        field.set(merged, this.merge(localValue, remoteValue));
                        break;
                    default:
                        field.set(merged, (remoteValue != null) ? remoteValue : localValue);
                }
            }
        }
        return (T) merged;
    }

    public <T, K> T mergeDiff(T local, K remote) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = local.getClass();
        Class<?> classRemote = remote.getClass();
        Object merged = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            for (Field fieldRem : classRemote.getDeclaredFields()) {
                fieldRem.setAccessible(true);
                System.out.println(fieldRem + "    " + field);
                if (field.getName().equals(fieldRem.getName())) {
                    Object localValue = field.get(local);
                    Object remoteValue = fieldRem.get(remote);

                    field.set(merged, (remoteValue != null) ? remoteValue : localValue);
                }
            }
        }
        return (T) merged;
    }
}
