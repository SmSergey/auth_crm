package com.exceedit.auth.utils.crypto;

import com.exceedit.auth.utils.crypto.Algorithms;
import com.exceedit.auth.web.controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

@Service
public class HashHelper {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private static MessageDigest mdClient;

    HashHelper() {
        try {
            mdClient = MessageDigest.getInstance(Algorithms.MD5);
        } catch (NoSuchAlgorithmException err) {
            logger.error("Couldn't create md digest , error - " + err.getMessage());
        }
    }

    public static String getMD5Hash(String text) {
        mdClient.update(text.getBytes(StandardCharsets.UTF_8));
        return new String(mdClient.digest()).toUpperCase(Locale.ROOT);
    }
}
