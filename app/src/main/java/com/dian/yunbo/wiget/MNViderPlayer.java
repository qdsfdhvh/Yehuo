package com.dian.yunbo.wiget;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aplayer.aplayerandroid.APlayerAndroid;
import dian.com.yunbo.R;
import com.dian.yunbo.wiget.config.ConfigAudio;
import com.dian.yunbo.wiget.config.ConfigSubtitle;
import com.dian.yunbo.wiget.config.ConfigVideo;
import com.dian.yunbo.wiget.config.Record;
import com.dian.yunbo.wiget.model.APlayerParam;
import com.dian.yunbo.wiget.model.PlayConfig;
import com.dian.yunbo.wiget.model.PropertyNameToConfigID;
import com.dian.yunbo.wiget.utils.APlayerSationUtil;
import com.dian.yunbo.wiget.utils.LightnessControl;
import com.dian.yunbo.wiget.utils.PlayerUtils;
import com.dian.yunbo.wiget.utils.PreferencesAttribute;
import com.dian.yunbo.wiget.utils.StringUtil;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by maning on 16/6/14.
 * 播放器
 */
public class MNViderPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, GestureDetector.OnGestureListener {

    private static final String TAG = "MNViderPlayer";
    private Context context;
    private Activity activity;
    static final Handler myHandler = new Handler(Looper.getMainLooper()) {
    };

    // SurfaceView的创建比较耗时，要注意
    private APlayerParam aPlayerParam;
    private APlayerAndroid aPlayer = null;
    private APlayerAndroid.OnPlayCompleteListener mPlayCompleteListener = null;
    private PlayConfig mPlayConfig = null;            //播放配置接口,部分和具体媒体文件强相关的参数，必须打开文件成功后设置
    //地址
    private String videoPath;
    private String videoTitle;
    private int video_position = 0;

    //控件的位置信息
    private float mediaPlayerX;
    private float mediaPlayerY;

    // 计时器
    private Timer timer_video_time;
    private TimerTask task_video_timer;
    private Timer timer_controller;
    private TimerTask task_controller;

    //是否是横屏
    private boolean isFullscreen = true;
    private boolean isLockScreen = false;
    private boolean isPrepare = false;
    private boolean isNeedBatteryListen = true;
    private boolean isNeedNetChangeListen = true;
    private boolean isFirstPlay = false;

    //控件
    private LinearLayout mn_rl_bottom_menu;
    private SurfaceView mn_palyer_surfaceView;
    private ImageView mn_iv_play_pause;
    private ImageView mn_iv_fullScreen;
    private TextView mn_tv_time;
    private SeekBar mn_seekBar;
    private ImageView mn_iv_back;
    private TextView mn_tv_title;
    private TextView mn_tv_system_time;
    private LinearLayout mn_rl_top_menu;
    private RelativeLayout mn_player_rl_progress;
    private ImageView mn_player_iv_lock;
    private TextView mn_player_ll_error;
    private TextView mn_player_ll_net;
    private ProgressWheel mn_player_progressBar;
    private ImageView mn_iv_battery;
    private ImageView mn_player_iv_play_center;
    private TextView mTextViewBufferProgress = null;         //拖动进度条时，在屏幕中央以大字体显示当前的进度

    private boolean isRestPath = false;

    public MNViderPlayer(Context context) {
        this(context, null);
    }

