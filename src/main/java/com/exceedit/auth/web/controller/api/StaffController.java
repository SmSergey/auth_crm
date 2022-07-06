package com.exceedit.auth.web.controller.api;


import com.exceedit.auth.web.controller.api.response.ApiResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/staff")
public class StaffController {

    private final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @RequestMapping(path = "", method = {RequestMethod.OPTIONS, RequestMethod.GET})
    public ResponseEntity<String> getStaff() {
        return new ApiResponse()
                .setStatus(200)
                .shouldNotify(false)
                .addField("data", new JSONObject()
                        .put("items", new JSONArray())
                ).build();
    }

}
