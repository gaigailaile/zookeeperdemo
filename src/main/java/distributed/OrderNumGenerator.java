package distributed;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumGenerator {

    private static int count = 0;

    public String getOrderNumber(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return sdf.format(new Date()) + "-" + (++count);
    }
}
