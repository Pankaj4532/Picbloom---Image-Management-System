// assets
import { InfoCircleOutlined } from '@ant-design/icons';

// icons
const icons = {
  InfoCircleOutlined
};

const albums = {
  id: 'pages',
  title: 'Pages',
  type: 'group',
  children: [
    {
      id: 'about',
      title: 'About',
      type: 'item',
      url: '/about',
      icon: icons.InfoCircleOutlined
    }
  ]
};

export default albums;
