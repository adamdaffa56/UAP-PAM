package adam.kuliah.uap_pam;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class DetailRestaurant extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName, tvAddress, tvBusinessHour, tvDescription;
    private ImageView ivRestaurant, btn_back;

    private String restaurantName, imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);

        tvName = findViewById(R.id.tvRestoran);
        tvAddress = findViewById(R.id.tvAlamat);
        tvBusinessHour = findViewById(R.id.tvJam);
        tvDescription = findViewById(R.id.tvDeskripsi);
        ivRestaurant = findViewById(R.id.ivDetailRestoran);

        btn_back = findViewById(R.id.btn_back);


        btn_back.setOnClickListener(this);

        btnLihatMaps = findViewById(R.id.btnLihatMaps);
        btnLihatMaps.setOnClickListener(this);

        Bundle restaurantBundle = getIntent().getBundleExtra("restaurantBundle");
        if (restaurantBundle != null) {
            String name = restaurantBundle.getString("name");
            String address = restaurantBundle.getString("address");
            String businessHour = restaurantBundle.getString("businessHour");
            String description = restaurantBundle.getString("description");
            String imageURL = restaurantBundle.getString("imageURL");

            latitude = restaurantBundle.getString("latitude");
            longitude = restaurantBundle.getString("longitude");

            tvLatitude.setText(latitude);
            tvLongitude.setText(longitude);

            tvName.setText(name);
            tvAddress.setText(address);
            tvBusinessHour.setText(businessHour);
            tvDescription.setText(description);

            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_restaurant)
                    .error(R.drawable.ic_error);

            Glide.with(this)
                    .load(imageURL)
                    .apply(requestOptions)
                    .into(ivRestaurant);
        }
    }

    // Download image
    private void downloadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        try {
            // Create storage reference
            StorageReference storageRef = storage.getReferenceFromUrl(imgUrl);

            File localFile;
            File storagePath = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "");

            if (!storagePath.exists()) {
                storagePath.mkdirs();
            }

            if(storagePath.exists()) {
                localFile = storagePath;
            } else {
                localFile = getApplication().getExternalFilesDir("images");
            }

            final File myFile = new File(localFile, restaurantName + ".jpg");

            storageRef.getFile(myFile).addOnSuccessListener(taskSnapshot -> {
                // Local file berhasil created
                Toast.makeText(this, "File downloaded", LENGTH_SHORT).show();
            }).addOnFailureListener(exception -> {
                // Handle errors
                Toast.makeText(getBaseContext(), "Download failed. Try again!", LENGTH_SHORT).show();
                exception.printStackTrace();
            });


            new Handler().postDelayed(() -> {
                ContentValues values = new ContentValues();

                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                values.put(MediaStore.Images.Media.DISPLAY_NAME, restaurantName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.MediaColumns.DATA, myFile.getAbsolutePath());
                getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }, 1000);
            ContentValues values = new ContentValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLihatMaps:
                Intent intentmap = new Intent(this, MapsActivity.class);
                intentmap.putExtra("latitude", latitude);
                intentmap.putExtra("longitude", longitude);
                startActivity(intentmap);
                break;
        }
    }
}