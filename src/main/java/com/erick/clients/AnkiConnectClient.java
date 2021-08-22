package com.erick.clients;

import com.erick.model.*;
import feign.RequestLine;

public interface AnkiConnectClient {
  @RequestLine("POST /")
  AnkiResultFindNotes queryNotes(AnkiRequestFindNotes request);

  @RequestLine("POST /")
  AnkiResultNotesInfo findNotesByIds(AnkiRequestNotesInfo request);

  @RequestLine("POST /")
  Object addNotes(AnkiRequestAddNotes request);

  @RequestLine("POST /")
  Object deleteNotesByIds(AnkiRequestDeleteNotes request);
}