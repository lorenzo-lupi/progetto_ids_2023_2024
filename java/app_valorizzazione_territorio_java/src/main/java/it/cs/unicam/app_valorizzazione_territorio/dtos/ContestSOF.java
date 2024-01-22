package it.cs.unicam.app_valorizzazione_territorio.dtos;

import java.util.Date;
public record ContestSOF(
        String topic,
        Date date,
        long ID) {
    public long getID() {
        return this.ID();
    }
}
