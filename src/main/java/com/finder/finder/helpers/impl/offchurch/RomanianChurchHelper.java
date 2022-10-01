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
public class RomanianChurchHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static final String URL_START = "https://ziarullumina.ro";
    private static final int NEWS_COUNT = 15;
    private static Logger logger = LogManager.getLogger(RomanianChurchHelper.class);

//    https://basilica.ro/flux-stiri/

    @Override
    public List<Item> getItems() {

        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from RomanianChurch");
            standardHttpResponse = super.getStandardHttpResponse("https://www.ziarullumina.ro/actualitate-religioasa/stiri/");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            Elements elements = document.getElementsByClass("related cols2").get(0).child(0).getElementsByClass(" ");
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < NEWS_COUNT; i++) {
                Element element = elements.get(i);
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
        Elements entryTitle = element.select("h2");
        try {
            item.setTitle(GoogleTranslate.translate("ro", "uk", entryTitle.text()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
//        item.setDescription(element.select("p").text());
        item.setLink(URL_START + entryTitle.get(0).child(0).attr("href"));
    }

    private boolean isNewPublications(Element element) {
        String datetime = element.getElementsByClass("date").text();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");
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