    public MNViderPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MNViderPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        activity = (Activity) this.context;
        //自定义属性相关
        initAttrs(context, attrs);
        //其他
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MNViderPlayer);
        //遍历拿到自定义属性
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int index = typedArray.getIndex(i);
            if (index == R.styleable.MNViderPlayer_mnFirstNeedPlay) {
                isFirstPlay = typedArray.getBoolean(R.styleable.MNViderPlayer_mnFirstNeedPlay, false);
            }
        }
        //销毁
        typedArray.recycle();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int screenWidth = PlayerUtils.getScreenWidth(activity);
        int screenHeight = PlayerUtils.getScreenHeight(activity);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();

        //newConfig.orientation获得当前屏幕状态是横向或者竖向
        //Configuration.ORIENTATION_PORTRAIT 表示竖向
        //Configuration.ORIENTATION_LANDSCAPE 表示横屏
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //计算视频的大小16：9
            layoutParams.width = screenWidth;
            layoutParams.height = screenWidth * 9 / 16;

            setX(mediaPlayerX);
            setY(mediaPlayerY);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            layoutParams.width = screenWidth;
            layoutParams.height = screenHeight;

            setX(0);
            setY(0);
        }
        setLayoutParams(layoutParams);
    }

    //初始化
    private void init() {
        View inflate = View.inflate(context, R.layout.mn_player_view, this);
        mn_rl_bottom_menu = (LinearLayout) inflate.findViewById(R.id.mn_rl_bottom_menu);
        mn_palyer_surfaceView = (SurfaceView) inflate.findViewById(R.id.mn_palyer_surfaceView);
        mn_iv_play_pause = (ImageView) inflate.findViewById(R.id.mn_iv_play_pause);
        mn_iv_fullScreen = (ImageView) inflate.findViewById(R.id.mn_iv_fullScreen);
        mn_tv_time = (TextView) inflate.findViewById(R.id.mn_tv_time);
        mn_tv_system_time = (TextView) inflate.findViewById(R.id.mn_tv_system_time);
        mn_seekBar = (SeekBar) inflate.findViewById(R.id.mn_seekBar);
        mn_iv_back = (ImageView) inflate.findViewById(R.id.mn_iv_back);
        mn_tv_title = (TextView) inflate.findViewById(R.id.mn_tv_title);

        mn_rl_top_menu = (LinearLayout) inflate.findViewById(R.id.mn_rl_top_menu);
        mn_player_rl_progress = (RelativeLayout) inflate.findViewById(R.id.mn_player_rl_progress);
        mn_player_iv_lock = (ImageView) inflate.findViewById(R.id.mn_player_iv_lock);
        mn_player_ll_error = (TextView) inflate.findViewById(R.id.mn_player_ll_error);
        mn_player_ll_net = (TextView) inflate.findViewById(R.id.mn_player_ll_net);
        mn_player_progressBar = (ProgressWheel) inflate.findViewById(R.id.mn_player_progressBar);
        mn_iv_battery = (ImageView) inflate.findViewById(R.id.mn_iv_battery);
        mn_player_iv_play_center = (ImageView) inflate.findViewById(R.id.mn_player_iv_play_center);

        mn_seekBar.setOnSeekBarChangeListener(this);
        mn_iv_play_pause.setOnClickListener(this);
        mn_iv_fullScreen.setOnClickListener(this);
        mn_iv_back.setOnClickListener(this);
        mn_player_iv_lock.setOnClickListener(this);
        mn_player_ll_error.setOnClickListener(this);
        mn_player_ll_net.setOnClickListener(this);
        mn_player_iv_play_center.setOnClickListener(this);
        mTextViewBufferProgress = (TextView) findViewById(R.id.play_buffering_text_view);
        mTextViewBufferProgress.setVisibility(View.INVISIBLE);
        //初始化
        initViews();

        if (!isFirstPlay) {
            mn_player_iv_play_center.setVisibility(View.VISIBLE);
            mn_player_progressBar.setVisibility(View.GONE);
        }

        //初始化手势
        initGesture();

        //存储控件的位置信息
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayerX = getX();
                mediaPlayerY = getY();
                Log.i(TAG, "控件的位置---X：" + mediaPlayerX + "，Y：" + mediaPlayerY);
            }
        }, 1000);


        initPlayer();
