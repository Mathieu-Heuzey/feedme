package mobile.feedme;

/**
 * Created by stevy_000 on 5/28/2016.
 */
public class Tag {
    public static final int DISH = 1;
    public static final int ORDER_CANCEL = 2;
    public static final int ORDER_CONFIRM = 3;
    public static final int ORDER_REFUSE = 4;
    public static final int ORDER_CODEVALIDATION = 5;

    public int Type;
    public Object Content;

    public Tag(int type, Object content)
    {
        this.Type = type;
        this.Content = content;
    }
}
