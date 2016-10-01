package com.example.administrator.project1_2048.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.GridLayout;

import com.example.administrator.project1_2048.activity.MainActivity;
import com.example.administrator.project1_2048.application.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/2 0002.
 */
public class GameView extends GridLayout {

    private int row_number ;
    private int columns_number;
    private int maxNumber ;
    private String TAG = "GrameView:";
    List<Point> blankitemList;
    NumberItem [][] itemMatrix;
    int[][] history_Matrix;
    private boolean noSlide;
    private Context context;
    private MainActivity activity;
    private int current_score ;
    private int current_record ;
    boolean user_direction;
    int history_score;
    private SharedPreferences sharedPreferences;

    public GameView(Context context) {
        super(context);
        //子向上转型
        this.context =context;
        activity =(MainActivity)context;
        this.
        getInfoFromShare();
        init(context);
    }

    public void getInfoFromShare() {
        //获得sharePreference中的数据
        MyApplication myApplicationpplication = (MyApplication) activity.getApplication();
        sharedPreferences = myApplicationpplication.sharedPreferences;
        String lines = sharedPreferences.getString("lines","4");
        String target = sharedPreferences.getString("target", "2048");
        setColumns_number(Integer.parseInt(lines));
        setRow_number(Integer.parseInt(lines));
        Log.i(TAG,lines+"行列数");
    }


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context con) {
        Log.i(TAG,row_number+"uuu"+columns_number);
        //向下转型，本身是Activity（this）
        //
        removeAllViews();

        //显示历史记录分数
        showRecord();

        setColumnCount(row_number);
        setRowCount(columns_number);
        //新建集合存储类，此类包含有x、y坐标
        blankitemList = new ArrayList();
        //新建一个4*4的二维数组,记录哪个fragmentlayout
        itemMatrix = new NumberItem[row_number][columns_number];
        history_Matrix = new int[row_number][columns_number];

        //获得屏幕分辨率 方法二
        WindowManager windowManager = (WindowManager) con.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int window_width = displayMetrics.widthPixels;


        for(int i=0;i<row_number;i++) {
            for (int j = 0; j < columns_number; j++) {

                NumberItem numberItem = new NumberItem(con);
                numberItem.setNumber(0);
                itemMatrix[i][j] = numberItem;

                Point point = new Point();
                point.x = i;
                point.y = j;
                blankitemList.add(point);
                Log.i(TAG,"weizhi1");
               // numberItem.settext();
                addView(numberItem, window_width/columns_number, window_width/columns_number);
            }

        }

        addRandomNumber();
        addRandomNumber();

    }

    private void showRecord() {
        Log.i(TAG,"showRecord");
        current_record = sharedPreferences.getInt("record", 0);
        Log.i(TAG,current_record+"");
        activity.setRecord(current_record+"");
    }

    //随机找一个位置，供第一进入页面使用
    private void addRandomNumber() {

        //获得最新的空位信息
        updataBlanklist();
        //从集合中随机选出一个类
        int sumofBlackList = blankitemList.size();
        //math。floor返回一个小于等于参数，并等于某个整数
        int indexOfRandom = (int) Math.floor( Math.random() * sumofBlackList);
        Log.i(TAG,sumofBlackList+"空位子");
        Log.i(TAG,indexOfRandom+"随机下标");
        //获取的位置为
        Point point = blankitemList.get(indexOfRandom);
        itemMatrix[point.x][point.y].setNumber(2);

    }

    //更新空位 的数据，判断其framelayout维护的number值是否为0
    public void updataBlanklist(){
        //先清除集合的所有point数据
        blankitemList.clear();
        //判断number是否为0
        for(int i =0;i<row_number;i++){
            for(int j =0;j<columns_number;j++){
                if(itemMatrix[i][j].getNumber()==0){
                    Point point = new Point();
                    point.x = i;
                    point.y = j;
                    blankitemList.add(point);
                }
            }
        }
    }


    //设置gridlayout的监听事件，判断用户滑动方向,
    //当用户在gridlayout上有滑动事件，就调用onTouchEvent(）
    int start_x;
    int start_y;
    int end_x;
    int end_y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        handleDirection(event);
        return true;//super.onTouchEvent(event);
    }
    //判断用户滑动方向
    private void handleDirection(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //刚接触屏幕，记录此时的位置坐标
                start_x = (int) event.getX();
                start_y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //下次滑动之前保存好上一次的棋盘数据
                save_historyMatrix();
                history_score = current_score;
                judgeDirection(event);
                //判断是否为误触
                if(user_direction){
                    //更新游戏分数
                    activity.setScore(current_score);
                    //判断是否破纪录
                    breakRecord(current_score);
                    //实时更新记录
                    showRecord();

                    //在每次滑动之后判断游戏是否结束
                    handleNext(isOver());
                    user_direction = false;

                    //保存当前的棋盘的数据，防止
                }
                break;
        }
    }

    private void breakRecord(int record) {

        if(record>current_record){
            //破纪录，则保存把数据保存到手机中
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //写入数据
            editor.putInt("record",record);
            //提交数据
            editor.commit();
            Log.i(TAG,"SharedPreferences");
        }
    }

    private boolean judgeDirection(MotionEvent event) {
        //用户只点击，而未滑动方向
        //用户触摸完毕，判断其滑动的方向
        end_x = (int) event.getX();
        end_y = (int) event.getY();
        //首先判断水平还是竖直方向
        if (Math.abs(end_x - start_x) > Math.abs(end_y - start_y)) {
            //为水平方向的滑动
            user_direction = true;
            if (end_x > start_x) {
                //为向右滑动
                Log.i(TAG, "slideRight");
                slideRight();
            } else if (end_x < start_x) {
                Log.i(TAG, "slideLeft");
                //为向左滑动
                slideLeft();
            }
        } else if (Math.abs(end_x - start_x) < Math.abs(end_y - start_y)) {
            user_direction = true;
            //为竖直的方向滑动
            if (end_y > start_y) {
                Log.i(TAG, "slideDown");
                //为向下滑动
                slideDown();
            } else if (end_y < start_y) {
                Log.i(TAG, "slideUp");
                //为向上滑动
                slideUp();
            }
        }
        return user_direction;
    }

    //创建四个函数来处理四个方向滑动，相应的处理
    private void slideLeft() {


        ArrayList<Integer>  after_number= new ArrayList<>();
        for (int i=0;i<row_number;i++){
            int pre_number =-1;//当前item前一个item里的数字。
            for (int j=0;j<columns_number;j++){
                int current_number =itemMatrix[i][j].getNumber();
                //如果当前拿到的数字是0，直接忽略掉
                if (current_number==0){
                    continue;
                }else{
                    //当前拿出来的number不是0
                    if (current_number==pre_number){//如果当前number跟前一个一样的时候，合并
                        after_number.add(current_number*2);
                        current_score+=current_number*2;
                        noSlide = true;//
                        //current_score+=pre_number*2;
                        pre_number=-1;
                    }else {//如果当前number跟前一个不一样的时候

                        if (pre_number==-1){ //如果前一个保存的是-1，则需要进一步去看一下个数字
                            pre_number=current_number;
                        }else{ //前一个不是-1 （这个跟前一个不相等）
                            after_number.add(pre_number);
                            pre_number=current_number;
                        }
                    }
                }
            }

            //如果某一行只有最后一个item上有数字，那么会漏掉最后一个item
            if (pre_number!=-1){
                after_number.add(pre_number);
            }


            //计算的结果可能小于这一行的长度
            for (int k=0;k<after_number.size();k++){
                int new_number = after_number.get(k);
                itemMatrix[i][k].setNumber(new_number);
                //can_slide=true;
            }


            //剩下的部分，这一行应该直接去填充0

            for (int p=after_number.size();p<columns_number;p++){
                itemMatrix[i][p].setNumber(0);
            }

            //itemMatrix[i][j].setNumber();
            after_number.clear();
        }
    }

    private void slideRight() {
        Log.i(TAG,"right()");
        //遍历棋盘获得数据的同时，进行及时的修改，提高效率
        //记录滑动之后需要维护的number
        ArrayList<Integer>  after_number= new ArrayList<>();
        for(int i =0;i<row_number;i++){
            //记录当前行最新有意义位置（非空位和非已结合位）所维护的值
            int pre_number = -1;
            for(int j=columns_number-1;j>=0;j--) {

                int current_number = itemMatrix[i][j].getNumber();
                Log.i(TAG,"当前number值："+current_number);
                if (current_number == 0) {
                    //当前为默认值0.说明为空位不处理
                    continue;
                } else {
                    //当前值不为0：为有意义的值
                    //当前值为有意义的值，判断是否与前一个位置值是否相同
                    if (current_number == pre_number) {
                        //相等，则结合并存到after_number集合中去
                        Log.i(TAG,"合并加入"+current_number);
                        after_number.add(current_number * 2);
                        current_score+=current_number*2;
                        //表示当前无有意义的位置
                        pre_number = -1;
                    } else {
                        if(pre_number==-1) {
                            //不相等，更新最新的有意义的位置
                            pre_number = current_number;
                        }else {
                            //前一个有意义位置数据加入after_number
                            after_number.add(pre_number);
                            Log.i(TAG,"加入"+current_number);
                            pre_number = current_number;
                        }
                    }
                }
            }
            //特殊情况，左无有意义的值，当前有意义的值却无法放入到after_number中去
            //判断pre_numbe是否还记录有意义的值
            if(pre_number!=-1){
                Log.i(TAG,"最后有意义："+pre_number);
                after_number.add(pre_number);
            }

            //及时更新数据，提高效率
            //
            for (int j=0;j<after_number.size();j++){
                //先把after_number中值从右向左填入，不够的补0
                //清空当前行的数据(覆盖的方法)
                Log.i(TAG,"更新数据1-"+i+"--"+(columns_number-1-j));
                itemMatrix[i][columns_number-1-j].setNumber(after_number.get(j));
            }
            //不够的全部填0，覆盖原来的number值
            for(int j=columns_number-after_number.size()-1;j>=0;j--){
                Log.i(TAG,i+"--"+j);
                itemMatrix[i][j].setNumber(0);
            }

            //一行结束，清空afer_number里的数据，防止影响下一行
            after_number.clear();//集合的清空方式
        }
    }

    private void slideDown() {

            ArrayList<Integer>  after_number= new ArrayList<>();
            for(int i =0;i<columns_number;i++){
                //记录当前行最新有意义位置（非空位和非已结合位）所维护的值
                int pre_number = -1;
                for(int j=row_number-1;j>=0;j--) {

                    int current_number = itemMatrix[j][i].getNumber();
                    Log.i(TAG,"当前number值："+current_number);
                    if (current_number == 0) {
                        //当前为默认值0.说明为空位不处理
                        continue;
                    } else {
                        //当前值不为0：为有意义的值
                        //当前值为有意义的值，判断是否与前一个位置值是否相同
                        if (current_number == pre_number) {
                            //相等，则结合并存到after_number集合中去
                            Log.i(TAG,"合并加入"+current_number);
                            after_number.add(current_number * 2);
                            current_score+=current_number*2;
                            noSlide = true;
                            //表示当前无有意义的位置
                            pre_number = -1;
                        } else {
                            if(pre_number==-1) {
                                //不相等，更新最新的有意义的位置
                                pre_number = current_number;
                            }else {
                                //前一个有意义位置数据加入after_number
                                after_number.add(pre_number);
                                Log.i(TAG,"加入"+current_number);
                                pre_number = current_number;
                            }
                        }
                    }
                }
                //特殊情况，左无有意义的值，当前有意义的值却无法放入到after_number中去
                //判断pre_numbe是否还记录有意义的值
                if(pre_number!=-1){
                    Log.i(TAG,"最后有意义："+pre_number);
                    after_number.add(pre_number);
                }

                //及时更新数据，提高效率
                //
                for (int j=0;j<after_number.size();j++){
                    //先把after_number中值从右向左填入，不够的补0
                    //清空当前行的数据(覆盖的方法)
                    Log.i(TAG,"更新数据1-"+i+"--"+(row_number-1-j));
                    itemMatrix[row_number-1-j][i].setNumber(after_number.get(j));
                }
                //不够的全部填0，覆盖原来的number值
                for(int j=row_number-after_number.size()-1;j>=0;j--){
                    Log.i(TAG,i+"--"+j);
                    itemMatrix[j][i].setNumber(0);
                }

                //一行结束，清空afer_number里的数据，防止影响下一行
                after_number.clear();//集合的清空方式
            }
    }

    private void slideUp() {
        ArrayList<Integer>  after_number= new ArrayList<>();
        for (int i=0;i<columns_number;i++) {
            int pre_number = -1;//当前item前一个item里的数字。
            for (int j = 0; j < row_number; j++) {
                int current_number = itemMatrix[j][i].getNumber();
                //如果当前拿到的数字是0，直接忽略掉
                if (current_number == 0) {
                    continue;
                } else {
                    //当前拿出来的number不是0
                    if (current_number == pre_number) {//如果当前number跟前一个一样的时候，合并
                        after_number.add(current_number * 2);
                        current_score+=current_number*2;
                        //current_score+=pre_number*2;
                        pre_number = -1;
                    } else {//如果当前number跟前一个不一样的时候

                        if (pre_number == -1) { //如果前一个保存的是-1，则需要进一步去看一下个数字
                            pre_number = current_number;
                        } else { //前一个不是-1 （这个跟前一个不相等）
                            after_number.add(pre_number);
                            pre_number = current_number;
                        }
                    }
                }
            }

            //如果某一行只有最后一个item上有数字，那么会漏掉最后一个item
            if (pre_number != -1) {
                after_number.add(pre_number);
            }


            //计算的结果可能小于这一行的长度
            for (int k = 0; k < after_number.size(); k++) {
                int new_number = after_number.get(k);
                itemMatrix[k][i].setNumber(new_number);
                //can_slide=true;
            }


            //剩下的部分，这一行应该直接去填充0

            for (int p = after_number.size(); p < columns_number; p++) {
                itemMatrix[p][i].setNumber(0);
            }

            //itemMatrix[i][j].setNumber();
            after_number.clear();
        }
    }

    //判断当前局游戏是否结束
    private  void handleNext(int isOver){
        //boolean flag = false;当前处理，效率更高
        if(isOver==0){
            //表示已达到用户设置的最大number
            //设置基本dialog
            new AlertDialog.Builder(context).setTitle("恭喜你")
                                             .setMessage("是否要重新来一局")
                                             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                 @Override
                                                 public void onClick(DialogInterface dialog, int which) {
                                                     //用户点击yes是调用
                                                     restart();
                                                 }
                                             })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    activity.finish();
                                                }
                                            }).show();


        }else {
            //判断下是否位置满
            if(isOver==1){
                //判断是否无法合并,只判断两个方向即可
                slideDown();
                slideLeft();
                if(!noSlide){
                    //不可以滑动
                    new AlertDialog.Builder(context).setTitle("挑战失败")
                            .setMessage("是否要重新来一局")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //用户点击yes是调用
                                    restart();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            }).show();
                    //初始化标记
                    noSlide = false;
                }else {
                    //可以下一步，游戏继续
                    addRandomNumber();
                }
            }else {
                //可以下一步，游戏继续
                addRandomNumber();
            }
        }
    }
    private int isOver(){
        //游戏结束的条件：超过用户设置的最高分，已经全部显示非0，且没有可以合并的
        //超过最高分，:遍历棋盘，是否有number值，超过设置的值
        int over =-1;
        for(int i=0;i<row_number;i++){
            for(int j=0;j<columns_number;j++){
                if(itemMatrix[i][j].getNumber()==maxNumber){
                   over = 0;//0表超过分数
                }
            }
        }
        //位置已满，且没有可以合并的
        if(blankitemList.size()==0){
           over = 1;//1表位置已满
        }
        return over;
    }

    private void save_historyMatrix(){
            Log.i(TAG,itemMatrix+"");
            for(int i=0;i<row_number;i++){
                for(int j=0;j<columns_number;j++){
                    history_Matrix[i][j] = itemMatrix[i][j].getNumber();
                }
            }

    }
    public void restart(){
        //重新开始
        init(context);
        activity.setScore(0);
    }
    public void revert() {
        //在下次滑动之前保存数据
        //写给itemMatrix,并设置单元格文本值
        for(int i=0;i<row_number;i++){
            for(int j=0;j<columns_number;j++){
                 itemMatrix[i][j].setNumber(history_Matrix[i][j]);
            }
        }
        activity.setScore(history_score);
    }

    public void option() {
        }

    public void setRow_number(int row_number) {
        this.row_number = row_number;
    }

    public void setColumns_number(int columns_number) {
        this.columns_number = columns_number;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }
}


