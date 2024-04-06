package com.example.thithu.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thithu.Model.XeMay;
import com.example.thithu.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterXeMay extends RecyclerView.Adapter<AdapterXeMay.ViewHolder>{
    private final Context context;
    private final ArrayList<XeMay> list;

    public AdapterXeMay(Context context, ArrayList<XeMay> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = list.get(position).getHinh_anh_ph44315();
        String newUrl = url.replace("localhost", "10.0.2.2");
        Glide.with(context)
                .load(newUrl)
                .thumbnail(Glide.with(context).load(R.drawable.noimageicon))
                .into(holder.imgHinhanh);
        holder.tvID.setText("ID: " + list.get(position).get_id());
        holder.tvTenXe.setText("Tên xe:" + list.get(position).getTen_xe_ph44315());
        holder.tvMauSac.setText("Màu sắc: " + list.get(position).getMau_sac_ph44315());
        holder.tvGiaBan.setText("Giá bán: " + list.get(position).getGia_ban_ph44315());
        holder.tvMoTa.setText("Mô tả: " + list.get(position).getMo_ta_ph44315());
        holder.tvID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XeMay xeMay = list.get(position);
                Dialog_ChiTiet(xeMay);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvTenXe, tvMauSac, tvGiaBan, tvMoTa;
        ImageView imgHinhanh;
        ImageButton edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tv_id);
            imgHinhanh = itemView.findViewById(R.id.img_hinh_anh);
            tvTenXe =  itemView.findViewById(R.id.tv_ten_xe);
            tvGiaBan = itemView.findViewById(R.id.tv_gia_ban);
            tvMauSac = itemView.findViewById(R.id.tv_mau_sac);
            tvMoTa = itemView.findViewById(R.id.tv_mo_ta);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }
    private void Dialog_ChiTiet(XeMay xeMay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_chitiet, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView img_hinhanh = view.findViewById(R.id.img_hinh_anh);
        TextView tv_ten = view.findViewById(R.id.tv_ten_xe);
        TextView tv_gia = view.findViewById(R.id.tv_gia_ban);
        TextView tv_mausac = view.findViewById(R.id.tv_mau_sac);
        TextView tv_mota = view.findViewById(R.id.tv_mo_ta);
        TextView tv_id = view.findViewById(R.id.tv_id);

        String url = xeMay.getHinh_anh_ph44315();
        String newUrl = url.replace("localhost", "10.0.2.2");
        Glide.with(context)
                .load(newUrl)
                .thumbnail(Glide.with(context).load(R.drawable.noimageicon))
                .into(img_hinhanh);
        tv_id.setText("ID: " + xeMay.get_id());
        tv_ten.setText("Tên xe: " + xeMay.getTen_xe_ph44315());
        tv_gia.setText("Màu sắc: " + xeMay.getGia_ban_ph44315());
        tv_mausac.setText("Màu sắc: " + xeMay.getMau_sac_ph44315());
        tv_mota.setText("Mô tả: " + xeMay.getMo_ta_ph44315());

        view.findViewById(R.id.btn_dong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
