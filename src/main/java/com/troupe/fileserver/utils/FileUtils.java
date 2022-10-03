package com.troupe.fileserver.utils;


import com.troupe.data.data.domains.dto.MessageDTO;
import com.troupe.fileserver.services.CommunicationService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import static io.netty.util.AsciiString.contains;
import static java.lang.String.format;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.isNull;

@Log4j2
public class FileUtils {
    @Value("${files.uploading.directory}")
    private static String FILE_DIRECTORY;

    private static final Path FILES_DESTINATION = Path.of(FILE_DIRECTORY).toAbsolutePath();

    private static final String IMAGES_DB_KEY = "IMAGES";
    @Autowired
    private final CommunicationService comService;

    private FileUtils() {
        throw new RuntimeException("You cannot create instance of that class");
    }

    public static Tuple2<String, String> loadFileToDirectory(MultipartFile multipartFile, String messageId) {
        String fileName = multipartFile.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(fileName);

        if ((!isNull(fileExtension) && !fileExtension.isEmpty())
                && (!isNull(fileName) && !fileName.isEmpty())) {
            String pathFileName = createFilename(messageId, fileExtension);
            String filePath = prepareFilePathForFileSaving(pathFileName);
            if (!isNull(filePath) && !filePath.isEmpty()) {
                try {
                    InputStream fileInputStream = multipartFile.getInputStream();
                    if (!isNull(fileExtension)) {
                        copy(fileInputStream, Path.of(filePath), REPLACE_EXISTING);
                    }
                    fileInputStream.close();
                    return Tuples.of(fileName, filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public static LinkedHashMap<String, Resource> fetchFileFromDirectory(String messageId) {
        if (isNull(messageId)) {
            return new LinkedHashMap<>();
        }
        //fetch messageDTO's from Troupe-data  module
        //move all data operations to Troupe-data
        //make RabbitMQ server & separated module before server uploading
        List<MessageDTO> messageDTOs = new ArrayList<>();
        return new LinkedHashMap<>();
    }

    protected static String createFilename(String messageId, String fileExtension) {
        int currentMessageFileMatches = findAnotherMessageFiles(messageId);
        return generateFilenameWithInstancesAndMessageData(messageId, fileExtension, currentMessageFileMatches);
    }

    protected static int findAnotherMessageFiles(String messageId) {
        int messageContentInstancesInMessagesDirectory = 0;
        try (DirectoryStream<Path> filesDirectoryStream = Files.newDirectoryStream(FILES_DESTINATION)) {
            Iterator<Path> pathIterator = filesDirectoryStream.iterator();
            while (pathIterator.hasNext()) {
                String pathIteratorNextInstance = pathIterator.next().getFileName().toString();
                if (contains(pathIteratorNextInstance, messageId)) {
                    messageContentInstancesInMessagesDirectory++;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return messageContentInstancesInMessagesDirectory;
    }

    private static String generateFilenameWithInstancesAndMessageData(String messageId, String fileExtension, int instanceNumber) {
        return format("%s-%s.%s", messageId, instanceNumber, fileExtension);
    }

    private static String prepareFilePathForFileSaving(String fileName) {
        return String.format("%s\\%s", FILES_DESTINATION, fileName);
    }
}
