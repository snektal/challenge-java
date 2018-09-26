package report.accesslog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import report.accesslog.processor.HttpServerAccessLogProcessor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/report")
public class ReportController {

    @Autowired
    private HttpServerAccessLogProcessor logProcessor;

    @RequestMapping(method = RequestMethod.GET, value = "/unique-page-hits/{date}", produces = APPLICATION_JSON_VALUE)
    public List<String> getDailyPagesByUniqueHits(
            @PathVariable(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return logProcessor.getDailyPagesByUniqueHits(date);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/pages-by-number-of-users/{date}", produces = APPLICATION_JSON_VALUE)
    public Map<String, Long> getDailyPagesByNumberOfUsers(
            @PathVariable(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return logProcessor.getDailyPagesByNumberOfUsers(date);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/number-of-users/{date}/{page}", produces = APPLICATION_JSON_VALUE)
    public Long getDailyPagesByNumberOfUsers(
            @PathVariable(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable(name = "page") String page) {
        return logProcessor.getDailyPagesByNumberOfUsers(date).get(page);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all-users-unique-page-views/{date}", produces = APPLICATION_JSON_VALUE)
    public Map<String, List<String>> getDailyUsersByUniquePageViews(
            @PathVariable(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return logProcessor.getDailyUsersByUniquePageViews(date);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user-unique-page-views/{date}/{userId}", produces = APPLICATION_JSON_VALUE)
    public List<String> getDailyUsersByUniquePageViews(
            @PathVariable(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable(name = "userId") String userId) {
        return logProcessor.getDailyUsersByUniquePageViews(date).get(userId);
    }


}
