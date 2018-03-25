package com.bignerdranch.android.gepquiz;

/**
 * Created by User on 14.02.2018.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    public boolean answerButtonEnable=true;
    public boolean isCheater;


    public Question(int textResId, boolean answerTrue)
    {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
