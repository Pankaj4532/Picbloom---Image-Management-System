import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Button, TextField } from "@mui/material";
import { fetchPutDataWithAuth, fetchGetDataWithAuth } from "client/client";

const EditAlbumform = () => {
  const navigate = useNavigate();

    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
    const album_id = queryParams.get('id'); // Extract album_id from url query params

  useEffect(() => {
    const isLoggedIn = localStorage.getItem("token");
    if (!isLoggedIn) {
      navigate("/login");
    }
    fetchGetDataWithAuth("/albums/"+album_id)
          .then(res =>{
            if(res.data){
              setFormData(prevFormData =>({
                ...prevFormData,
                name:res.data.name,
                description:res.data.description
              }))
            }
          })
  }, [navigate]);

  const [formData, setFormData] = useState({
    name: "",
    description: "",
  });
  const [errors, setErrors] = useState({
    name: "",
    description: "",
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    //Validation
    let isValid = true;
    const newErrors = { name: "", description: "" };

    if (!formData.name.trim()) {
      newErrors.name = "Name is required";
      isValid = false;
    }

    if (!formData.description.trim()) {
      newErrors.description = "Description is required";
      isValid = false;
    }

    setErrors(newErrors);

    // If form is valid, you can proceed with further actions

    if (isValid) {
      const payload = {
        name: formData.name,
        description: formData.description,
      };
      try {
        const response = await fetchPutDataWithAuth("/albums/"+album_id+"/update", payload);
        console.log("Album Created", response);
        navigate("/albums");
      } catch (error) {
        console.error("Album creation error:", error);
      }
    }
  };
  return (
    <form onSubmit={handleSubmit}>
      <TextField
        variant="outlined"
        margin="normal"
        fullWidth
        label="name"
        name="name"
        value={formData.name}
        onChange={handleInputChange}
        error={!!errors.name}
        helperText={errors.name}
      />
      <TextField
        variant="outlined"
        margin="normal"
        fullWidth
        label="description"
        name="description"
        value={formData.description}
        onChange={handleInputChange}
        error={!!errors.description}
        helperText={errors.description}
        multiline
        rows={4}
      />
      <Button variant="contained" color="primary" type="submit">
        Edit Album
      </Button>
    </form>
  );
};
export default EditAlbumform;
