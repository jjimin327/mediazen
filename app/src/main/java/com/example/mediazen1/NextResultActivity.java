package com.example.mediazen1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class NextResultActivity extends AppCompatActivity {
    List <String> titleList = new ArrayList<>();

    int areaCode;
    int contentTypeId;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_result);

        //------------------------ 음성 텍스트 받아오기 -----------------------------
        Bundle intent = getIntent().getExtras();
        text = intent.getString("TEXT");

        //areaCode
        if(text.contains("서울")){
            areaCode = 1;
        }else if(text.contains("인천")){
            areaCode = 2;
        }else if(text.contains("대전")){
            areaCode = 3;
        }else if(text.contains("대구")){
            areaCode = 4;
        }else if(text.contains("광주")){
            areaCode = 5;
        }else if(text.contains("부산")){
            areaCode = 6;
        }else if(text.contains("울산")) {
            areaCode = 7;
        }else if(text.contains("세종")){
            areaCode = 8;
        }else if(text.contains("경기")){
            areaCode = 31;
        }else if(text.contains("강원")){
            areaCode = 32;
        }else if(text.contains("충청북도")){
            areaCode = 33;
        }else if(text.contains("충청남도")){
            areaCode = 34;
        }else if(text.contains("경상북도")){
            areaCode = 35;
        }else if(text.contains("경상남도")){
            areaCode = 36;
        }else if(text.contains("전라북도")){
            areaCode = 37;
        }else if(text.contains("전라남도")){
            areaCode = 38;
        }else if(text.contains("제주도")){
            areaCode = 39;
        }

        //
        if(text.contains("명소") || text.contains("관광")){
            contentTypeId=12;
        }else if(text.contains("문화")){
            contentTypeId=14;
        }else if(text.contains("축제") || text.contains("공연") || text.contains("행사") ){
            contentTypeId=15;
        }else if(text.contains("레포츠")){
            contentTypeId=28;
        }


        // -------------------- xml 파싱 부분 ------------------


        StrictMode.enableDefaults();

        TextView status1 = (TextView)findViewById(R.id.title); //파싱된 결과확인!

        boolean initem = false;
        boolean inTitle = false, inAddr1 = false, inFirstimage = false;
        String title = null, addr1 = null, firstimage = null;


        try{
            URL url = new URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?"
                    + "serviceKey="
                    + "z%2FmESmOcmrPqKTi%2B6OvWUMdhyBXMvcfyEasPMnDyxKSDNnBAQGj95%2BFvPZHEeCIrSI%2FMWR7Xo4WkgSqoG%2FHZaA%3D%3D"
                    + "&pageNo=1&numOfRows=30&MobileApp=AppTest&MobileOS=AND&arrange=A&cat1=&" +
                    "contentTypeId=" + contentTypeId +
                    "&areaCode=" + areaCode +
                    "&sigunguCode=" +
                    "&cat2=&cat3=&listYN=Y&modifiedtime=&"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");


            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("title")){ //title 만나면 내용을 받을수 있게 하자
                            inTitle = true;
                        }
                        if(parser.getName().equals("addr1")){ //address 만나면 내용을 받을수 있게 하자
                            inAddr1 = true;
                        }
                        if(parser.getName().equals("firstimage")){ //mapx 만나면 내용을 받을수 있게 하자
                            inFirstimage = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            status1.setText(status1.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(inAddr1){ //isTitle이 true일 때 태그의 내용을 저장.
                            addr1 = parser.getText();
                            inAddr1 = false;
                        }
                        if(inTitle){ //isAddress이 true일 때 태그의 내용을 저장.
                            title = parser.getText();
                            inTitle = false;
                        }
                        if(inFirstimage){ //isMapy이 true일 때 태그의 내용을 저장.
                            firstimage = parser.getText();
                            inFirstimage = false;
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("item")){
                            status1.setText(status1.getText()+"\n"+"\n title: "+ title +"\n address : "+ addr1 +"\n image : " + firstimage + "\n");
                            initem = false;
                        }
                        break;
                }

                titleList.add(title);

                parserEvent = parser.next();
            }


        } catch(Exception e){
            status1.setText("에러가..났습니다...");
        }
    }

}
