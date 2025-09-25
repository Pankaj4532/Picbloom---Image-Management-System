package org.omnera.picbloom.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.omnera.picbloom.model.Account;
import org.omnera.picbloom.model.Album;
import org.omnera.picbloom.model.Photo;
import org.omnera.picbloom.payload.auth.album.AlbumPayloadDTO;
import org.omnera.picbloom.payload.auth.album.AlbumViewDTO;
import org.omnera.picbloom.payload.auth.album.PhotoDTO;
import org.omnera.picbloom.payload.auth.album.PhotoPayloadDTO;
import org.omnera.picbloom.payload.auth.album.PhotoViewDTO;
import org.omnera.picbloom.service.AccountService;
import org.omnera.picbloom.service.AlbumService;
import org.omnera.picbloom.service.PhotoService;
import org.omnera.picbloom.util.AppUtils.AppUtil;
import org.omnera.picbloom.util.constants.AlbumError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge=3600)
@RequestMapping("/api/v1")
@Tag(name = "Album Controller", description = "Controller for album and photo management")
@Slf4j
public class AlbumController {

    static final String PHOTOS_FOLDER_NAME = "photos";
    static final String THUMBNAIL_FOLDER_NAME = "thumbnail";
    static final int THUMBNAIL_WIDTH = 300;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoService photoService;

    // ---------------- Add Album ----------------

