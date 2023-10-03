package com.example.tts

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var textToSpeech: TextToSpeech
    private lateinit var editText: EditText
    private lateinit var spinner: Spinner
    private lateinit var speakButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editText)
        spinner = findViewById(R.id.spinner)
        speakButton = findViewById(R.id.speakButton)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Handle language initialization error
                } else {
                    // Language initialization succeeded
                    populateVoiceSpinner()
                    speakButton.isEnabled = true
                    speakButton.isActivated = false
                }
            } else {
                // TextToSpeech initialization failed
            }
        }

        speakButton.setOnClickListener {
            val text = editText.text.toString()
            val selectedVoice = spinner.selectedItem.toString()
            textToSpeech.voice =
                textToSpeech.voices.find { voice -> "${voice.name} (${voice.locale.language}-${voice.locale.country})" == selectedVoice }
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

        }
    }

    private fun populateVoiceSpinner() {
        val voiceNames = textToSpeech.voices.map { voice ->
            "${voice.name} (${voice.locale.language}-${voice.locale.country})"
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, voiceNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}