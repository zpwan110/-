package base.util;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/8/15.
 */
public class CountDownTimerUtils {
    private static final int MSG = 110;
    private final TextView mTextView;

    private long mMillisInFuture=0;
    private long mStopTimeInFuture=0;
    private long mCountdownInterval;
    private ScheduledExecutorService executorService;

    /**
     * @param textView          The TextView
     *
     *
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
//        this.mMillisInFuture = 1498462540000l;
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countDownInterval;
        this.mTextView = textView;
    }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            synchronized (CountDownTimerUtils.this) {
                mMillisInFuture = mMillisInFuture - mCountdownInterval;
                if(mMillisInFuture<=0){
                    onFinish();
                }else{
                    onTick(mMillisInFuture);
                }
            }
        }
    };
    private Handler mHandlerTime = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (CountDownTimerUtils.this) {
                LogUtil.d("time",mMillisInFuture+"--------"+mMillisInFuture+"--------"+mCountdownInterval);
                long diff =Math.abs(mMillisInFuture - mCountdownInterval);
                if(diff<=0){
                    mTextView.setText("");
                }else{
                    onTickTime(mMillisInFuture);
                }
            }
        }
    };

    public synchronized final void cancel() {

    }
    public synchronized final CountDownTimerUtils start() {
        if (executorService == null) {
            executorService = new ScheduledThreadPoolExecutor(1);
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG));
                }
            },0,mCountdownInterval, TimeUnit.SECONDS);
        }
        return this;
    }
    public synchronized final CountDownTimerUtils startTime() {
        if (executorService == null) {
            executorService = new ScheduledThreadPoolExecutor(1);
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG));
                }
            },0,1, TimeUnit.SECONDS);
        }
        return this;
    }
    public synchronized final CountDownTimerUtils updateTime(long millisInFuture, long countDownInterval) {
        stop();
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countDownInterval;
        if (executorService == null) {
            executorService = new ScheduledThreadPoolExecutor(1);
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG));
                }
            },0,1, TimeUnit.SECONDS);
        }
        return this;
    }
    /**
     *
     * @Description: 停止计时
     * @param
     * @return void
     * @throws
     */
    public void stop() {
        if (executorService != null) {
            executorService.shutdown();
            executorService =null;
        }
    }

    private void onTickTime(long mMillisInFuture) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(converTime(mMillisInFuture));  //设置倒计时时间
    }
    public static String converTime(long timestamp) {
        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap =Math.abs(timestamp/1000- currentSeconds);// 与现在时间相差秒数
        String timeStr = null;
        int hour = 0;
        int minute =0;
        int second = 0;
        if (timeGap > 60 * 60) {// 1小时以上
            hour = (int) (timeGap / (60 * 60));
            timeGap = timeGap-(hour*(60 * 60));
        }
        if (timeGap > 60) {//1分-1小时
            minute = (int) (timeGap / (60));
            timeGap = timeGap-(minute * 60);
        }
        if (timeGap > 0) {// 1分钟-59分钟
            second = (int) timeGap;
        }
        timeStr = hour+"小时"+minute+"分"+second+"秒";
        return timeStr;
    }


    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        Log.d("onTick", "millisUntilFinished"+millisUntilFinished);
        mTextView.setText(millisUntilFinished / 1000 + "秒后可重新发送");  //设置倒计时时间
//        mTextView.setBackgroundResource(R.drawable.bg_identify_code_press); //设置按钮为灰色，这时是不能点击的
        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.setText(spannableString);
    }

    public void onFinish() {
        mTextView.setText("重新获取验证码");
        mTextView.setClickable(true);//重新获得点击
        stop();
//        mTextView.setBackgroundResource(R.drawable.bg_identify_code_normal);  //还原背景色
    }
}
