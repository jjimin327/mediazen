package com.example.mediazen1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageView btn_imageView_mic;
    ImageView btn_play;
    Intent intent;

    SpeechRecognizer mRecognizer;
    //ImageButton voice;
    TextView text;
    final int PERMISSION=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_imageView_mic = findViewById(R.id.btn_imageView_mic);
        btn_imageView_mic.setClickable(true);

        btn_play = findViewById(R.id.btn_play);
        btn_play.setClickable(true);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NextResultActivity.class);

                final String TEXT = text.getText().toString();
                intent.putExtra("TEXT", TEXT);

                startActivity(intent);
            }
        });

        //--------------------------------stt----------------------------------------------------
        //퍼미션 설정
        if(Build.VERSION.SDK_INT >= 21){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        text=(TextView)findViewById(R.id.text);
        btn_imageView_mic=(ImageView)findViewById(R.id.btn_imageView_mic);

        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //사용자에게 음성 요구, 음성 인식기를 통해 전송
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName()); //음성 인식을 위한 음성 인식기의 의도에 사용되는 여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR"); //음성을 번역할 언어 설정

        btn_imageView_mic.setOnClickListener(v ->{
            //SpeechRecognizer 클래스 : 구글의 STT API를 쓸 수 있는 클래스
            mRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
            mRecognizer.setRecognitionListener(listener); //콜백 listener(RecognitionListener) 설정
            mRecognizer.startListening(intent);
        });
    }
    private RecognitionListener listener=new RecognitionListener() {

        //startListening 메서드가 호출되면 실행, 음성을 들을 준비가 되었다는 의미
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Toast.makeText(getApplicationContext(),"음성 인식 시작",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        //새로운 음성이 들어왔을 때 호출
        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        //음성이 끝났을 때 호출, 이 콜백 다음의 인식 결과에 따라 onError() 또는 onResults()가 호출됨
        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
            String message;
            switch(i){
                case SpeechRecognizer.ERROR_AUDIO:
                    message="오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message="클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message="퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message="네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message="네트워크 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message="찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message="RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message="서버 오류";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message="말하는 시간 초과";
                    break;
                default:
                    message="알 수 없는 오류";
                    break;
            }
            Toast.makeText(getApplicationContext(),"에러가 발생하였습니다. : "+message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle bundle) {
            ArrayList<String> matches=
                    bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            text.setText(matches.get(0));
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

}
