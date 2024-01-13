package it.cs.unicam.app_valorizzazione_territorio.model;

/**
 * This class represents relationship between a {@link Municipality} and a {@link Authorization}.
 * It is used to represent the role of a {@link User} in a {@link Municipality}.
 */
public record Role(Municipality municipality, Authorization authorization) { }
