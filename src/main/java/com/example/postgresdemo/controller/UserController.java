package com.example.postgresdemo.controller;

import com.example.postgresdemo.dto.CreateUserDTO;
import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.User;
import com.example.postgresdemo.repository.UserRepository;
import com.example.postgresdemo.util.Response;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.bson.types.ObjectId;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;


@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(params.getPassword()));
        user.set_id(new ObjectId().toString());
        System.out.println(user.getPassword());
        return userRepository.save(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @Valid @RequestBody User user) throws Exception {
        System.out.println("start");
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

    //    @PostMapping("")
//    public Answer addAnswer(@PathVariable Long questionId,
//                            @Valid @RequestBody Answer answer) {
//        return questionRepository.findById(questionId)
//                .map(question -> {
//                    answer.setQuestion(question);
//                    return answerRepository.save(answer);
//                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
//    }
//
//    @PutMapping("/questions/{questionId}/answers/{answerId}")
//    public Answer updateAnswer(@PathVariable Long questionId,
//                               @PathVariable Long answerId,
//                               @Valid @RequestBody Answer answerRequest) {
//        if(!questionRepository.existsById(questionId)) {
//            throw new ResourceNotFoundException("Question not found with id " + questionId);
//        }
//
//        return answerRepository.findById(answerId)
//                .map(answer -> {
//                    answer.setText(answerRequest.getText());
//                    return answerRepository.save(answer);
//                }).orElseThrow(() -> new ResourceNotFoundException("Answer not found with id " + answerId));
//    }
//
//    @DeleteMapping("/questions/{questionId}/answers/{answerId}")
//    public ResponseEntity<?> deleteAnswer(@PathVariable Long questionId,
//                                          @PathVariable Long answerId) {
//        if(!questionRepository.existsById(questionId)) {
//            throw new ResourceNotFoundException("Question not found with id " + questionId);
//        }
//
//        return answerRepository.findById(answerId)
//                .map(answer -> {
//                    answerRepository.delete(answer);
//                    return ResponseEntity.ok().build();
//                }).orElseThrow(() -> new ResourceNotFoundException("Answer not found with id " + answerId));
//
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
