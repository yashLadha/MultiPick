package io.github.yashladha.multipick;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String Tag = getClass().getSimpleName();
    private StorageReference storage;

    ArrayList<Uri> imagesUri;

    public ImageAdapter(ArrayList<Uri> imagesUri, StorageReference mStorageRef) {
        this.imagesUri = imagesUri;
        this.storage = mStorageRef;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutInflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_index, parent, false);
        RecyclerView.ViewHolder viewHolder = new ViewHolder(layoutInflater);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Uri content = imagesUri.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        ImageView iview = viewHolder.imageView;
        Glide.with(viewHolder.itemView.getContext())
                .load(content.getPath())
                .into(iview);
        Log.d(Tag, "Image view added");
        Log.d(Tag, holder.toString());
        Log.d(Tag, String.valueOf(viewHolder.itemView));

        Uri imageFile = Uri.fromFile(new File(content.toString()));
        StorageReference reference = storage.child("/images/" + imageFile.getLastPathSegment());
        UploadTask uploadTask = reference.putFile(imageFile);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                ProgressBar pBar = (ProgressBar) viewHolder.progressBar;
                pBar.setVisibility(View.VISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ProgressBar pBar = (ProgressBar) viewHolder.progressBar;
                pBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesUri.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivIndex);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
