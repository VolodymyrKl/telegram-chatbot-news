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
public class RussianChurchHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static final String URL_START = "http://www.patriarchia.ru";
    private static Logger logger = LogManager.getLogger(RussianChurchHelper.class);

    @Override
    public List<Item> getItems() {

        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from RussianChurch");
            standardHttpResponse = super.getStandardHttpResponse("http://www.patriarchia.ru/db/news/");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            List<Item> items = new ArrayList<>();

            populateNews(document, items, "top_news");
            populateNews(document, items, "news");

            logger.info("Items have been parsed.");
            return items;
        }
        return new ArrayList<>();
    }

    private void populateNews(Document document, List<Item> items, String news) {
        Elements content = document.getElementsByClass(news);
        for (Element element : content) {
            if (isNewPublications(element)) {
                Item item = new Item();

                populateNewsItem(element, item);

                items.add(item);
            }
        }
    }

    private void populateNewsItem(Element element, Item item) {
        Elements textElement = element.getElementsByClass("text");
        item.setTitle(element.getElementsByClass("title").text());
//        item.setDescription(textElement.text());
        item.setLink(URL_START + textElement.get(0).children().get(0).attr("href"));
    }

    private boolean isNewPublications(Element element) {
        String dateLink = element.getElementsByClass("news_img").get(0).child(0).child(0).attr("src");
        String datetime = dateLink.substring(25, 35);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date parsedDate = formatter.parse(datetime);
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