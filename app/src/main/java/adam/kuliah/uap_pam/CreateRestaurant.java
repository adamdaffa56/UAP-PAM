package adam.kuliah.uap_pam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import adam.kuliah.uap_pam.helper.ImageStorage;
import adam.kuliah.uap_pam.helper.RestaurantDatabase;

public class CreateRestaurant extends AppCompatActivity implements View.OnClickListener {

    private ImageStorage imageStorage;
    private EditText etName, etAddress, etBusinessHour, etDescription, etLongitude, etLatitude;
    private Button btnCreate, btnHapus;
    private ImageView btnBack,  btnChooseImage;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        imageStorage = new ImageStorage();

        etName = findViewById(R.id.etNamaRestoran);
        etAddress = findViewById(R.id.etAlamat);
        etBusinessHour = findViewById(R.id.etJamBukaTutup);
        etDescription = findViewById(R.id.etDeskripsi);
        btnBack = findViewById(R.id.btn_back);
        btnHapus = findViewById(R.id.btn_hapus);
        btnCreate = findViewById(R.id.btnCreate);
        btnChooseImage = findViewById(R.id.upload_image);

        etLongitude = findViewById(R.id.etLongitude);
        etLatitude = findViewById(R.id.etLatitude);

        btnCreate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnHapus.setOnClickListener(this);
        btnChooseImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreate:
                String name = etName.getText().toString();
                String address = etAddress.getText().toString();
                String businessHour = etBusinessHour.getText().toString();
                String description = etDescription.getText().toString();
                String longitude = etLongitude.getText().toString();
                String latitude = etLatitude.getText().toString();
//                String longitudeString = etLongitude.getText().toString();
//                String latitudeString = etLatitude.getText().toString();
//                double longitude = Double.parseDouble(longitudeString);
//                double latitude = Double.parseDouble(latitudeString);

                if (!validateForm(name, address, businessHour, description))
                    return;

                Thread thread = new Thread(() -> {
                    String imageURL = imageStorage.getImageURL();
                    RestaurantDatabase database = new RestaurantDatabase();
                    database.addRestaurant(name, address, businessHour, description, imageURL, longitude, latitude);
                });
                thread.start();
                finish();
                break;

            case R.id.upload_image:
                selectImage();
                break;

            case R.id.btn_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_hapus:
                etName.setText("");
                etAddress.setText("");
                etBusinessHour.setText("");
                etDescription.setText("");
                etLongitude.setText("");
                etLatitude.setText("");
                break;
        }
    }

    private boolean validateForm(String name, String address, String businessHour, String description) {
        if (TextUtils.isEmpty(name)) {
            etName.setError("Masukkan nama restoran");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Masukkan alamat restoran");
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Masukkan deskripsi restoran");
            return false;
        }

        if (TextUtils.isEmpty(businessHour)) {
            etBusinessHour.setError("Masukkan jam buka-tutup restoran");
            return false;
        }

        return true;
    }

    private void selectImage() {
        Thread thread = new Thread(() -> {
            Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            imageIntent.setType("image/*");
            imageIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(imageIntent, "Select Image"), PICK_IMAGE_REQUEST);
        });
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            Thread thread = new Thread(() -> {
                if (selectedImageUri != null) {
                    imageStorage.uploadImage(selectedImageUri);
                }
            });
            thread.start();
            runOnUiThread(() -> btnChooseImage.setImageURI(selectedImageUri));
        }
    }
}

