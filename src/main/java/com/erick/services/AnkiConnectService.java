package com.erick.services;

import com.erick.clients.AnkiConnectClient;
import com.erick.model.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.lang.String.format;

@RequiredArgsConstructor
public class AnkiConnectService {
    private final AnkiConnectClient client;

    public List<AnkiEntityNoteInfo> findNewNotes(String deck) {
        var query = format("deck:%s is:new -is:suspended", deck);
        return findNotes(query);
    }

    public List<AnkiEntityNoteInfo> findNotNewNotes(String deck) {
        var query = format("deck:%s -is:new -is:suspended", deck);
        return findNotes(query);
    }

    public List<AnkiEntityNoteInfo> findNotes(String query) {
        var request = buildFindNotesRequest(query);
        var noteIds = client.queryNotes(request).result;
        return client.findNotesByIds(buildFindNotesRequest(noteIds)).result;
    }

    public void addNotes(List<AnkiEntityAddNote> notesToAdd) {
        var addNoteRequest = buildAddNotesRequest(notesToAdd);
        var result = client.addNotes(addNoteRequest);
        System.out.println(result);
    }

    public void deleteNotesByIds(List<Long> noteIdsToDelete) {
        var result = client.deleteNotesByIds(buildDeleteNotesByIdsRequest(noteIdsToDelete));
        System.out.println(result);
    }

    private AnkiRequestFindNotes buildFindNotesRequest(String query) {
        return new AnkiRequestFindNotes(new AnkiParamsFindNotes(query));
    }

    private AnkiRequestNotesInfo buildFindNotesRequest(List<Long> noteIds) {
        return new AnkiRequestNotesInfo(new AnkiParamsNoteIds(noteIds));
    }

    private AnkiRequestAddNotes buildAddNotesRequest(List<AnkiEntityAddNote> notesToAdd) {
        var addNoteRequest = new AnkiRequestAddNotes(new AnkiParamsAddNotes());
        addNoteRequest.getParams().setNotes(notesToAdd);
        return addNoteRequest;
    }

    private AnkiRequestDeleteNotes buildDeleteNotesByIdsRequest(List<Long> noteIdsToDelete) {
        return new AnkiRequestDeleteNotes(new AnkiParamsNoteIds(noteIdsToDelete));
    }
}
