package com.donfyy.crowds.viewpager;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.donfyy.crowds.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewPagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerFragment newInstance(String param1, String param2) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        view.findViewById(R.id.jump).setOnClickListener(v -> {
            viewPager.setCurrentItem((viewPager.getCurrentItem() + 2) % viewPager.getAdapter().getCount(), false);
        });
        viewPager.setPageTransformer(true, new ZoomOutSlideTransformer1());
        viewPager.setAdapter(new PagerAdapterExample(Arrays.asList(Color.YELLOW, Color.BLUE, Color.RED, Color.CYAN)));
        int firstItemPosition = 2;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mPositionBeforeScroll = firstItemPosition;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    if (viewPager.getCurrentItem() != mPositionBeforeScroll) {
                        int currentItem = viewPager.getCurrentItem();
                        mPositionBeforeScroll = currentItem;

                        if (currentItem == 1) {
                            int item = viewPager.getAdapter().getCount() - firstItemPosition - 1;
                            viewPager.setCurrentItem(item, false);

                        } else if (currentItem == viewPager.getAdapter().getCount() - 2) {
                            viewPager.setCurrentItem(firstItemPosition, false);
                        }
                    }
                }
            }
        });
        viewPager.setCurrentItem(firstItemPosition, false);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static class PagerAdapterExample extends PagerAdapter {

        private List<Integer> mData;
        private List<Integer> mOriginData;
        private ViewPager.OnPageChangeListener mOnPageChangeListener;

        public PagerAdapterExample(List<Integer> mData) {
            mOriginData = mData;
            if (mData.size() >= 3) {
                ArrayList<Integer> data = new ArrayList<>(mData);
                data.addAll(0, mData.subList(mData.size() - 2, mData.size()));
                data.addAll(mData.subList(0, 2));
                this.mData = data;
            } else {
                this.mData = mData;
            }

        }

        private int getOriginPosition(int position) {
            if (position <= 1) {
                return position + mOriginData.size() - 2;
            } else if (position >= mData.size() - 2) {
                return position - mData.size() + 2;
            } else {
                return position - 2;
            }
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            View view = inflater.inflate(R.layout.fragment_view_pager_item, container, false);
            view.setBackgroundColor(mData.get(position));
            view.setTag(R.id.position, position);
            TextView textView = view.findViewById(R.id.number);
            textView.setText(String.valueOf(getOriginPosition(position)));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(((View) object));
        }
    }
}
