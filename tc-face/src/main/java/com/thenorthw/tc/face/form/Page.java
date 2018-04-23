package com.thenorthw.tc.face.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by theNorthW on $date.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
public class Page {
    @Pattern(regexp = "[1-9]\\d*")
    @NotNull
    String pageNumber;

    @Pattern(regexp = "[1-9]\\d*")
    @NotNull
    String pageSize;

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
