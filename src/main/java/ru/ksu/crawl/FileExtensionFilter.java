package ru.ksu.crawl;

import com.torunski.crawler.filter.ILinkFilter;

/**
 * Created by IntelliJ IDEA.
 * User: Heit
 * Date: 13.03.2010
 * Time: 18:55:37
 */
public class FileExtensionFilter implements ILinkFilter{

    private String[] extensions;

    public FileExtensionFilter(String[] extensions) {
        this.extensions = extensions;
    }

    public boolean accept(String s, String s1) {
        Boolean result = true;
        for (String ext:extensions){
            if (s1.endsWith(ext)){
                result = false;
            }
        }
        return result;
    }
}
