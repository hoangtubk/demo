package vn.edu.hust.tuhoangbk.demo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_READ_CAPTURE = 101;

    ImageButton btnOpenCamera, btnOpenGallery, btnNext;
    ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySuKienClickChupAnh();
            }
        });
        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                xuLySuKienClickGallery();
                takePictureFromGallery();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySuKienNext();
            }
        });
    }

    private void xuLySuKienNext() {
        Toast.makeText(MainActivity.this, "Bạn đã ấn vào nút Next", Toast.LENGTH_LONG).show();
    }

    private void xuLySuKienClickGallery() {
        Toast.makeText(MainActivity.this, "Bạn đã ấn vào nút Open Gallery", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MediaStore.EXTRA_MEDIA_ALBUM);
        this.startActivityForResult(intent, REQUEST_ID_READ_CAPTURE);
    }

    private void takePictureFromGallery()
    {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_ID_READ_CAPTURE);
    }

    private void xuLySuKienClickChupAnh() {
        Toast.makeText(MainActivity.this, "Bạn đã ấn vào Open Camera", Toast.LENGTH_LONG).show();
        // Tạo một Intent không tường minh,
        // để yêu cầu hệ thống mở Camera chuẩn bị chụp hình.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Start Activity chụp hình, và chờ đợi kết quả trả về.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(image);
        }
        else if (requestCode == REQUEST_ID_READ_CAPTURE){

            Uri selectedImageUri = data.getData();
            String tempPath = getPath(selectedImageUri);
            Toast.makeText(MainActivity.this, "Path: " + tempPath, Toast.LENGTH_LONG).show();
            upLoadImageToServer(tempPath);
            imgView.setImageURI(selectedImageUri);
        }
        else {
            Toast.makeText(MainActivity.this, "Không làm gì cả", Toast.LENGTH_LONG).show();
        }
    }

    private void upLoadImageToServer(String tempPath) {
        File file = new File(tempPath);
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private void addControls() {
        btnOpenCamera = findViewById(R.id.btnOpenCamera);
        btnOpenGallery = findViewById(R.id.btnOpenGallery);
        btnNext = findViewById(R.id.btnNext);
        imgView = findViewById(R.id.imgView);
    }
}
