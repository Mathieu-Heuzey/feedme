package mobile.feedme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.telerik.android.common.DateTimeExtensions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import mobile.feedme.POCO.Utilisateur;

public class AddMeal extends MenuActivity {
    static final int TIME_DIALOG_ID = 1111;   static final int TIME_DIALOG_ID_END = 2222; static final int TIME_DIALOG_EXP = 3333;
    private TextView output;
    private TextView output2;
    public Button btnClick;
    public Button btnClick2;
    private int hour;
    private int minute;
    private int select = 0;
    Calendar myCalendar = Calendar.getInstance();
    String ExpirDate;
    boolean isExpOk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setMenuItemEnabled(MenuActivity.SELLDISH, false);
        super.initialize(R.layout.activity_add_meal, true, true);
        super.setTitle("Sell your dish");
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

        EditText Address = (EditText) (findViewById(R.id.editTextTitre));
        Address.setError("Your address is required, street + number");
        EditText PostalCode = (EditText) (findViewById(R.id.TFdesc));
        PostalCode.setError("Your Postal Code is required");
        EditText name = (EditText) (findViewById(R.id.DishName));
        name.setError("The name of your dish is required");
        EditText Desc = (EditText) (findViewById(R.id.DishDescription));
        Desc.setError("The description of your dish is required");
        EditText nbPart = (EditText) (findViewById(R.id.DishNumberPotion));
        nbPart.setError("The number of portions available is required");
        EditText weight = (EditText) (findViewById(R.id.DishWeight));
        weight.setError("The weight of a portion Code is required");
        EditText price = (EditText) (findViewById(R.id.DishPrice));
        price.setError("The price of a potion is required");

        updateLabel(new GregorianCalendar());
    }

    public void submitDate(View vies)
    {
        new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar);
        }
    };

    private void updateLabel(Calendar calendar) {

        Date  tmp = calendar.getTime();
        ExpirDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE).format(tmp);

        Calendar myCalendar = Calendar.getInstance();
        Date tmp2 = myCalendar.getTime();

        if (tmp.compareTo(tmp2) == -1)
            isExpOk = false;
        else
            isExpOk = true;

        TextView output3 = (TextView) findViewById(R.id.output3);
        output3.setText("Your dish will expire : " + new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(tmp));
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
//            case  TIME_DIALOG_EXP:
//                select = 3;
//                return new TimePickerDialog(this, timePickerListener, hour, minute,
//                        true);
            case TIME_DIALOG_ID:

                // set time picker as current time
                select = 1;
                Log.d("select = ", String.valueOf(select));
                return new IntervalTimePickerDialog(this, timePickerListener, hour, minute,
                        true);
            case TIME_DIALOG_ID_END:

                // set time picker as current time
                select = 2;
                Log.d("select = ", String.valueOf(select));
                return new IntervalTimePickerDialog(this, timePickerListener, hour, minute,
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

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).toString();
        if (select == 0) {
            Log.d("select = ", String.valueOf(select));
            Log.d("dans le premier if ", String.valueOf(select));

            output.setText(aTime);
            output2.setText(aTime);
        }
     if (select == 1) {
            Log.d("Dans le if", String.valueOf(select));

            Log.d("select = ", String.valueOf(select));

            output.setText(aTime);
        }
        else {
            Log.d("Dans le else", String.valueOf(select));

            Log.d("select = ", String.valueOf(select));
            output2.setText(aTime);
        }
    }


    public void ExpireDate(View view)
    {
//        return new TimePickerDialog(this, timePickerListener, hour, minute, true);
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
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
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

        EditText Address = (EditText) (findViewById(R.id.editTextTitre));
        String Addr = Address.getText().toString();
        EditText PostalCode = (EditText) (findViewById(R.id.TFdesc));
        String Cp = PostalCode.getText().toString();
        EditText name = (EditText) (findViewById(R.id.DishName));
        String Dish = name.getText().toString();
        EditText Desc = (EditText) (findViewById(R.id.DishDescription));
        String Descr = Desc.getText().toString();
        EditText nbPart = (EditText) (findViewById(R.id.DishNumberPotion));
        String PortionNumer = nbPart.getText().toString();
        EditText weight = (EditText) (findViewById(R.id.DishWeight));
        String DishWeight = weight.getText().toString();
        EditText price = (EditText) (findViewById(R.id.DishPrice));
        String PortionPrice = price.getText().toString();
        CheckBox monday = (CheckBox) (findViewById(R.id.CheckBoxeMonday));
        CheckBox tursday = (CheckBox) (findViewById(R.id.CheckBoxeTuesday));
        CheckBox wenesday = (CheckBox) (findViewById(R.id.CheckBoxeWenesday));
        CheckBox thursday = (CheckBox) (findViewById(R.id.CheckBoxeThursday));
        CheckBox friday = (CheckBox) (findViewById(R.id.CheckBoxeFriday));
        CheckBox saturday = (CheckBox) (findViewById(R.id.CheckBoxeSaturday));
        CheckBox sunday = (CheckBox) (findViewById(R.id.CheckBoxeSunday));
        TextView PickStart = (TextView) (findViewById(R.id.output));
        String TimerStart = PickStart.getText().toString();
        TextView PickEnd = (TextView) (findViewById(R.id.output2));
        String TimerEnd = PickEnd.getText().toString();
        String days;//

        if (monday.isChecked())
            days = "1";
        else
            days = "0";

        if (tursday.isChecked())
            days += "1";
        else
            days += "0";
        if (wenesday.isChecked())
            days += "1";
        else
            days += "0";
        if (thursday.isChecked())
            days += "1";
        else
            days += "0";
        if (friday.isChecked())
            days += "1";
        else
            days += "0";
        if (saturday.isChecked())
            days += "1";
        else
            days += "0";
        if (sunday.isChecked())
            days += "1";
        else
            days += "0";

        if (Addr.isEmpty())
        {
            Address.setError("Your address is required, street + number");
        }
        if (Cp.isEmpty())
        {
            PostalCode.setError("Your Postal Code is required");
        }
        if (Dish.isEmpty())
        {
            name.setError("The name of your dish is required");
        }
        if (Descr.isEmpty())
        {
            Desc.setError("The description of your dish is required");
        }
        if (PortionNumer.isEmpty())
        {
            nbPart.setError("The number of portions available is required");
        }
        if (PortionPrice.isEmpty())
        {
            price.setError("The price of a potion is required");
        }

        if (DishWeight.isEmpty())
        {
            weight.setError("The weight of a portion Code is required");
        }



        if (Addr.isEmpty())
        {
            Toast.makeText(this, "Your address is required", Toast.LENGTH_LONG).show();
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            return;
        }
        String pattern = "^(?=.*[a-zA-Z])(?=.*[0-9]).{7,}";
        if(!Addr.matches(pattern))
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "Your address isn't correct. Your need to inform the street name and your number", Toast.LENGTH_LONG).show();
            return;
        }

        if (ExpirDate.isEmpty())
        {
            Toast.makeText(this, "You need to set an expiration date", Toast.LENGTH_LONG).show();
            return;
         }
        if (Cp.isEmpty())
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "Your CP is required", Toast.LENGTH_LONG).show();
            return;
        }
        if (Cp.length() != 5)
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "Your PC isn't correct, a normal one has 5 number", Toast.LENGTH_LONG).show();
            return;
        }
        if (Dish.isEmpty())
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "Your dish name is required", Toast.LENGTH_LONG).show();
            return;
        }
        String pattern2 = "[a-zA-Z àâçèéêîôùû]+";
        if(!Dish.matches(pattern2))
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "The name of your dish can only contains letters", Toast.LENGTH_LONG).show();
            return;
        }
        if (Descr.isEmpty())
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "Your description is required", Toast.LENGTH_LONG).show();
            return;
        }
