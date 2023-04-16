import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    private Queue<String> urlQueue;
    private List<String> visitedUrls;

    public WebCrawler() {
        urlQueue = new LinkedList<>();
        visitedUrls = new LinkedList<>();
    }

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        String rootURL = "https://fategrandorder.fandom.com/wiki/Fate/Grand_Order_Wikia";
        crawler.crawl(rootURL, 100);
    }

    public void crawl(String root, int breakPoint) {
        urlQueue.add(root);
        visitedUrls.add(root);

        while(!urlQueue.isEmpty()) {
            String tempUrl = urlQueue.remove();
            StringBuilder rawHTML = new StringBuilder();

            try {
                URL url = new URL(tempUrl);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String inputLine = in.readLine();

                while(inputLine != null) {
                    rawHTML.append(inputLine);

                    inputLine = in.readLine();
                }
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            String urlPattern = "(www|http:|https:)+[^\s]+[\\w]";
            Pattern pattern = Pattern.compile(urlPattern);
            Matcher matcher = pattern.matcher(rawHTML);

            breakPoint = getBReakpoint(breakPoint, matcher);

            if (breakPoint == 0) {
                break;
            }
        }
    }

    private int getBReakpoint(int breakPoint, Matcher matcher) {
        while(matcher.find()) {
            String actualUrl = matcher.group();

            if (!visitedUrls.contains(actualUrl)) {
                visitedUrls.add(actualUrl);
                System.out.println("Website found with URL" + actualUrl);
                urlQueue.add(actualUrl);
            }

            if (breakPoint == 0) {
                break;
            }
            breakPoint--;
        }

        return breakPoint;
    }
 }
