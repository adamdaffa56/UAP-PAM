package adam.kuliah.uap_pam.helper;

import java.util.List;

import adam.kuliah.uap_pam.model.Restaurant;

public interface RestaurantCallback {
    void onRestaurantListReceived(List<Restaurant> restaurantList);
    void onFailure(String errorMessage);
}
