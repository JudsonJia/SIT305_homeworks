package com.example.task61d;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;


public class Question {
    @SerializedName("question")
    private String question;
    @SerializedName("options")
    private List<String> options;
    @SerializedName("correct_answer")
    private String correct_answer;
    private String user_answer;

    private String time;

    public Question() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    private String topic;


    public Question(String questionText, List<String> options, String correctAnswer) {
        this.question = questionText;
        this.options = options;
        this.correct_answer = correctAnswer;
    }

    public String getQuestionText() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correct_answer;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public int getCorrectOptionIndex() {
        switch (correct_answer) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
            default:
                return -1;
        }
    }

    public int getUserAnswerIndex(){
        if(Objects.equals(user_answer, options.get(0))){
            return 0;
        } else if (Objects.equals(user_answer, options.get(1))) {
            return 1;
        }else if (Objects.equals(user_answer, options.get(2))) {
            return 2;
        }else if (Objects.equals(user_answer, options.get(3))) {
            return 2;
        }
        return 0;
    }

    public String getUserAnswer(){
        if(Objects.equals(user_answer, options.get(0))){
            return "A";
        } else if (Objects.equals(user_answer, options.get(1))) {
            return "B";
        }else if (Objects.equals(user_answer, options.get(2))) {
            return "C";
        }else if (Objects.equals(user_answer, options.get(3))) {
            return "D";
        }
        return "";
    }
}