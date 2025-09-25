package org.omnera.picbloom.service;

import java.util.List;
import java.util.Optional;

import org.omnera.picbloom.model.Photo;
import org.omnera.picbloom.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    
    @Autowired
    private PhotoRepository photoRepository;

    public Photo save(Photo photo){
        return  photoRepository.save(photo);
    }

    public Optional<Photo>findById(long id){
        return photoRepository.findById(id);
    }
    public List<Photo> findByAlbumId(long id){
        return photoRepository.findByAlbum_id(id);
    }

     // New method for deleting photo entity
    public void delete(Photo photo){
        photoRepository.delete(photo);
    }


}
