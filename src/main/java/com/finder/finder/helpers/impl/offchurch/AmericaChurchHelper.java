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
public class AmericaChurchHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static final int MAX_SIZE_STRING = 120;
    private static final String TREE_POINTS = "...";
    private static Logger logger = LogManager.getLogger(AmericaChurchHelper.class);

    // 404 ????

    @Override
    public List<Item> getItems() {
        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from RISU");
            standardHttpResponse = super.getStandardHttpResponse("https://www.oca.org/news/feed");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            Elements elements = document.select("item");
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
        try {
            item.setTitle(GoogleTranslate.translate("en", "uk", element.select("title").text()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        item.setLink(element.textNodes().get(2).text());
        String text = element.select("description").text();
        if (text.length() > MAX_SIZE_STRING) {
            populateDescription(item, text.substring(0, MAX_SIZE_STRING) + "" + TREE_POINTS);
        } else {
            populateDescription(item, text);
        }
    }

    private void populateDescription(Item item, String text) {
        try {
            item.setDescription(GoogleTranslate.translate("en", "uk", text));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private boolean isNewPublications(Element element) {
        String datetime = element.select("pubdate").text();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
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