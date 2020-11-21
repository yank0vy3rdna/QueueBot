package ru.bez_createha.queue_bot.context;

import lombok.Data;

@Data
public class RawQueue {

    private String name;
    private Integer day_start;
    private Integer month_start;
    private Integer year_start;
    private Integer min_start;
    private Integer hrs_start;
    private Integer day_end;
    private Integer month_end;
    private Integer year_end;
    private Integer min_end;
    private Integer hrs_end;

}
