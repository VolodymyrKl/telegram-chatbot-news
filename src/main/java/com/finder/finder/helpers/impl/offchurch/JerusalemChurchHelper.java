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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JerusalemChurchHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static Logger logger = LogManager.getLogger(JerusalemChurchHelper.class);

    @Override
    public List<Item> getItems() {

        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from JerusalemChurch");
            standardHttpResponse = super.getStandardHttpResponse("https://ru.jerusalem-patriarchate.info/blog");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        logger.info("News are received without issues, starting parsing.");
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            Elements content = document.getElementsByClass("content");
            List<Item> itemFulls = new ArrayList<>();
            for (Element element : content) {
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
        Elements entryTitle = getEntryTitle(element);
        item.setTitle(entryTitle.text());
        item.setLink(entryTitle.get(0).children().get(0).attr("href"));
    }

    private Elements getEntryTitle(Element element) {
        return element.getElementsByClass("title");
    }

    private boolean isNewPublications(Element element) {
        String datetime = element.getElementsByClass("top-meta date").text();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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