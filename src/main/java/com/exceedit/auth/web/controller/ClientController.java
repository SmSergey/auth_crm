package com.exceedit.auth.web.controller;

import com.exceedit.auth.exception.ResourceNotFoundException;
import com.exceedit.auth.model.Client;
import com.exceedit.auth.repository.ClientRepository;
import com.exceedit.auth.util.Response;
import com.exceedit.auth.web.dto.CreateUserDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;


@RestController
@RequestMapping("clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public ResponseEntity<Response> getUsers(Pageable pageable) {
        List<Client> items = clientRepository.findAll();
        Response resp = new Response(items);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("")
    public Client createItem(@Valid @RequestBody CreateUserDTO params) throws IllegalAccessException, InstantiationException {
        Client user = new Client();
        user = mergeDiff(user, params);
//        user.setPassword(passwordEncoder.encode(params.getPassword()));
//        System.out.println(user.getPassword());
        return clientRepository.save(user);
    }

    @PutMapping("/{id}")
    public Client updateItem(@PathVariable Long id, @Valid @RequestBody Client user) throws Exception {
        System.out.println("start");
        return clientRepository.findById(id).map(item -> {
                    try {
                        Client data = merge(item, user);
                        return clientRepository.save(data);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
    }

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