package org.roxomi.roxy.snapdiary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.roxomi.roxy.snapdiary.utils.PhotoInfo;
import org.roxomi.roxy.snapdiary.widget.CalendarItemView;
import org.roxomi.roxy.snapdiary.widget.CalendarView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Roxy on 2017-04-18.
 */
public class FrgCalendar extends Fragment {
    private int position;
    CalendarView calendarView;
    private long timeByMillis;
    private OnFragmentListener onFragmentListener;
    private View mRootView;

    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener {
        public void onFragmentListener(View view);
    }

    public static FrgCalendar newInstance(int position) {
        FrgCalendar frg = new FrgCalendar();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        return frg;
    }

    public FrgCalendar() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("poisition");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_calendar, null);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    protected void initView() {
        calendarView = (CalendarView) mRootView.findViewById(R.id.calendarview);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        calendar.set(Calendar.DATE, 1);
        // 1일의 요일
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //이달의 마지막 날
        int maxDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendarView.initCalendar(dayOfWeek, maxDateOfMonth);
        for (int i = 0; i < maxDateOfMonth + 7; i++) {
            CalendarItemView child = new CalendarItemView(getActivity().getApplicationContext());
//            if (i == 26) {
//                child.setEvent(R.color.colorPrimaryDark);
//            }
            child.setDate(calendar.getTimeInMillis());
            if (i < 7) {
                child.setDayOfWeek(i);
            } else {
                calendar.add(Calendar.DATE, 1);
            }
            calendarView.addView(child);
        }
        setEventDay(calendar);
    }

    private void setEventDay(Calendar calendar){
        int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(calendar.getTimeInMillis()));
        int month = Integer.parseInt(new SimpleDateFormat("MM").format(calendar.getTimeInMillis()));
        ArrayList<Integer> days = new PhotoInfo(getContext()).getDays(year, month);

        for(int i : days){
            CalendarItemView v = (CalendarItemView) calendarView.getChildAt(i+6);
            v.setEvent(R.color.colorPrimaryDark);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && onFragmentListener != null && mRootView != null) {
            onFragmentListener.onFragmentListener(mRootView);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {

            mRootView.post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    onFragmentListener.onFragmentListener(mRootView);
                }
            });

        }
    }

    public void setTimeByMillis(long timeByMillis) {
        this.timeByMillis = timeByMillis;
    }
}