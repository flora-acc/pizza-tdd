package com.accenture.shared;

import java.time.LocalDate;

public record ErreurReponse(LocalDate localDate, String type, String message) {

}
