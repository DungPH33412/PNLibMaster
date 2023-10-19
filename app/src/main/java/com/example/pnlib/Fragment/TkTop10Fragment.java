package com.example.pnlib.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.pnlib.Adapter.Top10Adapter;
import com.example.pnlib.Dao.ThongKeDao;
import com.example.pnlib.Model.Top;
import com.example.pnlib.R;

import java.util.ArrayList;



public class TkTop10Fragment extends Fragment {
    View view;
    ListView listView;
    ThongKeDao thongKeDao;

    Top10Adapter top10Adapter;
    ArrayList<Top> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_top10, container, false);
        listView = view.findViewById(R.id.ListView_Top10);
        thongKeDao = new ThongKeDao(getContext());
        list = thongKeDao.getTop();
        top10Adapter = new Top10Adapter(getContext(), list);
        listView.setAdapter(top10Adapter);
        return view;
    }
}