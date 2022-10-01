package com.finder.finder.helpers.impl.offchurch;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UgccChurchHelper extends AbstractRequestSenderService implements ItemsHandler {
    private static Logger logger = LogManager.getLogger(UgccChurchHelper.class);
    private static final int NEWS_COUNT = 5;

    @Override
    public List<Item> getItems() {
        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from PolandChurch");
            standardHttpResponse = super.getStandardHttpResponse("https://ugcc.ua/data/rss/");
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());

            Elements elements = document.select("item");

            List<Item> itemFulls = new ArrayList<>();
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < NEWS_COUNT; i++) {
                Element element = elements.get(i);

                if (isNewPublications(element)) {
                    Item item = new Item();
                    populateNewsItem(element, item);
                    itemFulls.add(item);
                }
            }
            logger.info("Items have been parsed.");
            return itemFulls;
        }
        return new ArrayList<>();
    }

    private void populateNewsItem(Element element, Item item) {
        item.setTitle(element.select("title").get(0).text());
        String description = element.select("description").get(0).text();
        if (description.length() > 200) {
            item.setDescription(description.substring(0, 200));
        } else {
            item.setDescription(description);
        }
        item.setLink(element.childNodes().get(4).toString());
    }

    private boolean isNewPublications(Element element) {
        Elements pubdate = element.select("pubdate");
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        try {
            Date parsedDate = formatter.parse(pubdate.get(0).text());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = simpleDateFormat.format(parsedDate);

            LocalDate dateOfPublication = LocalDate.parse(formattedDate);
            LocalDate currentDate = LocalDate.now();

//            return dateOfPublication.isBefore(currentDate);
            return true;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
