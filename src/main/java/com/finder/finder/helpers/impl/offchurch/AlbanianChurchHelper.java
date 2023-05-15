package com.finder.finder.helpers.impl.offchurch;

import com.darkprograms.speech.translator.GoogleTranslate;
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
public class AlbanianChurchHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static Logger logger = LogManager.getLogger(AlbanianChurchHelper.class);

    @Override
    public List<Item> getItems() {

        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from AlbanianChurch");
            standardHttpResponse = super.getStandardHttpResponse("https://orthodoxalbania.org/2020/en/feed/");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            Elements elements = document.getElementsByClass("recent-posts-content");
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
        Element entryTitle = element.getElementsByClass("entry-title").get(0);
        try {
            item.setTitle(GoogleTranslate.translate("el", "uk", entryTitle.getElementsByTag("a").text()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        item.setLink(entryTitle.getElementsByTag("a").attr("href"));

        String text = element.getElementsByTag("p").text();
        if (text != null) {
            try {
                item.setDescription(GoogleTranslate.translate("el", "uk", text));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private boolean isNewPublications(Element element) {
        String datetime = element.getElementsByClass("updated").text();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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