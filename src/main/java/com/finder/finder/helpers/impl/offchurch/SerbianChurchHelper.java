package com.finder.finder.helpers.impl.offchurch;

import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import com.finder.finder.model.Rss;
import com.finder.finder.service.DatePublicationService;
import com.finder.finder.service.ItemsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class SerbianChurchHelper extends AbstractRequestSenderService implements ItemsHandler {
    private static Logger logger = LogManager.getLogger(SerbianChurchHelper.class);
    private DatePublicationService datePublicationService;
    private ItemsService itemsService;

    @Override
    public List<Item> getItems() {
        Rss rss = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            URL url = new URL("https://spc.rs/feed/");
            rss = (Rss) unmarshaller.unmarshal(url);
        } catch (MalformedURLException | JAXBException e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(rss)) {
            List<Item> items = rss.getChannel().getItems();
            List<Item> filteredItems = items.stream()
                    .filter(this::isTodayPublicationRss)
                    .collect(Collectors.toList());
            for (Item item : filteredItems) {
                item.setTitle(itemsService.translateItem("sr", "uk", item.getTitle()));
//                item.setDescription(itemsService.translateItem("sr", "uk", item.getDescription()));
                item.setDescription(null);
            }
            return filteredItems;
        }
        logger.info("Items have been parsed.");
        return List.of();
    }

    private boolean isTodayPublicationRss(Item item) {
        return datePublicationService.isTodayPublicationRss(item.getPubDate());
    }

    @Autowired
    public void setDatePublicationService(DatePublicationService datePublicationService) {
        this.datePublicationService = datePublicationService;
    }

    @Autowired
    public void setItemsService(ItemsService itemsService) {
        this.itemsService = itemsService;
    }}