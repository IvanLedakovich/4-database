package com.ivanledakovich.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanledakovich.models.FileModel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileSystemRepository implements FileRepository {
    private final Path storagePath;
    private final Path metadataFile;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileSystemRepository(String storagePath) {
        this.storagePath = Paths.get(storagePath);
        this.metadataFile = this.storagePath.resolve("_metadata.json");
        createStorageStructure();
    }

    private void createStorageStructure() {
        try {
            Files.createDirectories(storagePath);
            if (!Files.exists(metadataFile)) {
                Files.createFile(metadataFile);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize file storage", e);
        }
    }

    @Override
    public void insertAFile(File txtFile, File imageFile) throws IOException, SQLException {
        Path targetTxt = storagePath.resolve(txtFile.getName());
        Path targetImg = storagePath.resolve(imageFile.getName());

        FileModel model = new FileModel();
        model.setDate(Date.from(Instant.now()));
        model.setFileName(targetTxt.getFileName().toString());
        model.setFileData(Files.readAllBytes(targetTxt));
        model.setImageName(targetImg.getFileName().toString());
        model.setImageType(FilenameUtils.getExtension(targetImg.toString()));
        model.setImageData(Files.readAllBytes(targetImg));

        List<FileModel> metadata = loadMetadata();
        metadata.add(model);
        saveMetadata(metadata);
    }

    @Override
    public FileModel getFileByName(String fileName) throws SQLException {
        try {
            return loadMetadata().stream()
                    .filter(f -> f.getFileName().equals(fileName) || f.getImageName().equals(fileName))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new SQLException("Metadata read failed", e);
        }
    }

    @Override
    public List<FileModel> getAllFiles() throws SQLException {
        try {
            return loadMetadata();
        } catch (IOException e) {
            throw new SQLException("Failed to load files", e);
        }
    }

    @Override
    public void deleteFileByName(String fileName) throws SQLException {
        try {
            List<FileModel> metadata = loadMetadata();
            Optional<FileModel> file = metadata.stream()
                    .filter(f -> f.getFileName().equals(fileName) || f.getImageName().equals(fileName))
                    .findFirst();

            if (file.isPresent()) {
                Files.deleteIfExists(storagePath.resolve(file.get().getFileName()));
                Files.deleteIfExists(storagePath.resolve(file.get().getImageName()));
                metadata.remove(file.get());
                saveMetadata(metadata);
            }
        } catch (IOException e) {
            throw new SQLException("Deletion failed", e);
        }
    }

    private List<FileModel> loadMetadata() throws IOException {
        if (Files.size(metadataFile) == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(metadataFile.toFile(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, FileModel.class));
    }

    private void saveMetadata(List<FileModel> metadata) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(metadataFile.toFile(), metadata);
    }
}