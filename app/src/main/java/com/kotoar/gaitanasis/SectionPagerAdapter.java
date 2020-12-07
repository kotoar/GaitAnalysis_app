package com.kotoar.gaitanasis;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private FragmentManager mFragmentManager;
    private static final int[] tab_titles = new int[]{R.string.tab_text_gait, R.string.tab_text_ctrl};
    List<String> tags = new ArrayList<>();

    public SectionPagerAdapter(Context context, FragmentManager fragmentManager){
        super(fragmentManager);
        mFragmentManager = fragmentManager;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0){
            return new FragmentGait();
        }
        return new FragmentCtrl();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(tab_titles[position]);
        //return getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pageIndex) {
        //创建tag并存储到集合中
        String fragmentNameTag = makeFragmentName(container.getId(), pageIndex);
        if (!tags.contains(fragmentNameTag)) {
            tags.add(fragmentNameTag);
        }
        return super.instantiateItem(container, pageIndex);
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
