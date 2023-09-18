package com.stage.elearning.utility;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class CustomResponseList  <T> {
    private HttpStatus statusString;
    private long status;
    private LocalDateTime timestamp;
    private List<T> data;
    private long currentLength;
    private long total;

    public CustomResponseList(final HttpStatus statusString, final List<T> data , final long currentLength, final  long total)
    {
        this.statusString = statusString;
        this.status = statusString.value();
        this.timestamp = LocalDateTime.now();
        this.data = data;
        this.currentLength = currentLength;
        this.total = total;
    }


}
