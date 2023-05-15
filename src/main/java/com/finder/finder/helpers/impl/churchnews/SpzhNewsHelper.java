package com.finder.finder.helpers.impl.churchnews;

import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;

@Component
public class SpzhNewsHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static final String URL_START = "https://spzh.news";
    private static final int NEWS_COUNT = 10;
    private static Logger logger = LogManager.getLogger(SpzhNewsHelper.class);
    private Map<String, String> months = new HashMap<>();

    {
        months.put("Января", "Jan");
        months.put("Февраля", "Feb");
        months.put("Марта", "Mar");
        months.put("Апреля", "Apr");
        months.put("Мая", "May");
        months.put("Июня", "Jun");
        months.put("Июля", "Jul");
        months.put("Августа", "Aug");
        months.put("Сентября", "Sept");
        months.put("Октября", "Oct");
        months.put("Ноября", "Nov");
        months.put("Декабря", "Dec");
    }

    @Override
    public List<Item> getItems() {
        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from SPZH");
            standardHttpResponse = super.getStandardHttpResponse("https://spzh.news/ru/news");
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());

            Elements elements = document.getElementsByClass("content-tile");

            List<Item> items = new ArrayList<>();
            for (Element element : elements) {
                if (isNewPublications(element)) {
                    Item item = new Item();
                    populateNewsItem(element, item);
                    items.add(item);
                }
            }
            logger.info("Items have been parsed.");
            return items;
        }
        return new ArrayList<>();
    }

    private void populateNewsItem(Element element, Item item) {
        item.setTitle(element.text());
        item.setLink(URL_START + element.child(0).attr("href"));
    }

    private boolean isNewPublications(Element element) {
        Elements contentDate = element.getElementsByClass("content-tile__date");
        String originalDate = contentDate.first().childNodes().get(0).toString();
        String date = getDateOfPublic(originalDate);
        if (Objects.nonNull(date)) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
            try {
                Date parsedDate = formatter.parse(date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = simpleDateFormat.format(parsedDate);
                LocalDate dateOfPublication = LocalDate.parse(formattedDate);
                LocalDate currentDate = LocalDate.now();
                return !dateOfPublication.isBefore(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getDateOfPublic(String originalDate) {
        return months.entrySet()
                .stream()
                .filter(month -> originalDate.contains(month.getKey()))
                .findFirst()
                .map(month -> originalDate.replace(month.getKey(), getMonthYear(month)))
                .orElse(null);
    }

    private String getMonthYear(Entry<String, String> month) {
        return month.getValue() + " " + LocalDate.now().getYear();
    }

}
