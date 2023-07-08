package com.example.skysiren

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.example.skysiren.Model.Icons
import com.example.skysiren.databinding.ActivityAlertPopUpBinding

class AlertPopUpActivity : AppCompatActivity() {
    private lateinit var description: String
    private lateinit var icon: String
    lateinit var binding: ActivityAlertPopUpBinding
    var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("TAG", "onCreate:  alert popup activity")
        binding = ActivityAlertPopUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        description = intent.getStringExtra("desc").toString()
        icon = intent.getStringExtra("icon").toString()

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setFinishOnTouchOutside(false)
        setUiAndSound()
    }

    private fun startSound() {
        if (mMediaPlayer == null) { //mMediaPkayer is your variable
            mMediaPlayer = MediaPlayer.create(
                this,
                R.raw.alert_sound
            ) //raw is the folder where you have the audio files or sounds, water is the audio file (is a example right)
            mMediaPlayer!!.isLooping = true //to repeat again n again
            mMediaPlayer!!.start() //to start the sound
        }
    }

    private fun setUiAndSound() {
        Log.i("TAG", "setUiAndSound: souuuuuuuund")
        startSound()
        Icons.replaceIcons(icon, binding.image)
        binding.alertDesc.text = description
        binding.btnDismiss.setOnClickListener {
            mMediaPlayer!!.stop()
            this.finish()
        }
    }
}
