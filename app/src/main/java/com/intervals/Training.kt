package com.intervals

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class Training : AppCompatActivity() {
    //-1 is minus, 0 is plus and 1 is plusminus
    var plusMinus = 0;
    var octave = 0;
    val length = 500;
    var random = Random();
    val intervalMap = mapOf<Int, String>(0 to "Min 2nd", 1 to "Maj 2nd", 2 to "Min 3rd", 3 to "Maj 3rd", 4 to "4th", 5 to "5th", 6 to "Min 6th", 7 to "Maj 6th", 8 to "Min 7th", 9 to "Maj 7th");
    val intervalSemitoneMap = mapOf<Int,Int>(0 to 1, 1 to 2, 2 to 3, 3 to 4, 4 to 5, 5 to 7, 6 to 8, 7 to 9, 8 to 10, 9 to 11);
    var currInterval = "";
    var firstFreq = 0.0;
    var secondFreq = 0.0;

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        plusMinus = intent.getIntExtra("plusMinus", 0);
        octave = intent.getIntExtra("octave", 0);
        randomizeAndSet();
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun randomizeAndSet() {
        val currIntervalNumber = random.nextInt(9);
        currInterval = intervalMap.getOrDefault(currIntervalNumber, "");
        twoSounds(currIntervalNumber);
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun twoSounds(intervalNumber: Int){
        val semitones = intervalSemitoneMap.getOrDefault(intervalNumber, 1);
        val offset = random.nextInt(11);
        // returns 1 or -1
        var offsetSignal = random.nextInt(1)*2-1;

        //randomize first frequency
        firstFreq = offsetFrequency(440.0,offset*offsetSignal);

        //randomize second frequency
        if(plusMinus == 0 || plusMinus == 1){
            offsetSignal = plusMinus*2-1;
        } else {
            offsetSignal = random.nextInt(1)*2-1;
        }
        secondFreq = offsetFrequency(firstFreq, semitones*offsetSignal);

        playTones();
    }

    fun playTones() {
        val tone1 = generateTone(firstFreq, length);
        val tone2 = generateTone(secondFreq, length);
        if (tone1 != null && tone2 != null) {
            tone1.play();
            Thread.sleep(length.toLong());
            tone2.play();
        } else {
            Log.i("Tones not found", "tones not found");
        }
    }

    fun offsetFrequency(baseFreq: Double, semitones: Int): Double {
        val retVal = baseFreq*Math.pow(Math.pow(2.0,1/12.0), semitones.toDouble());
        return retVal;
    }

    //stole from https://gist.github.com/slightfoot/6330866
    // Usage:
    //    AudioTrack tone = generateTone(440, 250);
    //    tone.play();
    private fun generateTone(freqHz: Double, durationMs: Int): AudioTrack {
        val count = (44100.0 * 2.0 * (durationMs / 1000.0)).toInt() and 1.inv();
        val samples = ShortArray(count);
        var i = 0;
        while (i < count) {
            val sample = (Math.sin(2 * Math.PI * i / (44100.0 / freqHz)) * 0x7FFF).toInt().toShort();
            samples[i + 0] = sample;
            samples[i + 1] = sample;
            i += 2;
        }
        val track = AudioTrack(
            AudioManager.STREAM_MUSIC, 44100,
            AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
            count * (java.lang.Short.SIZE / 8), AudioTrack.MODE_STATIC
        )
        track.write(samples, 0, count)
        return track
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkGuess(view: View){
        val but: Button = view as Button
        //Log.i("button interval", but.text.toString())
        //Log.i("current interval", currInterval)
        if(currInterval.equals(but.text)){
            // TODO()
            //put in like a green checkmark or smth
            randomizeAndSet();
        } else {
            // TODO()
            //Put in like a red checkmark or smth
        }
    }

    fun replayButton(view: View){
        playTones();
    }
}