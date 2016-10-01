package com.example.administrator.project1_2048.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.project1_2048.R;
import com.example.administrator.project1_2048.application.MyApplication;
import com.example.administrator.project1_2048.view.GameView;
//设置主页面的ui，分为三部分
//1，分数栏
//2，游戏区
//3. 按钮区

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String tag = "项目一：";
    private TextView tv_score;
    private TextView tv_record;
    private GameView gameView;
    private TextView tv_maintitle;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //函数顺序执行，下面函数用的成员变量，最好先初始化，否则出现空指针异常
        //查找到分数显示控件
        tv_score = (TextView) findViewById(R.id.tv_MainActivity_score);
        tv_record = (TextView) findViewById(R.id.tv_MainActivity_record);
        tv_maintitle = (TextView) findViewById(R.id.tv_maintitle);
        Log.i(tag,tv_score+"fenshu"+tv_record);

        RelativeLayout rl_checkerboard = (RelativeLayout) findViewById(R.id.rl_checkerboard);
        gameView = new GameView(this);
        gameView.setBackgroundColor(Color.GRAY);
        rl_checkerboard.addView(gameView);

        //找到底部三个button
        Button button_revert =(Button) findViewById(R.id.button_revert);
        Button button_Restart =(Button)findViewById(R.id.button_Restart);
        Button button_Options =(Button)findViewById(R.id.button_Options);

        //为三个设置点击事件
        button_revert.setOnClickListener(MainActivity.this);
        button_Restart.setOnClickListener(this);
        button_Options.setOnClickListener(this);

        //获得sharePreference中的数据，不能在activity里设置，因为new GameView时需要初始化类
        MyApplication myApplicationpplication = (MyApplication) MainActivity.this.getApplication();
        sharedPreferences = myApplicationpplication.sharedPreferences;
        String target = sharedPreferences.getString("target", "2048");
        tv_maintitle.setText(target);
        gameView.setMaxNumber(Integer.parseInt(target));


    }
    public void setScore(int number){
        Log.i(tag,number+"");
        tv_score.setText(number+"");
    }
    public void setRecord(String number){
        Log.i(tag,number+"ppp");
       tv_record.setText(number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_revert:
                main_revert();
                break;
            case R.id.button_Restart:
                main_restart();
                break;
            case R.id.button_Options:
                main_options();
                break;
        }
    }


    private void main_options() {
        startActivityForResult(new Intent(this, OptionsActivity.class),100);
    }

    private void main_restart() {
        Log.i(tag,"main_restart");
        gameView.restart();
    }

    private void main_revert() {
        //退到上一步
        gameView.revert();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(tag,requestCode+"");
        if(requestCode==100){
            String target = sharedPreferences.getString("target", "2048");
            String lines = sharedPreferences.getString("lines", "2048");
            tv_maintitle.setText(target);
            gameView.setColumns_number(Integer.parseInt(lines));
            gameView.setRow_number(Integer.parseInt(lines));
            gameView.setMaxNumber(Integer.parseInt(target));
            gameView.getInfoFromShare();
            gameView.init(this);
        }
    }
}
