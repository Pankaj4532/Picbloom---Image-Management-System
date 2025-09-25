// material-ui
import useMediaQuery from '@mui/material/useMediaQuery';


// project imports
import MobileSection from './MobileSection';



// ==============================|| HEADER - CONTENT ||============================== //

export default function HeaderContent() {
  const downLG = useMediaQuery((theme) => theme.breakpoints.down('lg'));

  return (
    <>
      {downLG && <MobileSection />}
    </>
  );
}
