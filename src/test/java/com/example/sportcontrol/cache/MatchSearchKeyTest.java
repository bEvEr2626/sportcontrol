package com.example.sportcontrol.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.example.sportcontrol.dto.MatchFilter;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class MatchSearchKeyTest {

    @Test
    void constructorsProduceEquivalentKeysForSameData() {
        LocalDateTime from = LocalDateTime.of(2026, 5, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2026, 5, 31, 23, 59, 59);

        MatchFilter filter = new MatchFilter("Final", "Moscow", 2L, "Spartak", "Zenit", from, to);

        MatchSearchKey fromFilter = new MatchSearchKey(filter, 1, 20);
        MatchSearchKey allArgs = new MatchSearchKey("Final", "Moscow", 2L, "Spartak", "Zenit", from, to, 1, 20);

        assertEquals(fromFilter, allArgs);
        assertEquals(fromFilter.hashCode(), allArgs.hashCode());
    }

    @Test
    void equalsSupportsSelfNullAndDifferentType() {
        MatchSearchKey key = new MatchSearchKey("Final", "Moscow", 2L, "Spartak", "Zenit", null, null, 1, 20);

        assertEquals(key, key);
        assertNotEquals(null, key);
        assertNotEquals("not-a-key", key);
    }

    @Test
    void keysDifferWhenPagingOrFiltersDiffer() {
        LocalDateTime from = LocalDateTime.of(2026, 5, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2026, 5, 31, 23, 59, 59);

        MatchSearchKey base = new MatchSearchKey("Final", "Moscow", 2L, "Spartak", "Zenit", from, to, 1, 20);
        MatchSearchKey differentPage = new MatchSearchKey("Final", "Moscow", 2L, "Spartak", "Zenit", from, to, 2, 20);
        MatchSearchKey differentName = new MatchSearchKey("Semi", "Moscow", 2L, "Spartak", "Zenit", from, to, 1, 20);

        assertNotEquals(base, differentPage);
        assertNotEquals(base, differentName);
    }
}
