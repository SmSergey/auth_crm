package com.exceedit.auth.web.controller.api;

import com.exceedit.auth.security.services.PrincipalService;
import com.exceedit.auth.utils.Utils;
import com.exceedit.auth.utils.crypto.HashHelper;
import com.exceedit.auth.utils.messages.ErrorMessages;
import com.exceedit.auth.utils.messages.SuccessMessages;
import com.exceedit.auth.web.controller.api.response.advices.annotations.ApiException;
import com.exceedit.auth.web.controller.api.response.ApiResponse;
import com.exceedit.auth.web.dto.users.CreateUserParams;
import com.exceedit.auth.data.models.entities.User;
import com.exceedit.auth.data.repository.UserRepository;
import com.exceedit.auth.web.dto.users.UpdatePermissionsParams;
import lombok.val;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@ApiException
@RequestMapping("api/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("{jwt.authorization.header.name}")
    private String AUTHORIZATION_HEADER_NAME;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<String> getUsers() {
        List<User> users = userRepository.findAll();
        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS)
                .addField("data", users).build();
    }

    @PostMapping("")
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserParams params) {

        val userInDb = userRepository.findByEmail(params.getEmail());
        if (userInDb != null) return new ApiResponse()
                .setStatus(400)
                .setMessage("user with this email already exists")
                .shouldNotify(true).build();

        var user = Utils.mergeDiff(new User(), params);
        if (user == null) return new ApiResponse()
                .setMessage(ErrorMessages.INTERNAL_ERROR)
                .setStatus(500).build();

        user.set_id(new ObjectId().toString());
        user.setPassword(HashHelper.getBCryptHash(user.getPassword()));

        userRepository.save(user);

        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS)
                .shouldNotify(true)
                .addField("data", new JSONObject()
                        .put("hasImage", false)
                        .put("active", true)
                        .put("deleted", false)
                        .put("_id", user.get_id())
                        .put("email", user.getEmail())
                        .put("name", user.getName())
                        .put("role", "Developer")
                        .put("team", "5fd8977aec290cace76dc08d") // teamID
                        .put("surname", user.getSurname())
                        .put("createdAt", user.getCreatedAt())
                        .put("updatedAt", user.getUpdatedAt())
                        .put("fullName", user.getFullName())
                ).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {

        val userInDb = userRepository.findById(userId);
        if (userInDb.isEmpty()) return new ApiResponse().buildAsUserNotFound();

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
        val userInDb = userRepository.findById(userId);
        if (userInDb.isEmpty()) return new ApiResponse().buildAsUserNotFound();

        val newPermissions = params.getPermissionsList();

        userInDb.get().setPermissions(newPermissions.toString());
        userRepository.save(userInDb.get());

        return new ApiResponse()
                .setStatus(200)
                .setMessage(SuccessMessages.SUCCESS).build();
    }

    @RequestMapping(path = "/me", method = {RequestMethod.GET, RequestMethod.OPTIONS})
    public ResponseEntity<String> getMe(HttpServletRequest request) {

        var user = userRepository.findByEmail(PrincipalService.getCurrentUserLogin());
        if (user == null) return new ApiResponse().buildAsUserNotFound();

        return new ApiResponse()
                .setStatus(200)
                .addField("data", new JSONObject()
                        .put("_id", user.get_id())
                        .put("permissions", new JSONObject()
                                .put("FULL", 1)
                        )
                        .put("active", true)
                        .put("email", user.getEmail())
                        .put("name", user.getName())
                        .put("role", "Admin")
                        .put("createdAt", user.getCreatedAt())
                        .put("updatedAt", user.getUpdatedAt())
                        .put("fullName", user.getFullName())
                        .put("hasImage", true)
                        .put("old_permissions", new JSONObject()
                                .put("FULL", 1)
                        )
                        .put("allowedCategories", new JSONObject()
                                .put("FULL", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", "Техника")
                                                .put("id", "TECH")
                                        ).put(new JSONObject()
                                                .put("name", "Корпоративы")
                                                .put("id", "CORPORATE")
                                        ).put(new JSONObject()
                                                .put("name", "Украшение офиса")
                                                .put("id", "OFFICE_DECORATIONS")
                                        ).put(new JSONObject()
                                                .put("name", "Сервисы")
                                                .put("id", "SERVICES")
                                        ).put(new JSONObject()
                                                .put("name", "ДР")
                                                .put("id", "BIRTH_DAY")
                                        ).put(new JSONObject()
                                                .put("name", "Спорт")
                                                .put("id", "SPORT")
                                        ).put(new JSONObject()
                                                .put("name", "События Внут")
                                                .put("id", "LOCAL_EVENT")
                                        ).put(new JSONObject()
                                                .put("name", "Реклама")
                                                .put("id", "AD")
                                        ).put(new JSONObject()
                                                .put("name", "Конференции")
                                                .put("id", "CONFERENCE")
                                        ).put(new JSONObject()
                                                .put("name", "Коммуналка")
                                                .put("id", "COMMUNAL_PAY")
                                        ).put(new JSONObject()
                                                .put("name", "Уборка")
                                                .put("id", "CLEANING")
                                        ).put(new JSONObject()
                                                .put("name", "Чай/Печенье/Вода")
                                                .put("id", "COFFEE_TEA")
                                        ).put(new JSONObject()
                                                .put("name", "Налоги сотрудники")
                                                .put("id", "EMPLOYEE_TAX")
                                        ).put(new JSONObject()
                                                .put("name", "Бухгалтер")
                                                .put("id", "ACCOUNTANT")
                                        ).put(new JSONObject()
                                                .put("name", "Английский")
                                                .put("id", "ENGLISH")
                                        ).put(new JSONObject()
                                                .put("name", "Взносы Head Office")
                                                .put("id", "HEAD_OFFICE_TAX")
                                        ).put(new JSONObject()
                                                .put("name", "Разное")
                                                .put("id", "OTHER")
                                        ).put(new JSONObject()
                                                .put("name", "Банковское Обслуживание")
                                                .put("id", "BANK_TAX")
                                        ).put(new JSONObject()
                                                .put("name", "ЗП")
                                                .put("id", "SALARY")
                                        ).put(new JSONObject()
                                                .put("name", "Обслуживание офиса")
                                                .put("id", "OFFICE_SERVICE")
                                        ).put(new JSONObject()
                                                .put("name", "Обучение/развитие")
                                                .put("id", "EDUCATION")
                                        ).put(new JSONObject()
                                                .put("name", "Внутренний перевод")
                                                .put("id", "TRANSFER_LOCAL")
                                        ).put(new JSONObject()
                                                .put("name", "Продажа")
                                                .put("id", "SALE")
                                        ).put(new JSONObject()
                                                .put("name", "Контракт")
                                                .put("id", "CONTRACT")
                                        ).put(new JSONObject()
                                                .put("name", "Начисление бонусов и процентов")
                                                .put("id", "BONUSES")
                                        ).put(new JSONObject()
                                                .put("name", "Продажа оборудования")
                                                .put("id", "TECH_SALE")
                                        ).put(new JSONObject()
                                                .put("name", "Распределения")
                                                .put("id", "DISTRIBUTIONS_PAY")
                                        )
                                )
                        )
                )
                .shouldNotify(false)
                .build();
    }
}
