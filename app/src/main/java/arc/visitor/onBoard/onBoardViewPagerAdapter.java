package arc.visitor.onBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;

import arc.visitor.R;

public class onBoardViewPagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    Button button;
    Animation animation;

    private String[] animations = {"events.json","get_notified.json","relax.json"};

    private String[] titles = {"Events","Notices","Relax"};

    private String[] descs = {"All the academic events will be at your finger tips there's no way you're gonna loose them",
    "Any academic notifications! don't worry we are on it always",
    "Let us do all the things you need, just relax"};

    public onBoardViewPagerAdapter(Context context) {
        this.context = context;
        animation = AnimationUtils.loadAnimation(context,R.anim.appearence_animation);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboard_event_layout,container,false);
        button = view.findViewById(R.id.get_started);

        LottieAnimationView lottieAnimationView = view.findViewById(R.id.lottie_animation);
        TextView title = view.findViewById(R.id.title);
        TextView desc = view.findViewById(R.id.desc);

        title.setText(titles[position]);
        desc.setText(descs[position]);
        lottieAnimationView.setAnimation(animations[position]);

        if(position == 2) {
            button.setVisibility(View.VISIBLE);
            button.startAnimation(animation);
        }
        else
            button.setVisibility(View.GONE);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
