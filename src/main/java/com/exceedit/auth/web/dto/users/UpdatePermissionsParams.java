package com.exceedit.auth.web.dto.users;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.json.JSONObject;

@Data
public class UpdatePermissionsParams {

    private String _id;

    private String team;

    private PermissionList permissionsList;


    @Data
    public static class PermissionList {
        @JsonProperty("GLOBAL")
        private int GLOBAL;

        @JsonProperty("BA")
        private int BA;

        @JsonProperty("CONTRACTS")
        private int CONTRACTS;

        @JsonProperty("FM")
        private int FM;

        @JsonProperty("STAFF")
        private int STAFF;

        @JsonProperty("T")
        private int T;

        @JsonProperty("TR")
        private int TR;

        @JsonProperty("TR_CAT")
        private int TR_CAT;

        @JsonProperty("AN")
        private int AN;

        @JsonProperty("R")
        private int R;

        @Override
        public String toString() {
            return new JSONObject()
                    .put("GLOBAL", this.GLOBAL)
                    .put("BA", this.BA)
                    .put("CONTRACTS", this.CONTRACTS)
                    .put("FM", this.FM)
                    .put("STAFF", this.STAFF)
                    .put("T", this.T)
                    .put("TR", this.TR)
                    .put("TR_CAT", this.TR_CAT)
                    .put("AN", this.AN)
                    .put("R", this.R).toString();
        }
    }

}
