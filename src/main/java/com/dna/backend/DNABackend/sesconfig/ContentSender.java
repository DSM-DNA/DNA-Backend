package com.dna.backend.DNABackend.sesconfig;

import java.util.Map;

public interface ContentSender {
    boolean sendMessage(String email, Map<String, String> params);
}
