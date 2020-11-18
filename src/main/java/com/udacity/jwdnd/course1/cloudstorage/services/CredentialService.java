package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public void addCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        this.credentialMapper.insert(new Credential(null, credential.getUrl(),
                credential.getUsername(), encodedKey, encryptedPassword, credential.getUserId()));
    }

    public void updateCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);

        this.credentialMapper.updateCredential(credential);
    }

    public void deleteCredential(Integer credentialId) {
        this.credentialMapper.deleteCredential(credentialId);
    }

    public List<Credential> getUserCredentials(Integer userId) {
        return this.credentialMapper.getUserCredentials(userId);
    }

    public String getDecryptedPassword(String password, String key) {
        return encryptionService.decryptValue(password, key);
    }

    public Credential getCredentialById(Integer id) {
        return this.credentialMapper.getCredentialById(id);
    }
}
