package com.alamedapps.questionsandanswers.dto;

import java.util.List;

public class QuestionsAndAnswers {

    private Integer userId;
    private String userName;
    private Integer questionId;
    private String questionContent;
    private List<AnswerUser> answerUserList;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<AnswerUser> getAnswerUserList() {
        return answerUserList;
    }

    public void setAnswerUserList(List<AnswerUser> answerUserList) {
        this.answerUserList = answerUserList;
    }
}
