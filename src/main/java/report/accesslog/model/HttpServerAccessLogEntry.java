package report.accesslog.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class HttpServerAccessLogEntry {
    private static final String COMMA_DELIMITER = ",";
    private String path;
    private String userId;
    private LocalDate date;

    public HttpServerAccessLogEntry(String line) {
        String[] logEntry = line.split(COMMA_DELIMITER);

        if (!logEntry[0].isEmpty()) {
            this.path = logEntry[0];
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
        }

        if (!logEntry[1].isEmpty())
            this.userId = logEntry[1];

        if (!logEntry[2].isEmpty()) {
            String dateTimeWithoutMillis = logEntry[2].split("T")[0];
            this.date = LocalDate.parse(dateTimeWithoutMillis, DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    public String getPath() {
        return path;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpServerAccessLogEntry logEntry = (HttpServerAccessLogEntry) o;
        return Objects.equals(path, logEntry.path) &&
                Objects.equals(userId, logEntry.userId) &&
                Objects.equals(date, logEntry.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, userId, date);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HttpServerAccessLogEntry{");
        sb.append("path=").append(path)
                .append(", userId=").append(userId)
                .append(", date=").append(date);
        sb.append('}');
        return sb.toString();
    }
}

