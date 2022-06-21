package com.exceedit.auth.web.controller;

import com.exceedit.auth.web.dto.CreateUserDTO;
import com.exceedit.auth.exception.ResourceNotFoundException;
import com.exceedit.auth.model.User;
import com.exceedit.auth.repository.UserRepository;
import com.exceedit.auth.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.types.ObjectId;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;


@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<Response> getUsers(Pageable pageable) {
        List<User> users = userRepository.findAll();
        Response resp = new Response(users);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("")
    public User createUser(@Valid @RequestBody CreateUserDTO params) throws IllegalAccessException, InstantiationException {
        User user = new User();
        user = mergeDiff(user, params);
        user.set_id(new ObjectId().toString());
        System.out.println(user.getPassword());
        return userRepository.save(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {
        return userRepository.findById(userId).map(item -> {
                    try {
                        User data = merge(item, user);
                        return userRepository.save(data);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
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