//        initPlayConfig(mn_palyer_surfaceView);
        setPlayerView(mn_palyer_surfaceView);


        if (isFirstPlay) {
            //判断当前有没有网络（播放的是网络视频）
            if (!PlayerUtils.isNetworkConnected(context) && videoPath.startsWith("http")) {
                Toast.makeText(context, context.getString(R.string.mnPlayerNoNetHint), Toast.LENGTH_SHORT).show();
                showNoNetView();
            } else {
                //手机网络给提醒
                if (PlayerUtils.isMobileConnected(context)) {
                    Toast.makeText(context, context.getString(R.string.mnPlayerMobileNetHint), Toast.LENGTH_SHORT).show();
                }
                //添加播放路径
                aPlayer.open(videoPath);
            }
        }
    }

    private void initViews() {
        mn_tv_system_time.setText(PlayerUtils.getCurrentHHmmTime());
        mn_rl_bottom_menu.setVisibility(View.GONE);
        mn_rl_top_menu.setVisibility(View.GONE);
        mn_player_iv_lock.setVisibility(View.GONE);
        initLock();
        mn_player_rl_progress.setVisibility(View.VISIBLE);
        mn_player_progressBar.setVisibility(View.VISIBLE);
        mn_player_ll_error.setVisibility(View.GONE);
        mn_player_ll_net.setVisibility(View.GONE);
        mn_player_iv_play_center.setVisibility(View.GONE);
        initTopMenu();
    }

    private void initLock() {
        if (isFullscreen) {
            mn_player_iv_lock.setVisibility(View.VISIBLE);
        } else {
            mn_player_iv_lock.setVisibility(View.GONE);
        }
    }

    private void initTopMenu() {
        mn_tv_title.setText(videoTitle);
//        if (isFullscreen) {
//            mn_rl_top_menu.setVisibility(View.VISIBLE);
//        } else {
//            mn_rl_top_menu.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.mn_iv_play_pause) {
            if (aPlayer != null) {
                if (isPlay()) {
                    aPlayer.pause();
                    mn_iv_play_pause.setImageResource(R.drawable.mn_player_play);
                } else {
                    aPlayer.play();
                    mn_iv_play_pause.setImageResource(R.drawable.mn_player_pause);
                }
            }
        } else if (i == R.id.mn_iv_fullScreen) {
            if (isFullscreen) {
                setProtrait();
            } else {
                setLandscape();
            }
        } else if (i == R.id.mn_iv_back) {
            activity.onBackPressed();
//            setProtrait();
        } else if (i == R.id.mn_player_iv_lock) {
            if (isFullscreen) {
                if (isLockScreen) {
                    unLockScreen();
                    initBottomMenuState();
                } else {
                    lockScreen();
                    destroyControllerTask(true);
                }
            }
        } else if (i == R.id.mn_player_ll_error || i == R.id.mn_player_ll_net || i == R.id.mn_player_iv_play_center) {
            playVideo(videoPath, videoTitle, 0, "");
        }
    }

    //--------------------------------------------------------------------------------------
    // ######## 相关View的操作 ########
    //--------------------------------------------------------------------------------------

    private void unLockScreen() {
        isLockScreen = false;
        mn_player_iv_lock.setImageResource(R.drawable.mn_player_landscape_screen_lock_open);
    }

    private void lockScreen() {
        isLockScreen = true;
        mn_player_iv_lock.setImageResource(R.drawable.mn_player_landscape_screen_lock_close);
    }

    private void setBufferProgress(int progress) {
        int visibility = (progress == 100) ? View.INVISIBLE : View.VISIBLE;
        mTextViewBufferProgress.setVisibility(visibility);
        String strProgress = "正在缓冲" + progress + "%";
        mTextViewBufferProgress.setText(strProgress);
        //记住这里 重要的一B  这是来填满第二缓存区的
        // if (progress >= 0 && progress <= 100) {
        //    int secondProgress = aPlayer.getDuration() * progress / 100;
        //    mn_seekBar.setSecondaryProgress(secondProgress);

        // }
    }

    //下面菜单的显示和隐藏
    private void initBottomMenuState() {
        mn_tv_system_time.setText(PlayerUtils.getCurrentHHmmTime());
        if (mn_rl_bottom_menu.getVisibility() == View.GONE) {
            initControllerTask();
            mn_rl_bottom_menu.setVisibility(View.VISIBLE);
            if (isFullscreen) {
                mn_rl_top_menu.setVisibility(View.VISIBLE);
                mn_player_iv_lock.setVisibility(View.VISIBLE);
            }
        } else {
            destroyControllerTask(true);
        }
    }

    private void dismissControllerMenu() {
        if (isFullscreen && !isLockScreen) {
            mn_player_iv_lock.setVisibility(View.GONE);
        }
        mn_rl_top_menu.setVisibility(View.GONE);
        mn_rl_bottom_menu.setVisibility(View.GONE);
    }

    private void showErrorView() {
        mn_player_iv_play_center.setVisibility(View.GONE);
        mn_player_ll_net.setVisibility(View.GONE);
        mn_player_progressBar.setVisibility(View.GONE);
        mn_player_ll_error.setVisibility(View.VISIBLE);
    }

    private void showNoNetView() {
        mn_player_iv_play_center.setVisibility(View.GONE);
        mn_player_ll_net.setVisibility(View.VISIBLE);
        mn_player_progressBar.setVisibility(View.GONE);
        mn_player_ll_error.setVisibility(View.GONE);
    }

    private void setLandscape() {
        isFullscreen = true;
        //设置横屏
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (mn_rl_bottom_menu.getVisibility() == View.VISIBLE) {
            mn_rl_top_menu.setVisibility(View.VISIBLE);
        }
        initLock();
    }

    private void setProtrait() {
        isFullscreen = false;
        //设置横屏
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mn_rl_top_menu.setVisibility(View.GONE);
        unLockScreen();
        initLock();
    }

    //--------------------------------------------------------------------------------------
    // ######## 计时器相关操作 ########
    //--------------------------------------------------------------------------------------

    private void initTimeTask() {
        timer_video_time = new Timer();
        task_video_timer = new TimerTask() {
            @Override
            public void run() {
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (aPlayer == null) {
                            return;
                        }
                        //设置时间
                        mn_tv_time.setText(String.valueOf(PlayerUtils.converLongTimeToStr(aPlayer.getPosition()) + " / " + PlayerUtils.converLongTimeToStr(aPlayer.getDuration())));
                        //进度条
                        int progress = aPlayer.getPosition();
                        mn_seekBar.setProgress(progress);
                    }
                });
            }
        };
        timer_video_time.schedule(task_video_timer, 0, 1000);
    }

    private void destroyTimeTask() {
        if (timer_video_time != null && task_video_timer != null) {
            timer_video_time.cancel();
            task_video_timer.cancel();
            timer_video_time = null;
            task_video_timer = null;
        }
    }

    private void initControllerTask() {
        // 设置计时器,控制器的影藏和显示
        timer_controller = new Timer();
        task_controller = new TimerTask() {
            @Override
            public void run() {
                destroyControllerTask(false);
            }
        };
        timer_controller.schedule(task_controller, 5000);
        initTimeTask();
    }

    private void destroyControllerTask(boolean isMainThread) {
        if (isMainThread) {
            dismissControllerMenu();
        } else {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    dismissControllerMenu();
                }
            });
        }
        if (timer_controller != null && task_controller != null) {
            timer_controller.cancel();
            task_controller.cancel();
            timer_controller = null;
            task_controller = null;
        }
        destroyTimeTask();
    }

    //--------------------------------------------------------------------------------------
    // ######## 接口方法实现 ########
    //--------------------------------------------------------------------------------------
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (aPlayer != null && aPlayer.getState() == APlayerAndroid.PlayerState.APLAYER_PLAYING) {
            int maxCanSeekTo = seekBar.getMax() - 5 * 1000;
            if (seekBar.getProgress() < maxCanSeekTo) {
                aPlayer.setPosition(seekBar.getProgress());
            } else {
                //不能拖到最后
                aPlayer.setPosition(maxCanSeekTo);
            }
        }
    }


    //--------------------------------------------------------------------------------------
    // ######## 手势相关 ########
    //--------------------------------------------------------------------------------------
    private RelativeLayout gesture_volume_layout;// 音量控制布局
    private TextView geture_tv_volume_percentage;// 音量百分比
    private ImageView gesture_iv_player_volume;// 音量图标
    private RelativeLayout gesture_light_layout;// 亮度布局
    private TextView geture_tv_light_percentage;// 亮度百分比
    private RelativeLayout gesture_progress_layout;// 进度图标
    private TextView geture_tv_progress_time;// 播放时间进度
    private ImageView gesture_iv_progress;// 快进或快退标志
    private GestureDetector gestureDetector;
    private AudioManager audiomanager;
    private int maxVolume, currentVolume;
    private static final float STEP_PROGRESS = 2f;// 设定进度滑动时的步长，避免每次滑动都改变，导致改变过快
    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快
    private static final float STEP_LIGHT = 2f;// 协调亮度滑动时的步长，避免每次滑动都改变，导致改变过快
    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量
    private static final int GESTURE_MODIFY_PROGRESS = 1;
    private static final int GESTURE_MODIFY_VOLUME = 2;
    private static final int GESTURE_MODIFY_BRIGHTNESS = 3;

    private void initGesture() {
        gesture_volume_layout = (RelativeLayout) findViewById(R.id.mn_gesture_volume_layout);
        geture_tv_volume_percentage = (TextView) findViewById(R.id.mn_gesture_tv_volume_percentage);
        gesture_iv_player_volume = (ImageView) findViewById(R.id.mn_gesture_iv_player_volume);

        gesture_progress_layout = (RelativeLayout) findViewById(R.id.mn_gesture_progress_layout);
        geture_tv_progress_time = (TextView) findViewById(R.id.mn_gesture_tv_progress_time);
        gesture_iv_progress = (ImageView) findViewById(R.id.mn_gesture_iv_progress);

        //亮度的布局
        gesture_light_layout = (RelativeLayout) findViewById(R.id.mn_gesture_light_layout);
        geture_tv_light_percentage = (TextView) findViewById(R.id.mn_geture_tv_light_percentage);

        gesture_volume_layout.setVisibility(View.GONE);
        gesture_progress_layout.setVisibility(View.GONE);
        gesture_light_layout.setVisibility(View.GONE);

        gestureDetector = new GestureDetector(getContext(), this);
        setLongClickable(true);
        gestureDetector.setIsLongpressEnabled(true);
        audiomanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (!isPrepare || isLockScreen) {
            return false;
        }
        initBottomMenuState();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        if (!isPrepare || isLockScreen) {
            return false;
        }

        int FLAG = 0;

        // 横向的距离变化大则调整进度，纵向的变化大则调整音量
        if (Math.abs(distanceX) >= Math.abs(distanceY)) {
            if (aPlayer != null && aPlayer.getState() == APlayerAndroid.PlayerState.APLAYER_PLAYING) {
                FLAG = GESTURE_MODIFY_PROGRESS;
            }
        } else {
            int intX = (int) e1.getX();
            int screenWidth = PlayerUtils.getScreenWidth((Activity) context);
            if (intX > screenWidth / 2) {
                FLAG = GESTURE_MODIFY_VOLUME;
            } else {
                //左边是亮度
                FLAG = GESTURE_MODIFY_BRIGHTNESS;
            }
        }

        if (GESTURE_FLAG != 0 && GESTURE_FLAG != FLAG) {
            return false;
        }

        GESTURE_FLAG = FLAG;

        if (FLAG == GESTURE_MODIFY_PROGRESS) {
            //表示是横向滑动,可以添加快进
            // distanceX=lastScrollPositionX-currentScrollPositionX，因此为正时是快进
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_light_layout.setVisibility(View.GONE);
            gesture_progress_layout.setVisibility(View.VISIBLE);
            try {
                if (aPlayer != null && aPlayer.getState() == APlayerAndroid.PlayerState.APLAYER_PLAYING) {
                    if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
                        if (distanceX >= PlayerUtils.dip2px(context, STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
                            gesture_iv_progress
                                    .setImageResource(R.drawable.mn_player_backward);
                            if (aPlayer.getPosition() > 3 * 1000) {// 避免为负
                                int cpos = aPlayer.getPosition();
                                aPlayer.setPosition(cpos - 3000);
                                mn_seekBar.setProgress(aPlayer.getPosition());
                            } else {
                                //什么都不做
                                aPlayer.setPosition(3000);
                            }
                        } else if (distanceX <= -PlayerUtils.dip2px(context, STEP_PROGRESS)) {// 快进
                            gesture_iv_progress
                                    .setImageResource(R.drawable.mn_player_forward);
                            if (aPlayer.getPosition() < aPlayer.getDuration() - 5 * 1000) {// 避免超过总时长
                                int cpos = aPlayer.getPosition();
                                aPlayer.setPosition(cpos + 3000);
                                // 把当前位置赋值给进度条
                                mn_seekBar.setProgress(aPlayer.getPosition());
                            }
                        }
                    }
                    String timeStr = PlayerUtils.converLongTimeToStr(aPlayer.getPosition()) + " / "
                            + PlayerUtils.converLongTimeToStr(aPlayer.getDuration());
                    geture_tv_progress_time.setText(timeStr);

                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
        else if (FLAG == GESTURE_MODIFY_VOLUME) {
            //右边是音量
            gesture_volume_layout.setVisibility(View.VISIBLE);
            gesture_light_layout.setVisibility(View.GONE);
            gesture_progress_layout.setVisibility(View.GONE);
            currentVolume = audiomanager
                    .getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
            if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                if (currentVolume == 0) {// 静音，设定静音独有的图片
                    gesture_iv_player_volume.setImageResource(R.drawable.mn_player_volume_close);
                }
                if (distanceY >= PlayerUtils.dip2px(context, STEP_VOLUME)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                    if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
                        currentVolume++;
                    }
                    gesture_iv_player_volume.setImageResource(R.drawable.mn_player_volume_open);
                } else if (distanceY <= -PlayerUtils.dip2px(context, STEP_VOLUME)) {// 音量调小
                    if (currentVolume > 0) {
                        currentVolume--;
                        if (currentVolume == 0) {// 静音，设定静音独有的图片
                            gesture_iv_player_volume.setImageResource(R.drawable.mn_player_volume_close);
                        }
                    }
                }
                int percentage = (currentVolume * 100) / maxVolume;
                geture_tv_volume_percentage.setText(String.valueOf(percentage + "%"));
                audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
            }
        }
        //调节亮度
        else if (FLAG == GESTURE_MODIFY_BRIGHTNESS) {
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_light_layout.setVisibility(View.VISIBLE);
            gesture_progress_layout.setVisibility(View.GONE);
            currentVolume = audiomanager
                    .getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
            if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
                // 亮度调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
                int mLight = LightnessControl.GetLightness((Activity) context);
                if (mLight >= 0 && mLight <= 255) {
                    if (distanceY >= PlayerUtils.dip2px(context, STEP_LIGHT)) {
                        if (mLight > 245) {
                            LightnessControl.SetLightness((Activity) context, 255);
                        } else {
                            LightnessControl.SetLightness((Activity) context, mLight + 10);
                        }
                    } else if (distanceY <= -PlayerUtils.dip2px(context, STEP_LIGHT)) {// 亮度调小
                        if (mLight < 10) {
                            LightnessControl.SetLightness((Activity) context, 0);
                        } else {
                            LightnessControl.SetLightness((Activity) context, mLight - 10);
                        }
                    }
                } else if (mLight < 0) {
                    LightnessControl.SetLightness((Activity) context, 0);
                } else {
                    LightnessControl.SetLightness((Activity) context, 255);
                }
                //获取当前亮度
                int currentLight = LightnessControl.GetLightness((Activity) context);
                int percentage = (currentLight * 100) / 255;
                geture_tv_light_percentage.setText(String.valueOf(percentage + "%"));
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 手势里除了singleTapUp，没有其他检测up的方法
        if (event.getAction() == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_progress_layout.setVisibility(View.GONE);
            gesture_light_layout.setVisibility(View.GONE);
        }
        return gestureDetector.onTouchEvent(event);
    }

    //--------------------------------------------------------------------------------------
    // ######## 对外提供的方法 ########
    //--------------------------------------------------------------------------------------

    /**
     * 设置视频信息
     *
     * @param url   视频地址
     * @param title 视频标题
     */
    public void setDataSource(String url, String title) {
        //赋值
        videoPath = url;
        videoTitle = title;
    }

    /**
     * 播放视频
     *
     * @param url   视频地址
     * @param title 视频标题
     */
    public void playVideo(String url, String title, String cookie) {
        video_position = 0;
        playVideo(url, title, video_position, cookie);
    }

    /**
     * 播放视频（支持上次播放位置）
     * 自己记录上一次播放的位置，然后传递position进来就可以了
     *
     * @param url      视频地址
     * @param title    视频标题
     * @param position 视频跳转的位置
     */
    public void playVideo(String url, String title, int position, String cookie) {
        //地址判空处理
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, context.getString(R.string.mnPlayerUrlEmptyHint), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> pramMap = PreferencesAttribute.getPreferencesAll(context);
        aPlayerParam = new APlayerParam(url, pramMap);

        setPlayerParameter(cookie);

        //销毁ControllerView
        destroyControllerTask(true);

        //赋值
        videoPath = url;
        videoTitle = title;
        video_position = position;
        isPrepare = false;

        //判断当前有没有网络（播放的是网络视频）
        if (!PlayerUtils.isNetworkConnected(context) && url.startsWith("http")) {
            Toast.makeText(context, context.getString(R.string.mnPlayerNoNetHint), Toast.LENGTH_SHORT).show();
            showNoNetView();
            return;
        }
        //手机网络给提醒
        if (PlayerUtils.isMobileConnected(context)) {
            Toast.makeText(context, context.getString(R.string.mnPlayerMobileNetHint), Toast.LENGTH_SHORT).show();
        }

        //重置MediaPlayer
        resetMediaPlayer();

        //初始化View
        initViews();
        //判断广播相关
        if (isNeedBatteryListen) {
            registerBatteryReceiver();
        } else {
            unRegisterBatteryReceiver();
            mn_iv_battery.setVisibility(View.GONE);
        }
        //网络监听的广播
        if (isNeedNetChangeListen) {
            registerNetReceiver();
        } else {
            unregisterNetReceiver();
        }
    }

    private void resetMediaPlayer() {
        try {
            if (aPlayer != null) {
                aPlayer.close();
                if (!isPlay()) {
                    isRestPath = true;
                    aPlayer.open(videoPath);
                }
            } else {
                Toast.makeText(context, "播放器初始化失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放视频
     */
    public void startVideo() {
        if (aPlayer != null) {
            aPlayer.play();
            mn_iv_play_pause.setImageResource(R.drawable.mn_player_pause);
        }
    }

    /**
     * 暂停视频
     */
    public void pauseVideo() {
        if (aPlayer != null) {
            aPlayer.pause();
            mn_iv_play_pause.setImageResource(R.drawable.mn_player_play);
            video_position = aPlayer.getPosition();
            destroyControllerTask(true);
        }
    }

    /**
     * 竖屏
     */
    public void setOrientationPortrait() {
        setProtrait();
    }

    /**
     * 横屏
     */
    public void setOrientationLandscape() {
        setLandscape();
    }

    /**
     * 设置是否需要电量监听
     */
    public void setIsNeedBatteryListen(boolean isNeedBatteryListen) {
        this.isNeedBatteryListen = isNeedBatteryListen;
    }

    /**
     * 设置是否需要网络变化监听
     */
    public void setIsNeedNetChangeListen(boolean isNeedNetChangeListen) {
        this.isNeedNetChangeListen = isNeedNetChangeListen;
    }

    /**
     * 判断是不是全屏状态
     *
     * @return
     */
    public boolean isFullScreen() {
        return isFullscreen;
    }

    /**
     * 获取当前播放的位置
     */
    public int getVideoCurrentPosition() {
        int position = 0;
        if (aPlayer != null) {
            position = aPlayer.getPosition();
        }
        return position;
    }

    /**
     * 获取视频总长度
     */
    public int getVideoTotalDuration() {
        int position = 0;
        if (aPlayer != null) {
            position = aPlayer.getDuration();
        }
        return position;
    }

    /**
     * 获取管理者
     */
    public APlayerAndroid getMediaPlayer() {
        return aPlayer;
    }

    /**
     * 销毁资源
     */
    public void destroyVideo() {
        if (aPlayer != null) {
            aPlayer.close();
            aPlayer.destroy();
            aPlayer = null;
        }
        mn_palyer_surfaceView = null;
        video_position = 0;
        unRegisterBatteryReceiver();
        unregisterNetReceiver();
        removeAllListener();
        destroyTimeTask();
        myHandler.removeCallbacksAndMessages(null);
    }


    //--------------------------------------------------------------------------------------
    // ######## 广播相关 ########
    //--------------------------------------------------------------------------------------

    /**
     * 电量广播接受者
     */
    class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //判断它是否是为电量变化的Broadcast Action
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);

                int battery = (level * 100) / scale;

                //把它转成百分比
                Log.i(TAG, "电池电量为" + battery + "%");

                mn_iv_battery.setVisibility(View.VISIBLE);
                if (battery > 0 && battery < 20) {
                    mn_iv_battery.setImageResource(R.drawable.mn_player_battery_01);
                } else if (battery >= 20 && battery < 40) {
                    mn_iv_battery.setImageResource(R.drawable.mn_player_battery_02);
                } else if (battery >= 40 && battery < 65) {
                    mn_iv_battery.setImageResource(R.drawable.mn_player_battery_03);
                } else if (battery >= 65 && battery < 90) {
                    mn_iv_battery.setImageResource(R.drawable.mn_player_battery_04);
                } else if (battery >= 90 && battery <= 100) {
                    mn_iv_battery.setImageResource(R.drawable.mn_player_battery_05);
                } else {
                    mn_iv_battery.setVisibility(View.GONE);
                }


            }
        }
    }


    private BatteryReceiver batteryReceiver;

    private void registerBatteryReceiver() {
        if (batteryReceiver == null) {
            //注册广播接受者
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            //创建广播接受者对象
            batteryReceiver = new BatteryReceiver();
            //注册receiver
            context.registerReceiver(batteryReceiver, intentFilter);
        }
    }

    private void unRegisterBatteryReceiver() {
        if (batteryReceiver != null) {
            context.unregisterReceiver(batteryReceiver);
        }
    }

    //-------------------------网络变化监听
    public class NetChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (onNetChangeListener == null || !isNeedNetChangeListen) {
                return;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isAvailable()) {
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) { //WiFi网络
                    onNetChangeListener.onWifi(aPlayer);
                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {   //3g网络
                    onNetChangeListener.onMobile(aPlayer);
                } else {    //其他
                    Log.i(TAG, "其他网络");
                }
            } else {
                onNetChangeListener.onNoAvailable(aPlayer);
            }
        }
    }

    private NetChangeReceiver netChangeReceiver;

    private void registerNetReceiver() {
        if (netChangeReceiver == null) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            netChangeReceiver = new NetChangeReceiver();
            context.registerReceiver(netChangeReceiver, filter);
        }
    }

    private void unregisterNetReceiver() {
        if (netChangeReceiver != null) {
            context.unregisterReceiver(netChangeReceiver);
        }
    }


    //--------------------------------------------------------------------------------------
    // ######## 自定义回调 ########
    //--------------------------------------------------------------------------------------

    private void removeAllListener() {
        if (onNetChangeListener != null) {
            onNetChangeListener = null;
        }
        if (onPlayerCreatedListener != null) {
            onPlayerCreatedListener = null;
        }
    }


    //网络监听回调
    private OnNetChangeListener onNetChangeListener;

    public void setOnNetChangeListener(OnNetChangeListener onNetChangeListener) {
        this.onNetChangeListener = onNetChangeListener;
    }

    public interface OnNetChangeListener {
        //wifi
        void onWifi(APlayerAndroid aPlayer);

        //手机
        void onMobile(APlayerAndroid aPlayer);

        //不可用
        void onNoAvailable(APlayerAndroid aPlayer);
    }

    //SurfaceView初始化完成回调
    private OnPlayerCreatedListener onPlayerCreatedListener;

    public void setOnPlayerCreatedListener(OnPlayerCreatedListener onPlayerCreatedListener) {
        this.onPlayerCreatedListener = onPlayerCreatedListener;
    }

    public interface OnPlayerCreatedListener {
        //不可用
        void onPlayerCreated(String url, String title);
    }

    //-----------------------播放完回调
    private OnCompletionListener onCompletionListener;

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public interface OnCompletionListener {
        void onCompletion(APlayerAndroid aPlayer);
    }

    private void initPlayer() {
        if (null != aPlayer) {
            return;
        }

        aPlayer = new APlayerAndroid();
        aPlayer.setOnOpenSuccessListener(new APlayerAndroid.OnOpenSuccessListener() {
            @Override
            public void onOpenSuccess() {
                aPlayer.play();

                //test code,avoid listener covered
                aPlayer.setOnPlayCompleteListener(mPlayCompleteListener);

                isPrepare = true;

                // 把得到的总长度和进度条的匹配
                mn_seekBar.setMax(aPlayer.getDuration());
                mn_iv_play_pause.setImageResource(R.drawable.mn_player_pause);
                mn_tv_time.setText(String.valueOf(PlayerUtils.converLongTimeToStr(aPlayer.getPosition()) + "/" + PlayerUtils.converLongTimeToStr(aPlayer.getDuration())));
                //延时：避免出现上一个视频的画面闪屏
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initBottomMenuState();
                        mn_player_rl_progress.setVisibility(View.GONE);
                    }
                }, 500);
            }
        });

        aPlayer.setOnOpenCompleteListener(new APlayerAndroid.OnOpenCompleteListener() {
            public void onOpenComplete(boolean isOpenSuccess) {
                //aPlayer.play();
                if (!isOpenSuccess) {
                    showErrorView();
                }
            }
        });


        aPlayer.setOnPlayStateChangeListener(new APlayerAndroid.OnPlayStateChangeListener() {

            @Override
            public void onPlayStateChange(int nCurrentState, int nPreState) {
                if (APlayerAndroid.PlayerState.APLAYER_PLAYING == nCurrentState) {
                    if (video_position > 0) {
                        Log.i(TAG, "onPrepared---video_position:" + video_position);
                        aPlayer.setPosition(video_position);
                        video_position = 0;
                    }

                } else {
                }
            }
        });

        mPlayCompleteListener = new APlayerAndroid.OnPlayCompleteListener() {
            @Override
            public void onPlayComplete(String playRet) {
                boolean isUseCallStop = APlayerSationUtil.isStopByUserCall(playRet);
                if (!isUseCallStop) {
                    mn_iv_play_pause.setImageResource(R.drawable.mn_player_play);
                    destroyControllerTask(true);
                    video_position = 0;
                    if (onCompletionListener != null) {
                        onCompletionListener.onCompletion(aPlayer);
                    }
                    //只在自然播放结束
                }

                if (isRestPath && aPlayer != null) {
                    aPlayer.open(videoPath);
                    isRestPath = false;
                    mn_palyer_surfaceView.setVisibility(GONE);
                    mn_palyer_surfaceView.setVisibility(VISIBLE);
                    Log.d("MNTAG", "isRestPath:" + isRestPath);
                }
                long stopCode = StringUtil.StringToLong(APlayerAndroid.PlayCompleteRet.PLAYRE_RESULT_CLOSE);
                long doneCode = StringUtil.StringToLong(APlayerAndroid.PlayCompleteRet.PLAYRE_RESULT_COMPLETE);
                long currentCode = StringUtil.StringToLong(playRet);
                if (currentCode != stopCode && currentCode != doneCode) {
                    showErrorView();
                }

            }
        };
        aPlayer.setOnPlayCompleteListener(mPlayCompleteListener);

        aPlayer.setOnSeekCompleteListener(new APlayerAndroid.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {
                // TODO Auto-generated method stub
            }
        });

        aPlayer.setOnBufferListener(new APlayerAndroid.OnBufferListener() {

            @Override
            public void onBuffer(int percent) {
                //((TextView) findViewById(R.id.textView1)).setText(progress+ " ");
                Log.i(TAG, "二级缓存onBufferingUpdate: " + percent);
                setBufferProgress(percent);


            }
        });

        aPlayer.setOnSurfaceDestroyListener(new APlayerAndroid.OnSurfaceDestroyListener() {

            @Override
            public void onSurfaceDestroy() {

//                aPlayer.close();
            }
        });

        aPlayer.setOnShowSubtitleListener(new APlayerAndroid.OnShowSubtitleListener() {

            @Override
            public void onShowSubtitle(String subtitle) {

            }
        });


        aPlayer.setOnSystemPlayerFailListener(new APlayerAndroid.OnSystemPlayerFailListener() {

            @Override
            public void onSystemPlayerFail() {
                aPlayer.setConfig(APlayerAndroid.CONFIGID.HW_DECODER_USE, "1");
                showErrorView();
            }
        });
    }

    private void setPlayerParameter(String cookie) {
        //aPlayer.UseSystemPlayer(true);
        //aPlayer.UseSystemPlayer(false);

        Map<String, String> configParam = aPlayerParam.getConfigParam();
        if (!TextUtils.isEmpty(cookie)) {
            configParam.put("Cookie", cookie);
        }
        if (null != configParam) {
            PropertyNameToConfigID propertyNameToConfigID = PropertyNameToConfigID.getInstance(context);
            for (Map.Entry<String, String> entry : configParam.entrySet()) {
                Integer integer = propertyNameToConfigID.getIDByName(entry.getKey());
                if (null == integer) {
                    continue;
                }

                int configID = integer.intValue();
                if (0 != aPlayer.setConfig(configID, entry.getValue())) {
                    Log.e("DEBUG_TAG", "SetConfig() faile " + "configID = " + configID + " val = " + entry.getValue());
                }
            }
        }

    }

    private void initPlayConfig(View displayView) {
        final String filePath = aPlayerParam.getFilePath();
        PlayConfig.PlayerListener playerListener = new PlayConfig.PlayerListener();
        playerListener.playCompleteListener = mPlayCompleteListener;
        mPlayConfig = new PlayConfig(new ConfigVideo(aPlayer, filePath, playerListener, displayView),
                new ConfigAudio(aPlayer), new ConfigSubtitle(aPlayer), new Record(aPlayer));
    }

    private void setPlayerView(SurfaceView mSurView) {
        aPlayer.setView(mSurView);
    }

    public boolean isPlay() {

        if (aPlayer == null) {
            return false;
        }

        int status = aPlayer.getState();

        if (APlayerAndroid.PlayerState.APLAYER_PLAYING == status) {
            return true;
        }

        return false;
    }
}



