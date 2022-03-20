package com.gexingw.shop.modules.oms.dto;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

@Data
public class OmsOrderSearchParam extends BaseSearchParam {
    private Long orderId;

    private Integer status;

    private Integer date;

    public String getDateStart() {
        if (date == null) {
            return null;
        }

        // 近3个月
        if (date == 1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -3);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return simpleDateFormat.format(calendar.getTime());
        }

        // 今年
        if (date == 2) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-01-01 00:00:00");
            return simpleDateFormat.format(new Date());

        }

        // 往年
        if (Pattern.matches("\\d{4}", date.toString())) {
            return date + "-01-01 00:00:00";
        }

        return null;
    }

    public String getDateEnd() {
        if (date == null) {
            return null;
        }

        // 往年
        if (Pattern.matches("\\d{4}", date.toString())) {
            return date + "-12-31 23:59:59";
        }

        return null;
    }
}
