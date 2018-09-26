package report.log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import report.accesslog.processor.HttpServerAccessLogProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpServerAccessLogProcessorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger("HttpServerAccessLogProcessorTest");

    @Autowired
    private HttpServerAccessLogProcessor logProcessor;

    @Test
    public void getDailyPagesByUniqueHits_ForGivenDate_ShouldReturnFourDailyUniquePageHits() {
        LocalDate dateKey = LocalDate.parse("2017-09-29", DateTimeFormatter.ISO_LOCAL_DATE);
        LOGGER.info("date key passed: {}", dateKey);

        List<String> dailyPagesByUniqueHits = logProcessor.getDailyPagesByUniqueHits(dateKey);

        int expectedHits = 4;
        int actualHits = dailyPagesByUniqueHits.size();
        LOGGER.info("date key = {}", dateKey);
        LOGGER.info("expectedHits: {}, actualHits: {}", expectedHits, actualHits);
        assertEquals(expectedHits, actualHits);

    }

    @Test
    public void getDailyPagesByNumberOfUsers_ForGivenDateAndPage_ShouldReturnTwoUsers() {
        LocalDate dateKey = LocalDate.parse("2017-09-29", DateTimeFormatter.ISO_LOCAL_DATE);
        LOGGER.info("date key passed: {}", dateKey);
        Map<String, Long> dailyPagesByNumberOfUsers = logProcessor.getDailyPagesByNumberOfUsers(dateKey);
        String path = "index.html";
        LOGGER.info("page (path) passed: {}", path);

        long expectedNumberOfUsers = 2;
        long actualNumberOfUsers = dailyPagesByNumberOfUsers.get(path);

        LOGGER.info("expectedNumberOfUsers: {}, actualNumberOfUsers: {}", expectedNumberOfUsers, actualNumberOfUsers);
        assertEquals(expectedNumberOfUsers, actualNumberOfUsers);

    }

    @Test
    public void getDailyUsersByUniquePageViews_ForGivenDateAndUser_ShouldReturnOneDailyUniquePageViewByUser() {
        LocalDate dateKey = LocalDate.parse("2017-09-28", DateTimeFormatter.ISO_LOCAL_DATE);
        LOGGER.info("date key passed: {}", dateKey);
        Map<String, List<String>> usersByUniquePageViews = logProcessor.getDailyUsersByUniquePageViews(dateKey);

        String userIdKey = "04a5d9a7-0a76-47a8-abd3-9e39a1abce51";
        LOGGER.info("user ID key passed: {}", userIdKey);


        int expectedUniquePageViews = 1;
        int actualUniquePageViews = usersByUniquePageViews.get(userIdKey).size();

        LOGGER.info("expectedUniquePageViews: {}, actualHits: {}", expectedUniquePageViews, actualUniquePageViews);
        assertEquals(expectedUniquePageViews, actualUniquePageViews);

    }


}