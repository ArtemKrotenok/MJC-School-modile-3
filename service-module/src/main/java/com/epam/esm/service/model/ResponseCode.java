package com.epam.esm.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    UNKNOWN(1500, "sorry, something went wrong (try again)"),
    NOT_FOUND(1100, "requested resource not found"),
    NOT_VALID_INPUT_DATA(1400, "not valid input data"),
    NOT_UPDATE(1402, "error update data"),
    NOT_DELETE(1403, "error delete data"),
    NOT_CREATE(1404, "error create data"),
    CREATE(1201, "data create successfully"),
    UPDATE(1202, "data update successfully"),
    DELETE(1203, "data delete successfully");

    private int code;
    private String message;
}
