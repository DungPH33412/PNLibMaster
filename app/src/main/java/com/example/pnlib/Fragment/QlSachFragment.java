package com.example.pnlib.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pnlib.Adapter.SachAdapter;
import com.example.pnlib.Adapter.TheLoaiSpinner;
import com.example.pnlib.Dao.SachDao;
import com.example.pnlib.Dao.TheLoaiDao;
import com.example.pnlib.Interface.ItemClickListener;
import com.example.pnlib.Model.Sach;
import com.example.pnlib.Model.TheLoai;
import com.example.pnlib.R;

import java.util.ArrayList;


public class QlSachFragment extends Fragment {

    View view;

    RecyclerView recyclerView;

    SachDao sachDao;

    ArrayList<Sach> list = new ArrayList<>();

    EditText edt_maSach, edt_tenSach, edt_giathue;
    Spinner spinner_theLoai;

    TheLoaiDao theLoaiDao;

    int selectedPosition;
    TheLoaiSpinner theLoaiSpinner;

    ArrayList<TheLoai> list_TheLoai = new ArrayList<>();

    int maLoaiSach;

    SachAdapter sachAdapter;

    private boolean isChuoi(String str) {
        return str.matches("[a-zA-Z0-9]+");
    }

    private boolean isInteger(String str) {
        return str.matches("[\\d]+");
    }

    private static final String TAG = "QlSach";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sach, container, false);
        recyclerView = view.findViewById(R.id.RecyclerView_Sach);
        sachDao = new SachDao(getContext());
        list = sachDao.selectAll();
        sachAdapter = new SachAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sachAdapter);
        view.findViewById(R.id.fab_Sach).setOnClickListener(v -> {
            showAddOrUpdateDialog(getContext(), 0, null);
        });
        sachAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void UpdateItem(int position) {
                Sach sach = list.get(position);
                showAddOrUpdateDialog(getContext(), 1, sach);
            }
        });
        return view;
    }

    private void showAddOrUpdateDialog(Context context, int type, Sach sach) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_sach, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();

        edt_maSach = view1.findViewById(R.id.edt_maSach_dialog_sach);
        edt_tenSach = view1.findViewById(R.id.edt_tenSach_dialog_sach);
        edt_giathue = view1.findViewById(R.id.edt_giaSach_dialog_sach);
        spinner_theLoai = view1.findViewById(R.id.spinner_TheLoai_dialog_sach);
        edt_maSach.setEnabled(false);//vô hiệu hóa Edittext


        //Spinner Thể loại
        theLoaiDao = new TheLoaiDao(context);
        list_TheLoai = theLoaiDao.selectAll();
        theLoaiSpinner = new TheLoaiSpinner(context, list_TheLoai);
        spinner_theLoai.setAdapter(theLoaiSpinner);

        if (type != 0) {
            edt_maSach.setText(String.valueOf(sach.getMaSach()));
            edt_tenSach.setText(sach.getTenSach());
            edt_giathue.setText(String.valueOf(sach.getGiaThue()));
            for (int i = 0; i < list_TheLoai.size(); i++) {
                if (sach.getMaLoai() == list_TheLoai.get(i).getMaLoai()) {
                    selectedPosition = i;
                }
            }

            spinner_theLoai.setSelection(selectedPosition);
        }

        spinner_theLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maLoaiSach = list_TheLoai.get(position).getMaLoai();//khi click spinner gan ma vao maLoaiSach
                Log.e(TAG, "ClickSpinner: " + maLoaiSach);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view1.findViewById(R.id.btnSave_ql_Sach).setOnClickListener(v -> {
            String tenSach = edt_tenSach.getText().toString();
            String giaSach = edt_giathue.getText().toString();
            if (validate(tenSach, giaSach)) {
                if (type == 0) {
                    Sach sachNew = new Sach();
                    sachNew.setTenSach(tenSach);
                    sachNew.setGiaThue(Integer.parseInt(giaSach));
                    sachNew.setMaLoai(maLoaiSach);
                    try {
                        if (sachDao.insertData(sachNew)) {
                            Toast.makeText(context, R.string.add_success, Toast.LENGTH_SHORT).show();
                            list.add(sachNew);
                            sachAdapter.notifyDataSetChanged();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, R.string.add_not_success, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi Database: ", e);
                        Toast.makeText(context, R.string.add_not_success, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sach.setTenSach(tenSach);
                    sach.setGiaThue(Integer.parseInt(giaSach));
                    sach.setMaLoai(maLoaiSach);
                    Log.e(TAG, "showAddOrUpdateDialog: " + maLoaiSach);
                    try {
                        if (sachDao.Update(sach)) {
                            Toast.makeText(context, R.string.edit_success, Toast.LENGTH_SHORT).show();
                            updateList();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, R.string.edit_not_success, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi Database: ", e);
                        Toast.makeText(context, R.string.edit_not_success, Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
        view1.findViewById(R.id.btnCancle_ql_Sach).setOnClickListener(v -> {
            clearFrom();
            alertDialog.dismiss();
        });
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private boolean validate(String tenSach, String giaSach) {
        try {
            if (tenSach.isEmpty() || giaSach.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng cung cấp đủ thông tin", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Xẩy ra lỗi", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void updateList() {
        list.clear();
        list.addAll(sachDao.selectAll());
        sachAdapter.notifyDataSetChanged();
    }

    private void clearFrom() {
        edt_maSach.setText("");
        edt_giathue.setText("");
        edt_tenSach.setText("");
    }
}
