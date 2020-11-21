package ru.bez_createha.queue_bot.context;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class RawQueue {

    private String name;
    private Integer day_start;
    private Integer month_start;
    private Integer year_start;
    private Integer min_start;
    private Integer hrs_start;

    public Date buildDate() throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return ft.parse(year_start+"-"+month_start+"-"+day_start+" "+hrs_start+":"+min_start);
    }


}
