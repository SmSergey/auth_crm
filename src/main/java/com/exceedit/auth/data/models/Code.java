package com.exceedit.auth.data.models;

import com.exceedit.auth.utils.crypto.HashHelper;
import lombok.Getter;
import lombok.val;
import org.bson.internal.Base64;

import java.util.Random;


public class Code {

    @Getter
    private String codeString;

    public String getLastGeneratedCode() {
        return this.codeString;
    }

    public Code() {
        this.generateCode();
    }

    public void generateCode() {
        val rawCodeArray = new byte[20];
        new Random().nextBytes(rawCodeArray);
        this.codeString = Base64.encode(rawCodeArray)
                .replace("+", "");
    }
}
