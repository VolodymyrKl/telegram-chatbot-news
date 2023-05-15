package com.finder.finder.helpers.impl.general;

import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import com.finder.finder.model.Rss;
import com.finder.finder.service.DatePublicationService;
import com.finder.finder.service.ItemsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class GuardianHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static Logger logger = LogManager.getLogger(GuardianHelper.class);
    private DatePublicationService datePublicationService;
    private ItemsService itemsService;

    @Override
    public List<Item> getItems() {
        Rss rss = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            URL url = new URL("https://www.theguardian.com/international/rss");
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
                item.setTitle(itemsService.translateItem("en", "uk", item.getTitle()));
                item.setDescription(null);
//                item.setDescription(itemsService.translateItem("en", "uk", item.getDescription()));
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
    }
}
