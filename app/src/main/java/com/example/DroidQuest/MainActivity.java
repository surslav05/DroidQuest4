package com.example.DroidQuest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_DECEIT = 0;
    private static final String TAG = "QuestActivity";
    private static final String KEY_INDEX = "index";
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() вызван");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() вызван");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() вызван");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() вызван");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() вызван");
    }
    public Button mTrueButton;
    public Button mFalseButton;
    private ImageButton mNextButton;
    private Button mDeceitButton;
    private ImageButton mBackButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_android, true),
            new Question(R.string.question_linear, false),
            new Question(R.string.question_service, false),
            new Question(R.string.question_res, true),
            new Question(R.string.question_manifest, true),
            new Question(R.string.question_1, true),
            new Question(R.string.question_2, true),
            new Question(R.string.question_3, false),
            new Question(R.string.question_4, true),
            new Question(R.string.question_5, true),
    };
    private int mCurrentIndex = 0;
    private boolean mIsDeceiter;
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue =
                mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsDeceiter) {
            messageResId = R.string.judgment_toast;
        } else {
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }}
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) вызван");
        setContentView(R.layout.activity_main);

        mQuestionTextView =
                (TextView)findViewById(R.id.question_text_view);


        mTrueButton = (Button) findViewById(R.id.true_button);


        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mNextButton = (ImageButton)findViewById(R.id.next_button);
        mBackButton = (ImageButton)findViewById(R.id.back_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsDeceiter = false;
                updateQuestion();
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });
        mDeceitButton = (Button)findViewById(R.id.deceit_button);
        mDeceitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex]
                        .isAnswerTrue();
                Intent i = DeceitActivity.newIntent(MainActivity.this,
                        answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_DECEIT);
            }
        });
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        updateQuestion();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_DECEIT) {
            if (data == null) {
                return;
            }
            mIsDeceiter = DeceitActivity.wasAnswerShown(data);
        }
    }
}