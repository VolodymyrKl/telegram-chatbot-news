package com.finder.finder.helpers.impl.offchurch;

import com.darkprograms.speech.translator.GoogleTranslate;
import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.helpers.impl.news.RisuNewsHelper;
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
public class AlexandriaChurchHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static final int NEWS_COUNT = 11;
    private static Logger logger = LogManager.getLogger(AlexandriaChurchHelper.class);

    @Override
    public List<Item> getItems() {

        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from AlexandriaChurch");
            standardHttpResponse = super.getStandardHttpResponse("https://www.patriarchateofalexandria.com/el/activities");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            Elements entryBody = document.getElementsByClass("entry-body");
            List<Item> items = new ArrayList<>();
            for (int i = 1; i < NEWS_COUNT; i++) {
                Element element = entryBody.get(i);
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
        Elements entryTitle = getEntryTitle(element);
        item.setTitle("");
        try {
            item.setDescription(GoogleTranslate.translate("el", "uk", element.text()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        item.setLink(entryTitle.get(0).childNodes().get(0).attributes().get("href"));
    }

    private Elements getEntryTitle(Element element) {
        return element.getElementsByClass("entry-title");
    }

    private boolean isNewPublications(Element element) {
        String datetime = element.getElementsByClass("entry-date").get(0).attributes().get("datetime");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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