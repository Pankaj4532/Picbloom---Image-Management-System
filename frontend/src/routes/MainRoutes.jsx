import { lazy } from 'react';

// project imports
import Loadable from 'components/Loadable';
import DashboardLayout from 'layout/Dashboard';

// render - Albums page
const AlbumsPage = Loadable(lazy(() => import('pages/albums/albums')));
const AboutPage = Loadable(lazy(() => import('pages/staticPages/about')));
const AlbumAddPage = Loadable(lazy(() => import('pages/albums/albumAdd')));
const AlbumShowPage = Loadable(lazy(() => import('pages/albums/albumShow')));
const AlbumEditPage = Loadable(lazy(() => import('pages/albums/albumEdit')));
const AlbumUploadPage = Loadable(lazy(() => import('pages/albums/albumUpload')));
const PhotoEditPage = Loadable(lazy(() => import('pages/albums/photoEdit')));


const MainRoutes = {
  path: '/',
  element: <DashboardLayout />,
  children: [
    {
      path: '/albums',
      element: <AlbumsPage />
    },
    {
      path: '/albums/add',
      element: <AlbumAddPage />
    },
    {
      path: '/about',
      element: <AboutPage />
    },
    {
      path: '/albums/show',
      element: <AlbumShowPage />
    },
    {
      path: '/albums/edit',
      element: <AlbumEditPage />
    },
    {
      path: '/albums/upload',
      element: <AlbumUploadPage />
    },
    {
      path: '/photo/edit',
      element: <PhotoEditPage />
    },
  ]
};

export default MainRoutes;
