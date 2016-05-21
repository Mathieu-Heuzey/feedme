package mobile.feedme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMeal extends MenuActivity {

    public Api api = new Api();
    static final int TIME_DIALOG_ID = 1111;   static final int TIME_DIALOG_ID_END = 2222;
    private TextView output;
    private TextView output2;
    public Button btnClick;
    public Button btnClick2;
    private int hour;
    private int minute;
    private int select = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.initialize(R.layout.activity_add_meal, true, true);
        output = (TextView) findViewById(R.id.output);
        output2 = (TextView) findViewById(R.id.output2);
        /********* display current time on screen Start ********/

        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        updateTime(hour, minute);

        /********* display current time on screen End ********/

        // Add Button Click Listener
        addButtonClickListener();
    }

    public void addButtonClickListener() {

        btnClick = (Button) findViewById(R.id.btnHourPicker);

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });


        btnClick2 = (Button) findViewById(R.id.btnHourPickerEnd);

        btnClick2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID_END);
            }
        });

    }

    @Override
    protected TimePickerDialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:

                // set time picker as current time
                select = 1;
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);

            case TIME_DIALOG_ID_END:

                // set time picker as current time
                select = 2;
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        true);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);

        }

    };

    private static String utilTime(int value) {

        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        if (select == 0) {
            output.setText(aTime);
            output2.setText(aTime);
        }
    else if (select == 1)
        output.setText(aTime);
        else
        output2.setText(aTime);
    }

    public void uploadImage(View view) {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
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
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        Bitmap resized = Bitmap.createScaledBitmap(photo, 400, 400, true);
        ImageView ImageView = (ImageView) findViewById(R.id.ivImage);
        ImageView.setImageBitmap(resized);
    }

    public void saveMeal(View view) {

        EditText titre = (EditText) (findViewById(R.id.editTextTitre));
        String Addr = titre.getText().toString();
        EditText desc = (EditText) (findViewById(R.id.TFdesc));
        String Cp = desc.getText().toString();
        EditText prix = (EditText) (findViewById(R.id.DishName));
        String Dish = prix.getText().toString();
        EditText poid = (EditText) (findViewById(R.id.DishDescription));
        String Descr = poid.getText().toString();
        EditText nbPart = (EditText) (findViewById(R.id.DishNumberPotion));
        String PortionNumer = poid.getText().toString();
        EditText weight = (EditText) (findViewById(R.id.DishWeight));
        String DishWeight = poid.getText().toString();
        EditText price = (EditText) (findViewById(R.id.DishPrice));
        String PortionPrice = poid.getText().toString();
        CheckBox monday = (CheckBox) (findViewById(R.id.CheckBoxeMonday));
        CheckBox tursday = (CheckBox) (findViewById(R.id.CheckBoxeTuesday));
        CheckBox wenesday = (CheckBox) (findViewById(R.id.CheckBoxeWenesday));
        CheckBox thursday = (CheckBox) (findViewById(R.id.CheckBoxeThursday));
        CheckBox friday = (CheckBox) (findViewById(R.id.CheckBoxeFriday));
        CheckBox saturday = (CheckBox) (findViewById(R.id.CheckBoxeSaturday));
        CheckBox sunday = (CheckBox) (findViewById(R.id.CheckBoxeSunday));
        TextView PickStart = (TextView) (findViewById(R.id.output));
        String TimerStart = poid.getText().toString();
        TextView PickEndt = (TextView) (findViewById(R.id.output2));
        String TimerEnd = poid.getText().toString();

//appel a l'api
        
    }
}