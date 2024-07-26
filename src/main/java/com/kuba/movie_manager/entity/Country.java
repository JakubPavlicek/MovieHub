package com.kuba.movie_manager.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Country {
    UNITED_STATES("United States"),
    CANADA("Canada"),
    UNITED_KINGDOM("United Kingdom"),
    AUSTRALIA("Australia"),
    GERMANY("Germany"),
    FRANCE("France"),
    ITALY("Italy"),
    SPAIN("Spain"),
    JAPAN("Japan"),
    CHINA("China"),
    INDIA("India"),
    BRAZIL("Brazil"),
    RUSSIA("Russia"),
    MEXICO("Mexico"),
    SOUTH_KOREA("South Korea"),
    SOUTH_AFRICA("South Africa"),
    NETHERLANDS("Netherlands"),
    SWEDEN("Sweden"),
    NORWAY("Norway"),
    DENMARK("Denmark"),
    FINLAND("Finland"),
    NEW_ZEALAND("New Zealand"),
    ARGENTINA("Argentina"),
    BELGIUM("Belgium"),
    SWITZERLAND("Switzerland");

    private final String name;
}
