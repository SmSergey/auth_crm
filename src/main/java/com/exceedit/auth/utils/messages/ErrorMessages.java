package com.exceedit.auth.utils.messages;

public interface ErrorMessages {
    String BAD_CREDS = "email or password incorrect";
    String BAD_AUTHORIZATION_TOKEN = "access token is not valid";
    String USER_NOT_FOUND = "user wasn't found";
    String INTERNAL_ERROR = "internal server error";
    String TEAM_NOT_FOUND = "team wasn't found";
    String CLIENT_NOT_FOUND = "client wasn't found";

}
