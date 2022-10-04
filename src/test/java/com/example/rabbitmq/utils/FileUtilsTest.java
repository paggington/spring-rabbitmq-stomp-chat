package com.example.rabbitmq.utils;

import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void loadFileToDirectory() {
        Long timeStart = System.currentTimeMillis();
        FileUtils.findAnotherMessageFiles("FIF");
        System.out.println((System.currentTimeMillis() - timeStart)/1000.0);
    }
}