package com.wx.seeworld.Fragment;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabFragmentAdapter extends FragmentPagerAdapter {

	private final String[] titles;
	private Context context;
	private List<Fragment> fragments;

	public TabFragmentAdapter(List<Fragment> fragments, String[] titles,
			FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
		this.fragments = fragments;
		this.titles = titles;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
}
