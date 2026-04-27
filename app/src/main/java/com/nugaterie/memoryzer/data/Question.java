package com.nugaterie.memoryzer.data;

public class Question {

    private String questionText;
    private String answer;

    public Question(String questionText, String answer) {
        this.questionText = questionText;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
