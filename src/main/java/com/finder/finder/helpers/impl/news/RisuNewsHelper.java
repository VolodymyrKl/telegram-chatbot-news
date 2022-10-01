package com.finder.finder.helpers.impl.news;

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
import java.util.ArrayList;
import java.util.List;

@Component
public class RisuNewsHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static final String URL_START = "https://risu.ua";
    private static Logger logger = LogManager.getLogger(RisuNewsHelper.class);

    @Override
    public List<Item> getItems() {
        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from RISU");
            standardHttpResponse = super.getStandardHttpResponse("https://risu.ua/novini_t1");
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());

            Elements elements = document.getElementsByClass("card news-card");

            List<Item> items = new ArrayList<>();
            for (Element element : elements) {
                if (isNewPublications(element)) {
                    Item itemFull = new Item();
                    populateNewsItem(element, itemFull);
                    items.add(itemFull);
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
//        Elements pubdate = element.select("pubdate");
//        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
//        try {
//            Date parsedDate = formatter.parse(pubdate.get(0).text());
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String formattedDate = simpleDateFormat.format(parsedDate);
//
//            LocalDate dateOfPublication = LocalDate.parse(formattedDate);
//            LocalDate currentDate = LocalDate.now();
//
////            return dateOfPublication.isBefore(currentDate);
//            return true;
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
        return true;
    }
}
