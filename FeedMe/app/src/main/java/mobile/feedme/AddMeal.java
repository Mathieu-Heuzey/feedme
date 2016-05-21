package mobile.feedme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddMeal extends MenuActivity {

    public Api api = new Api();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.initialize(R.layout.activity_add_meal, true, true);
    }

    public void uploadImage(View view) {

        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMeal.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, 0);

                    Context context = getApplicationContext();
                    CharSequence text = "ON OUVRE APPARAEIL FOTO";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);

                    Context context = getApplicationContext();
                    CharSequence text = "ON OUVRE LA GALERIE";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else if (items[item].equals("Cancel")) {
                    Context context = getApplicationContext();
                    CharSequence text = "ON FERME LA CHECKBOX";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
        builder.show();


//        EditText titre = (EditText) (findViewById(R.id.editTextTitre));
//        String titreStr = titre.getText().toString();
//
//        EditText desc = (EditText) (findViewById(R.id.textViewDescription));
//        String descStr = desc.getText().toString();
//
//        EditText prix = (EditText) (findViewById(R.id.textViewPrice));
//        String prixStr = prix.getText().toString();
//
//        EditText poid = (EditText) (findViewById(R.id.textViewHeight));
//        String poidStr = poid.getText().toString();
//
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//
//        Log.d("ADebugTag", "Value: " + titre);
//        Log.d("ADebugTag", "Value: " + desc);
//        Log.d("ADebugTag", "Value: " + prix);
//        Log.d("ADebugTag", "Value: " + poid);
//        Log.d("ADebugTag", "Value: " + longitude);
//        Log.d("ADebugTag", "Value: " + latitude);
//
//        List<String> data = new ArrayList<String>();
////        this.api.addMeal(data);
//        Handler handler = new Handler();
//        int millisDelay = 2000;
//        handler.postDelayed(task, millisDelay);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1)
                onSelectFromGalleryResult(data);
            else if (requestCode == 0)
                onCaptureImageResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            String imgDecodableString;
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            ImageView ImageView = (ImageView) findViewById(R.id.ivImage);
            ImageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
        }
        catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        Bitmap resized = Bitmap.createScaledBitmap(photo, 400, 400, true);
        ImageView ImageView = (ImageView) findViewById(R.id.ivImage);
        ImageView.setImageBitmap(resized);
    }
}
