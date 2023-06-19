package adam.kuliah.uap_pam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class DetailRestaurant extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName, tvAddress, tvBusinessHour, tvDescription;
    private ImageView ivRestaurant, btn_back;
    private Button btnDetailEdit, btnDetailHapus;

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
        btnDetailEdit = findViewById(R.id.btnDetailEdit);
        btnDetailHapus = findViewById(R.id.btnDetailHapus);

        btn_back.setOnClickListener(this);
        btnDetailEdit.setOnClickListener(this);
        btnDetailHapus.setOnClickListener(this);

        Bundle restaurantBundle = getIntent().getBundleExtra("restaurantBundle");
        if (restaurantBundle != null) {
            String name = restaurantBundle.getString("name");
            String address = restaurantBundle.getString("address");
            String businessHour = restaurantBundle.getString("businessHour");
            String description = restaurantBundle.getString("description");
            String imageURL = restaurantBundle.getString("imageURL");

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnDetailEdit:
                Intent intent1 = new Intent(this, EditRestaurant.class);
                startActivity(intent1);
                break;
            case R.id.btnDetailHapus:
                //Fungsi Hapus
        }
    }
}