package ru.bez_createha.queue_bot.context;

import lombok.Data;

import java.util.Date;

@Data
public class RawQueue {

    private String name;
    private Integer day_start;
    private Integer month_start;
    private Integer year_start;
    private Integer min_start;
    private Integer hrs_start;

    public Date buildDate(){
        return new Date(year_start, month_start, day_start, hrs_start, min_start);
    }


}
