package com.domus.hugemoviemock;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonPropertyOrder({ "page", "per_page", "total", "total_pages", "data" })
public class PageDTO {
    private int page;
    @JsonProperty("per_page") private int perPage;
    private int total;
    @JsonProperty("total_pages") private int totalPages;

    List<MovieDTO> data = new ArrayList<>();
}
