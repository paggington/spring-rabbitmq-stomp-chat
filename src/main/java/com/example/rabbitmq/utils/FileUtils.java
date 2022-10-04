package com.example.rabbitmq.utils;

import com.example.rabbitmq.api.domains.dto.MessageDTO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import static io.netty.util.AsciiString.contains;
import static java.lang.String.format;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.isNull;

@Log4j2
public class FileUtils {

    private static final Path FILES_DESTINATION = Path.of("C:\\Users\\PAG\\Pictures\\RabbitMqChatDirectory\\MessagesData").toAbsolutePath();

    private FileUtils() {
        throw new RuntimeException("You cannot create instance of that class");
    }

    public static Tuple2<String, String> loadFileToDirectory(MultipartFile multipartFile, MessageDTO messageDTO) {
        String fileName = multipartFile.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(fileName);

        if ((!isNull(fileExtension) && !fileExtension.isEmpty())
                && (!isNull(fileName) && !fileName.isEmpty())) {
            String pathFileName = createFilename(messageDTO.getId(), fileExtension);
            String filePath = prepareFilePathForFileSaving(pathFileName);
            if (!isNull(filePath) && !filePath.isEmpty()) {
                try {
                    InputStream fileInputStream = multipartFile.getInputStream();
                    if (!isNull(fileExtension)) {
                        copy(fileInputStream, Path.of(filePath), REPLACE_EXISTING);
                    }
                    return Tuples.of(fileName, filePath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
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
