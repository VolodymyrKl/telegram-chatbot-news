package com.finder.finder.helpers.impl.offchurch;

import com.darkprograms.speech.translator.GoogleTranslate;
import com.finder.finder.helpers.AbstractRequestSenderService;
import com.finder.finder.helpers.ItemsHandler;
import com.finder.finder.model.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BulgarianChurchHelper extends AbstractRequestSenderService implements ItemsHandler {
    @Override
    public List<Item> getItems() {

        HttpResponse<String> standardHttpResponse = null;
        try {
            standardHttpResponse = super.getStandardHttpResponse("https://bg-patriarshia.bg/news/");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());

            Elements elements = document.getElementsByClass("blog-content");

            List<Item> items = new ArrayList<>();
            for (Element element : elements) {
                if (isNewPublications(document, element, elements.size())) {
                    Item item = new Item();
                    populateNewsItem(element, item);
                    items.add(item);
                }
            }
            return items;
        }
        return new ArrayList<>();
    }

    private void populateNewsItem(Element element, Item item) {
        try {
            item.setTitle(GoogleTranslate.translate("bg", "uk", element.getElementsByClass("blog-title").text()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        item.setLink(getCurrentLink(element));
    }

    private boolean isNewPublications(Document document, Element element, int mainContentSize) {
        Elements content = document.getElementsByClass("post-date");

        for (int i = 0; i < content.size(); i++) {
            Element dateElement = content.get(i);
            if (getCurrentLink(element).equals(getCurrentLinkFromDate(dateElement)) && mainContentSize >= i) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm, dd.MM.yyyy");
                try {
                    return isCurrentDateEqualsToDateOfPublication(content.get(i), formatter);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private String getCurrentLinkFromDate(Element dateElement) {
        return dateElement.getElementsByClass("post-date").get(0).child(0).attributes().get("href");
    }

    private String getPubDate(Element dateElement) {
        return dateElement.getElementsByClass("post-date").get(0).child(0).text();
    }

    private boolean isCurrentDateEqualsToDateOfPublication(Element dateElement, SimpleDateFormat formatter) throws ParseException {
        Date parsedDate = formatter.parse(getPubDate(dateElement));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = simpleDateFormat.format(parsedDate);

        LocalDate dateOfPublication = LocalDate.parse(formattedDate);
        LocalDate currentDate = LocalDate.now();

//            return dateOfPublication.isBefore(currentDate);
        return true;
    }

    private String getCurrentLink(Element element) {
        return element.getElementsByAttribute("href").get(0).attr("href");
    }
}
