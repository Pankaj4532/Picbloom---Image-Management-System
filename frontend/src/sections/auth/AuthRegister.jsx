import { Button } from '@mui/material';
import { TextField } from '@mui/material';
import { Container } from '@mui/material';
import {fetchPostData} from 'client/client'
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
// ============================|| LOGIN ||============================ //

const AuthRegister = () =>{
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errors, setErrors] = useState({email: '', password: ''});
  const [loginError, setLoginError] = useState('');
  const navigate = useNavigate(); 

  useEffect(()=>{
    const isLoggedIn = localStorage.getItem('token');
    if(isLoggedIn){
      navigate('/');
      window.location.reload()

    }
  },[]);// the empty array dependency ensures that the effect runs only once, on mount




  const validateEmail = () =>{
    //Basic email format validation
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  };

  const validatePassword = () =>{
    //Basic Password length validation
    return password.length >= 8 && password.length <=20;
  };

  const handleLogin = async () =>{
    //Reset Previous Errors
    setErrors({email: '', password: ''});


    //validation
    if(!validateEmail()){
      setErrors((prevErrors) =>({ ...prevErrors, email: 'Invalid email format'}));
      return;
    }

    if(!validatePassword()){
      setErrors((prevErrors) => ({ ...prevErrors, password: 'Password must be at least 8 characters.'}));
      return;
    }

    //adding register logic here

    fetchPostData("/auth/users/add",{email,password})
                  .then(() =>{
                    setLoginError('');
                    navigate('/login');
                    window.location.reload()

                  }).catch((error)=>{
                    console.error('Login error: ',error);

                    //Handle Other Login errors
                    setLoginError('An error occured during login');
                  });

  };








  return(
    <Container component="main" maxWidth="xs">
      <TextField variant="outlined"
                margin="normal"
                fullWidth
                label="email"
                value={email}
                onChange={(e)=>setEmail(e.target.value)}
                error={!!errors.email}
                helperText={errors.email}
                />
      <TextField variant="outlined"
                margin="normal"
                fullWidth
                label="password"
                type="password"
                value={password}
                onChange={(e)=>setPassword(e.target.value)}
                error={!!errors.password}
                helperText={errors.password}
                />
      <Button variant = "contained" color="primary" fullWidth onClick={handleLogin}>
        Register
      </Button>
      {loginError && <p style={{color:'red'}}>{loginError}</p>}


    </Container>
  );
}


export default AuthRegister;