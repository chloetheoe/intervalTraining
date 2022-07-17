package com.intervals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Spinner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    //-1 is minus, 0 is plus and 1 is plusminus
    var plusMinus = 0
    var octaveNumber = 0

    fun getSpinnerValues(){
        var plusMinusSpinner: Spinner = findViewById(R.id.spinnerPlusMinus);
        var octaveSpinner: Spinner = findViewById(R.id.spinnerOctaves);
        when(plusMinusSpinner.selectedItem.toString()){
            "-" -> plusMinus = 0;
            "+" -> plusMinus = 1;
            "±" -> plusMinus = 2;
        }
    }

    fun startTraining(view: View){
        getSpinnerValues();
        val intent = Intent(this, Training::class.java).apply {
            putExtra("plusMinus", plusMinus);
            putExtra("octave", octaveNumber);
        }
        startActivity(intent)
    }
}