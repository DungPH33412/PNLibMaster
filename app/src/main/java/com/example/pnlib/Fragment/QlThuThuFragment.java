package com.example.pnlib.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pnlib.Adapter.ThuThuAdapter;
import com.example.pnlib.Dao.ThuThuDao;
import com.example.pnlib.Model.ThuThu;
import com.example.pnlib.R;

import java.util.ArrayList;



public class QlThuThuFragment extends Fragment {

    View view;
    ThuThuDao thuThuDao;
    RecyclerView recyclerView;
    ArrayList<ThuThu> list = new ArrayList<>();

    ThuThuAdapter thuThuAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ql_thu_thu, container, false);
        recyclerView = view.findViewById(R.id.RecyclerView_ThuThu);
        thuThuDao = new ThuThuDao(getContext());
        list = thuThuDao.SelectAll();
        thuThuAdapter = new ThuThuAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(thuThuAdapter);
        return view;
    }
}