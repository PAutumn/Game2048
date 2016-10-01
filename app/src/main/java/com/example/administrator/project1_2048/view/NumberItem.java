package com.example.administrator.project1_2048.view;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/8/1 0001.
 */
public class NumberItem extends FrameLayout{

    private  TextView tv_numberItem;
    private  int number;

    public NumberItem(Context context) {
        super(context);
        //在fragment里动态加入一个textView
        addFrameLayout(context);
    }

    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    //动态加入一个子控件
    private void addFrameLayout(Context context) {

        tv_numberItem = new TextView(context);
        tv_numberItem.setGravity(Gravity.CENTER);
        tv_numberItem.setTextSize(20);


        /*textView.setText(3+"");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLUE);
        textView.setBackgroundColor(Color.GRAY);*/

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5,5,5,5);

        this.addView(tv_numberItem,layoutParams);

    }

    //设置单元格的textview的文本内容,并同时维护单元格的number值
    public void setNumber(int number){
        this.number = number;
        switch (number) {
            case 0:
                tv_numberItem.setText("");
                tv_numberItem.setBackgroundColor(0x00000000);
                break;
            case 2:
                tv_numberItem.setText(2+"");
                tv_numberItem.setBackgroundColor(0xFFFFF5EE);
                break;
            case 4:
                tv_numberItem.setBackgroundColor(0xFFFFEC8B);
                tv_numberItem.setText(4+"");
                break;
            case 8:
                tv_numberItem.setBackgroundColor(0xFFFFE4C4);
                tv_numberItem.setText(8+"");
                break;
            case 16:
                tv_numberItem.setBackgroundColor(0xFFFFDAB9);
                tv_numberItem.setText(16+"");
                break;
            case 32:
                tv_numberItem.setBackgroundColor(0xFFFFC125);
                tv_numberItem.setText(32+"");
                break;
            case 64:
                tv_numberItem.setBackgroundColor(0xFFFFB6C1);
                tv_numberItem.setText(64+"");
                break;
            case 128:
                tv_numberItem.setBackgroundColor(0xFFFFA500);
                tv_numberItem.setText(128+"");
                break;
            case 256:
                tv_numberItem.setBackgroundColor(0xFFFF83FA);
                tv_numberItem.setText(256+"");
                break;
            case 512:
                tv_numberItem.setBackgroundColor(0xFFFF7F24);
                tv_numberItem.setText(512+"");
                break;
            case 1024:
                tv_numberItem.setBackgroundColor(0xFFFF6A6A);
                tv_numberItem.setText(1024+"");
                break;
            case 2048:
                tv_numberItem.setBackgroundColor(0xFFFF1493);
                tv_numberItem.setText(2048+"");
                break;
            case 4096:
                tv_numberItem.setBackgroundColor(0xFFFF3030);
                tv_numberItem.setText(4096+"");
                break;


        }
    }
    public int getNumber(){
        return number;
    }
    public void settext(String numberString){

        //传来的string设置textview文本内容
        tv_numberItem.setText(numberString);
    }
}
