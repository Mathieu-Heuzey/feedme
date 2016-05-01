package mobile.feedme;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Mateo on 01/05/2016.
 */
public class Checker {

    public static boolean CheckMdp(String password1, String password2)
    {
        if (password1.equals(password2))
            return true;
        else
            return false;
    }

    public static boolean CheckMail(String email)
    {
        if (TextUtils.isEmpty(email))
        {
            return false;
        }
        else
        {
            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                return true;
            else
                return false;
        }
    }

}