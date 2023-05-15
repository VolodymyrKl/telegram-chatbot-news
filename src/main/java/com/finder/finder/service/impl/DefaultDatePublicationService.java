package com.finder.finder.service.impl;

import com.finder.finder.service.DatePublicationService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Service
public class DefaultDatePublicationService implements DatePublicationService {
    @Override
    public boolean isTodayPublicationRss(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        try {
            Date parsedDate = formatter.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");

            String formattedDate = simpleDateFormat.format(parsedDate);
            LocalDateTime dateTimeOfPublication = LocalDateTime.parse(formattedDate);

            if (dateTimeOfPublication.toLocalDate().isEqual(LocalDate.now())) {
                if (dateTimeOfPublication.isBefore(getMidDayDateTime())) {
                    return true;
                }
                if (dateTimeOfPublication.isAfter(getMidDayDateTime())) {
                    return true;
                }
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private LocalDateTime getMidDayDateTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0, 0, 0));
    }
}
