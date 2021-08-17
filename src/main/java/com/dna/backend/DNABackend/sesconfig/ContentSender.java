package com.dna.backend.DNABackend.sesconfig;

public interface ContentSender {
    boolean sendMessage(String email, String code);
}
