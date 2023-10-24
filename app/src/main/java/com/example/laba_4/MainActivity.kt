package com.example.laba_4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_RESULT = "result"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private val quizViewModel: QuizViewModel by
    lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        val result_User = savedInstanceState?.getInt(KEY_RESULT, 0)?:0
        quizViewModel.currentIndex = currentIndex
        quizViewModel.result = result_User
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        trueButton.setOnClickListener {
            checkAnswer(true)
            trueButton.visibility= View.INVISIBLE
            falseButton.visibility= View.INVISIBLE
            if (quizViewModel.currentIndex == quizViewModel.questionBank.size - 1){
                nextButton.visibility= View.INVISIBLE
                Toast.makeText(this, "Your result = " + quizViewModel.result, Toast.LENGTH_SHORT).show()
            }
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
            trueButton.visibility= View.INVISIBLE
            falseButton.visibility= View.INVISIBLE
            if (quizViewModel.currentIndex == quizViewModel.questionBank.size - 1){
                nextButton.visibility= View.INVISIBLE
                Toast.makeText(this, "Your result = " + quizViewModel.result, Toast.LENGTH_SHORT).show()
            }
        }
        nextButton.setOnClickListener {
            trueButton.visibility= View.VISIBLE
            falseButton.visibility= View.VISIBLE
            quizViewModel.moveToNext()
            if(quizViewModel.currentIndex == quizViewModel.questionBank.size - 1){
                nextButton.visibility= View.INVISIBLE
            }
            updateQuestion()
        }
        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }
        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK){
            return
        }
        if (resultCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN,false)?:false
        }
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putInt(KEY_RESULT, quizViewModel.result)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId =  when{
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        if (userAnswer == correctAnswer){
            quizViewModel.result += 1
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}
