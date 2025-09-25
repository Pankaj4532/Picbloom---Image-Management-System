import React, { useState, useCallback } from "react";
import { useDropzone } from "react-dropzone";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "./albums/header"; // import the header
import {
  Box,
  Button,
  Typography,
  Grid,
  Card,
  CardMedia,
  CardContent,
  IconButton,
  CircularProgress,
} from "@mui/material";
import CloudUploadIcon from "@mui/icons-material/CloudUpload";
import DeleteIcon from "@mui/icons-material/Delete";
import { fetchPostFileUploadWithAuth } from "../../client/client";



const PhotoUploadPage = () => {
  const [files, setFiles] = useState([]);
  const [uploading, setUploading] = useState(false);

  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const id= queryParams.get("id");
  const navigate = useNavigate();

  const onDrop = useCallback((acceptedFiles) => {
    setFiles((prev) => [...prev, ...acceptedFiles]);
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: { "image/*": [] },
    multiple: true,
  });

  const handleRemove = (index) => {
    setFiles((prev) => prev.filter((_, i) => i !== index));
  };

  const handleUpload = async () => {
    if (!files.length) return;

    const formData = new FormData();
    files.forEach((file) => formData.append("files", file));

    try {
    setUploading(true); // start uploading before request

    const res = await fetchPostFileUploadWithAuth(
      `/albums/${id}/upload-photos`,formData);

    console.log(res.data);
    alert("Upload successful!");
    setFiles([]);

    navigate(`/albums/show?id=${id}`);
    } catch (err) {
      console.error(err);
      console.log("Upload failed!");
      alert("Upload failed!");
    } finally {
      setUploading(false);
    }
  };

  return (
    <div>
      {/* Header */}
      <Header />

      {/* Photo uploader UI */}
      <Box
        sx={{
          minHeight: "60vh",
          p: 4,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          background: "linear-gradient(135deg, #6EE7B7 0%, #3B82F6 100%)",
          position: "relative",
          mt: 1,
        }}
      >
        {/* abstract shapes */}
        <Box
          sx={{
            position: "absolute",
            width: 200,
            height: 100,
            borderRadius: "50%",
            background: "rgba(255,255,255,0.2)",
            top: "5%",
            left: "5%",
            filter: "blur(80px)",
          }}
        />
        <Box
          sx={{
            position: "absolute",
            width: 250,
            height: 250,
            borderRadius: "50%",
            background: "rgba(255,255,255,0.15)",
            bottom: "10%",
            right: "10%",
            filter: "blur(100px)",
          }}
        />

        <Typography
          variant="h4"
          sx={{ fontWeight: 700, mb: 3, color: "white", textAlign: "center" }}
        >
          Upload Photos Here
        </Typography>

        {/* Dropzone */}
        <Box
          {...getRootProps()}
          sx={{
            width: "90%",
            maxWidth: 800,
            height: 220,
            border: "2px dashed rgba(255,255,255,0.6)",
            borderRadius: 6,
            p: 4,
            textAlign: "center",
            background: "rgba(255,255,255,0.2)",
            backdropFilter: "blur(8px)",
            color: "white",
            cursor: "pointer",
            transition: "0.3s",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            "&:hover": {
              borderColor: "white",
              background: "rgba(255,255,255,0.3)",
            },
            margin: "0 auto",
          }}
        >
          <input {...getInputProps()} />
          <CloudUploadIcon sx={{ fontSize: 60, mb: 2 }} />
          <Typography variant="h6">
            {isDragActive
              ? "Drop your photos here..."
              : "Drag & drop photos or click to browse"}
          </Typography>
          <Typography variant="body2" sx={{ mt: 1, opacity: 0.8 }}>
            PNG, JPG, GIF up to 10MB
          </Typography>
        </Box>

        {/* previews */}
{files.length > 0 && (
  <Box sx={{ mt: 4, width: "90%", maxWidth: 700, margin: "0 auto" }}>
    <Grid container spacing={2} columns={{ xs: 12, sm: 8, md: 12 }}>
      {files.map((file, index) => (
        <Grid key={index} size={{ xs: 6, sm: 4, md: 3 }}>
          <Card sx={{ position: "relative", borderRadius: 3, mt: 2 }}>
            <CardMedia
              component="img"
              image={URL.createObjectURL(file)}
              alt={file.name}
              sx={{
                width: "100%",
                height: 150,
                objectFit: "cover",
                borderRadius: 2,
              }}
            />
            <CardContent
              sx={{ p: 1, textAlign: "center", fontSize: "0.8rem" }}
            >
              {file.name}
            </CardContent>
            <IconButton
              size="small"
              sx={{
                position: "absolute",
                top: 5,
                right: 5,
                background: "rgba(0,0,0,0.5)",
                color: "white",
                "&:hover": { background: "rgba(0,0,0,0.7)" },
              }}
              onClick={() => handleRemove(index)}
            >
              <DeleteIcon fontSize="small" />
            </IconButton>
          </Card>
        </Grid>
      ))}
    </Grid>

    {/* Centered Button */}
    <Box sx={{ display: "flex", justifyContent: "center", mt: 3 }}>
      <Button
        variant="contained"
        size="large"
        onClick={handleUpload}
        disabled={uploading}
        startIcon={
          uploading ? (
            <CircularProgress size={20} color="inherit" />
          ) : null
        }
        sx={{ px: 5, py: 1.5, borderRadius: 4 }}
      >
        {uploading ? "Uploading..." : "Upload Photos"}
      </Button>
    </Box>
  </Box>
)}

      </Box>
    </div>
  );
};

export default PhotoUploadPage;
