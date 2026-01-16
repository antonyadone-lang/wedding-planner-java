package com.wedding.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvitatoTest {

    @Test
    void testCreazioneInvitato() {
        Invitato inv = new Invitato("Mario", "Rossi", "mario@test.com");

        assertEquals("Mario", inv.getNome());
        assertEquals("Rossi", inv.getCognome());
        assertEquals("mario@test.com", inv.getEmail());
        assertEquals(StatoInvitato.IN_ATTESA, inv.getStato());
    }
}