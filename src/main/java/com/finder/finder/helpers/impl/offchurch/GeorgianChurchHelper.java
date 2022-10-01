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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GeorgianChurchHelper extends AbstractRequestSenderService implements ItemsHandler {

    @Override
    public List<Item> getItems() {

        HttpResponse<String> standardHttpResponse = null;
        try {
            standardHttpResponse = super.getStandardHttpResponse("https://patriarchate.ge/news/category/1");
        } catch (
                IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        if (standardHttpResponse != null) {
            Document document = Jsoup.parse(standardHttpResponse.body());
            Elements elements = document.getElementsByClass("q-item");
            List<Item> items = new ArrayList<>();
            for (Element element : elements) {
                if (isNewPublications(element)) {
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
        Elements entryTitle = element.getElementsByClass("m-0");
        try {
            item.setTitle(GoogleTranslate.translate("ka", "uk", entryTitle.text()));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        item.setLink(entryTitle.get(0).child(0).attr("href"));
    }

    private boolean isNewPublications(Element element) {
        String datetime = element.getElementsByClass("d-flex flex-column justify-content-between")
                .get(0).childNodes().get(3).attr("datetime");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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