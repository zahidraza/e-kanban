package com.example.ekanban.storage;

import com.example.ekanban.exception.StorageFileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;

/**
 * Created by razamd on 4/30/2017.
 */
@Service
public class BarcodeService {

    private final Logger logger = LoggerFactory.getLogger(BarcodeService.class);
    private final Path rootLocation;


    @Autowired
    public BarcodeService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public void init() {
        logger.info("init()");
        try {
            if (!Files.exists(rootLocation, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                Files.createDirectory(rootLocation);
                logger.info("Created " + rootLocation.toString() + " directory.");
            }
        } catch (IOException e) {
            logger.error("Error initializing proposal directory.");
        }
    }

    public void store(InputStream stream, String filename) throws FileAlreadyExistsException {
        logger.info("store()");

        try {
            Files.copy(stream, rootLocation.resolve(filename));
        } catch (FileAlreadyExistsException ex) {
            logger.error("File Already Exist");
            throw ex;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        logger.info("loadAsResource(): filename-" + filename);
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                logger.info("Could not read file: " + filename);
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            logger.info("Could not read file: " + filename, e);
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    public Path getRootLocation() {
        return rootLocation;
    }
}
