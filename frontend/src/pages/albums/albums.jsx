// project imports
import MainCard from 'components/MainCard';
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import { Link } from 'react-router-dom';


import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { fetchGetDataWithAuth } from '../../client/client';


const brightPopColors = [
  "#f38130ff", 
  "#F2BED1", 
  "#8785A2", 
  "#AD8B73",
  "#A6B1E1", 
  "#BBDEFB", 
  "#F08A5D",
  "#FFABAB",
  "#BC6FF1",
  "#B68973", 
  "#4CAF50", 
  "#E38B29", 
  "#C8AE7D",
  "#FFAB00",
  "#50c07dff",
  "#607D8B",
  "#A1887F",
  "#FFCDD2",
  "#4FC3F7",
  "#FF5722",
  
]

const getRandomColor = () =>{
  const randomIndex = Math.floor(Math.random() * brightPopColors.length)
  return brightPopColors[randomIndex];
};

const StyledCard = styled(Card)(({theme})=>({
  backgroundColor: getRandomColor(),
  textAlign: 'center',
  padding: theme.spacing(4),
  borderRadius: '20px',
  minHeight: '250px',   // keeps them all same tile size
  width: '250px',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'center',
  alignItems: 'center',
  boxShadow: '0 6px 12px rgba(0,0,0,0.15)',
}));


const AlbumDynamicGridPage = () =>{
  const [dataArray, setDataArray] = useState([]);
  const navigate = useNavigate();


  useEffect(()=>{
    const isLoggedIn = localStorage.getItem('token');
    if(!isLoggedIn){
      navigate("/login");
      window.location.reload()
    }

  fetchGetDataWithAuth("/albums")
      .then(res =>{
        setDataArray(res.data);
        console.log("dataArray: ",dataArray);
      })
  },[]);

  return(
    <Grid container spacing={2}>
      {dataArray.map((data,index)=>(
      <Grid key={index} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
      <Link to={`/albums/show?id=${data.id}`}>
      <StyledCard>
      <CardContent>
        <h1 style={{
        fontSize: '2rem',
        margin: 0,
        color: 'black',
        textOverflow: 'ellipsis', // ✅ handles overflow
        overflow: 'hidden',
        display: '-webkit-box',
        WebkitLineClamp: 2,       // ✅ max 2 lines of text
        WebkitBoxOrient: 'vertical',
        lineHeight: 1.2,
      }}>
          {data.name}
        </h1>
      </CardContent>
    </StyledCard>
    </Link>
  </Grid>
))}

</Grid>
)


}

export default AlbumDynamicGridPage;