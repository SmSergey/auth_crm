package com.example.postgresdemo.controller;

import com.example.postgresdemo.exception.ResourceNotFoundException;
import com.example.postgresdemo.model.User;
import com.example.postgresdemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;


class Response {
    private Object data;
    private Boolean shouldNotify = false;
    Response(Object item){
        this.data = item;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getShouldNotify() {
        return shouldNotify;
    }

    public void setShouldNotify(Boolean shouldNotify) {
        this.shouldNotify = shouldNotify;
    }
}


@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("")
    public ResponseEntity<Response> getUsers(Pageable pageable) {
       List<User> users =  userRepository.findAll();
        Response resp = new Response(users);
       return  ResponseEntity.ok(resp);
    }

    @PostMapping("")
    public User createUser(@Valid @RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("INIT  " + user.getPassword());
        return userRepository.save(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable Long userId, @Valid @RequestBody User user) throws Exception {
        System.out.println("start");
        return userRepository.findById (userId).map (item -> {
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
}
