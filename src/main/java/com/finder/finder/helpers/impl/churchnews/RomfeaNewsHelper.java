package com.finder.finder.helpers.impl.churchnews;

import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import com.finder.finder.service.DatePublicationService;
import com.finder.finder.service.ItemsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RomfeaNewsHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static Logger logger = LogManager.getLogger(RomfeaNewsHelper.class);

    private ItemsService itemsService;

    private DatePublicationService datePublicationService;

    @Override
    public List<Item> getItems() {
        logger.info("Starting to get news from Romfea");
        Document document = Jsoup.parse("https://www.romfea.gr/component/ninjarsssyndicator/?feed_id=1&format=raw");
        logger.info("News are received without issues, starting parsing.");

        Elements elements = document.select("item");

        List<Item> items = new ArrayList<>();
        for (Element element : elements) {
            if (datePublicationService.isTodayPublicationRss(element.select("pubdate").get(0).text())) {
                Item item = new Item();
                populateNewsItem(element, item);
                items.add(item);
            }
        }
        logger.info("Items have been parsed.");
        return items;
    }

    private void populateNewsItem(Element element, Item item) {
        logger.info("Filter items by translating.");

        item.setTitle(itemsService.translateItem("el", "uk", element.select("title").get(0).text()));
//        Elements description = element.select("description");
//        item.setDescription(description.get(0).text());
        item.setLink(element.select("guid").get(0).text());
    }

    @Autowired
    public void setDatePublicationService(DatePublicationService datePublicationService) {
        this.datePublicationService = datePublicationService;
    }

    @Autowired
    public void setItemsService(ItemsService itemsService) {
        this.itemsService = itemsService;
    }
}
