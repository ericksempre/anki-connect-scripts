package com.erick.clients;

import com.erick.model.anki.AnkiRequestAddNotes;
import com.erick.model.anki.AnkiRequestDeleteNotes;
import com.erick.model.anki.AnkiRequestFindNotes;
import com.erick.model.anki.AnkiRequestNotesInfo;
import com.erick.model.anki.AnkiResultFindNotes;
import com.erick.model.anki.AnkiResultNotesInfo;
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
