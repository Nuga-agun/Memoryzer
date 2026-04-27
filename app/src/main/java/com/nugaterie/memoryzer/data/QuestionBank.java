package com.nugaterie.memoryzer.data;

import java.util.Arrays;
import java.util.List;

public class QuestionBank {
    private static QuestionBank instance;
    public List<Question> getDefaultQuestions() {
        return Arrays.asList(
                new Question("SSH", "20"),
                new Question("HTTP", "80"),
                new Question("FTP (control)", "21"),
                new Question("FTP (data)", "20")
        );
    }

    public static QuestionBank getInstance() {
        if (instance == null) {
            instance = new QuestionBank();
        }
        return instance;
    }

}
