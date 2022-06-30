package com.exceedit.auth.web.controller.api;

import com.exceedit.auth.utils.Utils;
import com.exceedit.auth.utils.crypto.HashHelper;
import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.utils.messages.SuccessMessages;
import com.exceedit.auth.web.controller.advices.annotations.ApiException;
import com.exceedit.auth.web.controller.api.response.ApiResponse;
import com.exceedit.auth.web.dto.users.CreateUserParams;
import com.exceedit.auth.data.models.User;
import com.exceedit.auth.data.repository.UserRepository;
import com.exceedit.auth.web.dto.users.UpdatePermissionsParams;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.types.ObjectId;

import javax.validation.Valid;
import java.util.List;


@RestController
@ApiException
@RequestMapping("users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<String> getUsers() {
        List<User> users = userRepository.findAll();
        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS)
                .addField("users", users).build();
    }

    @PostMapping("")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserParams params) {
        val user = Utils.mergeDiff(new User(), params);
        if (user == null) return new ApiResponse()
                .setMessage(ErrorMessages.INTERNAL_ERROR)
                .setStatus(500).build();

        user.set_id(new ObjectId().toString());
        user.setPassword(HashHelper.getBCryptHash(user.getPassword()));

        userRepository.save(user);

        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {

        val userInDb = userRepository.findById(userId);
        if (userInDb.isEmpty()) return new ApiResponse()
                .setMessage(ErrorMessages.USER_NOT_FOUND)
                .setStatus(404).build();

        val mergedUser = Utils.merge(userInDb, user);
        if (mergedUser == null) return new ApiResponse()
                .setMessage(ErrorMessages.INTERNAL_ERROR)
                .setStatus(500).build();

        userRepository.save((User) mergedUser);

        return new ApiResponse()
                .setMessage(SuccessMessages.SUCCESS)
                .setStatus(200).build();
    }

    @PutMapping(value = "/{userId}/permissions")
    public ResponseEntity<String> updateUserPermissions(
            @RequestBody UpdatePermissionsParams params,
            @PathVariable Long userId
    ) {

        logger.info(params.toString());
        val userInDb = userRepository.findById(userId);
        if (userInDb.isEmpty()) return new ApiResponse()
                .setStatus(404)
                .setMessage(ErrorMessages.USER_NOT_FOUND).build();

        val newPermissions = params.getPermissionsList();

        userInDb.get().setPermissions(newPermissions.toString());
        userRepository.save(userInDb.get());

        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS).build();
    }
}
