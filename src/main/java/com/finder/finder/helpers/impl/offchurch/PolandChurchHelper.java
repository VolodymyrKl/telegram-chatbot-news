package com.finder.finder.helpers.impl.offchurch;

import com.darkprograms.speech.translator.GoogleTranslate;
import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import com.finder.finder.service.UpdateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PolandChurchHelper extends AbstractRequestSenderService implements ItemsHandler {
    private static Logger logger = LogManager.getLogger(PolandChurchHelper.class);

    private UpdateService updateService;

    @Override
    public List<Item> getItems() {
        HttpResponse<String> standardHttpResponse = null;
        try {
            logger.info("Starting to get news from PolandChurch");
            standardHttpResponse = super.getStandardHttpResponse("https://www.orthodox.pl/aktualnosci/feed/");
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
            for (Element element : elements) {
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
        try {
            item.setTitle(GoogleTranslate.translate("pl", "uk", element.select("title").get(0).text()));
            item.setDescription(GoogleTranslate.translate("pl", "uk", element.select("description").get(0).text()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Elements link = element.select("guid");
        item.setLink(link.get(0).text());
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

    @Autowired
    public void setUpdateService(UpdateService updateService) {
        this.updateService = updateService;
    }
}
