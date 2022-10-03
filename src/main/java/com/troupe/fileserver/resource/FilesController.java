package com.troupe.fileserver.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FilesController {

    @GetMapping("/fetch")
    public ResponseEntity<Resource> fetchFile(@RequestParam("messageId") String messageIdm) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/save")
    public ResponseEntity<? extends Object> saveFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("messageId") String messageId) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
