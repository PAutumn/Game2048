package com.example.administrator.project1_2048.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.project1_2048.R;
import com.example.administrator.project1_2048.application.MyApplication;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener{
    String lines;
    String target;
    private String tag = "OptionsActivity:";
    private Button button_optionsActivity_gameLine;
    private Button button_optionsActivity_target;
    private Button button_optionsActivity_back;
    private Button button_optionsActivity_confirm;
    private SharedPreferences sharedPreferences;
    String [] targetNumbers={"1024","2048","4096"};
    String [] linesNumbers={"4","5","6"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        //找到四个button设置点击事件
        button_optionsActivity_gameLine = (Button) findViewById(R.id.button_optionsActivity_gameLine);
        button_optionsActivity_target = (Button) findViewById(R.id.button_optionsActivity_target);
        button_optionsActivity_back = (Button) findViewById(R.id.button_optionsActivity_back);
        button_optionsActivity_confirm = (Button) findViewById(R.id.button_optionsActivity_confirm);

        //设置butoon点击监听事件
        button_optionsActivity_gameLine.setOnClickListener(this);
        button_optionsActivity_target.setOnClickListener(this);
        button_optionsActivity_back.setOnClickListener(this);
        button_optionsActivity_confirm.setOnClickListener(this);

        //从sharepreference中获取用户设置
        MyApplication myApplication = (MyApplication) getApplication();
        sharedPreferences = myApplication.sharedPreferences;
        if(sharedPreferences !=null){
            lines = sharedPreferences.getString("lines","4");
            target = sharedPreferences.getString("target","2048");
            Log.i(tag,lines+target+"first");
            //控件setText传入int型系统会在r类中寻找
            button_optionsActivity_gameLine.setText(lines);
            button_optionsActivity_target.setText(target);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button_optionsActivity_gameLine:
                setLine();
                break;
            case R.id.button_optionsActivity_target:
                setTarget();
                break;
            case R.id.button_optionsActivity_back:
                //返回主页面，并destory设置页面
               // startActivity(new Intent(this, MainActivity.class));
                OptionsActivity.this.finish();
                break;
            case R.id.button_optionsActivity_confirm:
                confirm();
                break;
        }
    }

    private void confirm() {
        //保存信息到sharePreference中
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("lines",lines);
        edit.putString("target",target);
        edit.commit();
        resultBack();
        finish();
    }

    private void resultBack() {
        Intent intent = new Intent();
        intent.putExtra("lines",lines);
        intent.putExtra("target",target);
    }

    //设置分数页面交互
    private void setTarget() {
        //单选dialog
        //Integer.parseInt(target)优化选项体验
        int checkedNumber =1;
        switch (Integer.parseInt(target)){
            case 1024:
                checkedNumber=0;
                break;
            case 2048:
                checkedNumber=1;
                break;
            case 4096:
                checkedNumber=2;
                break;
        }
        Log.i(tag,target);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择最高分数")
                .setSingleChoiceItems(targetNumbers, checkedNumber, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        target = targetNumbers[which]+"";
                        button_optionsActivity_target.setText(targetNumbers[which]);
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void setLine() {
        //如果为int型 系统会优先找r类的资源id，出现找不到的情况
        Log.i(tag,lines+"fff");
        int checkedNumber =0;
        switch (Integer.parseInt(target)){
            case 4:
                checkedNumber=0;
                break;
            case 5:
                checkedNumber=1;
                break;
            case 6:
                checkedNumber=2;
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择棋盘的行列数")
                .setSingleChoiceItems(linesNumbers,checkedNumber,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lines = linesNumbers[which]+"";
                        button_optionsActivity_gameLine.setText(linesNumbers[which]);
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