    @PostMapping(value = "/albums/add", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please add valid name in a description")
    @ApiResponse(responseCode = "201", description = "Account Added")
    @Operation(summary = "Add an Album")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,
            Authentication authentication) {
        try {
            Album album = new Album();
            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();
            album.setAccount(account);
            album = albumService.save(album);
            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), null);
            return ResponseEntity.ok(albumViewDTO);

        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }
    // ---------------- Add Album ends here ----------------

    // ---------------- List Albums ----------------

    @GetMapping(value = "/albums", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of albums")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List Album Api")
    @SecurityRequirement(name = "omnera-demo-api")
    public List<AlbumViewDTO> albums(Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        List<AlbumViewDTO> albums = new ArrayList<>();
        for (Album album : albumService.findByAccount_id(account.getId())) {

            List<PhotoDTO> photos = new ArrayList<>();
            for (Photo photo : photoService.findByAlbumId(album.getId())) {
                // link generation for photos
                String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";

                photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(),
                        photo.getFileName(), link));

            }

            albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos));

        }

        return albums;
    }

    // ---------------- List Albums ends here ------------------

    // ---------------- Albums View Starts here ----------------
    @GetMapping(value = "/albums/{album_id}", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of albums")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List Album by album Id")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<AlbumViewDTO> album_by_id(@PathVariable long album_id, Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (account.getId() != album.getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<PhotoDTO> photos = new ArrayList<>();
        for (Photo photo : photoService.findByAlbumId(album.getId())) {
            // link generation for photos
            String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";

            photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(),
                    photo.getFileName(), link));

        }

        AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos);

        return ResponseEntity.ok(albumViewDTO);
    }
    // ---------------- Albums View Ends here ----------------

    // ---------------- Update Album Starts here ----------------

    @PutMapping(value = "/albums/{album_id}/update", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please add valid name a description")
    @ApiResponse(responseCode = "204", description = "Album update")
    @Operation(summary = "Update an Album")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<AlbumViewDTO> update_Album(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,
            @PathVariable long album_id, Authentication authentication) {

        try {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;

            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();
                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            album = albumService.save(album);
            List<PhotoDTO> photos = new ArrayList<>();
            for (Photo photo : photoService.findByAlbumId(album.getId())) {

                String link = "/albums/" + album.getId() + "/photos/" + photo.getId() + "/download-photo";

                photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(),
                        photo.getFileName(), link));
            }

            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(),
                    photos);
            return ResponseEntity.ok(albumViewDTO);

        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // ---------------- Update Album Ends here ----------------
    // Delete album

    @DeleteMapping(value = "/albums/{album_id}/delete", produces = "application/json")
    @ApiResponse(responseCode = "202", description = "Album Deleted")
    @Operation(summary = "Delete an Album")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<AlbumViewDTO> delete_album(@PathVariable long album_id, Authentication authentication) {

        try {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;
            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();
                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            for (Photo photo : photoService.findByAlbumId(album.getId())) {
                AppUtil.delete_photo_from_path(photo.getFileName(), PHOTOS_FOLDER_NAME, album_id);
                AppUtil.delete_photo_from_path(photo.getFileName(), THUMBNAIL_FOLDER_NAME, album_id);
                photoService.delete(photo);
            }

            
            albumService.delete(album);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    // Delete album

    // ---------------- Upload Photos ----------------

    @PostMapping(value = "albums/{album_id}/upload-photos", consumes = "multipart/form-data")
    @Operation(summary = "Upload photo into album")
    @ApiResponse(responseCode = "400", description = "Please check the payload or token")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<List<HashMap<String, List<?>>>> photos(
            @RequestPart(required = true) MultipartFile[] files,
            @PathVariable long album_id, Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            // Use .equals() to compare IDs safely
            if (account.getId() != album.getAccount().getId()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        List<PhotoViewDTO> uploaded = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        Arrays.asList(files).stream().forEach(file -> {
            String contentType = file.getContentType();
            if (contentType != null &&
                    (contentType.equals("image/png")
                            || contentType.equals("image/jpg")
                            || contentType.equals("image/jpeg"))) {

                try {
                    // Short UUID + truncated original filename (max 15 chars)
                    String originalFileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
                    if (originalFileName.length() > 15) {
                        originalFileName = originalFileName.substring(originalFileName.length() - 15);
                    }
                    String safeFileName = UUID.randomUUID().toString().substring(0, 8) + "_" + originalFileName;

                    Path path = AppUtil.getUploadPath(safeFileName, album_id, PHOTOS_FOLDER_NAME);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    // save photo entity
                    Photo photo = new Photo();
                    photo.setName(originalFileName);
                    photo.setFileName(safeFileName);
                    photo.setOriginalFileName(originalFileName);
                    photo.setAlbum(album);
                    photoService.save(photo);

                    PhotoViewDTO photoViewDTO = new PhotoViewDTO(photo.getId(), photo.getName(),
                            photo.getDescription());
                    uploaded.add(photoViewDTO);

                    BufferedImage thumbImg = AppUtil.getThumbnail(file, THUMBNAIL_WIDTH);
                    File thumbnail_location = AppUtil.getUploadPath(safeFileName, album_id, THUMBNAIL_FOLDER_NAME)
                            .toFile();

                    ImageIO.write(thumbImg, file.getContentType().split("/")[1], thumbnail_location);

                } catch (IOException e) {
                    log.debug(AlbumError.PHOTO_UPLOAD_ERROR.toString() + " : " + e.getMessage());
                    errors.add(file.getOriginalFilename());

                }

            } else {
                errors.add("Invalid file type: " + file.getOriginalFilename());
            }

            HashMap<String, List<?>> result = new HashMap<>();
            result.put("SUCCESS", uploaded);
            result.put("ERRORS", errors);

            List<HashMap<String, List<?>>> response = new ArrayList<>();
            response.add(result);

        });

        HashMap<String, List<?>> result = new HashMap<>();
        result.put("SUCCESS", uploaded);
        result.put("ERRORS", errors);

        List<HashMap<String, List<?>>> response = new ArrayList<>();
        response.add(result);

        return ResponseEntity.ok(response);

    }

    // ---------------- Updating Photos starts here ----------------

    @PutMapping(value = "albums/{album_id}/photos/{photo_id}/update-photos", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update photo from album")
    @ApiResponse(responseCode = "400", description = "Please Add valid name a description")
    @ApiResponse(responseCode = "204", description = "Update Photo")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<PhotoViewDTO> update_photos(@Valid @RequestBody PhotoPayloadDTO photoPayloadDTO,
            @PathVariable long album_id, @PathVariable long photo_id, Authentication authentication) {

        try {

            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);

            Album album;
            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();
                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Optional<Photo> optionalPhoto = photoService.findById(photo_id);
            if (optionalPhoto.isPresent()) {
                Photo photo = optionalPhoto.get();
                if (photo.getAlbum().getId() != album_id) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                photo.setName(photoPayloadDTO.getName());
                photo.setDescription(photoPayloadDTO.getDescription());
                photoService.save(photo);

                PhotoViewDTO photoViewDTO = new PhotoViewDTO(photo.getId(), photoPayloadDTO.getName(),
                        photoPayloadDTO.getDescription());
                return ResponseEntity.ok(photoViewDTO);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    // ---------------- Updating Photos Ends here ----------------
    // ---------------- Deleting Photos Starts here ----------------

    @DeleteMapping(value = "albums/{album_id}/photos/{photo_id}/delete")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "202", description = "photo delete")
    @Operation(summary = "delete photo")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<String> delete_photo(@PathVariable long album_id, @PathVariable long photo_id,
            Authentication authentication) {

        try {

            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(album_id);
            Album album;
            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();
                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            Optional<Photo> optionalPhoto = photoService.findById(photo_id);
            if (optionalPhoto.isPresent()) {
                Photo photo = optionalPhoto.get();
                if (photo.getAlbum().getId() != album_id) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }

                AppUtil.delete_photo_from_path(photo.getFileName(), PHOTOS_FOLDER_NAME, album_id);
                AppUtil.delete_photo_from_path(photo.getFileName(), THUMBNAIL_FOLDER_NAME, album_id);
                photoService.delete(photo);

                return ResponseEntity.status(HttpStatus.ACCEPTED).body("photo deleted");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    // ---------------- Deleting Photos Ends here ----------------
    // ---------------- Download Photos ----------------

    @GetMapping("albums/{album_id}/photos/{photo_id}/download-photo")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<?> downloadPhoto(@PathVariable("album_id") long album_id,
            @PathVariable("photo_id") long photo_id, Authentication authentication) {

        return downloadFile(album_id, photo_id, PHOTOS_FOLDER_NAME, authentication);
    }

    @GetMapping("albums/{album_id}/photos/{photo_id}/download-thumbnail")
    @SecurityRequirement(name = "omnera-demo-api")
    public ResponseEntity<?> downloadThumbnail(@PathVariable("album_id") long album_id,
            @PathVariable("photo_id") long photo_id, Authentication authentication) {

        return downloadFile(album_id, photo_id, THUMBNAIL_FOLDER_NAME, authentication);
    }

    public ResponseEntity<?> downloadFile(long album_id, long photo_id, String folderName,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();

        Optional<Album> optionalAlbum = albumService.findById(album_id);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
            if (account.getId() != (album.getAccount().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Optional<Photo> optionalPhoto = photoService.findById(photo_id);
        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();
            if (photo.getAlbum().getId() != album_id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            Resource resource = null;

            try {
                // use folderName instead of hardcoded PHOTOS_FOLDER_NAME
                resource = AppUtil.getFileAResource(album_id, folderName, photo.getFileName());
            } catch (IOException e) {
                return ResponseEntity.internalServerError().build();
            }
            if (resource == null) {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }

            String contentType = "application/octet-stream";
            String headerValue = "attachment; filename=\"" + photo.getOriginalFileName() + "\"";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
