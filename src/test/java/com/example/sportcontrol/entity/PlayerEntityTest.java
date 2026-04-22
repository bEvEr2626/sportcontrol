package com.example.sportcontrol.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class PlayerEntityTest {

    @Test
    void gettersAndSettersWork() {
        Player player = new Player();
        Team team = new Team();
        team.setId(10L);
        team.setName("Spartak");

        player.setId(1L);
        player.setName("John");
        player.setTeam(team);

        assertEquals(1L, player.getId());
        assertEquals("John", player.getName());
        assertSame(team, player.getTeam());
    }

    @Test
    void equalsAndHashCodeUseIdField() {
        Player left = new Player();
        left.setId(7L);
        left.setName("Left");

        Player right = new Player();
        right.setId(7L);
        right.setName("Right");

        Player different = new Player();
        different.setId(8L);

        Player nullId = new Player();

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
        assertNotEquals(left, different);
        assertNotEquals(left, nullId);
        assertNotEquals(null, left);
        assertNotEquals("player", left);
        assertEquals(left, left);
        assertNull(nullId.getId());
    }
}
