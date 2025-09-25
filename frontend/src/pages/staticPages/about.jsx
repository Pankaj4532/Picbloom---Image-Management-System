// material-ui
import Typography from '@mui/material/Typography';

// project imports
import MainCard from 'components/MainCard';


// ==============================|| ABOUT PAGE ||============================== //


const AlbumsPage =()=> {




  return (
    <MainCard title="About Picbloom">
      <Typography variant="body2">
        Picbloom is a full-stack gallery application that enables users to upload, organize, and manage photos securely. It features user authentication, role-based access, and a Spring Boot + PostgreSQL backend with RESTful APIs documented using Swagger. The frontend is responsive and user-friendly, ensuring a smooth experience across devices.
      </Typography>
    </MainCard>
  );
}
export default AlbumsPage;