package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calc
{
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z");
    private static final long MINUTES_PER_DAY = 24 * 60;

    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) throws IllegalArgumentException
    {
        if(start == null || end == null || dailyRate == null)
        {
            throw new IllegalArgumentException("Null argument");
        }

        ZonedDateTime startTime = ZonedDateTime.parse(start, DATE_FORMATTER);
        ZonedDateTime endTime = ZonedDateTime.parse(end, DATE_FORMATTER);

        if(!endTime.isAfter(startTime))
        {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        Duration duration = Duration.between(startTime, endTime);
        long totalMinutes = duration.toMinutes();

        long fullDays = totalMinutes / MINUTES_PER_DAY;
        long remainingMinutes = totalMinutes % MINUTES_PER_DAY;

        BigDecimal result = dailyRate.multiply(BigDecimal.valueOf(fullDays));

        if(remainingMinutes > 0)
        {
            BigDecimal toAdd;
            long remainingHours = remainingMinutes / 60;

            if(remainingHours > 12)
            {
                toAdd = dailyRate;
            }
            else if(remainingHours > 8)
            {
                toAdd = dailyRate.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
            }
            else
            {
                toAdd = dailyRate.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
            }

            result = result.add(toAdd);
        }
        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
