package com.kitkat.android.butterknife;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** Dependency Injection (의존성 주입)
 *
 *  Dependency Injection, DI 는 객체지향 프로그래밍에서 구성요소 (객체) 사이의 의존 관계가 소스코드 상이 아닌
 *  외부의 설정파일 (Bean Context..) 등을 통해 정의하고 참조시키는 디자인 패턴
 *
 *  ButterKnife
 *      Field and Method binding for Android Views
 */
public class MainActivity extends AppCompatActivity {

    // View Binding → @BindView(int value)
    @BindView(R.id.textView) TextView textView;
    @BindView(R.id.imageView) ImageView imageView;

    // Resource Binding → @Bind..()
    @BindString(R.string.app_name) String string;
    @BindDrawable(R.drawable.img) Drawable image;

    // View List → @BindViews(int value)
    @BindViews({R.id.textViewYear, R.id.textViewMonth, R.id.textViewDate})
    List<TextView> textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife.bind(View view);
        // Reflection 대신 ButterKnife 는 bind() Method 코드로 VIew 조회
        ButterKnife.bind(this);
        textView.setText(string);
    }

    // Listener Binding
    @OnClick({R.id.btnTime, R.id.btnImage})
    public void onClick(Button button) {
        switch (button.getId()) {
            case R.id.btnTime:
                ButterKnife.apply(textViews, TIME);
                break;
            case R.id.btnImage:
                imageView.setImageDrawable(image);
                break;
        }
    }

    // ButterKnife Action Interface for ButterKnife.apply() Method
    static final ButterKnife.Action<View> TIME = new ButterKnife.Action<View>() {
        @Override
        @TargetApi(Build.VERSION_CODES.N)
        public void apply(@NonNull View view, int index) {
            TextView textView = (TextView) view;

            Date now = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            String nowString = dateFormat.format(now);

            String[] arr = nowString.split("-");

            switch (view.getId()) {
                case R.id.textViewYear:
                    textView.setText(arr[0]);
                    break;

                case R.id.textViewMonth:
                    textView.setText(arr[1]);
                    break;

                case R.id.textViewDate:
                    textView.setText(arr[2]);
                    break;
            }
        }
    };
}
