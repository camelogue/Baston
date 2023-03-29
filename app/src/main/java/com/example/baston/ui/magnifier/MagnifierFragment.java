package com.example.baston.ui.magnifier;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baston.MainActivity;
import com.example.baston.R;
import com.example.baston.databinding.FragmentMagnifierBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class MagnifierFragment extends Fragment {
    private static final int REQUEST_CODE = 22;
    private int RESULT_OK;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private FragmentMagnifierBinding binding;
    Button btnPictureCamera;
    ImageView imageViewCamera;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MagnifierViewModel galleryViewModel =
                new ViewModelProvider(this).get(MagnifierViewModel.class);

        binding = FragmentMagnifierBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMagnifier;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnPictureCamera = getView().findViewById(R.id.btncamera_id);
        imageViewCamera = getView().findViewById(R.id.imageview_camera);
        btnPictureCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(cameraIntent);
            }
        });
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Bundle extras = result.getData().getExtras();
                Uri imageUri;
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                WeakReference<Bitmap> result_1 = new WeakReference<>(Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth(), imageBitmap.getHeight(), false).copy(Bitmap.Config.RGB_565, true));
                Bitmap bm = result_1.get();
                imageUri = saveImage(bm, getActivity().getApplicationContext());
                imageViewCamera.setImageURI(imageUri);
            }
        });
    }

    // İmage saving func
    private Uri saveImage(Bitmap image, Context context){
        File imageFolder = new File(getActivity().getApplicationContext().getCacheDir(), "images");
        Uri uri = null;
        try{
            File file = new File(imageFolder, "captured_image.jpg");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.example.baston", file);

        }catch (IOException e) {
            e.printStackTrace();
        }

        return uri ;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}