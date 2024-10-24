package java.com.example.ground_station.presentation.floating;

import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ground_station.R;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;

public class FloatingWindowHelper {
    public static final String tag = "floating_window";

    private static final long DEBOUNCE_DELAY_MS = 300; // 防抖延迟时间（毫秒）
    private long lastClickTime = 0;

    private ImageView audioBtn;
    private ImageView textToSpeechBtn;
    private ImageView audioFileBtn;
    private ImageView lightBtn;
    private ImageView detectorAlarmBtn;
    private ImageView tvOptionsInputSettingsBtn;

    private ImageView currentSelectedBtn;

    public void showFloatingWindow(AppCompatActivity activity) {
        EasyFloat.with(activity)
                .setLayout(R.layout.floating_window)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.DEFAULT)
                .setGravity(Gravity.RIGHT, 0, 0)
                .setDragEnable(true)
                .setTag(tag)
                .registerCallbacks(new OnFloatCallbacks() {
                    @Override
                    public void dragEnd(@NonNull View view) {

                    }

                    @Override
                    public void hide(@NonNull View view) {

                    }

                    @Override
                    public void show(@NonNull View view) {

                    }

                    @Override
                    public void drag(@NonNull View view, @NonNull MotionEvent motionEvent) {

                    }

                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void touchEvent(@NonNull View view, @NonNull MotionEvent motionEvent) {

                    }

                    @Override
                    public void createdResult(boolean b, @Nullable String s, @Nullable View view) {

                        audioBtn = view.findViewById(R.id.audio_btn);
                        textToSpeechBtn = view.findViewById(R.id.text_to_speech_btn);
                        audioFileBtn = view.findViewById(R.id.audio_file_btn);
                        lightBtn = view.findViewById(R.id.light_btn);
                        detectorAlarmBtn = view.findViewById(R.id.detector_alarm_btn);
                        tvOptionsInputSettingsBtn = view.findViewById(R.id.tv_options_input_settings_btn);

                        View.OnClickListener clickListener = v -> {
                            toggleButtonSelection((ImageView) v, activity);
                        };

                        audioBtn.setOnClickListener(clickListener);
                        textToSpeechBtn.setOnClickListener(clickListener);
                        audioFileBtn.setOnClickListener(clickListener);
                        lightBtn.setOnClickListener(clickListener);
                        detectorAlarmBtn.setOnClickListener(clickListener);
                        tvOptionsInputSettingsBtn.setOnClickListener(clickListener);
//                        currentSelectedBtn = audioBtn;
                    }
                })
                .show();
    }

    private void toggleButtonSelection(ImageView selectedBtn, AppCompatActivity activity) {
        // 当前时间
        long currentTime = SystemClock.elapsedRealtime();

        // 检查是否在防抖延迟时间内点击
        if (currentTime - lastClickTime < DEBOUNCE_DELAY_MS) {
            return; // 忽略点击
        }
        // 更新上次点击时间
        lastClickTime = currentTime;

        closeAllFloatingWindows();

        if (currentSelectedBtn != null) {
            if (currentSelectedBtn == selectedBtn) {
                currentSelectedBtn.setBackgroundResource(R.drawable.floating_bg_shape); // Deselect the button
                currentSelectedBtn = null;
                return;
            } else {
                currentSelectedBtn.setBackgroundResource(R.drawable.floating_bg_shape); // Reset background of previously selected button
            }
        }

        selectedBtn.setBackgroundResource(R.drawable.floating_bg_selected_shape); // Set background of selected button
        currentSelectedBtn = selectedBtn; // Update the currently selected button

        if (selectedBtn == audioBtn) {
            new FloatingAudioHelper().showFloatingAudio(selectedBtn.getContext(), () -> {
                changeCloseBackground();
            });
        } else if (selectedBtn == textToSpeechBtn) {
            new FloatingTextToSpeechHelper().showFloatingTextToSpeech(selectedBtn.getContext(), () -> {
                changeCloseBackground();
            });
        } else if (selectedBtn == audioFileBtn) {
            new FloatingAudioFileHelper().showFloatingAudioFile(activity, () -> {
                changeCloseBackground();
            });
        } else if (selectedBtn == lightBtn) {
            new FloatingLightHelper().showFloatingLight(selectedBtn.getContext(), () -> {
                changeCloseBackground();
            });
        } else if (selectedBtn == detectorAlarmBtn) {
            new FloatingDetectorHelper().showFloatingDetector(selectedBtn.getContext(), () -> {
                changeCloseBackground();
            });
        } else if (selectedBtn == tvOptionsInputSettingsBtn) {
            new FloatingSettingsHelper().showFloatingSettings(selectedBtn.getContext(), () -> {
                changeCloseBackground();
            });
        }
    }

    private void changeCloseBackground() {
        if (currentSelectedBtn != null) {
            currentSelectedBtn.setBackgroundResource(R.drawable.floating_bg_shape); // Deselect the button
            currentSelectedBtn = null;
        }
    }

    private void closeAllFloatingWindows() {
        // You should keep track of all tags or use a specific tag management strategy.
        EasyFloat.dismiss("audio_tag");
        EasyFloat.dismiss("text_to_speech_tag");
        EasyFloat.dismiss("audio_file_tag");
        EasyFloat.dismiss("light_tag");
        EasyFloat.dismiss("detector_alarm_tag");
        EasyFloat.dismiss("settings_tag");
    }

}
