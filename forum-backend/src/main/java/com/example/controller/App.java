package com.example.controller;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

import com.algolia.search.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {
    public static void main(String[] args) throws Exception {
        URL url = new URI("https://dashboard.algolia.com/sample_datasets/movie.json").toURL();

        InputStream stream = url.openStream();
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> result = mapper.readValue(stream, new TypeReference<List<JsonNode>>() {});
        stream.close();

        SearchClient client = DefaultSearchClient.create("QVO19583C4", "92a4d57bb761aecdd052e0856db48229");

        SearchIndex<JsonNode> index = client.initIndex("your_index_name", JsonNode.class);

        index.saveObjects(result, true);
    }
}