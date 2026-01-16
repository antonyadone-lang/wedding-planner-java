package com.wedding.util;

import com.wedding.model.Invitato;

@FunctionalInterface
public interface FiltroInvitato {
    boolean soddisfaCondizione(Invitato invitato);
}
