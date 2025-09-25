import {Card, CardContent, CardMedia, Grid, Typography, Tooltip, Button, Modal, Box,
} from "@mui/material";
import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { Buffer } from "buffer";
import {fetchGetDataWithAuthArrayBuffer, fetchGetDataWithAuth, fetchDeleteDataWithAuth, fetchGetBlobWithAuth,
} from "client/client";



const PhotoGrid = ({}) => {
  const [photos, setPhotos] = useState({}); // state to store fetch photos
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const album_id = queryParams.get("id"); // Extract album_id from url query params

  const [albumInfo, setAlbumInfo] = useState({}); //state to store album information

  const [open, setOpen] = useState(false);
  const [PhotoContent, setPhotoContent] = useState(null);
  const [PhotoDesc, setPhotoDesc] = useState(null);
  const [DownloadLink, setDownloadLink] = useState(null);

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleView = (download_link, description) => {
    fetchGetDataWithAuthArrayBuffer(download_link).then((response) => {
      //convert photo data to base 64 format
      const buffer = Buffer.from(response.data, "binary").toString("base64");
      setPhotoContent(buffer);
    });

    setDownloadLink(download_link);
    setPhotoDesc(description);
    handleOpen();
  };
  
  const handleDownload = (download_link) => {
    //console.log(download_link);
    fetchGetBlobWithAuth(download_link)
      .then((response) => {
        console.log(response);
        const disposition = response.headers.get("Content-Disposition");
        const match = /filename="(.*)"/.exec(disposition);
        const filename = match ? match[1] : "downloadedFile";

        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", filename);
        document.body.appendChild(link);
        link.click();
      })
      .catch((error) => {
        //Handle error
        console.error("Error downloading photo:", error);
      });
  };

  const handleDelete = (photo_id) => {
    const isConfirmed = window.confirm(
      "Are you sure you want to delete the Photo?"
    );
    if (isConfirmed) {
      //Perform delete operation
      console.log("Item Deleted!" + photo_id);
      fetchDeleteDataWithAuth(
        "/albums/" + album_id + "/photos/" + photo_id + "/delete"
      ).then((res) => {
        console.log(res);
        window.location.reload();
      });
    } else {
      console.log("Delete operation was Cancelled.");
    }
  };

  useEffect(() => {
    //Fetch album data  when component mounts or album_id changes
    fetchGetDataWithAuth("/albums/" + album_id).then((res) => {
      setAlbumInfo(res.data);
      const photosList = res.data.photos || []; // extract list of photos from album data

      //Fetch and update photos one by one as they are downloaded
      photosList.forEach((photo) => {
        //fetch individual photo data
        let thumbnail_link = photo.download_link.replace(
          "/download-photo",
          "/download-thumbnail"
        );
        fetchGetDataWithAuthArrayBuffer(thumbnail_link).then((response) => {
          //construct a unique ID for the photo
          const albumPhotoID = "album_" + album_id + "_photo_" + photo.id;

          //convert photo data to base 64 format
          const buffer = Buffer.from(response.data, "binary").toString(
            "base64"
          );

          //update state with the fetched data
          const temp = {
            album_id: album_id,
            photo_id: photo.id,
            name: photo.name,
            description: photo.description,
            content: buffer,
            download_link: photo.download_link,
          };
          setPhotos((prevPhotos) => ({ ...prevPhotos, [albumPhotoID]: temp }));
        });
      });
    });
  }, [album_id]); // Dependency array ensures useEffect runs when album_id changes

  return (
    <div>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description"
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}>
        <Box
          sx={{
            backgroundColor: "background.paper",
            borderRadius: 2,
            boxShadow: 5,
            p: 3,
            maxHeight: "90%",
            maxWidth: "90%",
            overflow: "auto",
          }}>
          
              {PhotoContent && (
                <img src={"data:image/jpeg;base64," + PhotoContent}
                 alt={PhotoDesc} style={{ maxHeight:"80vh",maxWidth: "80vh", height: "auto", width: "auto" }}/>)}
                <Box sx={{ display: "flex", gap: 2, mt: 2 }}>
              <Button onClick={() => handleDownload(DownloadLink)}>
                Download Photo
              </Button>
              <Button onClick={handleClose} 
              sx={{ backgroundColor: "error.main", color: "white","&:hover": { backgroundColor: "error.dark" },}}>
                Close
              </Button>
            </Box>
        </Box>
      </Modal>

      {/* Album Info */}
      <Typography variant="h4" gutterBottom>
        {albumInfo?.name || "Untitled Album"}
      </Typography>
      <Typography variant="subtitle1" gutterBottom>
        {albumInfo?.description || ""}
      </Typography>

    {/* Photos Grid */}
      <Grid container spacing={2}>
        {/* render each photo*/}
        {Object.keys(photos).map((key) => (
          <Grid key={key} xs={12} sm={6} md={4} lg={3}>
            <Card>
              <Tooltip title={photos[key]["description"]}>
                <CardMedia
                  component="img"
                  height="200"
                  image={"data:image/jpeg;base64," + photos[key]["content"]}
                  alt={photos[key]["description"]}
                />
              </Tooltip>
              <CardContent>
                <Tooltip title={photos[key]["description"]}>
                  <Typography variant="subtitle1">{photos[key]["name"]}</Typography>
                </Tooltip>
                <br />
                <a href="#" onClick={() =>handleView(photos[key]["download_link"], photos[key]["description"])}>{" "}View{" "}
                </a>|
                <a href={`/photo/edit?album_id=${album_id}&photo_id=${photos[key]["photo_id"]}&photo_name=${photos[key]["name"]}&photo_desc=${photos[key]["description"]}`}>{" "}Edit{" "}</a>|
                <a href="#" onClick={() => handleDownload(photos[key]["download_link"])}>{" "}Download{" "}</a>|
                <a href="#" onClick={() => handleDelete(photos[key]["photo_id"])}>{" "}Delete{" "}</a>|
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </div>
  );
};

export default PhotoGrid;
