package com.erick;

import com.erick.clients.AnkiConnectClient;
import com.erick.scripts.ScriptAddChuugakkouKanjiMeaningsAndExampleWords;
import com.erick.scripts.ScriptReOrderDeckByKanjiFrequency;
import com.erick.services.AnkiConnectService;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;

import static com.erick.constants.AnkiConnectConstants.ANKI_CONNECT_URL;

public class Run {
    public static void main(String[] args) {
        var client = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(AnkiConnectClient.class, ANKI_CONNECT_URL);

        var service = new AnkiConnectService(client);
        new ScriptAddChuugakkouKanjiMeaningsAndExampleWords(service).run();
    }
}
