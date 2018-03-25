package com.bignerdranch.android.gepquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import static com.bignerdranch.android.gepquiz.Constants.*;

public class CheatActivity extends AppCompatActivity {

    private boolean mAnswerIsTrue;
    private boolean isCheater;
    private Button mShowAnswerButton;
    private TextView mShowAnswerTextView;
    private TextView mShowApiVersionTextView;
    private TextView mShowHintTextView;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Intent intent = new Intent(packageContext,CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mShowApiVersionTextView = (TextView) findViewById(R.id.api_version);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);
        mShowHintTextView = (TextView) findViewById(R.id.hint);

        mShowApiVersionTextView.setText("API level is " + Build.VERSION.SDK  + "\nRelease : " + Build.VERSION.RELEASE);
        showCountOfHints();

        if(savedInstanceState!=null) {
            isCheater = savedInstanceState.getBoolean(IS_CHEATER);
            countHint = savedInstanceState.getInt(COUNT_HINT);
            setAnswerShownResult(savedInstanceState.getBoolean(IS_CHEATER, false));
            if(isCheater) {
                if (getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)) {
                    mShowAnswerTextView.setText("True");
                } else mShowAnswerTextView.setText("False");
                mShowAnswerButton.setEnabled(false);
            }
        }


        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countHint--;
                if(mAnswerIsTrue){
                    mShowAnswerTextView.setText(R.string.true_button);
                }
                else mShowAnswerTextView.setText(R.string.false_button);
                isCheater=true;
                setAnswerShownResult(true);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else{
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
                showCountOfHints();
            }
        });
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }

    public void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        Log.i(TAG_CheatActivity, "onSaveInstanceState");
        saveInstanceState.putBoolean(IS_CHEATER, isCheater );
        saveInstanceState.putInt(COUNT_HINT, countHint);

    }
    public void showCountOfHints(){
        if(countHint>1)
            mShowHintTextView.setText("You have only " + countHint + " hints");
        else if (countHint==1) mShowHintTextView.setText("You have only " + countHint + " hint");
        else {
            mShowHintTextView.setText("You have only " + countHint + " hint");
            mShowAnswerButton.setEnabled(false);
        }
    }
}
