package io.github.yashladha.multipick;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private String TAG = getClass().getSimpleName();
    private Button selectBtn;
    private RecyclerView indexView;
    private ArrayList<Uri> dataSent;
    ImageAdapter adapter;

    private static final int INTENT_REQUEST_GET_IMAGES = 2;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        selectBtn = (Button) findViewById(R.id.btSelect);
        indexView = (RecyclerView) findViewById(R.id.rvIndex);
        dataSent = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        indexView.setLayoutManager(layoutManager);
        indexView.setHasFixedSize(true);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });
    }

    public void getImages() {
        Intent intent  = new Intent(this, ImagePickerActivity.class);
        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK ) {
            ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            for (Uri c : image_uris) {
                Log.d(TAG, c.toString());
            }
            if (dataSent.size() == 0) {
                dataSent.addAll(image_uris);
                adapter = new ImageAdapter(dataSent, mStorageRef);
                indexView.setAdapter(adapter);
                Log.d(TAG, dataSent.toString());
            } else {
                dataSent.addAll(image_uris);
                adapter.notifyDataSetChanged();
                Log.d(TAG, dataSent.toString());
            }
        }
    }
}
