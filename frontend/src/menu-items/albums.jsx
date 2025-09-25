// assets
import { PictureOutlined, FileImageOutlined } from '@ant-design/icons';

// icons
const icons = {
  PictureOutlined,
  FileImageOutlined
  
};

const albums = {
  id: 'albums',
  title: 'Albums',
  type: 'group',
  children: [
    {
      id: 'albums',
      title: 'Albums',
      type: 'item',
      url: '/albums',
      icon: icons.PictureOutlined,
      end: true,
    },
    {
      id: 'AddAlbum',
      title: 'Add Albums',
      type: 'item',
      url: '/albums/add',
      icon: icons.FileImageOutlined,
      end: true,
    },
  ]
};

export default albums;
