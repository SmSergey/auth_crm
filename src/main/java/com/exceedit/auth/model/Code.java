package com.exceedit.auth.model;

import com.exceedit.auth.utils.crypto.HashHelper;
import lombok.Getter;
import lombok.val;

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
        this.codeString =  HashHelper.getMD5Hash(new String(rawCodeArray));
    }
}
