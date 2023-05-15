package com.finder.finder.helpers.impl.churchnews;

import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import com.finder.finder.model.Rss;
import com.finder.finder.service.DatePublicationService;
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
public class RisuNewsHelper extends AbstractRequestSenderService implements ItemsHandler {

    private static Logger logger = LogManager.getLogger(RisuNewsHelper.class);
//
//    {
//        months.put("Cічня", "01");
//        months.put("Лютого", "02");
//        months.put("Березня", "03");
//        months.put("Квітня", "04");
//        months.put("Травня", "05");
//        months.put("Червня", "06");
//        months.put("Липня", "07");
//        months.put("Серпня", "08");
//        months.put("Вересня", "09");
//        months.put("Жовтня", "10");
//        months.put("Листопада", "11");
//        months.put("Грудня", "12");
//    }
//


    private DatePublicationService datePublicationService;

    @Override
    public List<Item> getItems() {
        Rss rss = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            URL url = new URL("https://risu.ua/rss.xml");
            rss = (Rss) unmarshaller.unmarshal(url);
        } catch (MalformedURLException | JAXBException e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(rss)) {
            List<Item> items = rss.getChannel().getItems();
            return items.stream()
                    .filter(this::isTodayPublicationRss)
                    .collect(Collectors.toList());
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
}