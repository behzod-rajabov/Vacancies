package tuit.vacancies.uz;

import android.content.Context;

public class Common {
    public static String salaryFormat(String from, String to) {
        String s = "";
        if (!from.equals("")) {
            Float f = Float.parseFloat(from);

            if ((f / 1000000) > 1)
                s += f / 1000000 + " mln";
            else if (f / 1000 > 1)
                s += f / 1000 + " ming";
            else
                s += f;
            s += "dan ";
        }
        if (!to.equals("")) {
            Float t = Float.parseFloat(to);
            if ((t / 1000000) > 1)
                s += t / 1000000 + " mln";
            else if (t / 1000 > 1)
                s += t / 1000 + " ming";
            else
                s += t;
            s += "gacha";
        }

        s = s.replace(".0", "");
        s = s.replace(".", ",");
        return s;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