//        if (Desc.length() < 35)
//        {
//            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
//            sv.scrollTo(sv.getScrollY(), sv.getTop());
//            Toast.makeText(this, "Your description cannot be lower than 35 characters", Toast.LENGTH_LONG).show();
//            return;
//        }

        if (PortionNumer.isEmpty())
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "Your number of portions is required", Toast.LENGTH_LONG).show();
            return;
        }

//        if (Integer.parseInt(PortionNumer) > 15)
//        {
//            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
//            sv.scrollTo(sv.getScrollY(), sv.getTop());
//            Toast.makeText(this, "Your cannot make more than 15 portions for one Dish", Toast.LENGTH_LONG).show();
//            return;
//        }
        if (DishWeight.isEmpty())
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "The weight of your portions is required", Toast.LENGTH_LONG).show();
            return;
        }
//        if (Integer.parseInt(DishWeight) > 300)
//        {
//            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
//            sv.scrollTo(sv.getScrollY(), sv.getTop());
//            Toast.makeText(this, "Your cannot sell potion over 300 grammes", Toast.LENGTH_LONG).show();
//            return;
//        }
        if (PortionPrice.isEmpty())
        {
            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
            sv.scrollTo(sv.getScrollY(), sv.getTop());
            Toast.makeText(this, "The price of your portions is required", Toast.LENGTH_LONG).show();
            return;
        }

//        if (Integer.parseInt(PortionPrice) > 25)
//        {
//            ScrollView sv = (ScrollView) findViewById(R.id.scrolltest);
//            sv.scrollTo(sv.getScrollY(), sv.getTop());
//            Toast.makeText(this, "Your cannot sell potion over 25 €", Toast.LENGTH_LONG).show();
//            return;
//        }

        if (isExpOk == false)
        {
            Toast.makeText(this, "Your expiration date can't be in the past", Toast.LENGTH_LONG).show();
            return;
        }

        DateFormat formatIn = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        DateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.FRANCE);
        JSONObject params = new JSONObject();
        JSONObject adress = new JSONObject();
        try {
            adress.put("Road", Addr);
            adress.put("PostalCode", Cp);
            adress.put("Country", "France");
//            adress.put("Road", Addr);
//            adress.put("Road", Addr);

            params.put("Name", Dish);
            params.put("Price", Double.parseDouble(PortionPrice));
            params.put("Description", Descr);
            params.put("Address", adress);
            params.put("NbPart", Integer.parseInt(PortionNumer));
            params.put("SizePart", Integer.parseInt(DishWeight));
            params.put("Days", days);
            params.put("DateExpiration", ExpirDate);
            Date pickupStartDate = formatIn.parse(TimerStart);
            params.put("PickUpStartTime", formatOut.format(pickupStartDate));
            Date pickupEndDate = formatIn.parse(TimerEnd);
            params.put("PickUpEndTime", formatOut.format(pickupEndDate));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ImageView image = (ImageView) findViewById(R.id.ivImage);
        BitmapDrawable bitmapDrawable = ((BitmapDrawable)image.getDrawable());
        Api.addMealRequest(this, params, bitmapDrawable == null ? null : bitmapDrawable.getBitmap());
    }
}