package arc.visitor.onBoard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import arc.visitor.HotelApp;
import arc.visitor.Auth.LoginActivity;
import arc.visitor.R;

public class OnBoardActivity extends AppCompatActivity {

    ViewPager viewPager;
    onBoardViewPagerAdapter onBoardViewPagerAdapter;
    LinearLayout linearLayout;
    TextView[] mDots;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(HotelApp.themeDark)
        {
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.LightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        viewPager = findViewById(R.id.viewpager);
        linearLayout = findViewById(R.id.bottom);
        button = findViewById(R.id.get_started);

        onBoardViewPagerAdapter = new onBoardViewPagerAdapter(this);
        viewPager.setAdapter(onBoardViewPagerAdapter);
        addDotsIndicator(0);

        viewPager.addOnPageChangeListener(viewListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void addDotsIndicator(int position)
    {
        mDots = new TextView[3];
        linearLayout.removeAllViews();

        for(int i = 0;i < mDots.length;i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(40);
            mDots[i].setTextColor(getColor(R.color.white));

            linearLayout.addView(mDots[i]);
        }
        if(mDots.length > 0)
        {
            mDots[position].setTextColor(getColor(R.color.app_theme_color));
        }
    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void openLoginActivity(View v)
    {
        Intent i = new Intent(OnBoardActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
