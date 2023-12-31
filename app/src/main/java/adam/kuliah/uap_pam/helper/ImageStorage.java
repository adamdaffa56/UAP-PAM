package adam.kuliah.uap_pam.helper;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageStorage {

    private FirebaseStorage storage;
    private StorageReference storageRef, imageRef;
    private Uri file;

    public String getImageURL() {
        return imageURL;
    }
    private String imageURL;

    public ImageStorage() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public Task<Uri> uploadImage(Uri uriContent) {
        file = uriContent;
        imageRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = imageRef.putFile(file);

        return uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return imageRef.getDownloadUrl();
        }).addOnSuccessListener(uri -> {
            imageURL = uri.toString();
            Log.d("IMAGEURL", imageURL);
        }).addOnFailureListener(exception -> {
            Log.e("ERROR-UPLOAD", exception.getMessage());
        });
    }


}
