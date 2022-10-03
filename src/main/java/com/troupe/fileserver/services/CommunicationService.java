package com.troupe.fileserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunicationService {
    public static String hello(){
        return "Hells";
    }
}
