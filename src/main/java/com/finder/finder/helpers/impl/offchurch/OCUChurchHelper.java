package com.finder.finder.helpers.impl.offchurch;

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
import java.util.ArrayList;
import java.util.List;

@Component
public class OCUChurchHelper extends AbstractRequestSenderService implements ItemsHandler {
    private static Logger logger = LogManager.getLogger(OCUChurchHelper.class);

    @Override
    public List<Item> getItems() {
        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from OCUChurch");
            standardHttpResponse = super.getStandardHttpResponse("https://www.pomisna.info/uk/category/vsi-novyny/");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            Elements elements = document.getElementsByTag("h3");
            List<Item> items = new ArrayList<>();
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);

                Elements dateElements = document.getElementsByClass("date-item");
                if (isNewPublications(dateElements.get(i))) {
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
        item.setLink(element.child(0).attr("href"));
    }

    private boolean isNewPublications(Element element) {
        String datetime = element.text();
        SimpleDateFormat formatter = new SimpleDateFormat("dd E yyyy");
//        try {
//            Date parsedDate = formatter.parse(datetime);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String formattedDate = simpleDateFormat.format(parsedDate);
//
//            LocalDate dateOfPublication = LocalDate.parse(formattedDate);
//            LocalDate currentDate = LocalDate.now();

//            return dateOfPublication.isBefore(currentDate);
        return true;
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
//        return false;
    }
}