package org.omnera.picbloom.repository;


import java.util.List;

import org.omnera.picbloom.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>{
    
    List<Photo> findByAlbum_id(long id);
}
