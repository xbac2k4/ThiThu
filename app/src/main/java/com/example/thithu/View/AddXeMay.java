package com.example.thithu.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thithu.Model.Response;
import com.example.thithu.Model.XeMay;
import com.example.thithu.R;
import com.example.thithu.Service.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddXeMay extends AppCompatActivity {

    private File file;
    EditText edtTen, edtGia, edtMoTa, edtMauSac;
    ImageView img_hinhanh;
    HttpRequest httpRequest = new HttpRequest();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_xe_may);

        img_hinhanh = findViewById(R.id.img_hinhanh);
        edtTen = findViewById(R.id.edt_ten_xe);
        edtGia = findViewById(R.id.edt_gia_ban);
        edtMoTa = findViewById(R.id.edt_mo_ta);
        edtMauSac = findViewById(R.id.edt_mau_sac);

        img_hinhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String , RequestBody> mapRequestBody = new HashMap<>();
                String _ten = edtTen.getText().toString().trim();
                String _gia = edtGia.getText().toString().trim();
                String _mota = edtMoTa.getText().toString().trim();
                String _mausac = edtMauSac.getText().toString().trim();
                if (_ten.isEmpty() || _gia.isEmpty() || _mausac.isEmpty()) {
                    Toast.makeText(AddXeMay.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Pattern.compile("^[a-zA-Z]+$").matcher(_mausac).matches()) {
                    Toast.makeText(AddXeMay.this, "Màu sắc không được có số", Toast.LENGTH_SHORT).show();
                    return;
                }

                mapRequestBody.put("ten_xe_ph44315", getRequestBody(_ten));
                mapRequestBody.put("mau_sac_ph44315", getRequestBody(_mausac));
                mapRequestBody.put("mo_ta_ph44315", getRequestBody(_mota));
                mapRequestBody.put("gia_ban_ph44315", getRequestBody(_gia));
                MultipartBody.Part multipartBodyPart;
                if (file !=  null) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                    multipartBodyPart = MultipartBody.Part.createFormData("image", file.getName(),requestFile);
                } else {
                    multipartBodyPart = null;
                }
                httpRequest.callAPI().addXeMayWithFileImage(mapRequestBody, multipartBodyPart).enqueue(new Callback<Response<XeMay>>() {
                    @Override
                    public void onResponse(Call<Response<XeMay>> call, retrofit2.Response<Response<XeMay>> response) {
                        if (response.isSuccessful()) {
                            Log.d("123123", "onResponse: " + response.body().getStatus());
                            if (response.body().getStatus() == 200) {
                                Log.d("Multi", String.valueOf(multipartBodyPart));
                                Toast.makeText(AddXeMay.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<XeMay>> call, Throwable t) {

                    }
                });
            }
        });
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"),value);
    }
    private void chooseImage() {
//        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        getImage.launch(intent);
//        }else {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//        }
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {

                        Intent data = o.getData();
                        Uri tempUri = data.getData();

                        if (data.getData() != null) {
                            // Trường hợp chỉ chọn một hình ảnh
                            Uri imageUri = data.getData();

                            tempUri = imageUri;
                            // Thực hiện các xử lý với imageUri
                            file = createFileFormUri(imageUri, "image" );
                        }

                        if (tempUri != null) {
                            Glide.with(AddXeMay.this)
                                    .load(tempUri)
                                    .thumbnail(Glide.with(AddXeMay.this).load(R.drawable.noimageicon))
                                    .centerCrop()
//                                    .circleCrop()
                                    .skipMemoryCache(true)
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true)
                                    .into(img_hinhanh);
                        }

                    }
                }
            });

    private File createFileFormUri (Uri path, String name) {
        File _file = new File(AddXeMay.this.getCacheDir(), name + ".png");
        try {
            InputStream in = AddXeMay.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) >0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " +_file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}