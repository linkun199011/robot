package com.ustclin.petchicken.functiondisplay;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustclin.petchicken.utils.StatusBarUtils;
import com.ustclin.robot.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: LinKun EmployeeID:151750
 * email : linkun199011@163.com
 * created on: 2016/11/14 16:33
 * description:
 */
public class AbilitiesActivity extends AppCompatActivity {
    private List<View> viewList;//view数组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abilities);
        StatusBarUtils.setAbilitiesActivityStatusBarColor(this);
        viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        initData();

        if (viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }
    }

    /**
     * 初始化
     */
    private void initData() {
        LayoutInflater layout = getLayoutInflater();
        View view0 = layout.inflate(R.layout.function_activity, null);
        initView(view0, "baike");
        View view1 = layout.inflate(R.layout.function_activity, null);
        initView(view1, "calculation");
        View view2 = layout.inflate(R.layout.function_activity, null);
        initView(view2, "constellation");
        View view3 = layout.inflate(R.layout.function_activity, null);
        initView(view3, "joke");
        View view4 = layout.inflate(R.layout.function_activity, null);
        initView(view4, "answer");
        View view5 = layout.inflate(R.layout.function_activity, null);
        initView(view5, "poem");
        View view6 = layout.inflate(R.layout.function_activity, null);
        initView(view6, "story");
        View view7 = layout.inflate(R.layout.function_activity, null);
        initView(view7, "weather");

        viewList.add(view0);
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        viewList.add(view5);
        viewList.add(view6);
        viewList.add(view7);
    }

    /**
     * 初始化每一个View
     * @param view ViewGroup
     * @param type 功能的类型名称
     */
    private void initView(View view, String type) {
        switch (type) {
            case "baike":
                setValue(view, R.drawable.function_baike1, R.drawable.function_baike2, "查百科");
                break;
            case "calculation":
                setValue(view, R.drawable.function_calculation1, R.drawable.function_calculation2, "计算器");
                break;
            case "constellation":
                setValue(view, R.drawable.function_constellation1, R.drawable.function_constellation2, "查星座");
                break;
            case "joke":
                setValue(view, R.drawable.function_joke1, R.drawable.function_joke2, "讲笑话");
                break;
            case "answer":
                setValue(view, R.drawable.function_answer1, R.drawable.function_answer2, "知天下");
                break;
            case "poem":
                setValue(view, R.drawable.function_poem1, R.drawable.function_poem2, "背诗词");
                break;
            case "story":
                setValue(view, R.drawable.function_story1, R.drawable.function_story2, "讲故事");
                break;
            case "weather":
                setValue(view, R.drawable.function_weather1, R.drawable.function_weather2, "问天气");
                break;

            default:
                break;
        }
    }

    /**
     * 根据传入的参数，初始化View
     *
     * @param view         viewGroup
     * @param drawable1    第一个图片资源
     * @param drawable2    第二个图片资源
     * @param functionName 功能的名称
     */
    private void setValue(View view, int drawable1, int drawable2, String functionName) {
        ImageView imageView0 = (ImageView) view.findViewById(R.id.iv_example1);
        imageView0.setImageResource(drawable1);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.iv_example2);
        imageView1.setImageResource(drawable2);
        TextView textViewCal = (TextView) view.findViewById(R.id.title_function_name);
        textViewCal.setText(functionName);
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public int getCount() {
            return viewList.size();
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    };
}
