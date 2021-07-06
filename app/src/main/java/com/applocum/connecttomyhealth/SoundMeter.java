package com.applocum.connecttomyhealth;

import android.media.MediaRecorder;

import java.io.IOException;

public class SoundMeter {
    MediaRecorder mediaRecorder = null;

    public void start()
    {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile("/dev/null");

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stop()
    {
        if (mediaRecorder!= null)
        {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public double getAmplitude()
    {
        try {
            if (mediaRecorder != null)
            {
                return mediaRecorder.getMaxAmplitude();
            }else {
                return 0.0;
            }
        }catch (Exception e)
        {
            return 0.0;
        }
    }
}
