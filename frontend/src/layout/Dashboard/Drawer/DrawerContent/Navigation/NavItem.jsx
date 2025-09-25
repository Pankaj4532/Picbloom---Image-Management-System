import PropTypes from 'prop-types';
import { NavLink } from 'react-router-dom';

// material-ui
import useMediaQuery from '@mui/material/useMediaQuery';
import Avatar from '@mui/material/Avatar';
import Chip from '@mui/material/Chip';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

// project imports
import IconButton from 'components/@extended/IconButton';
import { handlerDrawerOpen, useGetMenuMaster } from 'api/menu';

export default function NavItem({ item, level, isParents = false, setSelectedID }) {
  const { menuMaster } = useGetMenuMaster();
  const drawerOpen = menuMaster.isDashboardDrawerOpened;

  const downLG = useMediaQuery((theme) => theme.breakpoints.down('lg'));

  const itemTarget = item.target ? '_blank' : '_self';

  const itemHandler = () => {
    if (downLG) handlerDrawerOpen(false);
    if (isParents && setSelectedID) setSelectedID(item.id);
  };

  const Icon = item.icon;
  const itemIcon = item.icon ? (
    <Icon
      style={{
        fontSize: drawerOpen ? '1rem' : '1.25rem',
        ...(isParents && { fontSize: 20, stroke: '1.5' })
      }}
    />
  ) : null;

  return (
    <Box sx={{ position: 'relative' }}>
      <ListItemButton
        component={NavLink}
        to={item.url}
        end={item.end} // exact match for highlighting
        target={itemTarget}
        disabled={item.disabled}
        onClick={itemHandler}
        sx={(theme) => ({
          zIndex: 1201,
          pl: drawerOpen ? `${level * 28}px` : 1.5,
          py: !drawerOpen && level === 1 ? 1.25 : 1,
          '&.active': {
            bgcolor: 'primary.lighter',
            ...theme.applyStyles('dark', { bgcolor: 'divider' }),
            borderRight: '2px solid',
            borderColor: 'primary.main',
            color: 'primary.main',
            '&:hover': {
              color: 'primary.main',
              bgcolor: 'primary.lighter',
              ...theme.applyStyles('dark', { bgcolor: 'divider' })
            }
          },
          '&:hover': { bgcolor: drawerOpen ? 'primary.lighter' : 'transparent' }
        })}
      >
        {itemIcon && (
          <ListItemIcon
            sx={(theme) => ({
              minWidth: 28,
              color: 'inherit',
              ...(!drawerOpen && {
                borderRadius: 1.5,
                width: 36,
                height: 36,
                alignItems: 'center',
                justifyContent: 'center',
                '&:hover': { bgcolor: 'secondary.lighter', ...theme.applyStyles('dark', { bgcolor: 'secondary.light' }) }
              })
            })}
          >
            {itemIcon}
          </ListItemIcon>
        )}

        {(drawerOpen || (!drawerOpen && level !== 1)) && (
          <ListItemText
            primary={
              <Typography variant="h6" sx={{ color: 'inherit' }}>
                {item.title}
              </Typography>
            }
          />
        )}

        {(drawerOpen || (!drawerOpen && level !== 1)) && item.chip && (
          <Chip
            color={item.chip.color}
            variant={item.chip.variant}
            size={item.chip.size}
            label={item.chip.label}
            avatar={item.chip.avatar && <Avatar>{item.chip.avatar}</Avatar>}
          />
        )}
      </ListItemButton>

      {/* Optional actions */}
      {(drawerOpen || (!drawerOpen && level !== 1)) &&
        item?.actions?.map((action, index) => {
          const ActionIcon = action.icon;
          const callAction = action?.function;

          return (
            <IconButton
              key={index}
              {...(action.type === 'function' && {
                onClick: (event) => {
                  event.stopPropagation();
                  callAction();
                }
              })}
              {...(action.type === 'link' && {
                component: NavLink,
                to: action.url,
                target: action.target ? '_blank' : '_self'
              })}
              color="secondary"
              variant="outlined"
              sx={{
                position: 'absolute',
                top: 12,
                right: 20,
                zIndex: 1202,
                width: 20,
                height: 20,
                mr: -1,
                ml: 1,
                color: 'secondary.dark',
                borderColor: 'secondary.light',
                '&:hover': { borderColor: 'secondary.main' }
              }}
            >
              <ActionIcon style={{ fontSize: '0.625rem' }} />
            </IconButton>
          );
        })}
    </Box>
  );
}

NavItem.propTypes = {
  item: PropTypes.any,
  level: PropTypes.number,
  isParents: PropTypes.bool,
  setSelectedID: PropTypes.oneOfType([PropTypes.any, PropTypes.func])
};
