package report.accesslog.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import report.accesslog.model.HttpServerAccessLogEntry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

public class HttpServerAccessLogProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger("KotlinAccessLogProcessorApp");
    private String logFileAbsolutePath;
    private static Function<String, HttpServerAccessLogEntry> logEntryConverter = HttpServerAccessLogEntry::new;
    private static Predicate<String> filterCriteria = line -> !line.startsWith("Path,User,Timestamp");

    public HttpServerAccessLogProcessor(String fileName) {
        this.logFileAbsolutePath = fileName;
    }

    public Map<LocalDate, List<HttpServerAccessLogEntry>> getLogEntriesGroupedByDate() {
        // read file as a stream
        // skip first HEADER line
        // filter all null entries
        // map line to HttpServerAccessLogEntry
        // group all entries by date

        Map<LocalDate, List<HttpServerAccessLogEntry>> logEntriesGroupedByDate = Collections.emptyMap();
        try (Stream<String> stream = Files.lines(Paths.get(logFileAbsolutePath))) {
            logEntriesGroupedByDate = stream.filter(filterCriteria)
                    .filter(Objects::nonNull)
                    .map(logEntryConverter)
                    .collect(groupingBy(HttpServerAccessLogEntry::getDate));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("logEntriesGroupedByDate = {}", logEntriesGroupedByDate);
            }
        } catch (IOException e) {
            LOGGER.error("{}", e);
        }
        return logEntriesGroupedByDate;
    }

    public List<String> getDailyPagesByUniqueHits(LocalDate date) {
        LOGGER.info("*** Invoking getDailyPagesByUniqueHits for date {}", date);

        Map<LocalDate, List<HttpServerAccessLogEntry>> logEntriesGroupedByDate = getLogEntriesGroupedByDate();
        List<HttpServerAccessLogEntry> entries = logEntriesGroupedByDate.get(date);
        if (entries != null) {
            return entries.stream().map(HttpServerAccessLogEntry::getPath).distinct().collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public Map<String, Long> getDailyPagesByNumberOfUsers(LocalDate date) {
        LOGGER.info("*** Invoking getDailyPagesByNumberOfUsers for date {}", date);

        Map<LocalDate, List<HttpServerAccessLogEntry>> logEntriesGroupedByDate = getLogEntriesGroupedByDate();
        Map<String, Long> dailyPagesByNumberOfUsers = Collections.emptyMap();

        List<HttpServerAccessLogEntry> entries = logEntriesGroupedByDate.get(date);
        if (entries != null) {
//            entries.stream().forEach(e -> {
//                dailyPagesByNumberOfUsers.computeIfAbsent(e.getPath(), v -> 0);
//                dailyPagesByNumberOfUsers.compute(e.getPath(), (key, value) -> value + 1);
//
//            });
            dailyPagesByNumberOfUsers = entries.stream().collect(groupingBy(HttpServerAccessLogEntry::getPath, counting()));
//            entries.stream().max(Comparator.comparingInt(e -> e.getPath().length()));
            entries.stream().collect(maxBy(comparing(e -> e.getPath().length())));

        }
        return dailyPagesByNumberOfUsers;
    }

    public Map<String, List<String>> getDailyUsersByUniquePageViews(LocalDate date) {
        LOGGER.info("*** Invoking getDailyUsersByUniquePageViews for date {}", date);
        Map<LocalDate, List<HttpServerAccessLogEntry>> logEntriesGroupedByDate = getLogEntriesGroupedByDate();
        Map<String, List<String>> usersByUniquePageViews = new HashMap<>();

        List<HttpServerAccessLogEntry> entries = logEntriesGroupedByDate.get(date);
        if (entries != null) {
            entries.stream().forEach(e ->
                usersByUniquePageViews.compute(e.getUserId(), (key, value) -> {
                    if (value == null) value = new ArrayList<>();
                    if (!value.contains(e.getPath())) {
                        value.add(e.getPath());
                    }
                    return value;
                })
            );
            return usersByUniquePageViews;
        }
        return Collections.emptyMap();
    }
}
