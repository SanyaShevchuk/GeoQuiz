package com.bignerdranch.android.gepquiz;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import static com.bignerdranch.android.gepquiz.Constants.*;


public class QuizActivity extends AppCompatActivity {
    public static Toast makeText(Context context, int resId, int duration) {
        return null;
    }
    private int points;
    private Button mTrueButton;
    private Button  mFalseButton;
    private TextView mQuestionTextView;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mCheatButton;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG_QuizActivity, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mTrueButton = (Button) findViewById(R.id.true_button);
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mNextButton = (ImageButton) findViewById(R.id.next_button);

        if(savedInstanceState!=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            points = savedInstanceState.getInt(SCORE,0);
            boolean visibilities [] = savedInstanceState.getBooleanArray(INVISIBILITY);
            boolean cheaters[] = savedInstanceState.getBooleanArray(IS_CHEATER);
            for(int i = 0; i < mQuestionBank.length; i++){
                mQuestionBank[i].answerButtonEnable = visibilities[i];
                mQuestionBank[i].isCheater = cheaters[i];
            }
            mFalseButton.setEnabled(mQuestionBank[mCurrentIndex].answerButtonEnable);
            mTrueButton.setEnabled(mQuestionBank[mCurrentIndex].answerButtonEnable);
        }
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        updateQuestion();

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT );
            }
        });

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCurrentIndex > 0) mCurrentIndex = (mCurrentIndex - 1);
                    else mCurrentIndex = mQuestionBank.length - mCurrentIndex - 1;
                updateQuestion();
                }
            });

        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                mCurrentIndex = (mCurrentIndex+1)%mQuestionBank.length;
                updateQuestion();
            }
        });
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;

        if(mQuestionBank[mCurrentIndex].isCheater){
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerTrue) {
                messageResId = R.string.correct_toast;
                points++;
            } else messageResId = R.string.incorrect_toast;
        }
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            mQuestionBank[mCurrentIndex].answerButtonEnable = false;

            isEnd();
    }
    private void updateQuestion()
    {
        try{
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        }
        catch(ArrayIndexOutOfBoundsException ex) {
            Log.e(TAG_QuizActivity, "Index was out of bounds", ex);
        }
        if(mQuestionBank[mCurrentIndex].answerButtonEnable)
        {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }else {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        }
    }

    private void isEnd(){
        boolean isEnd = true;
        for(int i=0; i < mQuestionBank.length; i++)
            if(mQuestionBank[i].answerButtonEnable)
                isEnd = false;

        if(isEnd){
            String str;
            if(points==mQuestionBank.length) str = "You are awesome!!! Your score is ";
            else if(points == mQuestionBank.length -1) str = "Good result!! Your score is ";
            else if(points == mQuestionBank.length -2) str = "Nice attempt! Your score is ";
            else str = "Game is over! Your score is ";
            AlertDialog.Builder myalert = new AlertDialog.Builder(this);
            myalert.setMessage(str+ points+"/" + mQuestionBank.length)
                    .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Restart();
                        }
                    })
                    .setTitle("GeoQuiz")
                    .setIcon(R.drawable.whowanna)
                    .create();
            myalert.show();
            Restart();
        }
    }

    private void Restart(){
        countHint=3;
        mCurrentIndex = 0;
        for(int i=0; i < mQuestionBank.length; i++) {
            mQuestionBank[i].answerButtonEnable = true;
        }
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
            points=0;
            updateQuestion();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG_QuizActivity,"onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG_QuizActivity,"onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG_QuizActivity,"onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG_QuizActivity, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        boolean visibilities [] = new boolean[mQuestionBank.length];
        boolean cheaters[] = new boolean[mQuestionBank.length];
        for(int i =0; i < mQuestionBank.length; i++) {
            visibilities[i] = mQuestionBank[i].answerButtonEnable;
            cheaters[i] = mQuestionBank[i].isCheater;
        }
        savedInstanceState.putBooleanArray(INVISIBILITY, visibilities);
        savedInstanceState.putBooleanArray(IS_CHEATER, cheaters);
        savedInstanceState.putInt(SCORE, points);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG_QuizActivity,"onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG_QuizActivity, "onDestroy() called");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode!= Activity.RESULT_OK){
            return;
        } else if(requestCode!=REQUEST_CODE_CHEAT){
            if(data!=null) {
                return;
            }
        }
        mQuestionBank[mCurrentIndex].isCheater = CheatActivity.wasAnswerShown(data);
    }

}